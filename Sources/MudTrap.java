/*
	MudTrap.java
	1. Inherits Tile Class (Abstract)
	2. Implements tileEffect() (Abstract)
	3. Part of Tile Package
	4. Decreases 3 coins from the ant
*/

package Tile;

public class MudTrap extends Tile {
	public int tileEffect() {
		return -3;
	}
}