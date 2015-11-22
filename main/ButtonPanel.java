package main;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;

import main.SudokuInteractor.SudokuState;

public class ButtonPanel extends JPanel implements Observer {

  private static final int BUTTON_WIDTH = 96;
  private static final int BUTTON_HEIGHT = 54;
  private int buttonCount;
  private LayoutManager layout;
  
  private Sudoku sudoku;
  private SudokuInteractor interactor;
  
  private JButton generateButton;
  private JButton resetButton;
  private JButton checkButton;
  private JButton hintButton;
  private JButton quitButton;
  
  public ButtonPanel(Sudoku sudoku) {
    this.sudoku = sudoku;
    interactor = sudoku.getSudokuInteractor();
    interactor.addObserver(this);
    buttonCount = 0;
    initialize();
  }

  private void initialize() {
    generateButton = new JButton("Generate");
    resetButton = new JButton("Reset");
    checkButton = new JButton("Check");
    hintButton = new JButton("Hint");
    quitButton = new JButton("Quit");
    
    initLayout();
    addComponents();
    
    setOpaque(true);
    setBackground(new Color(250, 255, 0));    
    
    setName("main.buttonPanel"); 
  }  
  
  private void initLayout() {
    layout = new GridBagLayout();
    setLayout(layout);
  }
  
  private void addComponents() {
    GridBagConstraints con = new GridBagConstraints();
    
    con.gridx = 0;
    con.gridy = 0;
    initGenerateButton(con);
    
    con.insets = new Insets(BUTTON_HEIGHT/2, 0, 0, 0);
    ++con.gridy;
    initResetButton(con);
    
    ++con.gridy;
    initCheckButton(con);
    
    ++con.gridy;
    initHintButton(con);
    
    ++con.gridy;
    initQuitButton(con);
  }
  
  @Override
  public void update(Observable o, Object arg) {
    SudokuState state = interactor.getCurrentState();
    if (state == SudokuState.GENERATED) {
      resetButton.setEnabled(false);
      checkButton.setEnabled(false);
      hintButton.setEnabled(true);
    } else if (state == SudokuState.PLAYING) {
      resetButton.setEnabled(true);
      checkButton.setEnabled(true);
      hintButton.setEnabled(true);
    } else {
      resetButton.setEnabled(true);
      checkButton.setEnabled(false);
      hintButton.setEnabled(false);
    }
  }
      
  private void initGenerateButton(GridBagConstraints con) {
    assert generateButton != null;
    generateButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        interactor.generate();
      }
    });
    generateButton.setName("sudoku.generate");
    add(generateButton, con);
    ++buttonCount; 
  }
  
  private void initResetButton(GridBagConstraints con) {
    assert resetButton != null;
    resetButton.setEnabled(false);
    resetButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        interactor.reset();
      }
    });
    resetButton.setName("main.reset");
    add(resetButton, con);
    ++buttonCount; 
  }
  
  private void initCheckButton(GridBagConstraints con) {
    assert checkButton != null;
    checkButton.setEnabled(false);
    checkButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        interactor.check();
      }
    });
    checkButton.setName("main.check");
    add(checkButton, con);
    ++buttonCount; 
  }

  private void initHintButton(GridBagConstraints con) {
    assert hintButton != null;
    hintButton.setEnabled(false);
    hintButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        interactor.hint();
      }
    });
    hintButton.setName("main.hint");
    add(hintButton, con);
    ++buttonCount; 
  }
  
  private void initQuitButton(GridBagConstraints con) {
    assert quitButton != null;
    quitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
    quitButton.setName("main.quit");
    add(quitButton, con);
    ++buttonCount; 
  }
}
