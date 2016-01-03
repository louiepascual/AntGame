/*
	RewardTile.java
	1. Inherits Tile Class (Abstract)
	2. Implements tileEffect() (Abstract)
	3. Part of Tile Package
	4. Increases 1 coin from the ant
*/

package Tile;

public class RewardTile extends Tile {
	boolean gotReward;

	public RewardTile() {
		gotReward = false;
	}

	public int tileEffect() {
		if(gotReward == false) {
			gotReward = true;
			return 1;
		}
		else
			return 0;
	}

}