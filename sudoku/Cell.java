package sudoku;

import java.util.List;
import java.util.ArrayList;

public class Cell {

  private List<Integer> values;
  private String name;
  private Sudoku sudoku;
  
  public Cell initialize(String name) {
    values = new ArrayList<Integer>();
    setName(name);
    return this;
  }
  
  public void setValues() {
    int size = sudoku.getDimensions();
    for (int i = 0; i < (int)Math.pow(size, 2); i++) {
      values.add(i + 1);
    }   
  }
  
  public void setValues(int value) {
    values.add(value);
  }
  
  public List<Integer> getValues() {
    return values;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public Cell withSudoku(Sudoku sudoku) {
    this.sudoku = sudoku;
    return this;
  }
}
