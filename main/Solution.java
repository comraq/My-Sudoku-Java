package main;

import java.util.ArrayList;
import java.util.List;

public class Solution implements Cloneable{
  
  private List<Cell> cells;
  private Sudoku sudoku;
  private boolean contradiction;
  private boolean solved;
  private int multiSquare;
  private int multiVal;
  
  public Solution (Sudoku sudoku) {
    this.sudoku = sudoku;
    contradiction = false;
    solved = false;
    cells = sudoku.initCells();
    initMulti();
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
  
  public void setContradiction(Boolean contradiction) {
    this.contradiction = contradiction;
  }
  
  public Boolean getContradiction() {
    return contradiction;
  }
  
  public void setSolved(Boolean solved) {
    this.solved = solved;
  }
  
  public Boolean getSolved() {
    return solved;
  }

  public void setMulti(int multiSquare, int multiVal) {
    this.multiSquare = multiSquare;
    this.multiVal = multiVal;
  }
  
  public void initMulti() {
    multiSquare = 0;
    multiVal = 0;
  }
  
  public int getMultiSquare() {
    return multiSquare;
  }
  
  public int getMultiVal() {
    return multiVal;
  }
}
