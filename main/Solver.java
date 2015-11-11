package main;

import java.util.List;
import java.util.ArrayList;

public class Solver {
  
  private Sudoku sudoku;
  private List<Cell> cells;
  
  public Solver parse(Solution solution) throws Exception {
    cells = solution.getCells();
    List<Integer> assignList = new ArrayList<Integer>();
    for (int i = 0; i < cells.size(); ++i) {
      if (cells.get(i).getValues().size() == 0) {
        cells.get(i).initValues();
      } else {
        assignList.add(i);
      }
    }
    for (int cellIndex : assignList) {
      for (int digit : cells.get(cellIndex).getValues()) {
        if (sudoku.getDigits().contains(digit) && !assign(solution, cells.get(cellIndex), digit)) {
          throw new Exception("Invalid Assignment in Solver.parse()!"); 
        }
      }  
    }
    return this;
  }
  
  private boolean assign(Solution solution, Cell cell, Integer digit) {
    cells = solution.getCells();
    List<Integer> elimList = new ArrayList<Integer>(cell.getValues());
    elimList.remove(digit);
    for (int elimDigit : elimList) {
      if (!eliminate(solution, cell, elimDigit)) {
        return false;
      }
    }
    return true;
  }
  
  private boolean eliminate (Solution solution, Cell cell, Integer digit) {
    return false;
  }
  
  public Solver withSudoku(Sudoku sudoku) {
    this.sudoku = sudoku;
    return this;
  }
  
  public Solver stringToCellList(List<Cell> cells, String grid) {
    String s_val = "";
    int cListIndex = 0;
    for (char c : grid.toCharArray()) {
      if (sudoku.getDigits().contains(Character.getNumericValue(c))) {
        s_val += c;
      } else if (c == ' ') {
        if (s_val != "") {
          cells.get(cListIndex).getValues().add(Integer.parseInt(s_val));
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
          cList.get(cListIndex).getValues().add(Integer.parseInt(s_val));
        }
        s_val = "";
        ++cListIndex;
      }
    }
    return cList;
  }
}