// Button Grid class: sets up the actual GUI for the game
// William Lin and Matthew Liu
// Jan 21, 2022
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ButtonGrid implements ActionListener {
  JFrame frame = new JFrame();
  JPanel title_panel = new JPanel();
  JPanel button_panel = new JPanel();
  JLabel textfield = new JLabel();
  ImageIcon flagIcon = new ImageIcon("flag.png");
  ImageIcon mineIcon = new ImageIcon("mine.png");
  public Color colors[] = new Color[10];
  private int rows = 10, cols = 18;
  private int mines = 30;
  private Pair firstClick;
  private boolean gameFinished = false;
  public Grid game;
  JButton[][] buttons = new JButton[rows][cols];

  // Constructor method for the ButtonGrid class
  public ButtonGrid() {
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize((800 / 18) * 18, (800 / 18) * 10 + 100);// width, height +title panel size
    frame.getContentPane().setBackground(new Color(50, 50, 50));
    frame.setLayout(new BorderLayout());
    frame.setVisible(true);

		// sets the text for the title
    textfield.setBackground(new Color(211, 211, 211));
    textfield.setForeground(new Color(10, 10, 10));
    textfield.setFont(new Font("Ink Free", Font.BOLD, 75));
    textfield.setHorizontalAlignment(JLabel.CENTER);
    textfield.setText("Minesweeper");
    textfield.setOpaque(true);

    title_panel.setLayout(new BorderLayout());
    title_panel.setBounds(0, 0, 792, 100);

    button_panel.setLayout(new GridLayout(rows, cols));// sets dimensions of grid
    button_panel.setBackground(new Color(150, 150, 150));
    // colours for the numbers in the cells (blue, green, red, purple, maroon, turquoise, black, gray)
    colors[1] = new Color(0, 0, 255);
    colors[2] = new Color(34, 139, 34);
    colors[3] = new Color(255, 0, 0);
    colors[4] = new Color(75, 0, 130);
    colors[5] = new Color(128, 0, 0);
    colors[6] = new Color(72, 209, 204);
    colors[7] = new Color(0, 0, 0);
    colors[8] = new Color(128, 128, 128);
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        buttons[i][j] = new JButton();
        button_panel.add(buttons[i][j]);
        buttons[i][j].setFont(new Font("Courier New", Font.BOLD, 35));
        buttons[i][j].setFocusable(false);
        buttons[i][j].addActionListener(this);
        buttons[i][j].setMargin(new Insets(0, 0, 0, 0));
        buttons[i][j].setBackground(new Color(211, 211, 211));
        buttons[i][j].setBorder(BorderFactory.createBevelBorder(0));
      }
    }

    title_panel.add(textfield);
		// adds all the components to the frame
    frame.add(title_panel, BorderLayout.NORTH);
    frame.add(button_panel);
  }

	// used to resize an icon to the correct dimensions
  public Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
    Image img = icon.getImage();
    Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight, java.awt.Image.SCALE_SMOOTH);
    return new ImageIcon(resizedImage);
  }

	// used when a given cell is clicked
  private void reveal(int r, int c, boolean flag) {// false = click, true = flag
    // get number type from backend
    // Grid uses 1-based indexing, ButtonGrid uses 0-based
    if (game.grid[r + 1][c + 1].getValue() == -1 && !flag && !game.grid[r + 1][c + 1].isFlagged()) { // Click on mine
      gameOver(false);
      return;
    }
    if (flag) { // flag
      game.grid[r + 1][c + 1].updateFlagged();
    }

    if (!flag) //if its not flagged, the user clicked, and we need to explore
      game.floodFill(new Pair(r + 1, c + 1));

		// show the updated cells after the user click
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        if (game.grid[i + 1][j + 1].isRevealed()) {
          buttons[i][j].setBackground(new Color(200, 200, 200));
          buttons[i][j].setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 2));

          int value = game.grid[i + 1][j + 1].getValue();
					// if the value is between [1,8], it means that its a proximity cell, and we need to showcase it
          if (1 <= value && value <= 8) {
            buttons[i][j].setForeground(colors[value]);
            buttons[i][j].setText(Integer.toString(value));
          }
        } 
				// if the cell is flagged, we display the flag
				else if (game.grid[i + 1][j + 1].isFlagged()) {
          buttons[i][j].setIcon(resizeIcon(flagIcon, buttons[i][j].getWidth(), buttons[i][j].getHeight()));
        } 
				// it is not explored, and we leave it as so
				else {
          buttons[i][j].setBackground(new Color(211, 211, 211));
          buttons[i][j].setBorder(BorderFactory.createBevelBorder(0));
          buttons[i][j].setIcon(null);
        }
      }
    }
  }
  
  // What to do if a button on the grid is clicked
  @Override
  public void actionPerformed(ActionEvent e) {
    if (gameFinished) return;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        if (e.getSource() == buttons[i][j]) {
          // If it is the user's first click, initialize game and generate mines
          if (firstClick == null && (e.getModifiers() & ActionEvent.SHIFT_MASK) == 0) {
            firstClick = new Pair(i + 1, j + 1);
            game = new Grid(firstClick, rows, cols, mines);
          }
          if ((e.getModifiers() & ActionEvent.SHIFT_MASK) != 0) {
            reveal(i, j, true);
          } else {
            reveal(i, j, false);
          }
        }
      }
    }
    if (checkWin()) {
      gameOver(true);
      return;
    }
  }

  // Checks if the user has won. The user wins by revealing all cells that do not contain a mine
  public boolean checkWin() {
    int countRevealed = 0;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        if (game.grid[i + 1][j + 1].isRevealed())
          countRevealed++;
      }
    }

    // Checks if the number of revealed cells equal the number of cells without mines
    if (countRevealed == rows * cols - mines) {
      return true;
    } else
      return false;
  }

  // Changes the title to You Lose or You Win, and reveals all the bombs if it is a loss. It disables all buttons after so the user cannot edit the grid after the game is over
  public void gameOver(boolean win) {
    gameFinished = true;
    if (win) {
      textfield.setForeground(new Color(34, 139, 34));
      textfield.setText("YOU WIN");
    } else {
      textfield.setForeground(new Color(255, 0, 0));
      textfield.setText("YOU LOSE");
    }
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        int value = game.grid[i + 1][j + 1].getValue();
        if (!win && value == -1) {
          buttons[i][j].setBackground(new Color(255, 0, 0));
          buttons[i][j].setIcon(resizeIcon(mineIcon, buttons[i][j].getWidth(), buttons[i][j].getHeight()));
        }
      }
    }
  }

  // Used to close the frame from main menu, acts the exact same way as clicking the x on the frame to close it.
  public void close() {
    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
  }
}
