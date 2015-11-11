package main;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainUI {

  private Sudoku sudoku;
  private Solution solution;
  
  public MainUI (Sudoku sudoku) {
    this.sudoku = sudoku;
  }
  
  public void display() {
    solution = sudoku.getSolution();
    int dimension = sudoku.getDimensions();
    int width = 0;
    for (Cell cell : solution.getCells()) {
      if (cell.getValues().size() > width) {
        width = cell.getValues().size();
        if (width == (int)Math.pow(dimension, 2)) {
          break;
        }
      }
    }
    width = 2*width + 2;
    String line = "";
    for (int i = 0; i < width*(int)Math.pow(dimension, 2); ++i) {
      if ((i != 0) && (i %(width*dimension) == 0)) {
        line += "+";
      }
      line += "-";
    }
    for (int r = 0; r < sudoku.getRows().size(); ++r) {
      if ((r != 0) && (r%dimension == 0)) {
        System.out.println(line);
      }
      for (int c = 0; c < sudoku.getCols().size(); ++c) {
        if ((c != 0) && (c%dimension == 0)) {
          System.out.print('|');
        }
        System.out.print("(" + joinList(solution.getCells().get(r*(int)Math.pow(dimension, 2)+ c).getValues(), " ") + ") ");
      }
      System.out.println();
    }
    System.out.flush();
    try {
      TimeUnit.MILLISECONDS.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  public <T> String joinList(List<T> list, String s) {
    if (list.isEmpty()) {
      return s;
    }
    String str = "";
    for (T item : list) {
      str += (s + item);
    }
    return str.substring(1);
  }
  
  @Deprecated
  public MainUI withSudoku(Sudoku sudoku) {
    this.sudoku = sudoku;
    return this;
  }
}
