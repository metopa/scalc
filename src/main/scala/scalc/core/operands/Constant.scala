package scalc.core.operands

import scalc.core.EvalContext

class Constant(value: BigDecimal) extends Value {
  override def evaluate(evalCtx: EvalContext): BigDecimal = value
}
