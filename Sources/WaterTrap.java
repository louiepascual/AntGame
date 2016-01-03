/*
	WaterTrap.java
	1. Inherits Tile Class (Abstract)
	2. Implements tileEffect() (Abstract)
	3. Part of Tile Package
	4. Decreases -999  coins from the ant (kills)
*/

package Tile;

public class WaterTrap extends Tile {
	public int tileEffect() {
		return -999;
	}
}