// Grid Class: stores all the backend information for the Grid, such as proximity and if its a mine or not
// William Lin and Matthew Liu
// Jan 21, 2022

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class Grid {
	public Cell[][] grid;
	public int n, m, numMines;
	public Pair firstClick;
	public HashSet<Pair> mines = new HashSet<Pair>();

	//Constructor for the Grid class; first is where the user clicks first, to ensure they dont start the game by clicking on a mine
	public Grid(Pair first, int rows, int cols, int mines) {
		n = rows;
		m = cols;
		numMines = mines;
		firstClick = new Pair(first.getX(), first.getY());
		grid = new Cell[n + 1][m + 1];
		// initializes the cell objects for the grid
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				grid[i][j] = new Cell();
			}
		}
		generateMines();
		generateProximity();
		for(int i =0; i<=n; i++){
			for(int j =0; j<=m; j++){
				if(grid[i][j].isFlagged()) grid[i][j].updateFlagged();
			}
		}
	}

	// used to randomly generate mines on the grid
	public void generateMines() {
		// generate numMines mines, by storing them in a hashset for efficient accessing later
		while (mines.size() < numMines) {
			int r = (int) ((Math.random() * n) + 1);
			int c = (int) ((Math.random() * m) + 1);
			// check if the mine generated is in a 3x3 around the initial click. This is put so that when the user first clicks, they uncover more than one cell
			if (Math.abs(r - firstClick.getX()) <= 1 || Math.abs(c - firstClick.getY()) <= 1)
				continue;
				
			// if the mine isn't positioned at the first click, we add it into the hashset
			Pair pos = new Pair(r, c);
			if (!pos.equals(firstClick))
				mines.add(pos);
		}
	}

  //Counts how many bombs are adjacent to every cell 
	public void generateProximity() {
		int[] adj = { -1, 0, 1 }; //used to store the changes in x and and y between two adjacent cells
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= m; j++) {
				int cnt = 0;

        //iterate through all cells adjacent to grid[i][j]
				for (int x : adj) {
					for (int y : adj) {
						int newi = x + i, newj = y + j;
						Pair temp = new Pair(newi, newj);
						if (1 <= newi && newi <= n && 1 <= newj && 1 <= m) {
							if (mines.contains(temp))
								cnt++;
						}
					}
				}
				Pair temp = new Pair(i, j);
				// check if its a mine or not, else update the proximity
				if (mines.contains(temp))
					grid[i][j].updateValue(-1);
				else
					grid[i][j].updateValue(cnt);
			}
		}
	}
  
  // uses breadth-first-search traversal to find all adjacent cells with a value of 0 and reveals them
	public void floodFill(Pair st) {
		// queue is used to store all the positions that we have yet to explore
		Queue<Pair> q = new LinkedList<Pair>();
		if (grid[st.getX()][st.getY()].isRevealed() || grid[st.getX()][st.getY()].isFlagged())
			return;
		if (grid[st.getX()][st.getY()].getValue() > 0) {
			grid[st.getX()][st.getY()].reveal();
			return;
		}
		if (grid[st.getX()][st.getY()].getValue() == -1) {
			grid[st.getX()][st.getY()].reveal();
			return;
		}
		q.add(st);
		// we continue to expand and explore, while the queue isnt empty
		while (!q.isEmpty()) {
			Pair cur = q.poll();
			grid[cur.getX()][cur.getY()].reveal();
			for (int dr = -1; dr <= 1; dr++) {
				for (int dc = -1; dc <= 1; dc++) {
					int nr = cur.getX() + dr;
					int nc = cur.getY() + dc;
					// check if the new position is within the grid, and isnt revealed yet
					if (1 <= nr && nr <= n && 1 <= nc && nc <= m && !grid[nr][nc].isRevealed()) {
						if (grid[nr][nc].getValue() != -1 && !grid[nr][nc].isFlagged()){
							grid[nr][nc].reveal();
							// if the cells proximity is 0, we add it to the queue of positions to further explore
							if (grid[nr][nc].getValue() == 0) {
								Pair nxt = new Pair(nr, nc);
								q.add(nxt);
							}
						}
					}
				}
			}
		}
	}
}
