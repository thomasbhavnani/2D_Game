package entities;



import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.*;
import static utilz.HelpMethods.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class Player extends Entity{

	private BufferedImage[][] animations;
	
	
	private boolean moving = false, attacking = false;
	private boolean left, right, jump;
	
	private int[][] lvlData;
	// x offset of the player hitbox from edge of sprite tile
	private float xDrawOffset = 21 * Game.SCALE;	
	// y offset for the player hitbox from edge of sprite tile
	private float yDrawOffset = 4 * Game.SCALE; 
	
	// jumping / gravity
	
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	
	
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
	
	private int powerBarWidth = (int) (104 * Game.SCALE);
	private int powerBarHeight = (int) (2 * Game.SCALE);
	private int powerBarXStart = (int) (44 * Game.SCALE);
	private int powerBarYStart = (int) (34 * Game.SCALE);
	private int powerWidth = powerBarWidth;
	private int powerMaxValue = 200;
	private int powerValue = powerMaxValue;

	

	private int healthWidth = healthBarWidth;
	


	
	// used to flip player sprite based on which direction it's going (left or right)
	private int flipX = 0;
	private int flipW = 1;
	
	private boolean attackChecked;
	private Playing playing;
	
	private int tileY = 0;
	private boolean powerAttackActive;
	private int powerAttackTick;
	private int powerGrowSpeed = 15;
	private int powerGrowTick;
	
	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		this.state = IDLE;
		this.maxHealth = 100;
		this.currentHealth = 35;
		this.walkSpeed = 1.0f * Game.SCALE;
		loadAnimations();
		initHitbox(20, 27);
		initAttackBox();
	}
	
	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int)(20 * Game.SCALE), (int)(20 * Game.SCALE));
		resetAttackBox();
	}

	public void update() {
		updateHealthBar();
		updatePowerBar();
		if(currentHealth <= 0) {
			if(state != DEAD) {
				state = DEAD;
				aniTick = 0;
				aniIndex = 0;
				playing.setPlayerDying(true);
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
				// aniIndex is sprite amount - 1
			} else if(aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= ANI_SPEED - 1) {
				playing.setGameOver(true);
				playing.getGame().getAudioPlayer().stopSong();
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
			} else
				updateAnimationTick();
			//playing.setGameOver(true);
			return;
		}
			
		updateAttackBox();
		updatePos();
		if(moving) {
			checkPotionTouched();
			checkSpikesTouched();
			tileY = (int) (hitbox.y / Game.TILES_SIZE);
			if(powerAttackActive) {
				powerAttackTick++;
				if(powerAttackTick >= 35) {
					powerAttackTick = 0;
					powerAttackActive = false;
				}
			}
		}
			
		if(attacking || powerAttackActive)
			checkAttack();
		
		updateAnimationTick();
		setAnimation();
		
	}
	
	private void checkSpikesTouched() {
		playing.checkSpikesTouched(this);
		
	}

	private void checkPotionTouched() {
		playing.checkPotionTouched(hitbox);
		
	}

	private void checkAttack() {
		//attack activates at frame 2 of the player attack animation
		if(attackChecked || aniIndex != 1)
			return;
		
		if(powerAttackActive)
			attackChecked = false;
		
		attackChecked = true;
		playing.checkEnemyHit(attackBox);
		playing.checkObjectHit(attackBox);
		playing.getGame().getAudioPlayer().playAttackSound();
			
	}

	private void updateAttackBox() {
		if(right && left) {
			if(flipW == 1) {
				attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 10);
			} else {
				attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 10);
			}
			
		} else if(right || (powerAttackActive && flipW == 1)) {
			attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 10);
		} else if(left || (powerAttackActive && flipW == -1)) {
			attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 10);
		}
		
		attackBox.y = hitbox.y + (Game.SCALE * 10);
		
	}
	
	private void updatePowerBar() {
		powerWidth = (int) ((powerValue / (float) powerMaxValue) * powerBarWidth);
		
		powerGrowTick++;
		
		if(powerGrowTick >= powerGrowSpeed) {
			powerGrowTick = 0;
			changePower(1);
		}
	}

	private void updateHealthBar() {
		// the portion of the max health bar that your current health takes up
		healthWidth = (int) ((currentHealth / (float)maxHealth) * healthBarWidth);
		
	}

	public void render(Graphics g, int lvlOffset) {
		g.drawImage(animations[state][aniIndex], 
				(int) (hitbox.x - xDrawOffset) - lvlOffset + flipX,
				(int) (hitbox.y - yDrawOffset), 
				width * flipW, height, null); // multiply the width by flipW (+- 1) to switch the direction the player sprite is moving
	
		 drawHitbox(g, lvlOffset);
		
		drawAttackBox(g, lvlOffset);
		// no level offset needed for UI because it's always in the same spot on screen
		drawUI(g);
	}
	

	private void drawUI(Graphics g) {
		// Background Ui
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		
		// Health bar
		g.setColor(Color.red);
		// draws the actual red health amount as it fills and depletes in the health bar
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
		
		// Power Bar
		g.setColor(Color.yellow);
		g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
		
	}

	private void updateAnimationTick() {
		aniTick++;
		
		// let each animation image stay for aniSpeed frames out of 120 FPS
		if(aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			
			// loop back through the images in the animation
			if(aniIndex >= GetSpriteAmount(state)) {
				aniIndex = 0;
				// don't want the attack animation on a constant loop like running
				attacking = false;
				// always reset attack check to false
				attackChecked = false;
			}
			
		}
		
	}
	
	private void setAnimation() {
		int startAni = state;
		if (moving) 
			state = RUNNING;
		else
			state = IDLE;
		if(inAir) {
			if(airSpeed > 0) {
				// going upwards
				state = JUMP;
			} else {
				state = FALLING;
			}
		}
		
		if(powerAttackActive) {
			state = ATTACK;
			// want to start the attack animation from the active frame
			aniIndex = 1;
			// make sure animation tick never grows throughout duration of power attack
			aniTick = 0;
			return;
		}
		
		if (attacking) {
			state = ATTACK;
			if(startAni != ATTACK) {
				// want to start the attack animation from the active frame
				
				aniIndex = 1;
				aniTick = 0;
				return;
			}
		}
		// checking for a change in player animation
		if (startAni != state) {
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
		
		if(!inAir)
			if(!powerAttackActive)
				if((!left && !right) || (left && right))
					return;
		
		float xSpeed = 0;
		
		
		if(left && !right) {
			xSpeed -= walkSpeed;
			// used to flip player sprite based on direction it's going
			flipX = width;
			flipW = -1;
		}
		
		if(right && !left) {
			xSpeed += walkSpeed;
			// used to flip player sprite based on direction it's going
			flipX = 0;
			flipW = 1;
		}
		if(powerAttackActive) {
			if((!left && !right) || (left && right))  {
					if(flipW == -1)
						xSpeed = -walkSpeed;
					else
						xSpeed = walkSpeed;
			}
			xSpeed *= 3;
		}
		if(!inAir) 
			if(!IsEntityOnFloor(hitbox, lvlData)) 
				inAir = true;
			
		
		if(inAir && !powerAttackActive) {
			if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData )) {
				hitbox.y += airSpeed;
				airSpeed += GRAVITY;
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
		playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
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
		} else {
			//makes sure hitbox hits wall 
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
			if(powerAttackActive) {
				powerAttackActive = false;
				powerAttackTick = 0;
			}
		}
	
	}
	
	public void changeHealth(int value) {
		currentHealth += value;
		
		if(currentHealth <= 0) {
			currentHealth = 0;
		} else if(currentHealth >= maxHealth)
			currentHealth = maxHealth;
	}
	
	public void kill() {
		currentHealth = 0;
	}
	
	public void changePower(int value) {
		powerValue += value;
		
		if(powerValue>= powerMaxValue)
			powerValue = powerMaxValue;
		else if (powerValue <= 0)
			powerValue = 0;
		
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
		left = false;
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

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}
	
	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		attacking = false;
		moving = false;
		airSpeed = 0f;
		jump = false;
		state = IDLE;
		currentHealth = maxHealth;
		hitbox.x = x;
		hitbox.y = y;
		
		resetAttackBox();
		
		if(!IsEntityOnFloor(hitbox, lvlData)) 
			inAir = true;
	}
	
	private void resetAttackBox() {
		if(flipW == 1) {
			attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 10);
		} else {
			attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 10);
		}
	}

	public int getTileY() {
		return tileY;
	}

	public void powerAttack() {
		if(powerAttackActive)
			return;
		if(powerValue >= 60) {
			powerAttackActive = true;
			changePower(-60);
		}
		
	}




	
	

			
			
			
	
}
