/*
	Board.java
	1. Creates the GridLayout for the game 
	2. Generates the Tiles
	3. Tile distribution:
		a. 50% of BoardSize for Reward Tile
		b. 1 Tile for Water Trap
		c. 5% of BoardSize for Mud Trap
		d. 10% of BoardSize for Hill Trap
	4. Tiles are randomly generated
*/

import Tile.*;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import java.util.Random;

public class Board {

	private JPanel gamePanel;
	private Tile[][] tiles;
	private int boardSize;

	public Board(int i) {

		// Initialize Board UI
		boardSize = i;
		gamePanel = new JPanel(new GridLayout(boardSize,boardSize));
		tiles = new Tile[boardSize][boardSize];

		// Calls the tile generator function
		generateTiles();

	}

	// Algorithm for producing randomized tiles
	public void generateTiles() {
		Random randomGenerator = new Random();
		int xRand;
		int yRand;

		// Set all tiles to be normal first
		for(int r=0; r<boardSize; r++) {
			for(int c=0; c<boardSize; c++) {
				tiles[r][c] = new NormalTile();	
			}
		}

		// Generate Reward Tiles (50% of all tiles)
		int counter = 0;
		int numOfNT = (boardSize*boardSize)/2;
		
		while(counter < numOfNT) {
			xRand = randomGenerator.nextInt(boardSize);
			yRand = randomGenerator.nextInt(boardSize);

			if(xRand == 0 && yRand == 0) {
				continue;
			}
			else if(tiles[xRand][yRand] instanceof NormalTile) {
				tiles[xRand][yRand] = new RewardTile();
				counter++;
			}

		}
		

		// Generate Water Traps (5% of all tiles)
		counter = 0;
		int numOfWT = 1;//(int)((boardSize*boardSize)*0.05);
		while(counter < numOfWT) {
			xRand = randomGenerator.nextInt(boardSize);
			yRand = randomGenerator.nextInt(boardSize);

			if(xRand == 0 && yRand == 0) {
				continue;
			}
			else if(tiles[xRand][yRand] instanceof NormalTile) {
				tiles[xRand][yRand] = new WaterTrap();
				counter++;
			}
		}

		// Generate Mud Traps (5% of all tiles)
		counter = 0;
		int numOfMT = (int)((boardSize*boardSize)*0.05);
		while(counter < numOfMT) {
			xRand = randomGenerator.nextInt(boardSize);
			yRand = randomGenerator.nextInt(boardSize);

			if(xRand == 0 && yRand == 0) {
				continue;
			}
			else if(tiles[xRand][yRand] instanceof NormalTile) {
				tiles[xRand][yRand] = new MudTrap();
				counter++;
			}
		}

		// Generate Hill Traps (10% of all tiles)
		counter = 0;
		int numOfHT = (int)((boardSize*boardSize)*0.1);
		while(counter < numOfHT) {
			xRand = randomGenerator.nextInt(boardSize);
			yRand = randomGenerator.nextInt(boardSize);

			if(xRand == 0 && yRand == 0) {
				continue;
			}
			else if(tiles[xRand][yRand] instanceof NormalTile) {
				tiles[xRand][yRand] = new HillTrap();
				counter++;
			}
		}

		// Inserting the tiles to gamePanel
		for(int r=0; r<boardSize; r++) {
			for(int c=0; c<boardSize; c++) {
				tiles[r][c].setBorder(LineBorder.createGrayLineBorder());
				
				gamePanel.add(tiles[r][c]);
			}
		}
	}

	// Get Method for the variable gamePanel
	public JPanel getPanel() {
		return gamePanel;
	}

	// Get Method for the variable tiles
	public Tile getTile(int r, int c) {
		return tiles[r][c];
	}

	// Get Method for the variable boardSize
	public int getBoardSize() {
		return boardSize;
	}

}