package scalc.core

import scalc.core.operands.{Constant, Value}

import scala.collection.mutable

class SCalc {
  private val expressionParser = new Parser(this)

  def evaluate(s: String): BigDecimal = {
    val stmt = expressionParser.parseStmt(s)
    stmt.execute()
    val ans = stmt.evaluate()
    setNamedValue("ans", ans)
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

  def setNamedValue(name: String, value: BigDecimal): Unit = {
    namedValues.put(name, new Constant(value))
  }
}
