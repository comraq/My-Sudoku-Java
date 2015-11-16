package main;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel {

  private static final int BUTTON_WIDTH = 96;
  private static final int BUTTON_HEIGHT = 54;
  private int buttonCount;
  
  private MainUI ui;
  
  private JButton generateButton;
  private JButton resetButton;
  private JButton quitButton;
  private JButton hintButton;
  
  public ButtonPanel(MainUI ui) {
    this.ui = ui;
    buttonCount = 0;
    
  }

  public JPanel initialize() {
    initLayout();
    
    generateButton = new JButton("Generate");
    resetButton = new JButton("Reset");
    quitButton = new JButton("Quit");
    
    initGenerateButton();
    initResetButton();
    initQuitButton(); 
    
    setName("sudoku.buttonPanel"); 
    setPanelSize();
    return this;
  }  
  
  private void initLayout() {
    BoxLayout bLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
    setLayout(bLayout);
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
    add(Box.createRigidArea(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT/2)));
    add(generateButton);
    add(Box.createRigidArea(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT/2)));
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
    add(Box.createRigidArea(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT/2)));
    add(resetButton);
    add(Box.createRigidArea(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT/2)));
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
    add(Box.createRigidArea(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT/2)));
    add(quitButton);
    add(Box.createRigidArea(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT/2)));
    ++buttonCount; 
  }
  
  private void setPanelSize() {
    setSize(BUTTON_WIDTH, BUTTON_HEIGHT * 2 * buttonCount);
  }
}
