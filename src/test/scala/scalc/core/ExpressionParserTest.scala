package scalc.core

import org.scalatest.FunSuite

class ExpressionParserTest extends FunSuite {
  test("ExpressionParser.parseExpr") {
    assert(ExpressionParser.parseExpr("3").evaluate() === 3)
    assert(ExpressionParser.parseExpr("-3.14").evaluate() === -3.14)
    assert(ExpressionParser.parseExpr("1 + 2 * 3").evaluate() === 7)
    assert(ExpressionParser.parseExpr("1 + 2 - 3").evaluate() === 0)
    assert(ExpressionParser.parseExpr("1 + 2 + - 3").evaluate() === 0)
    assert(ExpressionParser.parseExpr("-1 - +2 + - 3").evaluate() === -6)
    assert(ExpressionParser.parseExpr("+1 + +2 - - 3").evaluate() === 6)
    assert(ExpressionParser.parseExpr("2 + 9 / 3 - 3").evaluate() === 2)
    assert(ExpressionParser.parseExpr("2 + 9 % 4 - 3").evaluate() === 0)
    assert(ExpressionParser.parseExpr("2 + 3 ^ 2 - 3").evaluate() === 8)
    assert(ExpressionParser.parseExpr("2 + 3 ^ |2 - 3|").evaluate() === 5)
    assert(ExpressionParser.parseExpr("2 * (3 + 2 ^ 2) ^ 2").evaluate() === 98)
    assert(
      ExpressionParser
        .parseExpr("(1 + 2) * |3 * 5 + 7| - 3 * 2 ^ 4")
        .evaluate() === 18)
  }
}
