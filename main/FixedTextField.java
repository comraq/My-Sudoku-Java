package main;

import java.awt.event.KeyListener;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.SimpleAttributeSet;

public class FixedTextField extends PlainDocument {

  private int lengthLimit;

  public FixedTextField(Sudoku sudoku, String text, int lengthLimit) throws BadLocationException {
    super();
    this.lengthLimit = lengthLimit; 
    insertString(0, text, new SimpleAttributeSet());
    addDocumentListener(sudoku.getDocumentListener());
  }

  @Override
  public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
    if (!(getLength() + str.length() > lengthLimit)) {
      super.insertString(offset, str, a);
    }
  }
}
