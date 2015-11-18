package main;

import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

public class TextFieldCell extends JTextField {
  
  protected static int cellHeight = 1;
  protected static int cellWidth = 2;
  
  final static Color  ERROR_COLOR = Color.PINK;

  public TextFieldCell(String text, int lengthLimit) throws BadLocationException {
    super(new FixedTextField(text, lengthLimit), text, cellWidth);
    //setVisible(true);
  }

/*  
  private void initialize() {
    setOpaque(true);
    setBackground(new Color(255, 255, 255));
    setSize(cellHeight, cellWidth);
  }
*/  
}
