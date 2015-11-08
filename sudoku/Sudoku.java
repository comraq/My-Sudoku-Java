package sudoku;

import java.util.ArrayList;
import java.util.HashMap;

public class Sudoku {

  public static final int dimensions = 3;
  
  private ArrayList<Character> rows, cols;
  private ArrayList<ArrayList<Cell>> unitList;
  private HashMap<Cell, ArrayList<Cell>> units, peers; 
  private ArrayList<Cell> cells;
 
  public static void main(String[] args) {
    new Sudoku().init(dimensions).start();
  }
  
  private Sudoku init(int size){
    rows = new ArrayList<Character>();
    cols = new ArrayList<Character>();
    for (int i = 97; i < (97 + Math.pow(size, 2)); ++i) {
      rows.add((char) i);
      cols.add((char) i);
    }
    
    cells = new ArrayList<Cell>();
    for (int i = 0; i < Math.pow(size, 4); ++i) {
      cells.add(new Cell(dimensions));
    }
    return this;
  }
  
  private void start() {
    for (char row : rows) {
      System.out.println(row);
    }  
  }
  
  public ArrayList<Character> getRows() {
    return rows;
  }
  
  public ArrayList<Character> getCols() {
    return cols;
  }
}
