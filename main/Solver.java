package main;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Solver {
  
  private Sudoku sudoku;
  private boolean generating;
  private boolean verbose;
  private Solution multiSolution;
  
  public Solver(Sudoku sudoku) {
    this.sudoku = sudoku;
    multiSolution = null;
    verbose = false;
    generating = false;
  }
  
  public Solution solve() throws CloneNotSupportedException {
    return solve(sudoku.getSolution());
  }
  
  public Solution checkSolve() throws CloneNotSupportedException {
    return checkSolve(sudoku.getSolution());
  }
  
  public Solution solve(Solution solution) throws CloneNotSupportedException {
    return solve(parse(solution), 'f');
  }
  
  public Solution checkSolve(Solution solution) throws CloneNotSupportedException {
    multiSolution = null;
    return solve(parse(solution), 'c');
  }
  
  private Solution parse(Solution solution) throws CloneNotSupportedException {
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
  
  private Solution eliminate(Solution solution, int square, Integer digit) {
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
  
  private Solution solve(Solution solution, char solveType) throws CloneNotSupportedException {
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
      return solution; //Solution is solved, we are done!
    } else {
      if (verbose && !generating) {
        sudoku.getUI().display(solution);
      }
      List<Cell> cells = solution.getCells();
      //Choosing an unfilled square s with the fewest possible values
      int s = 0;
      int minValues = (int)Math.pow(sudoku.getDimensions(), 2);
      
      List<Integer> randSquares = new ArrayList<Integer>(sudoku.getSquares());
      //List<Integer> randSquares = new ArrayList<Integer>(purgeSquares(cells));
      //System.out.println("Size of randSquares: " + randSquares.size());
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
        randSquares.remove(randS); //randS needs to be an Integer object and not an int index or else this remove statement will remove the wrong element
      }
      List<Integer> randValues = new ArrayList<Integer>(cells.get(s).getValues());
      while (!randValues.isEmpty()) {
        Integer d = randValues.get(ThreadLocalRandom.current().nextInt(0, randValues.size()));
        Solution solClone = solve(assign(solution.clone(), s, d), solveType);
        if (!solClone.getContradiction()) {
          if (solveType == 'f' || (solution.getSolved() && compareSol(solClone, multiSolution)) ) {
            return solClone;
          } else {
            if (verbose && !generating) {
              System.out.format("Found a solution! Cell = %s, Digit = %d\n", cells.get(s).getName(), d);
              sudoku.getUI().display(solClone);
            }
            if (solution.getSolved()) {
              if (!generating) {
                System.out.println("Multiple solutions found!");
                sudoku.getUI().display(multiSolution);
              }
              solClone.setMulti(s, d);
              return solClone;
            } else {
              solution.setSolved(true);
              multiSolution = solClone;
            }
          }
        }
        randValues.remove(d);
      }
      if (solution.getSolved()) {
        return multiSolution;
      }
    }
    solution.setContradiction(true);
    return solution;
  }
  
  public Solution generate(char diff) throws CloneNotSupportedException {
    generating = true;
    int minStart;
    if (diff == 'h') {
      minStart = (int)Math.pow(sudoku.getDimensions(), 4)/4;
    } else if (diff == 'e') {
      minStart = (int)Math.pow(sudoku.getDimensions(), 4)/2;
    } else if (diff == 'm') {
      minStart = (int)Math.pow(sudoku.getDimensions(), 4)/5;
    } else {
      minStart = (int)Math.pow(sudoku.getDimensions(), 4)/3;
    } 
    if (sudoku.getDimensions() > 3) {
      minStart = (int)Math.pow(sudoku.getDimensions(), 4)*5/12; //For large sudokus, given too few starting squares are difficult to check for multiple solutions
    }
    if (verbose) {
      System.out.println("Generating a unique sudoku board...");
      System.out.flush();
    }
    Solution solution = new Solution(sudoku);
    solution.setCells(stringToCells(sudoku.getGrid("blank")));
    solution = solve(parse(solution));
    if (verbose) {
      System.out.println("Finished producing a sudoku board");
      System.out.flush();
    }
    List<Integer> randSquares = new ArrayList<Integer>(sudoku.getSquares());
    while (randSquares.size() > minStart)  {
      Integer square = randSquares.get(ThreadLocalRandom.current().nextInt(0, randSquares.size()));
      solution.getCells().get((int)square).getValues().clear();
      randSquares.remove(square);
    }
    if (verbose) {
      System.out.println("Checking for multiple solutions...");
      System.out.flush();
    }
    solution.setSolved(false);
    solution.initMulti();
    
    //Check whether the generated Sudoku yields a unique solution, if not, add the square responsible for multiple solutions
    int addedS = 0;
    if (diff != 'm') {
      Solution tempSolution;
      do {
        tempSolution = checkSolve(solution.clone());
        if (tempSolution.getSolved()) {
          solution.getCells().get(tempSolution.getMultiSquare()).setValue(tempSolution.getMultiVal());
          if (verbose) {
            System.out.format("Adding %d to square %s.\n", tempSolution.getMultiVal(), solution.getCells().get(tempSolution.getMultiSquare()).getName());
            System.out.flush();
          }
          ++addedS;
        } else {
          if (verbose) {
            System.out.format("Adding %d squares to yield unique solution.\n\n", addedS);
            System.out.flush();
          }
          break;
        }
      } while (tempSolution.getSolved());
    }
    if (verbose) {
      System.out.format("Generated sudoku with %d number of given squares.\n", (addedS + minStart));
    }
    generating = false;
    return solution;
  }
  
  public Solver stringToCells(List<Cell> cells, String grid) {
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
  
  public List<Cell> stringToCells(String grid) {
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
  
  private boolean compareSol(Solution sol1, Solution sol2) {
    for (int i = 0; i < (int)Math.pow(sudoku.getDimensions(),4); ++i) {
      if (sol1.getCells().get(i).getValues().get(0) != sol2.getCells().get(i).getValues().get(0)) {
        return false;
      }
    }
    return true;
  }
  
  public void setVerbose(Boolean verbose) {
    this.verbose = verbose;
  }
  
  private List<Integer> purgeSquares(List<Cell> cells) {
    List<Integer> result = new ArrayList<Integer>();
    for (Cell cell : cells) {
      if (cell.getValues().size() > 1) {
        result.add(cells.indexOf(cell));
      }
    }
    return result;
  }
  
  @Deprecated
  public Solver withSudoku(Sudoku sudoku) {
    this.sudoku = sudoku;
    return this;
  }
}