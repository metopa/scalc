package scalc.core.operands.binop

import scalc.core.EvalContext
import scalc.core.operands.Value

abstract class BinaryOp(lhs: Value, rhs: Value) extends Value {
  override def evaluate(evalCtx: EvalContext): BigDecimal =
    evaluateOp(lhs.evaluate(evalCtx), rhs.evaluate(evalCtx))
  protected def evaluateOp(a: BigDecimal, b: BigDecimal): BigDecimal
}
