package scalc.core.operands

import scalc.core.EvalContext

abstract class Value {
  def evaluate(evalCtx: EvalContext): BigDecimal

  def evaluate(): BigDecimal = evaluate(new EvalContext)
}
