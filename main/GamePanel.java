package main;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class GamePanel extends JPanel {

  private MainUI ui;
  private SpringLayout layout;
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
    
    //addComponents();
    add(new textCell("1"));
    //add(new JSeparator(SwingConstants.HORIZONTAL));
    add(new textCell("123"));
    //add(new JSeparator(SwingConstants.VERTICAL));
    add(new textCell("987890"));

    setName("sudoku.gamePanel"); 
  }
  
  private void addComponents() {
    int dimension = sudoku.getDimensions();
    for (int r = 0; r < sudoku.getRows().size(); ++r) {
      if ((r != 0) && (r%dimension == 0)) {
        add(new JSeparator(SwingConstants.HORIZONTAL));
      }
      for (int c = 0; c < sudoku.getCols().size(); ++c) {
        if ((c != 0) && (c%dimension == 0)) {
          add(new JSeparator(SwingConstants.VERTICAL));
        }
        add(new textCell("4"));
      }
    }
  }
  
  private void initLayout() {
    layout = new SpringLayout();
    setLayout(layout);
  }
}
