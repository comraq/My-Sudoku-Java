package main;

import java.awt.LayoutManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainUI extends JFrame {

  private Sudoku sudoku;
  private Solver solver;
  private JPanel buttonPanel, gamePanel;  
  private LayoutManager layout;
  
  private int dimension;
  
  public static void main(String[] args) throws CloneNotSupportedException {
    Sudoku sudoku = new Sudoku().initialize();
    
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        sudoku.getMainUI().init();
        sudoku.getMainUI().setVisible(true);
      }
    });
  }
  
  public MainUI(Sudoku sudoku) {
    this.sudoku = sudoku;
    solver = sudoku.getSolver();
    
  }
  
  private void init() {
    gamePanel = new GamePanel(this);
    buttonPanel = new ButtonPanel(this);
    
    dimension = sudoku.getDimensions();
    setTitle(dimension + " x " + dimension + " Sudoku");
    setSize(400,400);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    initLayout();
    addComponents();
    
    setName("sudoku.mainUI"); 
  }
  
  private void initLayout() {
    layout = new BoxLayout(getContentPane(), BoxLayout.X_AXIS);
    setLayout(layout);
  }
  
  private void addComponents() {
    add(gamePanel);
    add(buttonPanel);    
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
        sudoku.initialize(Integer.parseInt(input));
        System.out.println("Please select puzzle (ex: e = easy, n = normal, h = hard, or nothing for an empty puzzle): ");
        input = reader.readLine();
        if (input.contains("q")) {
          return;
        } else if (input.contains("v")) {
          solver.setVerbose(true);
        }
        
        if (input.contains("e")) {
          sudoku.setSolution(solver.generate('e'));
        } else if (input.contains("n")) {
          sudoku.setSolution(solver.generate('n'));  
        } else if (input.contains("h")) {
          sudoku.setSolution(solver.generate('h'));
        } else if (input.contains("m")) {
          sudoku.setSolution(solver.generate('m'));
        } else if (input.contains("1")) {
          sudoku.setSolution(new Solution(sudoku));
          sudoku.getSolution().setCells(solver.stringToCells(sudoku.getGrid("test")));
        } else if (input.contains("2")) {
          sudoku.setSolution(new Solution(sudoku));
          sudoku.getSolution().setCells(solver.stringToCells(sudoku.getGrid("multi")));
        } else {
          sudoku.setSolution(new Solution(sudoku));
          sudoku.getSolution().setCells(solver.stringToCells(sudoku.getGrid("blank")));          
        }
        display();
        solver.setVerbose(false);
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
          if (input.contains("v")) {
            solver.setVerbose(true);
          } 
          if (input.contains("c")) {
            sudoku.setSolution(solver.checkSolve());
          } else {
            sudoku.setSolution(solver.solve());
          }
          display();
          break;
        }
      } catch (IOException e) {
        System.err.println("Error reading input.");
      }
    } 
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
    //delay();
  }
  
  
  protected void generate() {
    
  }
  
  protected void reset() {
    
  }

  protected void check() {
    
  }

  protected void hint() {
    
  }
  
  private void delay() {
    try {
      TimeUnit.MILLISECONDS.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  public Sudoku getSudoku() {
    return sudoku;  
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
