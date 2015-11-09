package sudoku;

import java.util.List;
import java.util.ArrayList;

public class Solver {
  
  private Sudoku sudoku;
  
  public List<Cell> parse(List<Cell> cells) throws Exception {
    for (Cell cell : cells) {
      if (cell.getValues().size() == 0) {
        cell.setValues();
      }
    }
    for (Cell cell : cells) {
      for (int digit : cell.getValues()) {
        if (sudoku.getDigits().contains(digit) && !assign(cells, cell, digit)) {
          throw new Exception("Invalid Assignment in Solver.parse()!"); 
        }
      }  
    }
    return cells;
  }
  
  public Solver stringToCellList(List<Cell> cells, String grid) {
    String s_val = "";
    int cListIndex = 0;
    for (char c : grid.toCharArray()) {
      if (sudoku.getDigits().contains(Character.getNumericValue(c))) {
        s_val += c;
      } else if (c == ' ') {
        if (s_val != "") {
          cells.get(cListIndex).addToValues(Integer.parseInt(s_val));
        }
        s_val = "";
        ++cListIndex;
      }
    }
    return this;
  }
  
  public List<Cell> stringToCellList(String grid) {
    List<Cell> cList = sudoku.initCells();
    String s_val = "";
    int cListIndex = 0;
    for (char c : grid.toCharArray()) {
      if (sudoku.getDigits().contains(Character.getNumericValue(c))) {
        s_val += c;
      } else if (c == ' ') {
        if (s_val != "") {
          cList.get(cListIndex).addToValues(Integer.parseInt(s_val));
        }
        s_val = "";
        ++cListIndex;
      }
    }
    return cList;
  }
  
  private Boolean assign(List<Cell> cells, Cell cell, int digit) {
    return true;
  }
  
  public Solver withSudoku(Sudoku sudoku) {
    this.sudoku = sudoku;
    return this;
  }
  
}