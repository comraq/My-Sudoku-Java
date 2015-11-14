package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Before;
import org.junit.Test;

import main.*;

public class FundamentalTest {

  private Sudoku sudoku;
  private Solution solution;
  
  @Before
  public void setUp() {
    sudoku = new Sudoku().initialize(3);
    solution = sudoku.getSolution();
  }
  
  @Test
  public void sanityTest() {
    
  }
  
  @Test
  public void cloneTest() throws CloneNotSupportedException {
    solution.getCells().get(0).setValue(7);
    solution.setSolved(true);
    Solution solutionClone = solution.clone();
    Cell cell1 = solution.getCells().get(0);
    Cell cell2 = solutionClone.getCells().get(0);
    
    assertNotSame(solutionClone, solution);
    assertNotSame(cell1, cell2);
    assertNotSame(cell1.getValues(), cell2.getValues());
    assertEquals(cell1.getValues().get(0), cell2.getValues().get(0));
    assertEquals(solution.getSolved(), solutionClone.getSolved());
    assertEquals(solution.getMultiSquare(), solutionClone.getMultiSquare());
    assertEquals(solution.getMultiVal(), solutionClone.getMultiVal());
    
    cell1.setValue(1);
    solution.setSolved(!solutionClone.getSolved());
    solution.setMulti(2, 2);
    
    assertNotSame(cell1.getValues().get(0), cell2.getValues().get(0));
    assertNotSame(solution.getSolved(), solutionClone.getSolved());
    assertNotSame(solution.getMultiSquare(), solutionClone.getMultiSquare());
  }
  
}
