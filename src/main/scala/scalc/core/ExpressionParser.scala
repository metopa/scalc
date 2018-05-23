package scalc.core

import scalc.core.operands.{binop, unop, Constant, Value}

import scala.util.parsing.combinator._

object ExpressionParser extends RegexParsers {
  override def skipWhitespace = true

  def parseExpr(expr: String): Value = {
    parse(E, expr) match {
      case Success(value, _) => value
      case Failure(msg, _) => throw new ParsingError(msg)
      case Error(msg, _) => throw new ParsingError(msg)
    }
  }

  private def E: Parser[Value] = FPlusE | FMinusE | F
  private def F: Parser[Value] = GMultiplyF | GDivF | GModF | GPowF | G
  private def G: Parser[Value] = plusT | minusT | T
  private def T: Parser[Value] = number | parenthesizedE | absE

  private def FPlusE: Parser[Value] = (F ~ "+" ~ E) ^^ {
    case f ~ "+" ~ e => new binop.Plus(f, e)
  }

  private def FMinusE: Parser[Value] = (F ~ "-" ~ E) ^^ {
    case f ~ "-" ~ e => new binop.Minus(f, e)
  }

  private def GMultiplyF: Parser[Value] = (G ~ "*" ~ F) ^^ {
    case g ~ "*" ~ f => new binop.Multiply(g, f)
  }

  private def GDivF: Parser[Value] = (G ~ "/" ~ F) ^^ {
    case g ~ "/" ~ f => new binop.Div(g, f)
  }

  private def GModF: Parser[Value] = (G ~ "%" ~ F) ^^ {
    case g ~ "%" ~ f => new binop.Mod(g, f)
  }

  private def GPowF: Parser[Value] = (G ~ "^" ~ F) ^^ {
    case g ~ "^" ~ f => new binop.Pow(g, f)
  }

  private def plusT: Parser[Value] = ("+" ~ T) ^^ {
    case "+" ~ t => new unop.Plus(t)
  }

  private def minusT: Parser[Value] = ("-" ~ T) ^^ {
    case "-" ~ t => new unop.Minus(t)
  }

  private def parenthesizedE: Parser[Value] = ("(" ~ E ~ ")") ^^ {
    case "(" ~ e ~ ")" => e
  }

  private def absE: Parser[Value] = ("|" ~ E ~ "|") ^^ {
    case "|" ~ e ~ "|" => new unop.Abs(e)
  }

  private def number: Parser[Value] =
    """\d+(\.\d*)?""".r ^^ { s: String =>
      new Constant(BigDecimal(s))
    }
}
