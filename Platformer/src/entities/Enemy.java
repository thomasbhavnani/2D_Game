package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;

import main.Game;
// cannot create enemy object without extending entity because enemy is abstract
public abstract class Enemy extends Entity{ 

	private int aniIndex, enemyState, enemyType;
	private int aniTick, aniSpeed = 25;
	private boolean firstUpdate = true;
	private boolean inAir;
	private float fallSpeed;
	private float gravity = 0.04f * Game.SCALE;
	private float walkSpeed = 0.35f * Game.SCALE;
	private int walkDir = LEFT;
			
	
	
	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		initHitbox(x,  y, width, height);
		
	}
	
	private void updateAnimationTick() {
		aniTick++;
		
		
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
				aniIndex = 0;
			}
		}
	}
	
	public void update(int[][] lvlData) {
		updateMove(lvlData);
		updateAnimationTick();
		
		
		
	}
	
	private void updateMove(int[][] lvlData) {
		// enemy patrols and moves around
		if(firstUpdate) {
			if(!IsEntityOnFloor(hitbox, lvlData)) 
				inAir = true;
			firstUpdate = false;
		}
		if(inAir) {
			// constantly checking if the enemy has hit the ground so you add the fall speed to hitbox.y
			if(CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
				// enemy is falling 
				hitbox.y += fallSpeed;
				fallSpeed += gravity;
			} else {
				inAir = false;
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
			}	
		} else {
			switch(enemyState) {
			case IDLE:
				// enemy should be moving
				enemyState = RUNNING;
				break;
			case RUNNING:
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
				break;
			}
		}
	}
	
	private void changeWalkfDir() {
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
