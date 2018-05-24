package scalc.core.stmt

import scalc.core.operands.Value

class Expression(value: Value) extends Statement {
  override def execute(): Unit = {}
  override def evaluate(): BigDecimal = value.evaluate()

}
