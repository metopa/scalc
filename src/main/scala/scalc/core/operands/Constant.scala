package scalc.core.operands


class Constant(value: BigDecimal) extends Value {
  override def evaluate(): BigDecimal = value
}
