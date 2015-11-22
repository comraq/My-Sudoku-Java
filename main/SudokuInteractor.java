package main;

import java.util.List;
import java.util.Observable;

import javax.swing.text.BadLocationException;

public class SudokuInteractor extends Observable {

  private Sudoku sudoku;
  private Solver solver;
  private GamePanel gamePanel;
  
  private SudokuState currentState = SudokuState.INIT;
  
  public SudokuInteractor(Sudoku sudoku) {
    this.sudoku = sudoku;
    solver = sudoku.getSolver();
  }
  
  public enum SudokuState {
    INIT("Starting"),
    GENERATED("Generated"),
    PLAYING("Playing"),
    SOLVED("All Solved!");
    
    private String theMessage;
    
    SudokuState(String m) {
      theMessage = m;
    }
    
    String message() {
      return theMessage;
    }
  }
  
  public void playing() {
    if (currentState != SudokuState.SOLVED) {
      updateState(SudokuState.PLAYING);
    }  
  }
  
  public void generate() {
    try {
      sudoku.setSolution(solver.generate('h'));
      List<Cell> cells = sudoku.getSolution().getCells();
      for (int i = 0; i < cells.size(); ++i) {
        TextFieldCell textField = gamePanel.getTextFields().get(i);
        textField.clearText();
        if (!cells.get(i).getValues().isEmpty()) {
          textField.insertString(Integer.toString(cells.get(i).getValues().get(0)));
          textField.setEditable(false);
        } else {
          textField.setEditable(true);
        }
      }
    } catch (CloneNotSupportedException e) {
      System.err.println("Error generating sudoku.");
    } catch (BadLocationException e) {
      System.err.println("Bad offset for inserted text.");
    }
    updateState(SudokuState.GENERATED);
  }
  
  public void reset() {
    try {
      if (sudoku.getSolution() != null) {
        List<Cell> cells = sudoku.getSolution().getCells();
        for (int i = 0; i < cells.size(); ++i) {
          if (cells.get(i).getValues().isEmpty()) {
            gamePanel.getTextFields().get(i).clearText();
          }
        }
      }
    } catch (BadLocationException e) {
      System.err.println("Bad offset for inserted text.");
    }
    updateState(SudokuState.GENERATED);
  }

  public void check() {
    boolean solved = true;
    List<Integer> values = solver.getGenValues();
    for (int i = 0; i < values.size(); ++i) {
      TextFieldCell textField = gamePanel.getTextFields().get(i);
      if (textField.isEditable()) {
        if (textField.getText().equals(Integer.toString(values.get(i)))) {
          textField.isCorrect();
        } else {
          textField.isIncorrect();
          solved = false;
        }
      }
    }
    if (solved) {
      updateState(SudokuState.SOLVED);
    }
  }

  public void hint() {
    
  }
  
  public SudokuState getCurrentState() {
    return currentState;
  }
  
  public void setGamePanel(GamePanel gamePanel) {
    this.gamePanel = gamePanel;  
  }
  
  private void updateState(SudokuState nextState) {
    currentState = nextState;
    setChanged();
    notifyObservers();
  }
}
