package scalc.core.operands.unop

import scalc.core.operands.Value

class Minus(lhs: Value) extends UnaryOp(lhs) {
  override protected def evaluateOp(a: BigDecimal): BigDecimal = -a
}
