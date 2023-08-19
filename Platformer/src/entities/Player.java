package entities;


import static utilz.Constants.PlayerConstants.GetSpriteAmount;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class Player extends Entity{

	private BufferedImage[][] animations;
	private int aniTick, aniIndex, aniSpeed = 15; 
	private int playerAction = IDLE;
	private boolean moving = false, attacking = false;
	private boolean left, up, right, down, jump;
	private float playerSpeed = 1.0f * Game.SCALE;
	private int[][] lvlData;
	// x offset of the player hitbox from edge of sprite tile
	private float xDrawOffset = 21 * Game.SCALE;	
	// y offset for the player hitbox from edge of sprite tile
	private float yDrawOffset = 4 * Game.SCALE; 
	
	// jumping / gravity
	private float airSpeed = 0f;
	private float gravity = 0.04f * Game.SCALE;
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	private boolean inAir = false;
	
	//StatusBarUI
	private BufferedImage statusBarImg;

	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);

	private int healthBarWidth = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);
	private int healthBarYStart = (int) (14 * Game.SCALE);
	
	private int maxHealth = 100;
	private int currentHealth = maxHealth;
	private int healthWidth = healthBarWidth;
	
	//AttackBox
	private Rectangle2D.Float attackBox;
	
	// used to flip player sprite based on which direction it's going (left or right)
	private int flipX = 0;
	private int flipW = 1;
	
	private boolean attackChecked;
	private Playing playing;
	
	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		loadAnimations();
		initHitbox(x, y, (int) (20 * Game.SCALE), (int)(28 * Game.SCALE));
		initAttackBox();
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int)(20 * Game.SCALE), (int)(20 * Game.SCALE));
		
	}

	public void update() {
		if(currentHealth <= 0) {
			playing.setGameOver(true);
			return;
		}
			
		
		updateHealthBar();
		updateAttackBox();
		updatePos();
		if(attacking)
			checkAttack();
		updateAnimationTick();
		setAnimation();
		
	}
	
	private void checkAttack() {
		//attack activates at frame 2 of the player attack animation
		if(attackChecked || aniIndex != 1)
			return;
		attackChecked = true;
		playing.checkEnemyHit(attackBox);
			
		
	}

	private void updateAttackBox() {
		if(right) {
			attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 10);
		} else if(left) {
			attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 10);
		}
		
		attackBox.y = hitbox.y + (Game.SCALE * 10);
		
	}

	private void updateHealthBar() {
		// the portion of the max health bar that your current health takes up
		healthWidth = (int) ((currentHealth / (float)maxHealth) * healthBarWidth);
		
	}

	public void render(Graphics g, int lvlOffset) {
		g.drawImage(animations[playerAction][aniIndex], 
				(int) (hitbox.x - xDrawOffset) - lvlOffset + flipX,
				(int) (hitbox.y - yDrawOffset), 
				width * flipW, height, null); // multiply the width by flipW (+- 1) to switch the direction the player sprite is moving
	
		// drawHitbox(g, lvlOffset);
		
		drawAttackBox(g, lvlOffset);
		// no level offset needed for UI because it's always in the same spot on screen
		drawUI(g);
	}
	
	
	
	
	private void drawAttackBox(Graphics g, int lvlOffsetX) {
		g.setColor(Color.red);
		g.drawRect((int)attackBox.x - lvlOffsetX, (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);
		
	}

	private void drawUI(Graphics g) {
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(Color.red);
		// draws the actual red health amount as it fills and depletes in the health bar
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
		
	}

	private void updateAnimationTick() {
		aniTick++;
		
		// let each animation image stay for aniSpeed frames out of 120 FPS
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			
			// loop back through the images in the animation
			if(aniIndex >= GetSpriteAmount(playerAction)) {
				aniIndex = 0;
				// don't want the attack animation on a constant loop like running
				attacking = false;
				// always reset attack check to false
				attackChecked = false;
			}
			
		}
		
	}
	
	private void setAnimation() {
		
		int startAni = playerAction;
		if (moving) 
			playerAction = RUNNING;
		else
			playerAction = IDLE;
		if(inAir) {
			if(airSpeed > 0) {
				// going upwards
				playerAction = JUMP;
			} else {
				playerAction = FALLING;
			}
		}
		if (attacking) {
			playerAction = ATTACK;
			if(startAni != ATTACK) {
				// want to start the attack animation from the active frame
				
				aniIndex = 1;
				aniTick = 0;
				return;
			}
		}
		// checking for a change in player animation
		if (startAni != playerAction) {
			// if there was a change in animation
			// then reset animation counter and animation frame index to 0
			resetAniTick();
		}
	}
	
	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		
		moving = false;
		
		if(jump)
			jump();
		
//		if(!left && !right && !inAir)
//			return;
		
		if(!inAir)
			if((!left && !right) || (left && right))
				return;
		
		float xSpeed = 0;
		
		
		if(left) {
			xSpeed -= playerSpeed;
			
			// used to flip player sprite based on direction it's going
			flipX = width;
			flipW = -1;
		}
		
		if(right) {
			xSpeed += playerSpeed;
			
			// used to flip player sprite based on direction it's going
			flipX = 0;
			flipW = 1;
		}
		if(!inAir) 
			if(!IsEntityOnFloor(hitbox, lvlData)) 
				inAir = true;
			
		
		if(inAir) {
			
			if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData )) {
				hitbox.y += airSpeed;
				airSpeed += gravity;
				updateXPos(xSpeed);
			} else {
				// cannot move up or down, hitting roof or floor 
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if(airSpeed > 0) {
					// falling down and player hits something 
					resetInAir();
				}else {
					// player hits tile mid air
					airSpeed = fallSpeedAfterCollision;
				}
				updateXPos(xSpeed);
			}
			
		} else 
			updateXPos(xSpeed);
		
		moving = true;
	}
	
	private void jump() {
		if(inAir)
			return;
		inAir = true;
		airSpeed = jumpSpeed;
		
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
		
	}

	private void updateXPos(float xSpeed) {
		if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData )) {
		hitbox.x += xSpeed;
		}else {
			//makes sure hitbox hits wall 
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
		}
	
	}
	
	public void changeHealth(int value) {
		currentHealth += value;
		
		if(currentHealth <= 0) {
			currentHealth = 0;
			//gameOver();
		} else if(currentHealth >= maxHealth)
			currentHealth = maxHealth;
	}

	private void loadAnimations() {
		// slash in front of image name tells pc that image is in a folder, not beside one
		 
			BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
			// creating a 2D array of images to iterate through the animation frames
			animations = new BufferedImage[7][8];
			for(int j = 0; j < animations.length; j++) 
				for(int i = 0 ; i < animations[j].length; i++)
					animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
					// sub image gets each 64 x 40 pixel image at specific positions in the main image
			
			statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
					
	}
	
	public void loadLvlData(int [][] lvlData) {
		this.lvlData = lvlData;
		// check if player spawns in the air
		if(!IsEntityOnFloor(hitbox, lvlData)) {
			inAir = true;
		}
	}
	
	public void resetDirBooleans() {
		up = false;
		left = false;
		down = false;
		right = false;
	}
	
	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}
	
	public void setJump(boolean jump) {
		this.jump = jump;
	}
	
	

			
			
			
	
}
