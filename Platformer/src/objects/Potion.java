package objects;

import main.Game;

public class Potion extends GameObject{
	
	private float hoverOffset;
	private int maxHoverOffset, hoverDir = 1;

	public Potion(int x, int y, int objType) {
		super(x, y, objType);
		doAnimation = true;
		initHitbox(7, 14);
		
		// where the actual item is within its sprite
		xDrawOffset = (int)(3 * Game.SCALE);
		yDrawOffset = (int)(2 * Game.SCALE);
		
		maxHoverOffset = (int) (10 * Game.SCALE);
	}
	
	public void update() {
		// potions will always be animating while they are present
		updateAnimationTick();
		updateHover();
	}

	private void updateHover() {
		hoverOffset += (0.075f * Game.SCALE * hoverDir);
		
		if(hoverOffset >= maxHoverOffset)
			hoverDir = -1;
		else if (hoverOffset < 0)
			hoverDir = 1;
		
		hitbox.y = y - hoverOffset;
	}

}
