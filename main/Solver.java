package main;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Solver {
  
  private Sudoku sudoku;
  private boolean generate;
  private boolean verbose;
  
  public Solver(Sudoku sudoku) {
    this.sudoku = sudoku;
    verbose = false;
    generate = false;
  }
  
  public Solution parse(Solution solution) throws CloneNotSupportedException {
    List<Cell> cells = solution.getCells();
    Map<Integer, Integer> assignMap = new HashMap<Integer, Integer>();
    for (int i = 0; i < cells.size(); ++i) {
      if (cells.get(i).getValues().size() == 1) {
        assignMap.put(i, cells.get(i).getValues().get(0));
      }
      cells.get(i).initValues();
    }
    for (Map.Entry<Integer, Integer> entry : assignMap.entrySet()) {
      if (sudoku.getDigits().contains(entry.getValue()) && assign(solution, entry.getKey(), entry.getValue()).getContradiction()) {
        /*if (verbose) {
          System.out.println("Invalid Assignment in Solver.parse()!");
        }*/
        return solution;
      }
    }
    return solution;
  }
  
  private Solution assign(Solution solution, int square, Integer digit) {
    List<Integer> elimList = new ArrayList<Integer>(solution.getCells().get(square).getValues());
    elimList.remove(digit);
    for (int elimDigit : elimList) {
      if (eliminate(solution, square, elimDigit).getContradiction()) { 
        return solution;
      }
    }
    return solution;
  }
  
  private Solution eliminate (Solution solution, int square, Integer digit) {
    List<Cell> cells = solution.getCells();
    if (!cells.get(square).getValues().contains(digit)) {
      return solution; //Indicating that digit d was already eliminated from the possible values of square s
    }
    cells.get(square).getValues().remove(digit);
    //Case 1) of Propagation
    if (cells.get(square).getValues().isEmpty()) {
      solution.setContradiction(true);
      return solution; //This is a contradiction as we just removed the last value
    } else if (cells.get(square).getValues().size() == 1) {
      for (int remainingDigit : cells.get(square).getValues()) {
        for (int peerSquare : sudoku.getPeerMap().get(square)) {
          if (eliminate(solution, peerSquare, remainingDigit).getContradiction()) {
            solution.setContradiction(true);
            return solution;
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
        return solution; //This is a contradiction as there is no available place for this digit in its unit
      } else if (places.size() == 1) {
        //Digit 'digit' only has one available place in its units, we will assign it there
        if (assign(solution, places.get(0), digit).getContradiction()) {
          solution.setContradiction(true);
          return solution;          
        }
      }  
    }
    return solution;
  }
  
  public Solution solve(Solution solution) throws CloneNotSupportedException {
    if (solution.getContradiction()) {
      return solution;
    }
    Boolean solved = true;
    for (Cell cell : solution.getCells()) {
      if (cell.getValues().size() != 1) {
        solved = false;
        break;
      }
    }
    if (solved) {
      solution.setSolved(true);
      return solution; //Solution is solved, we are done!
    } else {
      if (verbose && !generate) {
        sudoku.getUI().display(solution);
      }
      List<Cell> cells = solution.getCells();
      //Choosing an unfilled square s with the fewest possible values
      int s = 0;
      int minValues = (int)Math.pow(sudoku.getDimensions(), 2);
      List<Integer> randSquares = new ArrayList<Integer>(sudoku.getSquares());
      while (!randSquares.isEmpty()) {
        Integer randS = randSquares.get(ThreadLocalRandom.current().nextInt(0, randSquares.size()));
        if (cells.get(randS).getValues().size() > 1) {
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
      //System.out.println("s: " + cells.get(s).getName());
      List<Integer> randValues = new ArrayList<Integer>(cells.get(s).getValues());
      while (!randValues.isEmpty()) {
        Integer d = randValues.get(ThreadLocalRandom.current().nextInt(0, randValues.size()));
        //System.out.println("Cell: " + cells.get(s).getName() + " Assigning: " + d);
        Solution solClone = solution.clone();
        solClone = solve(assign(solClone, s, d));
        if (solClone.getSolved()) {
          //System.out.println("No contradiction, solved is: " + solClone.getSolved());
          return solClone;
        } else {
          randValues.remove(d);
        }
      }
    }
    solution.setContradiction(true);
    return solution;
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