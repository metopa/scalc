package scalc.core.stmt

import scalc.core.SCalc
import scalc.core.operands.Value

class VariableDef(name: String, value: Value, ctx: SCalc) extends Statement {
  override def execute(): Unit = ctx.setNamedValue(name, value.evaluate())

  override def evaluate(): BigDecimal = {
    ctx.getNamedValue(name).evaluate()
  }

}
