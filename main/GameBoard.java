package main;

import javax.swing.JFrame;

public class GameBoard extends JFrame {

  private MainUI ui;
  private Sudoku sudoku;
  
  public GameBoard(Sudoku sudoku) {
    this.sudoku = sudoku;
  }
  
}
