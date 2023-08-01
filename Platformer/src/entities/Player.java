package entities;


import static utilz.Constants.PlayerConstants.GetSpriteAmount;
import static utilz.Constants.PlayerConstants.*;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Player extends Entity{

	private BufferedImage[][] animations;
	private int aniTick, aniIndex, aniSpeed = 15; 
	private int playerAction = IDLE;
	// set direction as -1 if player is not moving
	private int playerDir = -1;
	private boolean moving = false, attacking = false;
	private boolean left, up, right, down;
	private float playerSpeed = 2.0f;
	
	
	public Player(float x, float y) {
		super(x, y);
		loadAnimations();
		
	}
	
	public void update() {
		updatePos();
		updateAnimationTick();
		setAnimation();
		
	}
	
	public void render(Graphics g) {
		g.drawImage(animations[playerAction][aniIndex], (int) x, (int) y, 256, 160, null);
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
		
		if(left && !right) {
			x -= playerSpeed;
			moving = true;
		} else if(right && !left){
			x += playerSpeed;
			moving = true;
		}
		
		if(up && !down) {
			y -= playerSpeed;
			moving = true;
		} else if(down && !up) {
			y += playerSpeed;
			moving = true;
		}
	}
	
	private void loadAnimations() {
		// slash in front of image name tells pc that image is in a folder, not beside one
		InputStream is = getClass().getResourceAsStream("/player_sprites.png");
		try {
			BufferedImage img = ImageIO.read(is);
			
			animations = new BufferedImage[9][6];
			for(int j = 0; j < animations.length; j++) {
				for(int i = 0 ; i < animations[j].length; i++){
					// sub image gets each 64 x 40 pixel image at specific positions in the main image
					animations[j][i] = img.getSubimage(i*64, j*40, 64, 40);
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
	
	

			
			
			
	
}
