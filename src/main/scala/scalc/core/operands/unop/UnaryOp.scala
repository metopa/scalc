package scalc.core.operands.unop

import scalc.core.EvalContext
import scalc.core.operands.Value

abstract class UnaryOp(lhs: Value) extends Value {
  override def evaluate(evalCtx: EvalContext): BigDecimal =
    evaluateOp(lhs.evaluate(evalCtx))

  protected def evaluateOp(a: BigDecimal): BigDecimal
}
