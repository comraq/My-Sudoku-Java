package sudoku;

import java.util.List;
import java.util.ArrayList;

public class Unit {
  
  private List<Cell> cells;
  private Sudoku sudoku;
  
  public Unit initialize (List<Character> rows, List<Character> cols) {
    cells = sudoku.initCells(rows, cols, new ArrayList<Cell>(), sudoku.getCells());
    return this;    
  }
  
  public List<Cell> getCells() {
    return cells;
  }
  public Unit withSudoku(Sudoku sudoku) {
    this.sudoku = sudoku;
    return this;
  }
}
