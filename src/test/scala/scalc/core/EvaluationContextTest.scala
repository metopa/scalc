package scalc.core

import org.scalatest.FunSuite

class EvaluationContextTest extends FunSuite {
  test("EvaluationContextTest.evaluate") {
    val ec = new SCalc
    assert(ec.evaluate("3") === 3)
    assert(ec.evaluate("-3.14") === -3.14)
    assert(ec.evaluate("1 + 2 * 3") === 7)
    assert(ec.evaluate("1 + 2 - 3") === 0)
    assert(ec.evaluate("1 + 2 + - 3") === 0)
    assert(ec.evaluate("-1 - +2 + - 3") === -6)
    assert(ec.evaluate("+1 + +2 - - 3") === 6)
    assert(ec.evaluate("2 + 9 / 3 - 3") === 2)
    assert(ec.evaluate("2 + 9 % 4 - 3") === 0)
    assert(ec.evaluate("2 + 3 ^ 2 - 3") === 8)
    assert(ec.evaluate("2 + 3 ^ |2 - 3|") === 5)
    assert(ec.evaluate("2 * (3 + 2 ^ 2) ^ 2") === 98)
    assert(ec.evaluate("(1 + 2) * |3 * 5 + 7| - 3 * 2 ^ 4") === 18)
  }
}
