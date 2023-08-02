package utilz;

import main.Game;

public class HelpMethods {
	
	// checks the player position and if that position overlaps any tile
	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
		
		
		if(!IsSolid(x, y ,lvlData)) 				// check top left corner of hitbox for collision
			if(!IsSolid(x + width, y + height, lvlData))	// check bottom right corner of htibox for collision
				if(!IsSolid(x + width, y, lvlData))		//check top right corner of hitbox for collision
					if(!IsSolid(x, y + height, lvlData))
						return true;
		return false;
			
	}
	
	// check that the position is a tile but also that the position is inside the game window
	private static boolean IsSolid(float x, float y, int[][] lvlData) {
		
		// if player reaches x or y border, it can't move past
		if (x < 0 || x >= Game.GAME_WIDTH) 
			return true;
		if (y < 0 || y >= Game.GAME_HEIGHT) 
			return true;
		
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;

		// can't pass floats into lvlData which is an integer array
		int value = lvlData[(int) yIndex][(int) xIndex];
		
		// we only have 48 sprites
		// sprite 11 is transparent it is not solid so it should return false
		// should not be able to pass through any of these sprite tiles
		if(value >= 48 || value < 0 || value != 11) 
			return true;
		
		return false;
		
	}

}