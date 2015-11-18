package main;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;

public class GamePanel extends JPanel {

  private MainUI ui;
  private LayoutManager layout;
  private Sudoku sudoku;
  
  public GamePanel(MainUI ui) {
    this.ui = ui;
    sudoku = ui.getSudoku();
    initialize();
    setVisible(true);
  }
  
  private void initialize() {
    initLayout();
    
    setOpaque(true);
    setBackground(new Color(0, 200, 25));
    
    addComponents();
    //addComponents2();

  }
  
  private void addComponents() {
    int lengthLimit = (sudoku.getDimensions() > 3) ? 2 : 1;
    add(new JSeparator(SwingConstants.HORIZONTAL));
    try {
      add(new TextFieldCell("1", lengthLimit));
      add(new JSeparator(SwingConstants.HORIZONTAL));
      add(new TextFieldCell("12", lengthLimit));
      add(new JSeparator(SwingConstants.VERTICAL));
      add(new TextFieldCell("987890", lengthLimit));      
    } catch (BadLocationException e) {
      System.err.println("Bad offset for inserted text.");
    }
 
    setName("sudoku.gamePanel"); 
  }
  
  private void addComponents2() {
    int lengthLimit = (sudoku.getDimensions() > 3) ? 2 : 1;
    int dimension = sudoku.getDimensions();
    for (int r = 0; r < sudoku.getRows().size(); ++r) {
      if ((r != 0) && (r%dimension == 0)) {
        add(new JSeparator(SwingConstants.HORIZONTAL));
      }
      for (int c = 0; c < sudoku.getCols().size(); ++c) {
        if ((c != 0) && (c%dimension == 0)) {
          add(new JSeparator(SwingConstants.VERTICAL));
        }
        try {
          add(new TextFieldCell("4", lengthLimit));
        } catch (BadLocationException e) {
          System.err.println("Bad offset for inserted text.");
        }
      }
    }
  }
  
  private void initLayout() {
    layout = new GridBagLayout();
    setLayout(layout);
  }
}
