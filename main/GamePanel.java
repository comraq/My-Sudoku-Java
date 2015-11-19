package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;


public class GamePanel extends JPanel {

  protected final int cellHeight = 1;
  protected final int cellWidth;
  
  //private MainUI ui;
  private LayoutManager layout;
  private Sudoku sudoku;
  private int dimension;
  
  private List<TextFieldCell> textCells;
  private List<JPanel> unitPanels;
  
  public GamePanel(MainUI ui) {
    //this.ui = ui;
    sudoku = ui.getSudoku();
    dimension = sudoku.getDimensions();
    cellWidth = (dimension > 3) ? 2 : 1;
    textCells = new ArrayList<TextFieldCell>();
    unitPanels = new ArrayList<JPanel>();
    initialize();
    setVisible(true);
  }
  
  private void initialize() {
    initLayout();
    
    setOpaque(true);
    setBackground(new Color(0, 200, 25));
    setSize((int)Math.pow(dimension, 4)*100, (int)Math.pow(dimension, 4)*100);
    
    addComponents();
    
    setName("sudoku.gamePanel"); 
  }
  
  private void addComponents() {
    int leftInset = 0;
    int topInset = 0;
    Insets inset = new Insets(topInset, leftInset, 0, 0);
    
    GridBagConstraints con = new GridBagConstraints();
    con.gridx = 0;
    con.gridy = 0;
    con.weightx = 1;
    con.weighty = 1;
    con.ipadx = 20;
    con.ipady = 20;    
    
    for (int r = 0; r < dimension; ++r) {
      if (r != 0) {
        topInset = 2;
        inset.set(topInset, leftInset, 0, 0);
      }
      for (int c = 0; c < dimension; ++c) {
        if (c != 0) {
          leftInset = 2;
          inset.set(topInset, leftInset, 0, 0);
        } else {
          leftInset = 0;
          inset.set(topInset, leftInset, 0, 0);
        }
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        unitPanels.add(panel);
        add(panel, con);
        panel.setVisible(true);
        ++con.gridx;
      }
      con.gridx = 0;
      ++con.gridy;
    }
    for (JPanel panel : unitPanels) {
      addTextFieldCells(panel);
    }
  }
  
  private void addTextFieldCells(JPanel panel) {
    GridBagConstraints con = new GridBagConstraints();
    con.gridx = 0;
    con.gridy = 0;
    con.ipadx = 20;
    con.ipady = 20;
    con.weightx = 1;
    con.weighty = 1;
    
    for (int r = 0; r < dimension; ++r) {
      for (int c = 0; c < dimension; ++c) {
        TextFieldCell textCell;
        try {
          textCell = new TextFieldCell(Integer.toString((r * (int)Math.pow(dimension,2)) + c), cellWidth);
          textCells.add(textCell);
          panel.add(textCell, con);
        } catch (BadLocationException e) {
          System.err.println("Bad offset for inserted text.");
        }
        //panel.setOpaque(true);
        //panel.setBackground(new Color(0, 100, 25));
        //panel.setSize(50, 50);
        ++con.gridx;
      }
      ++con.gridy;
      con.gridx = 0;
    }
  }
  
  private void initLayout() {
    layout = new GridBagLayout();
    setLayout(layout);
  }
}
