package main;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel {

  private static final int BUTTON_WIDTH = 96;
  private static final int BUTTON_HEIGHT = 54;
  private int buttonCount;
  private LayoutManager layout;
  
  private MainUI ui;
  
  private JButton generateButton;
  private JButton resetButton;
  private JButton checkButton;
  private JButton hintButton;
  private JButton quitButton;
  
  public ButtonPanel(MainUI ui) {
    this.ui = ui;
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
    setPanelSize();
    initGenerateButton();
    initResetButton();
    initCheckButton();
    initHintButton();
    initQuitButton(); 
    
    setName("sudoku.buttonPanel"); 
  }  
  
  private void initLayout() {
    layout = new BoxLayout(this, BoxLayout.Y_AXIS);
    setLayout(layout);
  }
  
  private void initGenerateButton() {
    assert generateButton != null;
    generateButton.setAlignmentX(CENTER_ALIGNMENT);
    generateButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ui.generate();
      }
    });
    generateButton.setName("sudoku.generate");
    add(Box.createRigidArea(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT/4)));
    add(generateButton);
    add(Box.createRigidArea(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT/4)));
    ++buttonCount; 
  }
  
  private void initResetButton() {
    assert resetButton != null;
    resetButton.setAlignmentX(CENTER_ALIGNMENT);
    resetButton.setEnabled(false);
    resetButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ui.reset();
      }
    });
    resetButton.setName("sudoku.reset");
    add(Box.createRigidArea(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT/4)));
    add(resetButton);
    add(Box.createRigidArea(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT/4)));
    ++buttonCount; 
  }
  
  private void initCheckButton() {
    assert checkButton != null;
    checkButton.setAlignmentX(CENTER_ALIGNMENT);
    checkButton.setEnabled(false);
    checkButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ui.check();
      }
    });
    checkButton.setName("sudoku.check");
    add(Box.createRigidArea(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT/4)));
    add(checkButton);
    add(Box.createRigidArea(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT/4)));
    ++buttonCount; 
  }

  private void initHintButton() {
    assert hintButton != null;
    hintButton.setAlignmentX(CENTER_ALIGNMENT);
    hintButton.setEnabled(false);
    hintButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ui.hint();
      }
    });
    hintButton.setName("sudoku.hint");
    add(Box.createRigidArea(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT/4)));
    add(hintButton);
    add(Box.createRigidArea(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT/4)));
    ++buttonCount; 
  }
  
  private void initQuitButton() {
    assert quitButton != null;
    quitButton.setAlignmentX(CENTER_ALIGNMENT);
    quitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
    quitButton.setName("sudoku.quit");
    add(Box.createRigidArea(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT/4)));
    add(quitButton);
    add(Box.createRigidArea(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT/4)));
    ++buttonCount; 
  }
  
  private void setPanelSize() {
    setSize(BUTTON_WIDTH, BUTTON_HEIGHT * 2 * buttonCount);
  }
}
