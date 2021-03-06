package main;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;

@SuppressWarnings("serial")
public class TextFieldCell extends JTextField {
  
  private final static Color INCORRECT = Color.PINK;
  private final static Color CORRECT = Color.GREEN;
  private final static Color UNCHECK = Color.WHITE;
  private final static Color HINT = Color.YELLOW;
  private final static Color DISABLE = new Color(240, 240, 240);
  private final static Font REGULAR_FONT = new Font("SansSerif", Font.PLAIN, 28);
  
  private FixedTextField textField;

  public TextFieldCell(FixedTextField textField, int lengthLimit) throws BadLocationException {
    super(textField, "", lengthLimit);
    this.textField = textField;
    setFont(REGULAR_FONT);
    setHorizontalAlignment(SwingConstants.CENTER);
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
  
  public void disable() {
    setEditable(false);
    setBackground(DISABLE);
  }
}
