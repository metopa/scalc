package scalc.core

import scalc.core.operands.{Constant, Value}

import scala.collection.mutable

class SCalc {
  private val expressionParser = new ExpressionParser(this)

  def evaluate(s: String): BigDecimal = {
    val ans = expressionParser.parseExpr(s).evaluate()
    setNamedValue("ans", new Constant(ans))
    ans
  }

  private val namedValues = new mutable.HashMap[String, Value]()

  def getNamedValue(name: String): Value = {
    if (namedValues.contains(name))
      namedValues(name)
    else
      throw new SCalcError("evaluation: unknown name " + name)
  }

  def setNamedValue(name: String, value: Value): Unit = {
    namedValues.put(name, value)
  }
}
