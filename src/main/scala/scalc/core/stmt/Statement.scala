package scalc.core.stmt

abstract class Statement {
  def execute(): Unit
  def evaluate(): BigDecimal
}
