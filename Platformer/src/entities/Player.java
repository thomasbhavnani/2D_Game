package entities;


import static utilz.Constants.PlayerConstants.GetSpriteAmount;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.CanMoveHere;


import java.awt.Graphics;
import java.awt.image.BufferedImage;


import utilz.LoadSave;

public class Player extends Entity{

	private BufferedImage[][] animations;
	private int aniTick, aniIndex, aniSpeed = 15; 
	private int playerAction = IDLE;
	private boolean moving = false, attacking = false;
	private boolean left, up, right, down;
	private float playerSpeed = 2.0f;
	private int[][] lvlData;
	
	
	public Player(float x, float y, int width, int height) {
		super(x, y, width, height);
		loadAnimations();
	}
	
	public void update() {
		updatePos();
		updateHitbox();
		updateAnimationTick();
		setAnimation();
		
	}
	
	public void render(Graphics g) {
		g.drawImage(animations[playerAction][aniIndex], (int) x, (int) y, width, height, null);
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
		if(!left && !right && !up && !down)
			return;
		
		float xSpeed = 0, ySpeed = 0;
		
		if(left && !right) 
			xSpeed = -playerSpeed;
		 else if(right && !left)
			xSpeed = playerSpeed;
			
		if(up && !down) 
			ySpeed = -playerSpeed;
		else if(down && !up) 
			ySpeed = playerSpeed;
		
		if(CanMoveHere(x+xSpeed, y+ySpeed,width, height, lvlData )) {
			this.x += xSpeed;
			this.y += ySpeed;
			moving = true;
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
	
	

			
			
			
	
}
