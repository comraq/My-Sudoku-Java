package main;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Sudoku {
  
  private final int dimensions = 3; //Temporary constant, to be converted into private integer variable 
  private final List<Integer> digits = new ArrayList<Integer>();
  private final List<Character> rows = new ArrayList<Character>();
  private final List<Character> cols = new ArrayList<Character>(); 
  private final List<List<Integer>> unitList = new ArrayList<List<Integer>>();
  private final Map<Integer, List<List<Integer>>> unitMap = new HashMap<Integer, List<List<Integer>>>();
  private final Map<Integer, List<Integer>> peerMap = new HashMap<Integer, List<Integer>>();
  private final Solver solver = new Solver(this);
  private final MainUI ui = new MainUI(this);
  
  private List<Integer> squares;
  private Solution solution;

  private String testGrid = "4 . . . . . 8 . 5 . 3 . . . . . . . . . . 7 . . . . . . 2 . . . . . 6 . . . . . 8 . 4 . . . . . . 1 . . . . . . . 6 . 3 . 7 . 5 . . 2 . . . . . 1 . 4 . . . . . .";
  private String blank;
  
  
  /** Initializing the private fields */
  public Sudoku initialize(){
    for (int i = 0; i < (int)Math.pow(dimensions, 2); i++) {
      digits.add(i + 1); //Initializing the list of possible digits
    }
    for (int i = 97; i < (97 + (int)Math.pow(dimensions, 2)); ++i) {
      rows.add((char) i); //Initializing rows and cols
      cols.add((char) i);
    }

    squares = initSquares(rows, cols);
    assert squares.size() == (int)Math.pow(dimensions, 4);
    solution = new Solution(this);
    assert solution.getCells().size() == (int)Math.pow(dimensions, 4);
    initUnitList();
    assert unitList.size() == (int)Math.pow(dimensions, 2)*3;
    initUnitMap();
    assert unitMap.get(squares.get(25)).size() == 3; //Cell chosen arbitrarily
    initPeerMap();
    assert peerMap.get(squares.get(50)).size() == 3*((int)Math.pow(dimensions, 2) - 1) - 2*(dimensions-1); //Cell chosen arbitrarily
    
    for (blank = "."; blank.length() < (int)Math.pow(dimensions, 4); blank += " .") {
      //Initializing the blank string/grid
    }
    return this;
  }
  
  public void start() throws CloneNotSupportedException {
    solution.setCells(solver.stringToCellList(blank));
    solver.parse(solution);
    //solver.setVerbose(true);
    solution = solver.solve(solution);
    ui.display();
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
  
  public List<Integer> getSquares() {
    return squares;
  }
  
  public List<List<Integer>> getUnitList() {
    return unitList;
  }
  
  public Map<Integer, List<List<Integer>>> getUnitMap() {
    return unitMap;
  }
  
  public Map<Integer, List<Integer>> getPeerMap() {
    return peerMap;
  }
  
  public Solution getSolution() {
    return solution;
  }
  
  public MainUI getUI() {
    return ui;
  }
  
  public List<Cell> initCells() {
    List<Cell> retCells = new ArrayList<Cell>();
    for (char row : rows) {
      for (char col : cols) {
        retCells.add(new Cell(this, "" + row + col));
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
  
  public List<Integer> initSquares(List<Character> rows, List<Character> cols) {
    List<Integer> retSquares = new ArrayList<Integer>();
    for (char row : rows) {
      for (char col : cols) {
        retSquares.add(this.rows.indexOf(row)*(int)Math.pow(dimensions, 2) + this.cols.indexOf(col));
      }
    }  
    return retSquares;
  }

  private final void initUnitList() {
    int size = dimensions;
    for (int row = 0; row < rows.size(); ++row) {
      unitList.add(initSquares(rows.subList(row, row+1), cols));
    }      
    for (int col = 0; col < cols.size(); ++col) {
      unitList.add(initSquares(rows, cols.subList(col, col+1)));
    }
    for (int row = 0; row < size; ++row) {
      for (int col = 0; col < size; ++col) {
        unitList.add(initSquares(rows.subList(row*dimensions, (row + 1)*dimensions), cols.subList(col*dimensions, (col+1)*dimensions)));
      }
    }
  }
  
  private final void initUnitMap() {
    for (int square : squares) {
      unitMap.put(square, new ArrayList<List<Integer>>());
      for (List<Integer> unit : unitList) {
        if (unit.contains(square)) {
          unitMap.get(square).add(unit);
          if (unitMap.get(square).size() == 3) {
            break;
          }
        }
      }
    }    
  }
  
  private final void initPeerMap() {
    for (int square : squares) {
      peerMap.put(square, new ArrayList<Integer>());
      for (List<Integer> unit : unitMap.get(square)) {
        for (int unitSquare : unit) {
          if (unitSquare != square && !peerMap.get(square).contains(unitSquare)) {
            peerMap.get(square).add(unitSquare);
          }
        }  
      }
    }
  }
}
