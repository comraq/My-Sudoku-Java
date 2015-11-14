package main;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Sudoku {
  
  private final List<Integer> digits;
  private final List<Character> rows;
  private final List<Character> cols; 
  private final List<List<Integer>> unitList;
  private final Map<Integer, List<List<Integer>>> unitMap;
  private final Map<Integer, List<Integer>> peerMap;
  private final Solver solver;
  private final MainUI ui;
  
  private int dimensions;
  private List<Integer> squares;
  private Solution solution;

  private String testGrid = "4 . . . . . 8 . 5 . 3 . . . . . . . . . . 7 . . . . . . 2 . . . . . 6 . . . . . 8 . 4 . . . . . . 1 . . . . . . . 6 . 3 . 7 . 5 . . 2 . . . . . 1 . 4 . . . . . .";
  private String multiGrid = ". . . . . 6 . . . . 5 9 . . . . . 8 2 . . . . 8 . . . . 4 5 . . . . . . . . 3 . . . . . . . . 6 . . 3 . 5 4 . . . 3 2 5 . . 6 . . . . . . . . . . . . . . . . . .";
  private String blank;
  
  public static void main(String[] args) throws CloneNotSupportedException{
    new Sudoku().start();
  }
  
  public Sudoku() {
    digits = new ArrayList<Integer>();
    rows = new ArrayList<Character>();
    cols = new ArrayList<Character>(); 
    unitList = new ArrayList<List<Integer>>();
    unitMap = new HashMap<Integer, List<List<Integer>>>();
    peerMap = new HashMap<Integer, List<Integer>>();
    solver = new Solver(this);
    ui = new MainUI(this);
  }
  
  public Sudoku initialize(int dimensions){
    //Initializing the private fields
    
    this.dimensions = dimensions; //Default is 3 unless later changed
    digits.clear();
    for (int i = 0; i < (int)Math.pow(dimensions, 2); i++) {
      digits.add(i + 1); //Initializing the list of possible digits
    }
    rows.clear();
    cols.clear();
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
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Enter q at any time to quit...");
    while (true) {
      try {
        System.out.print("Please enter size: ");
        String input = reader.readLine();
        if (input.contains("q")) {
          return;
        }
        initialize(Integer.parseInt(input));
        System.out.println("Please select puzzle (ex: e = easy, n = normal, h = hard, or nothing for an empty puzzle): ");
        input = reader.readLine();
        if (input.contains("q")) {
          return;
        } else if (input.contains("d")) {
          solver.setVerbose(true);
        }
        if (input.contains("e")) {
          solution = solver.generate('e');
        } else if (input.contains("n")) {
          solution = solver.generate('n');  
        } else if (input.contains("h")) {
          solution = solver.generate('h');
        } else if (input.contains("m")) {
          solution = solver.generate('m');
        } else if (input.contains("1")) {
          solution.setCells(solver.stringToCells(testGrid));
        } else if (input.contains("2")) {
          solution.setCells(solver.stringToCells(multiGrid));
        } else {
          solution.setCells(solver.stringToCells(blank));          
        }
        ui.display();
        solver.setVerbose(false);
        //System.out.format("Dimensions: %d cells.size(): %d values.size(): %d\n", dimensions, solution.getCells().size(), solution.getCells().get(0).getValues().size());
        System.out.println("Press Enter to solve puzzle or s to select another Sudoku: ");
        System.out.println("Please select options:\n"
                         + "Include flags? (optional)\n"
                         + "d = display steps\n"
                         + "c = check solve\n"
                         + "f = fast solve");
        input = reader.readLine();
        if (input.contains("q")) {
          return;
        } else if (!input.contains("s")) {
          if (input.contains("d")) {
            solver.setVerbose(true);
          }
          if (input.contains("c")) {
            solution = solver.checkSolve();
          } else {
            solution = solver.solve();
          }
          ui.display();
          return;
        }
      } catch (IOException e) {
        System.out.println("Error reading input.");
      }
    }  
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
  
  public Solver getSolver() {
    return solver;
  }
  
  public MainUI getUI() {
    return ui;
  }
  
  public String getGrid(String grid) {
    if (grid == "test") {
      return testGrid;
    } else if (grid == "multi") {
      return multiGrid;
    } else {
      return blank;
    }
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
  
  private List<Integer> initSquares(List<Character> rows, List<Character> cols) {
    List<Integer> retSquares = new ArrayList<Integer>();
    for (char row : rows) {
      for (char col : cols) {
        retSquares.add(this.rows.indexOf(row)*(int)Math.pow(dimensions, 2) + this.cols.indexOf(col));
      }
    }  
    return retSquares;
  }

  private final void initUnitList() {
    unitList.clear();
    for (int row = 0; row < rows.size(); ++row) {
      unitList.add(initSquares(rows.subList(row, row+1), cols));
    }      
    for (int col = 0; col < cols.size(); ++col) {
      unitList.add(initSquares(rows, cols.subList(col, col+1)));
    }
    for (int row = 0; row < dimensions; ++row) {
      for (int col = 0; col < dimensions; ++col) {
        unitList.add(initSquares(rows.subList(row*dimensions, (row + 1)*dimensions), cols.subList(col*dimensions, (col+1)*dimensions)));
      }
    }
  }
  
  private final void initUnitMap() {
    unitMap.clear();
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
    peerMap.clear();
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
