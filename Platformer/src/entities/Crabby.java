package entities;

import static utilz.Constants.EnemyConstants.*;
public class Crabby extends Enemy{

	// width height and enemy type will be constant for all the crabs
	public Crabby(float x, float y) {
		super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
		
	}

}
