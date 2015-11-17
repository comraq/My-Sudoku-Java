package main;

import java.awt.Color;

import javax.swing.JTextField;

public class textCell extends JTextField {
  
  protected static int cellHeight = 1;
  protected static int cellWidth = 1;
  
  final static Color  ERROR_COLOR = Color.PINK;
  
  public textCell(String text) {
    super(text); 
    initialize();
    setVisible(true);
  }

  private void initialize() {
    setOpaque(true);
    setBackground(new Color(255, 255, 255));
    setSize(cellHeight, cellWidth);
  }
}
