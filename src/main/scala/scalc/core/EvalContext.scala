package scalc.core

import scala.collection.mutable

class EvalContext {
  private val seenNames = new mutable.HashSet[String]()
  def see(s: String): Unit = {
    if (!seenNames.add(s))
      throw new SCalcError("evaluation: loop over " + s)
  }

  def unsee(s: String): Unit = {
    seenNames.remove(s)
  }
}
