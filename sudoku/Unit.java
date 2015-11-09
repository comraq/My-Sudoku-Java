package sudoku;

import java.util.List;
import java.util.ArrayList;

public class Unit {
  
  private List<Cell> cells;
  private Sudoku sudoku;
  
  public Unit initialize (List<Character> rows, List<Character> cols) {
    int size = sudoku.getDimensions();
    cells = new ArrayList<Cell>();
    for (char row : rows) {
      for (char col : cols) {
        cells.add(sudoku.getCells().get(sudoku.getRows().indexOf(row)*(int)Math.pow(size, 2) + sudoku.getCols().indexOf(col)));        
      }
    }
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
