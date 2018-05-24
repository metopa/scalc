package scalc.tui

import scalc.core.SCalc

object Main extends App {
  val calc = new SCalc()

  def repl(): Boolean = {
    val input = scala.io.StdIn.readLine(":>")
    input match {
      case null   => false
      case "exit" => false
      case _ =>
        try {
          val ans = calc.evaluate(input)
          println(ans)
          true
        } catch {
          case e: Exception => println(e); true
        }
    }

  }

  println("SCalc")
  while (repl()) {}
}
