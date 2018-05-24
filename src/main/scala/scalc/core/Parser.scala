package scalc.core

import scalc.core.operands._
import scalc.core.stmt.{Expression, MacroDef, Statement, VariableDef}

import scala.util.parsing.combinator._

class Parser(ctx: SCalc) extends RegexParsers {
  override def skipWhitespace = true

  def reduceOp(operands: List[~[Value, (Value, Value) => Value]]): Value = {
    operands match {
      case (lhs ~ op) :: (rhs ~ op2) :: tail =>
        reduceOp(new ~(op(lhs, rhs), op2) :: tail)
      case (value ~ _) :: Nil => value
      case Nil                => throw new AssertionError("Unreachable")
    }
  }

  def parseStmt(expr: String): Statement = {
    parseAll(stmt, expr) match {
      case Success(value, _) => value
      case Failure(msg, _)   => throw new SCalcError("parser: invalid input")
      case Error(msg, _)     => throw new SCalcError("parser: invalid input")
    }
  }

  private def stmt: Parser[Statement] = assignStmt | exprStmt

  private def exprStmt: Parser[Statement] = E1 ^^ (e => new Expression(e))

  private def assignStmt: Parser[Statement] = (ident ~ assign ~ E1) ^^ {
    case id ~ assignOp ~ e => assignOp(id.getName, e)
  }

  private def assign: Parser[(String, Value) => Statement] = ("=>" | "=") ^^ {
    case "=" =>
      (s, v) =>
        new VariableDef(s, v, ctx)
    case "=>" =>
      (s, v) =>
        new MacroDef(s, v, ctx)
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

  private def T: Parser[Value] = number | ident | parenthesizedE | absE

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
    """\d+(\.\d*)?""".r ^^ { s =>
      new Constant(BigDecimal(s))
    }

  private def ident: Parser[Variable] =
    """[a-zA-Z_]+""".r ^^ { s =>
      new Variable(s, ctx)
    }
}
