package main;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

public class Solver {
  
  private Sudoku sudoku;
  private boolean generate;
  private boolean verbose;
  
  public Solver(Sudoku sudoku) {
    this.sudoku = sudoku;
    verbose = false;
    generate = false;
  }
  
  public Solver parse(Solution solution) {
    List<Cell> cells = solution.getCells();
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
          /*if (verbose) {
            System.out.println("Invalid Assignment in Solver.parse()!");
          }*/
          return this;
        }
      }  
    }
    return this;
  }
  
  private boolean assign(Solution solution, Cell cell, Integer digit) {
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
    if (!cell.getValues().contains(digit)) {
      return true;
    }
    int square = solution.getCells().indexOf(cell);
    List<Cell> cells = solution.getCells();
    cell.getValues().remove(digit);
    //Case 1) of Propagation
    if (cell.getValues().isEmpty()) {
      solution.setContradiction(true);
      return false; //This is a contradiction as we just removed the last value
    } else if (cell.getValues().size() == 1) {
      for (int remainingDigit : cell.getValues()) {
        for (int peerSquare : sudoku.getPeerMap().get(square)) {
          if (!eliminate(solution, cells.get(peerSquare), remainingDigit)) {
            solution.setContradiction(true);
            return false;
          }
        }
      }
    }
    //Case 2) of Propagation
    List<Integer> places = new ArrayList<Integer>();
    for (List<Integer> unit : sudoku.getUnitMap().get(square)) {
      for (int s : unit) {
        if (cells.get(s).getValues().contains(digit)) {
          places.add(s);
          if (places.size() > 1) {
            break; //Cannot eliminate as digit has more than 1 possible place within this unit
          }
        }
      }
      if (places.isEmpty()) {
        solution.setContradiction(true);
        return false; //This is a contradiction as there is no available place for this digit in its unit
      } else if (places.size() == 1) {
        //Digit 'digit' only has one available place in its units, we will assign it there
        if (!assign(solution, cells.get(places.get(0)), digit)) {
          solution.setContradiction(true);
          return false;          
        }
      }  
    }
    return true;
  }
  
  public boolean solve(Solution solution) throws CloneNotSupportedException {
    if (solution.getContradiction()) {
      return false;
    }
    if (solution.getSolved()) {
      return true; //Solution is solved, we are done!
    } else {
      if (verbose && !generate) {
        sudoku.getUI().display();
      }
      List<Cell> cells = solution.getCells();
      //Choosing an unfilled square s with the fewest possible values
      int s = 0;
      int minValues = (int)Math.pow(sudoku.getDimensions(), 2);
      List<Integer> randSquares = new ArrayList<Integer>(sudoku.getSquares());
      while (!randSquares.isEmpty()) {
        int randS = ThreadLocalRandom.current().nextInt(0, randSquares.size());
        if (cells.get(randS).getValues().size() != 1) {
          if (cells.get(randS).getValues().size() == 2) {
            s = randS;
            break;
          } else if (cells.get(randS).getValues().size() < minValues) {
            minValues = cells.get(randS).getValues().size();
            s = randS;
          } 
        }
        randSquares.remove(randS);
      }
      List<Integer> randValues = new ArrayList<Integer>(cells.get(s).getValues());
      while (!randValues.isEmpty()) {
        Integer d = randValues.get(ThreadLocalRandom.current().nextInt(0, randValues.size()));
        Solution solClone = solution.clone();
        if (assign(solClone, cells.get(s), d)) {
          if (solve(solClone)) {
            solution = solClone;
          } else {
            randValues.remove(d);
          }
        }
      }
    }
    return true;
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
  
  public void setGenerate(Boolean generate) {
    this.generate = generate;
  }
  
  public void setVerbose(Boolean verbose) {
    this.verbose = verbose;
  }
  
  @Deprecated
  public Solver withSudoku(Sudoku sudoku) {
    this.sudoku = sudoku;
    return this;
  }
}