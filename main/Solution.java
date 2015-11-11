package main;

import java.util.ArrayList;
import java.util.List;

public class Solution implements Cloneable{
  
  private List<Cell> cells;
  private Sudoku sudoku;
  private boolean contradiction;
  
  public Solution initialize() {
    contradiction = false;
    cells = new ArrayList<Cell>();
    for (char row : sudoku.getRows()) {
      for (char col : sudoku.getCols()) {
        cells.add(new Cell().withSudoku(sudoku).initialize("" + row + col));
      }
    }  
    return this; 
  }
  
  @Override
  public Solution clone() throws CloneNotSupportedException {
    Solution cloned = (Solution)super.clone();
    List<Cell> clonedCells = new ArrayList<Cell>(); 
    for (Cell cell : cells) {
      clonedCells.add(cell.clone());
    }
    cloned.setCells(clonedCells);
    return cloned;
  }
  
  public List<Cell> getCells() {
    return cells;  
  }
  
  public void setCells(List<Cell> cells) {
    this.cells = cells;
  }
  
  public Solution withSudoku(Sudoku sudoku) {
    this.sudoku = sudoku;
    return this;
  }
}
