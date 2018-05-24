package scalc.core

import scalc.core.operands.{Constant, Value}

import scala.collection.mutable

class SCalc {
  private val expressionParser = new Parser(this)

  def evaluate(s: String, withExec: Boolean = true): BigDecimal = {
    val stmt = expressionParser.parseStmt(s)
    if (withExec)
      stmt.execute()
    val ans = stmt.evaluate()
    if (withExec)
      builtinValues.put("ans", new Constant(ans))
    ans
  }

  private val namedValues = mutable.HashMap[String, Value]()
  private val builtinValues = mutable.HashMap[String, Value](
    "pi" -> new Constant(BigDecimal(Math.PI)),
    "e" -> new Constant(BigDecimal(Math.E)),
    "ans" -> new Constant(BigDecimal(0)))

  def getNamedValue(name: String): Value = {
    if (builtinValues.contains(name)) {
      builtinValues(name)
    } else if (namedValues.contains(name))
      namedValues(name)
    else
      throw new SCalcError("evaluation: unknown name " + name)
  }

  def setNamedValue(name: String, value: Value): Unit = {
    if (builtinValues.contains(name))
      throw new SCalcError(s"Can't set built-in symbol $name")
    namedValues.put(name, value)
  }

  def setNamedValue(name: String, value: BigDecimal): Unit = {
    setNamedValue(name, new Constant(value))
  }
}
