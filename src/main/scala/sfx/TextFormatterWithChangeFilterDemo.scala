import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.TextFormatter.Change
import scalafx.scene.control.{Label, TextArea, TextField, TextFormatter}
import scalafx.scene.layout.{BorderPane, VBox}
import scalafx.util.StringConverter


object TextFormatterWithChangeFilterDemo extends JFXApp {

  case class Message(text: String) {
    override def toString = '"' + text + '"'
  }

  val prompt = "> "

  val converter = new StringConverter[Message] {
    override def fromString(s: String): Message = {
      val r =
        if (s.startsWith(prompt)) s.substring(prompt.length)
        else s
      Message(r)
    }
    override def toString(v: Message): String = {
      prompt + v.text
    }
  }

  // Filter the change restoring prompt if it was removed and correcting caret position
  val filter: (Change) => Change = { change: Change =>
    // Restore prompt if part was deleted
    if (change.controlNewText.length <= prompt.length) {
      change.text = prompt.substring(change.controlNewText.length)
    }
    // Restore caret position if it moved over the prompt
    if (change.anchor < prompt.length) change.anchor = prompt.length
    if (change.caretPosition < prompt.length) change.caretPosition = prompt.length
    change
  }
  val formatter = new TextFormatter[Message](converter, Message("hello"), filter)

  val outputTextArea = new TextArea {
    editable = false
    focusTraversable = false
  }

  val textField = new TextField {
    text = prompt
    textFormatter = formatter

    onAction = (a: ActionEvent) => {
      val str = text()
      val message = converter.fromString(str) + "\n"
      outputTextArea.text = message + outputTextArea.text()
      text() = ""
    }
  }

  stage = new PrimaryStage {
    scene = new Scene(300, 300) {
      title = "TextFormatter Demo"
      root = new VBox {
        spacing = 6
        padding = Insets(10)
        children = Seq(
          new Label("Example of using `TextFormatter` to ensure that the input field includes prompt text \"> \".") {
            wrapText = true
          },
          new Label("Type message at the prompt. Press \"Enter\" to send."),
          new BorderPane {
            top = textField
            center = outputTextArea
          }
        )
      }
    }
  }
}