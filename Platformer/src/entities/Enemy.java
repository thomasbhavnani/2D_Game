package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

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
	protected int tileY;
	protected float attackDistance = Game.TILES_SIZE;
	protected int maxHealth;
	protected int currentHealth;
	protected boolean active = true;
	protected boolean attackChecked;
			
	
	
	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		initHitbox(x,  y, width, height);
		maxHealth = GetMaxHealth(enemyType);
		currentHealth = maxHealth;
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
			
			// enemy tile Y location never changes
			tileY = (int) (hitbox.y / Game.TILES_SIZE);
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
	
	protected void turnTowardsPlayer(Player player) {
		// check if the player hitbox x location is to the right or left of the enemy hitbox
		if(player.hitbox.x > hitbox.x)
			walkDir = RIGHT;
		else
			walkDir = LEFT;
	}
	
	protected boolean canSeePlayer(int[][] lvlData, Player player) {
		int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);
		// check if the player and enemy are at the same tile in the y direction
		if(playerTileY == tileY)
			if(isPlayerInRange(player)) {
				// use capitals on all beginning letters if the method is public static
				if(IsSightClear(lvlData, hitbox, player.hitbox, tileY))
					return true;
			}
		
		return false;
	}
	


	protected boolean isPlayerInRange(Player player) {
		// finding distance between player hitbox and enemy hitbox
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		// is the player within range of the enemy?
		return absValue <= attackDistance * 5;
	}
	
	protected boolean isPlayerCloseForAttack(Player player) {
		// finding distance between player hitbox and enemy hitbox
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		// is the player within attack range of the enemy?
		return absValue <= attackDistance;
	}


	// change enemy state and reset animation counters
	protected void newState(int enemyState) {
		this.enemyState = enemyState;
		aniTick = 0;
		aniIndex = 0;
	}
	
	public void hurt(int amount) {
		currentHealth -= amount; 
		if(currentHealth <= 0)
			newState(DEAD);
		else
			newState(HIT);
	}
	
	protected void checkEnemyHit(Rectangle2D.Float attackBox, Player player) {
		// check if the enemy hits the player
		if(attackBox.intersects(player.hitbox));
			player.changeHealth(-GetEnemyDmg(enemyType));
		attackChecked = true;
	}

	// protected allows the Crabby class to use this method
	protected void updateAnimationTick() {
		aniTick++;
		
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
				aniIndex = 0;
				
				
				
				switch(enemyState) {
				case ATTACK:
					enemyState = IDLE;
				case HIT:
					enemyState = IDLE;
				case DEAD:
					active = false;
				}
				if(enemyState == ATTACK)
					// used to leave the attack animation
					enemyState = IDLE;
				else if(enemyState == HIT)
					enemyState = IDLE;
				else if(enemyState == DEAD)
					active = false;
					
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
	
	public boolean isActive() {
		return active;
	}

}
