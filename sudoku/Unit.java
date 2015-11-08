package sudoku;

import java.util.List;
import java.util.ArrayList;

public class Unit {
  
  private List<Cell> cells;
  private Sudoku sudoku;
  
  public Unit initialize (int startRow, int numRows, int startCol, int numCols) {
    int size = sudoku.getDimensions();
    cells = new ArrayList<Cell>();
    for (int row = startRow; row < (startRow+numRows); ++row) {
      for (int col = startCol; col < (startCol+numCols); ++col) {
        cells.add(sudoku.getCells().get(row*(int)Math.pow(size, 2) + col));        
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
