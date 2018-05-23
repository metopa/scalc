package scalc.core.operands.binop

import scalc.core.operands.Value

class Pow(lhs: Value, rhs: Value) extends BinaryOp(lhs, rhs) {
  override protected def evaluateOp(a: BigDecimal,
                                    b: BigDecimal): BigDecimal = {
    if (b.isValidInt) {
      a pow b.toInt
    } else {
      throw new ArithmeticException("^ rhs operand must be integer")
    }
  }
}
