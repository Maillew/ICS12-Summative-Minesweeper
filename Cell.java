// Cell Class: stores basic information for each given cell on the grid, such as its value(proximity or flag), and whether or not its revealed or flagged
// William Lin and Matthew Liu
// Jan 21, 2022
public class Cell {
  private boolean flagged = false, revealed = false;
  private int value; // -1 for bomb, >= 0 for number of mines adjacent to cell

	// constructor for the cell
  public Cell() {
     value = 0;
  }
  
	// mutator methods
  public void updateValue(int v) {
    value = v;
  }
  public void reveal() {
    revealed = true;
  }
  public void updateFlagged() {
    flagged = !flagged;
  }

	// accessor methods
  public boolean isFlagged() {
    return flagged;
  }
	
	public int getValue() {
    return value;
  }

  public boolean isRevealed() {
    return revealed;
  }
}
