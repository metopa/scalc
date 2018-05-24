package scalc.core.operands

import scalc.core.SCalc

class Variable(name: String, ctx: SCalc) extends Value {
  override def evaluate(): BigDecimal = ctx.getNamedValue(name).evaluate()

  def getName = name
}
