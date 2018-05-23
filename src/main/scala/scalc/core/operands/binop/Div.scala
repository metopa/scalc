package scalc.core.operands.binop

import scalc.core.operands.Value

class Div(lhs: Value, rhs: Value) extends BinaryOp(lhs, rhs) {
  override protected def evaluateOp(a: BigDecimal, b: BigDecimal): BigDecimal =
    a / b
}
