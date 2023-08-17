package utilz;

import java.awt.geom.Rectangle2D;

import main.Game;

public class HelpMethods {
	
	// checks the player position and if that position overlaps any tile
	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
		
		
		if(!IsSolid(x, y ,lvlData)) 							// check top left corner of hitbox for collision
			if(!IsSolid(x + width, y + height, lvlData))		// check bottom right corner of htibox for collision
				if(!IsSolid(x + width, y, lvlData))				// check top right corner of hitbox for collision
					if(!IsSolid(x, y + height, lvlData))		// check bottom left corner of hitbox for collision
						return true;							
		return false;
			
	}
	
	// check that the position is a tile but also that the position is inside the game window
	private static boolean IsSolid(float x, float y, int[][] lvlData) {
		
		// max width of entire level
		int maxWidth = lvlData[0].length * Game.TILES_SIZE;
		
		if (x < 0 || x >= maxWidth) 
			return true;
		if (y < 0 || y >= Game.GAME_HEIGHT) 
			return true;
		
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;

		// can't pass floats into lvlData which is an integer array
		// determines which tile in the lvlData array you are on
		// note: can be a little janky because of the integer rounding of the float location values, 
		// makes you collide with things when it doesn't look like you should
		int value = lvlData[(int) yIndex][(int) xIndex];
		
		// we only have 48 sprites so greater than 48 is no tile at all
		// should not be able to pass through any of these sprite tiles
		// checking if the location of the player is a tile
		// if the sprite is not transparent (sprite 11) than you cannot move through it and it is solid
		if(value >= 48 || value < 0 || value != 11) 
			return true;
		
		return false;
	}
	
	public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
		// find what tile player is currently on
		int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
		if(xSpeed > 0) {
			//right
			int tileXPos = currentTile * Game.TILES_SIZE;
			// determining where the player sprite is within the tile with xOffset from tile edge
			// allows player hitbox to be right up against a solid tile
			int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
			// -1 so that the hitbox edge does not overlap with the tile edge
			return tileXPos + xOffset - 1;
		}else {
			return currentTile * Game.TILES_SIZE;
		}
	}
	
	public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
		
		if(airSpeed > 0) {
			// player is falling - touching floor
			int tileYPos = currentTile * Game.TILES_SIZE;
			
			// distance between edge of sprite tile and hitbox in the y direction
			int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
			return tileYPos + yOffset - 1;
		} else {
			// player is jumping
			return currentTile * Game.TILES_SIZE;
		}
	}
	
	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		// Check the pixel below bottom left corner (which is why it is  hitbox.y + hitbox.height + 1 for the one pixel below)
		if(!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData)) 
			// check the pixel blow bottom right corner 
			if(!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData)) 
				return false;
			
		return true;
	}
	
	// check if entity is standing on a floor, checking if it walks to an edge
	public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
		return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
	}
}