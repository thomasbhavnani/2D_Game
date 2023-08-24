package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import main.Game;

// abstract class is a class you cannot create an object of
// other classes extend this and store types of values that are shared among those classes
public abstract class Entity {
	
	// protected means only classes that extend entity can use x and y
	protected float x, y;
	protected int width, height;
	// all entities need a hitbox
	protected Rectangle2D.Float hitbox;
	protected int aniTick, aniIndex; 
	protected int state;
	protected float airSpeed;
	protected boolean inAir = false;
	protected int maxHealth;
	protected int currentHealth;
	protected Rectangle2D.Float attackBox;
	protected float walkSpeed;
	
	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, (int)(width * Game.SCALE),(int) (height*Game.SCALE));
	}
	
	protected void drawHitbox(Graphics g, int xLvlOffset) {
		// for debugging the hitbox
		g.setColor(Color.PINK);
		g.drawRect((int)hitbox.x - xLvlOffset, (int)hitbox.y,(int) hitbox.width, (int)hitbox.height);
	}
	
	protected void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int)(attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int)attackBox.height);
	}
	
	// protected because we only want whatever is extending entity to update hitbox
	protected void updateHitbox() {
		hitbox.x = (int) x; // x and y are being updated along with player movement because Player extends Entity
		hitbox.y = (int) y;	// player uses super(x, y, width, height) so rectangle position changes with player position
	}
	
	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}
	
	public int getState() {
		return state;
	}
	
	// returns current animation frame being selected
	public int getAniIndex() {
		return aniIndex;
	}
	
}
