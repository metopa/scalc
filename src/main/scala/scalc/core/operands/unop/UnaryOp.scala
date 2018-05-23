package scalc.core.operands.unop

import scalc.core.operands.Value

abstract class UnaryOp(lhs: Value) extends Value {
  override def evaluate(): BigDecimal =
    evaluateOp(lhs.evaluate())
  protected def evaluateOp(a: BigDecimal): BigDecimal
}
