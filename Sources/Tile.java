/*
	Tile.java
	1. Inherits JButton Class
	2. Abstract class
	3. Part of Tile Package
	4. Holds abstract method tileEffect()
*/

package Tile;

import javax.swing.JButton;

public abstract class Tile extends JButton {

	protected Tile() {
		super();
	}
	
	public abstract int tileEffect();
}