package main;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SudokuDocumentListener  implements DocumentListener {
  
  private SudokuInteractor interactor;
  
  public SudokuDocumentListener(SudokuInteractor interactor) {
    this.interactor = interactor;
  }
  
  @Override
  public void changedUpdate(DocumentEvent arg0) {
    // Nothing
  }

  @Override
  public void insertUpdate(DocumentEvent arg0) {
    interactor.playing();
  }

  @Override
  public void removeUpdate(DocumentEvent arg0) {
    interactor.playing();
  }
}
