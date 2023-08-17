package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;

import main.Game;
// cannot create enemy object without extending entity because enemy is abstract
public abstract class Enemy extends Entity{ 

	// protected allows the Crabby class to use these variables
	protected int aniIndex, enemyState, enemyType;
	protected int aniTick, aniSpeed = 25;
	protected boolean firstUpdate = true;
	protected boolean inAir;
	protected float fallSpeed;
	protected float gravity = 0.04f * Game.SCALE;
	protected float walkSpeed = 0.35f * Game.SCALE;
	protected int walkDir = LEFT;
			
	
	
	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		initHitbox(x,  y, width, height);
		
	}
	
	
	// used to perform checks in enemy type-specific classes like Crabby
	protected void firstUpdateCheck(int[][] lvlData) {
		if(!IsEntityOnFloor(hitbox, lvlData)) 
			inAir = true;
		firstUpdate = false;
	}
	// used to perform checks in enemy type-specific classes like Crabby
	protected void updateInAir(int[][] lvlData) {
		// constantly checking if the enemy has hit the ground so you add the fall speed to hitbox.y
		if(CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
			// enemy is falling 
			hitbox.y += fallSpeed;
			fallSpeed += gravity;
		} else {
			inAir = false;
			hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
		}	
	}
	
	// used to perform checks in enemy type-specific classes like Crabby
	protected void move(int[][] lvlData) {
		float xSpeed = 0;
		
		if(walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;
		
		if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			// check if the enemy is moving to an edge
			if(IsFloor(hitbox, xSpeed, lvlData)) {
				hitbox.x += xSpeed;
				return;
			}
		// only gets here if the one of the above if statements are false
		changeWalkfDir();
	}
	
	// change enemy state and reset animation counters
	protected void newState(int enemyState) {
		this.enemyState = enemyState;
		aniTick = 0;
		aniIndex = 0;
	}
	
	// protected allows the Crabby class to use this method
	protected void updateAnimationTick() {
		aniTick++;
		
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
				aniIndex = 0;
			}
		}
	}
	

	
	
	// protected allows the Crabby class to use this method
	protected void changeWalkfDir() {
		if(walkDir == LEFT)
			walkDir = RIGHT;
		else 
			walkDir = LEFT;
		
	}

	// returns current animation frame being selected
	public int getAniIndex() {
		return aniIndex;
	}
	
	public int getEnemyState() {
		return enemyState;
	}

}
