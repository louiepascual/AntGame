/*
	Ant.java
	1. Holds the ant image
	2. Implements movement through arrow keys
		through KeyEvent
	3. Variable x & y holds ant coordinates
*/

import java.awt.Image;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Ant {
	private int coins;
	private String antFilePath;
	private int dx;
	private int dy;
	private int x;
	private int y;
	private Image image;

	// Construct the new ant
	public Ant() {

		// Get antwalk.gif Image
		antFilePath = "antwalk.gif";
		ImageIcon ii = new ImageIcon(this.getClass().getResource(antFilePath));
		image = ii.getImage();


		// Set coordinates to default
		x = 0;
		y = 0;

		// Set life to 0
		coins = 0;

		// Set temp. coordinates values to 0
		dx = 0;
		dy = 0;
	}

	// Adjusts the coordinates of ant
	public void move() {
		x = dx;
		y = dy;
	}

	// Get Method for variable x
	public int getX() { 
		return x; 
	}

	// Get Method for variable y
	public int getY() { 
		return y; 
	}

	// Get Method for variable dx
	public int getDX() { 
		return dx;
	}

	// Get Method for variable dy
	public int getDY() {
		return dy;
	}

	// Get Method for variable image
	public Image getImage() {
		return image;
	}

	// Get Method for variable coins
	public int getCoins() {
		return coins;
	}

	// Reflects the coin effect to the ant
	public void setCoinEffect(int c) {
		coins += c;
	}

	// Fixes the border check problem
	public void reset() {
		dx = x;
		dy = y;
	}	

	// Function to provide movement with the arrow keys
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if(key == KeyEvent.VK_LEFT) {
			dx = x-1;
		}
		else if(key == KeyEvent.VK_RIGHT) {
			dx = x+1;
		}
		else if(key == KeyEvent.VK_UP) {
			dy = y-1;
		}
		else if(key == KeyEvent.VK_DOWN) {
			dy = y+1;
		}
	} 
}