package sudoku;

import java.util.ArrayList;
import java.util.HashMap;

public class Cell {

  private ArrayList<Integer> values;

  public Cell (int size) {
    values = new ArrayList<Integer>();
    for (int i = 1; i < Math.pow(size, 2); i++) {
      values.add(i);
    }
  }
  
  public ArrayList<Integer> getValues() {
    return values;
  }


}
