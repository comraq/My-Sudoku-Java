package main;

import java.util.List;
import java.util.ArrayList;

public class Solver {
  
  private Sudoku sudoku;
  
  public Solver parse(List<Cell> cells) {
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
        if (sudoku.getDigits().contains(digit)) {
          assign(cells, cells.get(cellIndex), digit);
        }
      }  
    }
    return this;
  }
  
  private List<Cell> assign(List<Cell> cells, Cell cell, int digit) {
    return cells;
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