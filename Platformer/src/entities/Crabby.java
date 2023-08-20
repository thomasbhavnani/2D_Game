package entities;

import static utilz.Constants.Directions.LEFT;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;
import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.GetEntityYPosUnderRoofOrAboveFloor;
import static utilz.HelpMethods.IsEntityOnFloor;
import static utilz.HelpMethods.IsFloor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;
public class Crabby extends Enemy{
	
	// AttackBox
	private Rectangle2D.Float attackBox;
	private int attackBoxOffsetX;

	// width height and enemy type will be constant for all the crabs
	public Crabby(float x, float y) {
		super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
		initHitbox(x, y,(int) (22 * Game.SCALE), (int) (19 * Game.SCALE));
		initAttackBox();
		
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x,y,(int)(82 * Game.SCALE), (int)(19 * Game.SCALE));
		// for hitbox width added onto the sides of the crab as it attacks
		attackBoxOffsetX = (int)(Game.SCALE * 30);
	}
	
	public void update(int[][] lvlData, Player player) {
		updateBehavior(lvlData, player);
		updateAnimationTick();
		updateAttackBox();
	}
	
	private void updateAttackBox() {
		attackBox.x = hitbox.x - attackBoxOffsetX;
		attackBox.y = hitbox.y;
		
	}

	private void updateBehavior(int[][] lvlData, Player player) {
		// enemy patrols and moves around and attacks
		if(firstUpdate) 
			firstUpdateCheck(lvlData);
		
		if(inAir) 
			updateInAir(lvlData);
		else {
			switch(enemyState) {
			case IDLE:
				// enemy should be moving, not idle
				newState(RUNNING);
				break;
			case RUNNING:
				if(canSeePlayer(lvlData, player)) 
					turnTowardsPlayer(player);
				if(isPlayerCloseForAttack(player))
					newState(ATTACK);
				move(lvlData);
				break;
				// check for attacks at frame 3 of the attack animation
				// attacChecked makes sure we are only checking once per animation
			case ATTACK:
			
				if(aniIndex == 0)
					attackChecked = false;
					// makes sure that attack is checked at the beginning of each attack
					
				if(aniIndex == 3 && !attackChecked)
					checkEnemyHit(attackBox, player);
				break;
			case HIT:
				break;
			}
		}
	}
	


	public void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int)(attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int)attackBox.height);
	}
	
	public int flipX() {
		if(walkDir == RIGHT)
			return width;
		else
			return 0;
	}
	
	public int flipW() {
		if(walkDir == RIGHT)
			return -1;
		else
			return 1;
	}



}
