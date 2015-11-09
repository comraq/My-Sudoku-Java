package sudoku;

import java.util.List;
import java.util.ArrayList;

public class Solver {
  
  private Sudoku sudoku;
  
  public List<Cell> parse(List<Cell> cells) {
    int size = sudoku.getDimensions();
    List<Cell> retCells = sudoku.initCells();
    for (Cell cell : retCells) {
      for (int i = 0; i < (int)Math.pow(size, 2); i++) {
        cell.getValues().add(i + 1);
      }
    }
    for (Cell cell : cells) {
      for (int digit : cell.getValues()) {
        if (sudoku.getDigits().contains(digit) && !assign(retCells, cell, digit)) {
          throw InvalidAssignmentException;
        }
      }
    }
    return retCells;
  }
  
  private Boolean assign(List<Cell> cells, Cell cell, int digit) {
    return true;
  }
  
  public Solver withSudoku(Sudoku sudoku) {
    this.sudoku = sudoku;
    return this;
  }
  
}