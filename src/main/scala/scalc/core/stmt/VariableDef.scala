package scalc.core.stmt

import scalc.core.SCalc
import scalc.core.operands.Value

class VariableDef(name: String, value: Value, ctx: SCalc) extends Statement {
  private lazy val evaluated = value.evaluate()

  override def execute(): Unit = ctx.setNamedValue(name, evaluated)
  override def evaluate(): BigDecimal = evaluated
}
