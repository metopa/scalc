package scalc.core.operands

import scalc.core.{EvalContext, SCalc}

class Variable(name: String, ctx: SCalc) extends Value {
  override def evaluate(evalCtx: EvalContext): BigDecimal = {
    evalCtx see name
    val result = ctx.getNamedValue(name).evaluate(evalCtx)
    evalCtx unsee name
    result
  }

  def getName: String = name
}
