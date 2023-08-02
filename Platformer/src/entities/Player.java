package entities;


import static utilz.Constants.PlayerConstants.GetSpriteAmount;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;



import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import utilz.LoadSave;

public class Player extends Entity{

	private BufferedImage[][] animations;
	private int aniTick, aniIndex, aniSpeed = 15; 
	private int playerAction = IDLE;
	private boolean moving = false, attacking = false;
	private boolean left, up, right, down, jump;
	private float playerSpeed = 2.0f;
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
	
	public Player(float x, float y, int width, int height) {
		super(x, y, width, height);
		loadAnimations();
		initHitbox(x, y, 20 * Game.SCALE, 28 * Game.SCALE);
	}
	
	public void update() {
		updatePos();
	
		updateAnimationTick();
		setAnimation();
		
	}
	
	public void render(Graphics g) {
		g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset), (int) (hitbox.y - yDrawOffset), width, height, null);
		drawHitbox(g);
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
				// don't want the attack animation on a constant loop like running?
				attacking = false;
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
		if (attacking) 
			playerAction = ATTACK_1;
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
		
		if(!left && !right && !inAir)
			return;
		
		float xSpeed = 0;
		
		if(left) 
			xSpeed -= playerSpeed;
		if(right)
			xSpeed += playerSpeed;
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

	private void loadAnimations() {
		// slash in front of image name tells pc that image is in a folder, not beside one
		 
			BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
			// creating a 2D array of images to iterate through the animation frames
			animations = new BufferedImage[9][6];
			for(int j = 0; j < animations.length; j++) 
				for(int i = 0 ; i < animations[j].length; i++)
					animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
					// sub image gets each 64 x 40 pixel image at specific positions in the main image
					
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
