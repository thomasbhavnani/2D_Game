package entities;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.GetEntityYPosUnderRoofOrAboveFloor;
import static utilz.HelpMethods.IsEntityOnFloor;
import static utilz.HelpMethods.IsFloor;

import main.Game;
public class Crabby extends Enemy{

	// width height and enemy type will be constant for all the crabs
	public Crabby(float x, float y) {
		super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
		initHitbox(x, y,(int) (22 * Game.SCALE), (int) (19 * Game.SCALE));
		
	}
	
	public void update(int[][] lvlData, Player player) {
		updateMove(lvlData, player);
		updateAnimationTick();
		
	}
	
	private void updateMove(int[][] lvlData, Player player) {
		// enemy patrols and moves around
		if(firstUpdate) 
			firstUpdateCheck(lvlData);
		
		if(inAir) 
			updateInAir(lvlData);
		else {
			switch(enemyState) {
			case IDLE:
				// enemy should be moving
				newState(RUNNING);
				break;
			case RUNNING:
				if(canSeePlayer(lvlData, player)) 
					turnTowardsPlayer(player);
				if(isPlayerCloseForAttack(player))
					newState(ATTACK);
				move(lvlData);
				break;
			}
		}
	}

}
