package objects;
import static utilz.Constants.ObjectConstants.*;

import main.Game;

public class GameContainer extends GameObject{

	public GameContainer(int x, int y, int objType) {
		super(x, y, objType);
		
		createHitbox();
		
	}

	private void createHitbox() {
		// initializes the hitbox for all types of container objects
		
		if(objType == BOX) {
			initHitbox(25, 18);
			xDrawOffset = (int) (7 * Game.SCALE);
			yDrawOffset = (int) (12 * Game.SCALE);
		} else {
			initHitbox(23, 25);
			xDrawOffset = (int) (8 * Game.SCALE);
			yDrawOffset = (int) (5 * Game.SCALE);
		}
		
		// add game scale * 2 because there's two pixel difference between sprite height and tile size
		hitbox.y += yDrawOffset + (int)(Game.SCALE * 2);
		hitbox.x += xDrawOffset / 2;
		
	}
	
	public void update() {
		if(doAnimation)
			updateAnimationTick();
			
	}

}
