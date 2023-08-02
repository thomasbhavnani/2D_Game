package utilz;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import main.Game;

public class LoadSave {
	public static final String PLAYER_ATLAS = "player_sprites.png";
	public static final String LEVEL_ATLAS = "outside_sprites.png";
	public static final String LEVEL_ONE_DATA = "level_one_data.png";
	public static final String MENU_BUTTONS = "button_atlas.png";
	// we only have static methods so we do not have to 
	// create an object of this class to access any methods 
	
	// use this method to get any sprite assets
	// (i think an atlas is the grid of all the different sprites used in that asset)
	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		// slash in front of image name tells pc that image is in a folder, not beside one
		InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
		try {
			img = ImageIO.read(is);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return img;
	}
	// size of array matches size of game window in terms of tiles of width and height
	public static int[][] GetLevelData(){
		int[][] lvlData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH]; // [ROWS][COLUMNS]
		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
		
		// get height and width of the atlas in pixels
		for(int j = 0; j < img.getHeight(); j++) { 
			for(int i = 0; i < img.getWidth(); i++) {
				// how does getRGB(i, j) get the pixel at the correct sprite index in the atlas???
				Color color = new Color(img.getRGB(i, j)); 
				int value = color.getRed();
				// we only have 48 sprites so can't have index >= 48
				if(value >= 48) {
					value = 0;
				}
				// the red value of of the sprite is going to be the index for that sprite
				// if the red value is 3, it's going to be index 3 in the sprite array
				
				// what's happening here makes no sense, he iterates through every pixel in the atlas 
				// but somehow only gets 48 unique values for the 48 tiles in the game window?
				// how are j and i being used to iterate through pixels but also don't exceed the bounds
				// of the lvlData array which is only 4 rows 12 columns???
				
				lvlData[j][i] = value;
			}
		}
					
				

		return lvlData;
	}
}
