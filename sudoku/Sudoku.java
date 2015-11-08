package sudoku;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class Sudoku {

  //Temporary constant, to be converted into private integer variable 
  private final int dimensions = 3;
  
  private List<Character> rows, cols;
  private List<Cell> cells;
  private List<Unit> unitList;
  private Map<Cell, List<Unit>> unitMap; 
  private Map<Cell, List<Cell>> peerMap; 
 
  public static void main(String[] args) {
    new Sudoku().initialize().start();
  }
  
  /** Initializing the private variables */
  private Sudoku initialize(){
    int size = getDimensions();
    
    //Initializing rows and cols
    rows = new LinkedList<Character>();
    cols = new LinkedList<Character>();
    for (int i = 97; i < (97 + (int)Math.pow(size, 2)); ++i) {
      rows.add((char) i);
      cols.add((char) i);
    }
    
    //Initializing the list of cells
    cells = new LinkedList<Cell>();
    for (char row : rows) {
      for (char col : cols) {
        cells.add(new Cell().withSudoku(this).initialize());
        ((LinkedList<Cell>) cells).getLast().setName("" + row + col);
      }
    }
    assert cells.size() == (int)Math.pow(size, 4);
    
    //Initializing the list of units
    unitList = new LinkedList<Unit>();
    for (int row = 0; row < rows.size(); ++row) {
      unitList.add(new Unit().withSudoku(this).initialize(row, 1, 0, size*size));
    }      
    for (int col = 0; col < cols.size(); ++col) {
      unitList.add(new Unit().withSudoku(this).initialize(0, size*size, col, 1));
    }
    for (int row = 0; row < size; ++row) {
      for (int col = 0; col < size; ++col) {
        unitList.add(new Unit().withSudoku(this).initialize(row*size, size, col*size, size));
      }
    }
    assert unitList.size() == (int)Math.pow(size, 2)*3;
    
    //Initializing the mapping of each cell to its residing units
    unitMap = new HashMap<Cell, List<Unit>>();
    for (Cell cell : cells) {
      unitMap.put(cell, new LinkedList<Unit>());
      for (Unit unit : unitList) {
        if (unit.getCells().contains(cell)) {
          unitMap.get(cell).add(unit);
          if (unitMap.get(cell).size() == 3) {
            break;
          }
        }
      }
    }
    assert unitMap.get(cells.get(25)).size() == 3; //Cell chosen arbitrarily
    
    //Initializing the mapping of each cell to its peers
    peerMap = new HashMap<Cell, List<Cell>>();
    for (Cell cell : cells) {
      peerMap.put(cell, new LinkedList<Cell>());
      for (Unit u : unitMap.get(cell)) {
        for (Cell unitCell : u.getCells()) {
          if (unitCell != cell && !peerMap.get(cell).contains(unitCell)) {
            peerMap.get(cell).add(unitCell);
          }
        }  
      }
    }
    assert peerMap.get(cells.get(50)).size() == 3*((int)Math.pow(size, 2) - 1) - 2*(size-1); //Cell chosen arbitrarily
    
    return this;
  }
  
  private void start() {
    for (Cell cell : cells) {
      System.out.println("Square: " + cell.getName() + " " + cells.indexOf(cell) + " " + cell.getValues());
    }
    for (Unit unit : unitList) {
      for (Cell cell : unit.getCells()) {
        System.out.printf(cell.getName()+" ");
      }
      System.out.println("\nEnd of unit.");
    }
  }
  
  public int getDimensions() {
    return dimensions;
  }
  
  public List<Character> getRows() {
    return rows;
  }
  
  public List<Character> getCols() {
    return cols;
  }
  
  public List<Cell> getCells() {
    return cells;
  }
}
