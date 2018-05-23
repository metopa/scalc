package scalc.core.operands.binop

import scalc.core.operands.Value

abstract class BinaryOp(lhs: Value, rhs: Value) extends Value {
  override def evaluate(): BigDecimal =
    evaluateOp(lhs.evaluate(), rhs.evaluate())
  protected def evaluateOp(a: BigDecimal, b: BigDecimal): BigDecimal
}
