package main;

import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;

public class TextFieldCell extends JTextField {
  
  final static Color INCORRECT = Color.PINK;
  final static Color CORRECT = Color.GREEN;
  final static Color UNCHECK = Color.WHITE;
  final static Color HINT = Color.YELLOW;
  
  private FixedTextField textField;

  public TextFieldCell(FixedTextField textField, int lengthLimit) throws BadLocationException {
    super(textField, "", lengthLimit);
    this.textField = textField;
  }
  
  public void insertString(String text) throws BadLocationException {
    textField.insertString(0, text, new SimpleAttributeSet());
  }
  
  public TextFieldCell clearText() throws BadLocationException {
    textField.remove(0, textField.getLength());
    return this;
  }
  
  public void isCorrect() {
    setBackground(CORRECT);
  }
  
  public void isIncorrect() {
    setBackground(INCORRECT);
  }
  
  public void uncheck() {
    setBackground(UNCHECK);
  }
  
  public void hint() {
    setBackground(HINT);
  }
}
