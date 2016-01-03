/*
	HillTrap.java
	1. Inherits Tile Class (Abstract)
	2. Implements tileEffect() (Abstract)
	3. Part of Tile Package
	4. Decreases 1 coin from the ant
*/

package Tile;

public class HillTrap extends Tile {
	public int tileEffect() {
		return -1;
	}
}