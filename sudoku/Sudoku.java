package sudoku;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Sudoku {
  
  private final int dimensions = 3; //Temporary constant, to be converted into private integer variable 
  private final List<Integer> digits = new ArrayList<Integer>();
  private final List<Character> rows = new ArrayList<Character>();
  private final List<Character> cols = new ArrayList<Character>();
  private final List<Unit> unitList = new ArrayList<Unit>();
  private final Map<Cell, List<Unit>> unitMap = new HashMap<Cell, List<Unit>>(); 
  private final Map<Cell, List<Cell>> peerMap = new HashMap<Cell, List<Cell>>(); 
  private final Solver solver = new Solver();
  
  private List<Cell> cells;
 
  private String testGrid = "4 . . . . . 8 . 5 . 3 . . . . . . . . . . 7 . . . . . . 2 . . . . . 6 . . . . . 8 . 4 . . . . . . 1 . . . . . . . 6 . 3 . 7 . 5 . . 2 . . . . . 1 . 4 . . . . . .";
 
  public static void main(String[] args) throws Exception{
      new Sudoku().initialize().start();
  }
  
  /** Initializing the private fields */
  private Sudoku initialize(){
    for (int i = 0; i < (int)Math.pow(dimensions, 2); i++) {
      digits.add(i + 1); //Initializing the list of possible digits
    }
    for (int i = 97; i < (97 + (int)Math.pow(dimensions, 2)); ++i) {
      rows.add((char) i); //Initializing rows and cols
      cols.add((char) i);
    }
    cells = initCells();
    assert cells.size() == (int)Math.pow(dimensions, 4);
    initUnitList();
    assert unitList.size() == (int)Math.pow(dimensions, 2)*3;
    initUnitMap();
    assert unitMap.get(cells.get(25)).size() == 3; //Cell chosen arbitrarily
    initPeerMap();
    assert peerMap.get(cells.get(50)).size() == 3*((int)Math.pow(dimensions, 2) - 1) - 2*(dimensions-1); //Cell chosen arbitrarily
    
    solver.withSudoku(this);
    return this;
  }
  
  private void start() throws Exception {
    //cells = solver.stringToCellList(testGrid);
    solver.parse(cells);
    for (Cell cell : cells) {
      System.out.println("Square: " + cell.getName() + " " + cells.indexOf(cell) + " " + cell.getValues());
    }
    for (Unit unit : unitList) {
      for (Cell cell : unit.getCells()) {
        System.out.printf(cell.getName()+cell.getValues()+"");
      }
      System.out.println("\nEnd of unit.");
    }
    
    Cell tempCell = unitList.get(0).getCells().get(0);
    System.out.println("Cell: " + tempCell.getName() + " Digits: " + tempCell.getValues());
    cells.get(0).setValue(1);
    System.out.println("Cell: " + cells.get(0).getName() + " Digits: " + cells.get(0).getValues());
    System.out.println(cells.get(0) == tempCell);
    cells = dCopyCells(cells);
    cells.get(0).setValue(2);
    System.out.println("Cell: " + tempCell.getName() + " Digits: " + tempCell.getValues());
    System.out.println("Cell: " + cells.get(0).getName() + " Digits: " + cells.get(0).getValues());
    System.out.println(cells.get(0) == tempCell);
  }

  public int getDimensions() {
    return dimensions;
  }
  
  public List<Integer> getDigits() {
    return digits;
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
  
  public List<Unit> getUnitList() {
    return unitList;
  }
  
  public Map<Cell, List<Unit>> getUnitMap() {
    return unitMap;
  }
  
  public Map<Cell, List<Cell>> getPeerMap() {
    return peerMap;
  }
  
  public List<Cell> initCells() {
    List<Cell> retCells = new ArrayList<Cell>();
    for (char row : rows) {
      for (char col : cols) {
        retCells.add(new Cell().withSudoku(this).initialize("" + row + col));
      }
    }  
    return retCells; 
  }
  
  public List<Cell> initCells(List<Character> rows, List<Character> cols, List<Cell> toCells, List<Cell> fromCells) {
    for (char row : rows) {
      for (char col : cols) {
        toCells.add(fromCells.get(this.rows.indexOf(row)*(int)Math.pow(dimensions, 2) + this.cols.indexOf(col)));
      }
    }  
    return toCells; 
  }  
  
  public List<Cell> dCopyCells(List<Cell> original) {
    List<Cell> copy = new ArrayList<Cell>();
    Cell temp;
    for (Cell cell : original) {
      temp = new Cell().initialize(cell.getName());
      for (int i : cell.getValues()) {
        temp.addToValues(i);
      }
      copy.add(temp); 
    }
    return copy;
  }
  private final void initUnitList() {
    int size = dimensions;
    for (int row = 0; row < rows.size(); ++row) {
      unitList.add(new Unit().withSudoku(this).initialize(rows.subList(row, row+1), cols));
    }      
    for (int col = 0; col < cols.size(); ++col) {
      unitList.add(new Unit().withSudoku(this).initialize(rows, cols.subList(col, col+1)));
    }
    for (int row = 0; row < size; ++row) {
      for (int col = 0; col < size; ++col) {
        unitList.add(new Unit().withSudoku(this).initialize(rows.subList(row*dimensions, (row + 1)*dimensions), cols.subList(col*dimensions, (col+1)*dimensions)));
      }
    }
  }
  
  private final void initUnitMap() {
    for (Cell cell : cells) {
      unitMap.put(cell, new ArrayList<Unit>());
      for (Unit unit : unitList) {
        if (unit.getCells().contains(cell)) {
          unitMap.get(cell).add(unit);
          if (unitMap.get(cell).size() == 3) {
            break;
          }
        }
      }
    }    
  }
  
  private final void initPeerMap() {
    for (Cell cell : cells) {
      peerMap.put(cell, new ArrayList<Cell>());
      for (Unit u : unitMap.get(cell)) {
        for (Cell unitCell : u.getCells()) {
          if (unitCell != cell && !peerMap.get(cell).contains(unitCell)) {
            peerMap.get(cell).add(unitCell);
          }
        }  
      }
    }
  }
}
