package main;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainUI {

  private Sudoku sudoku;
  
  public MainUI (Sudoku sudoku) {
    this.sudoku = sudoku;
  }
  
  public void display() {
    output(sudoku.getSolution().getCells());
  }
  
  public void display(Solution solution) {
    output(solution.getCells());
  }
  
  public void output(List<Cell> cells) {
    int dimension = sudoku.getDimensions();
    int width = 1;
    for (Cell cell : cells) {
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
        String valStr = joinList(cells.get(r*(int)Math.pow(dimension, 2)+ c).getValues(), " ");
        System.out.print("(" + valStr + ")");
        for (int i = 0; i < (width - 2 - valStr.length()); ++i) {
          System.out.print(' ');
        }
      }
      System.out.println();
    }
    System.out.println();
    System.out.flush();
    //pause();
  }
  
  private void pause() {
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
