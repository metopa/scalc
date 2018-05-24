package scalc.core.stmt

import scalc.core.SCalc
import scalc.core.operands.Value

class MacroDef(name: String, value: Value, ctx: SCalc) extends Statement {
  override def execute(): Unit = {
    ctx.setNamedValue(name, value)
  }

  override def evaluate(): BigDecimal = value.evaluate()
}
