package main;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;

public class GamePanel extends JPanel {

  protected final int cellLengthLimit;
  
  private LayoutManager layout;
  private Sudoku sudoku;
  private int dimension;
  
  private Map<Integer, TextFieldCell> textCells;
  
  public GamePanel(Sudoku sudoku) {
    this.sudoku = sudoku;
    dimension = sudoku.getDimensions();
    cellLengthLimit = (dimension > 3) ? 2 : 1;
    textCells = new HashMap<Integer, TextFieldCell>();
    initialize();
    setVisible(true);
  }
  
  private void initialize() {
    initLayout();
    addComponents();
    
    setName("main.gamePanel"); 
  }
  
  private void addComponents() {
    GridBagConstraints con = new GridBagConstraints();
    con.gridx = 0;
    con.gridy = 0;
    con.weightx = 1;
    con.weighty = 1;
    con.fill = GridBagConstraints.BOTH;
    
    for (int r = 0; r < dimension; ++r) {
      for (int c = 0; c < dimension; ++c) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 255, 0));
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        panel.setLayout(new GridBagLayout());
        add(panel, con);
        addTextFieldCells(r*(int)Math.pow(dimension, 3) + c*dimension, panel);
        panel.setVisible(true);
        ++con.gridx;
        con.gridx += 2;
      }
      con.gridx = 0;
      ++con.gridy;
    }
  }
  
  private void addTextFieldCells(Integer square, JPanel panel) {
    GridBagConstraints con = new GridBagConstraints();
    con.gridx = 0;
    con.gridy = 0;
    con.weightx = 1;
    con.weighty = 1;
    con.fill = GridBagConstraints.BOTH;
    
    for (int r = 0; r < dimension; ++r) {
      for (int c = 0; c < dimension; ++c) {
        TextFieldCell textCell;
        try {
          textCell = new TextFieldCell(new FixedTextField("", cellLengthLimit), cellLengthLimit);
          textCells.put((int)square + r*(int)Math.pow(dimension, 2) + c, textCell);
          panel.add(textCell, con);
        } catch (BadLocationException e) {
          System.err.println("Bad offset for inserted text.");
        }
        ++con.gridx;
      }
      ++con.gridy;
      con.gridx = 0;
    }
  }
  
  protected Map<Integer, TextFieldCell> getTextCells() {
    return textCells;
  }
  
  private void initLayout() {
    layout = new GridBagLayout();
    setLayout(layout);
  }
}
