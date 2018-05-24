package scalc.gui

import javafx.scene.input.KeyCode
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Label, TextArea, TextField}
import scalafx.scene.input.KeyEvent
import scalafx.scene.layout.{BorderPane, Priority, VBox}
import scalc.core.SCalc

import scala.util.{Failure, Success, Try}

object SCalcWindow extends JFXApp {
  val core = new SCalc()

  val historyArea: TextArea = new TextArea {
    editable = false
    focusTraversable = false
    vgrow = Priority.Always
  }

  val inputField: TextField = new TextField {
    text = ""

    onAction = (a: ActionEvent) => {
      evaluate(text())
    }

    onKeyTyped = (a: KeyEvent) => {
      if (a.getCode != KeyCode.ENTER) {
        evaluateTmp(text())
      }
    }

    onKeyPressed = (a: KeyEvent) => {
      if (a.getCode != KeyCode.ENTER) {
        evaluateTmp(text())
      }
    }
  }

  val outputField: Label = new Label()

  private val validColor = tuple32JfxColor(0, 0, 0)
  private val invalidColor = tuple32JfxColor(160, 160, 160)
  private val errorColor = tuple32JfxColor(0xAD, 0x2A, 0x1A)

  private def showTmpResult(res: String): Unit = {
    outputField.setTextFill(validColor)
    outputField.text = res
  }

  private def showFinalResult(res: String): Unit = {
    outputField.text = ""
    inputField.text = res
  }

  private def showError(err: String): Unit = {
    outputField.setTextFill(errorColor)
    outputField.text = err
  }

  private def invalidateTmpResult(): Unit = {
    outputField.setTextFill(invalidColor)
  }

  private def appendHistory(expr: String, result: String): Unit = {
    historyArea.text = historyArea.text() + s"$expr == $result\n"
  }

  stage = new PrimaryStage {
    scene = new Scene(300, 400) {
      title = "SCalc"
      minHeight = 350
      minWidth = 200
      root = new VBox {
        vgrow = Priority.Always
        spacing = 6
        padding = Insets(10)
        children = Seq(
          new Label(
            "Expression is evaluated as you type in.\n" +
              "To define new variable, type ident = expr\n" +
              "To define new macro, type ident => expr") {
            wrapText = true
          },
          inputField,
          outputField,
          historyArea
        )
      }
    }
  }

  private def evaluate(expr: String): Unit = {
    Try(core.evaluate(expr, true)) match {
      case Success(result) =>
        val resultStr = result.toString
        showFinalResult(resultStr)
        appendHistory(expr, resultStr)
      case Failure(err) =>
        showError(err.getMessage)
    }
  }

  private def evaluateTmp(expr: String): Unit = {
    if (expr isEmpty) {
      outputField.text = ""
    } else {
      Try(core.evaluate(expr, false)) match {
        case Success(result) =>
          val resultStr = result.toString
          showTmpResult(resultStr)
        case Failure(_) =>
          invalidateTmpResult()
      }
    }
  }
}
