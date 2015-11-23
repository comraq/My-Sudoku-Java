package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.text.BadLocationException;

public class SudokuInteractor extends Observable {

  private Sudoku sudoku;
  private Solver solver;
  private GamePanel gamePanel;
  
  private SudokuState currentState = SudokuState.INIT;
  private List<Integer> hintSquares;
  
  private final int numButtons = 2;
  private JRadioButton[] radioButtons;
  private ButtonGroup buttonGroup;
  private JPanel generatePanel;
  
  public SudokuInteractor(Sudoku sudoku) {
    this.sudoku = sudoku;
    solver = sudoku.getSolver();
    initGeneratePanel();
  }
  
  public enum SudokuState {
    INIT("Press Generate to Select a Sudoku"),
    GENERATED("Generated a New Sudoku"),
    RESETTED("Here is the Original Sudoku"),
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
    if ((currentState == SudokuState.INIT) || (warningDialog("Generate?") == JOptionPane.YES_OPTION)) {
      int newDimensions = launchGenerateDialog();
      if (newDimensions != sudoku.getDimensions()) {
        sudoku.initialize(newDimensions);
        gamePanel.removeAll();
        gamePanel.initialize();
        gamePanel.revalidate();
      }
      generateHelper();
    }  
  }
  
  public void reset() {
    if (warningDialog("Reset?") == JOptionPane.YES_OPTION) {
      resetHelper();
    }
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
  
  private void generateHelper() {
    try {
      sudoku.setSolution(solver.generate('h'));
      List<Cell> cells = sudoku.getSolution().getCells();
      for (int i = 0; i < cells.size(); ++i) {
        TextFieldCell textCell = gamePanel.getTextCells().get(i);
        textCell.clearText();
        if (!cells.get(i).getValues().isEmpty()) {
          textCell.insertString(Integer.toString(cells.get(i).getValues().get(0)));
          textCell.disable();
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
  
  private void resetHelper() {
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
    updateState(SudokuState.RESETTED);
  }
  
  private int warningDialog(String title) {
    String message = "";
    if (title == "Generate?") {
      message = "Are you sure you would like to forfeit the current Sudoku and generate a new one?";
    } else if (title == "Reset?") {
      message = "Are you sure you would like to reset the current Sudoku?";
    }
    return JOptionPane.showConfirmDialog(sudoku.getMainUI(), message, title, JOptionPane.YES_NO_OPTION);
  }
  
  private void initGeneratePanel() {
    generatePanel  = new JPanel();
    radioButtons = new JRadioButton[numButtons];
    buttonGroup = new ButtonGroup();

    radioButtons[0] = new JRadioButton("3x3 - 81 Squares");
    radioButtons[0].setActionCommand("3");

    radioButtons[1] = new JRadioButton("4x4 - 256 Squares");
    radioButtons[1].setActionCommand("4");
    
    for (int i = 0; i < numButtons; i++) {
      buttonGroup.add(radioButtons[i]);
      generatePanel.add(radioButtons[i]);
    }
  }
  
  private int launchGenerateDialog() {
    radioButtons[0].setSelected(true);
    String[] next = {"Next"}; 
    JOptionPane.showOptionDialog(null, generatePanel, "Select Sudoku Size:", JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE, new ImageIcon(), next, next[0]);
    String command = buttonGroup.getSelection().getActionCommand();
    return Integer.parseInt(command);
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
