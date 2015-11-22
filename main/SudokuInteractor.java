package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.text.BadLocationException;

public class SudokuInteractor extends Observable {

  private Sudoku sudoku;
  private Solver solver;
  private GamePanel gamePanel;
  
  private SudokuState currentState = SudokuState.INIT;
  private List<Integer> hintSquares;
  
  public SudokuInteractor(Sudoku sudoku) {
    this.sudoku = sudoku;
    solver = sudoku.getSolver();
  }
  
  public enum SudokuState {
    INIT("Press Generate to Select a Sudoku"),
    GENERATED("Generated a Sudoku"),
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
    for (Map.Entry<Integer, TextFieldCell> entry : gamePanel.getTextCells().entrySet()) {
      if (entry.getValue().isEditable()) {
        entry.getValue().uncheck();
      }
    }
  }
  
  public void generate() {
    try {
      sudoku.setSolution(solver.generate('h'));
      List<Cell> cells = sudoku.getSolution().getCells();
      for (int i = 0; i < cells.size(); ++i) {
        TextFieldCell textCell = gamePanel.getTextCells().get(i);
        textCell.clearText();
        if (!cells.get(i).getValues().isEmpty()) {
          textCell.insertString(Integer.toString(cells.get(i).getValues().get(0)));
          textCell.setEditable(false);
        } else {
          textCell.setEditable(true);
        }
      }
    } catch (CloneNotSupportedException e) {
      System.err.println("Error generating sudoku.");
    } catch (BadLocationException e) {
      System.err.println("Bad offset for inserted text.");
    }
    hintSquares = new ArrayList<Integer>(sudoku.getSquares());
    updateState(SudokuState.GENERATED);
  }
  
  public void reset() {
    try {
      List<Cell> cells = sudoku.getSolution().getCells();
      for (int i = 0; i < cells.size(); ++i) {
        if (cells.get(i).getValues().isEmpty()) {
          gamePanel.getTextCells().get(i).clearText().uncheck();
        }
      }
    } catch (BadLocationException e) {
      System.err.println("Bad offset for inserted text.");
    }
    hintSquares = new ArrayList<Integer>(sudoku.getSquares());
    updateState(SudokuState.GENERATED);
  }

  public void check() {
    boolean solved = true;
    List<Integer> values = solver.getGenValues();
    for (int i = 0; i < values.size(); ++i) {
      TextFieldCell textCell = gamePanel.getTextCells().get(i);
      if (textCell.isEditable()) {
        if (textCell.getText().equals(Integer.toString(values.get(i)))) {
          textCell.isCorrect();
        } else {
          textCell.isIncorrect();
          solved = false;
        }
      }
    }
    if (solved) {
      updateState(SudokuState.SOLVED);
    }
  }

  public void hint() {
    try {
      Integer randS;
      for (List<Integer> values = solver.getGenValues(); !hintSquares.isEmpty(); hintSquares.remove(randS)) {
        randS = hintSquares.get(ThreadLocalRandom.current().nextInt(0, hintSquares.size()));
        TextFieldCell textCell = gamePanel.getTextCells().get(randS);
        if (textCell.isEditable() && !textCell.getText().equals(Integer.toString(values.get(randS)))) {
          textCell.clearText().insertString(Integer.toString(values.get(randS)));
          textCell.hint();
          hintSquares.remove(randS);
          break;
        }
      }
    } catch (BadLocationException e) {
      System.err.println("Bad offset for inserted text.");
    }
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
