package scalc.core

import scalc.core.operands.{Constant, Value, binop, unop}

import scala.util.parsing.combinator._

object ExpressionParser extends RegexParsers {
  override def skipWhitespace = true

  def reduceOp(operands: List[~[Value, (Value, Value) => Value]]): Value = {
    operands match {
      case (lhs ~ op) :: (rhs ~ op2) :: tail =>
        reduceOp(new ~(op(lhs, rhs), op2) :: tail)
      case (value ~ _) :: Nil => value
      case Nil                => throw new AssertionError("Unreachable")
    }
  }

  def parseExpr(expr: String): Value = {
    parse(E1, expr) match {
      case Success(value, _) => value
      case Failure(msg, _)   => throw new ParsingError(msg)
      case Error(msg, _)     => throw new ParsingError(msg)
    }
  }

  private def E1: Parser[Value] = ((E2 ~ binop1).* ~ E2) ^^ {
    case l ~ f => reduceOp(l :+ new ~(f, (x: Value, y: Value) => x))
  }

  private def E2: Parser[Value] = ((E3 ~ binop2).* ~ E3) ^^ {
    case l ~ f => reduceOp(l :+ new ~(f, (x: Value, y: Value) => x))
  }

  private def E3: Parser[Value] = ((F ~ binop3).* ~ F) ^^ {
    case l ~ f => reduceOp(l :+ new ~(f, (x: Value, y: Value) => x))
  }

  private def F: Parser[Value] = (opt(unop1) ~ T) ^^ {
    case Some(op) ~ t => op(t)
    case None ~ t     => t
  }
  private def T: Parser[Value] = number | parenthesizedE | absE

  private def binop1: Parser[(Value, Value) => Value] = ("+" | "-") ^^ {
    case "+" =>
      (a, b) =>
        new binop.Plus(a, b)
    case "-" =>
      (a, b) =>
        new binop.Minus(a, b)
  }

  private def binop2: Parser[(Value, Value) => Value] =
    ("*" | "/" | "%") ^^ {
      case "*" =>
        (a, b) =>
          new binop.Multiply(a, b)
      case "/" =>
        (a, b) =>
          new binop.Div(a, b)
      case "%" =>
        (a, b) =>
          new binop.Mod(a, b)
    }

  private def binop3: Parser[(Value, Value) => Value] =
    "^" ^^ { _ => (a, b) =>
      new binop.Pow(a, b)
    }

  private def unop1: Parser[Value => Value] = ("+" | "-") ^^ {
    case "+" =>
      a =>
        new unop.Plus(a)
    case "-" =>
      a =>
        new unop.Minus(a)
  }

  private def parenthesizedE: Parser[Value] = "(" ~> E1 <~ ")"

  private def absE: Parser[Value] = ("|" ~> E1 <~ "|") ^^ (e => new unop.Abs(e))

  private def number: Parser[Value] =
    """\d+(\.\d*)?""".r ^^ { s: String =>
      new Constant(BigDecimal(s))
    }
}
