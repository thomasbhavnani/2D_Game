package utilz;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.Crabby;
import static utilz.Constants.EnemyConstants.CRABBY;
import main.Game;

public class LoadSave {
	public static final String PLAYER_ATLAS = "player_sprites.png";
	public static final String LEVEL_ATLAS = "outside_sprites.png";
	public static final String MENU_BUTTONS = "button_atlas.png";
	public static final String MENU_BACKGROUND = "menu_background.png";
	public static final String PAUSE_BACKGROUND = "pause_menu.png";
	public static final String SOUND_BUTTONS = "sound_button.png";
	public static final String URM_BUTTONS = "urm_buttons.png"; // unpause, replay, menu
	public static final String VOLUME_BUTTONS = "volume_buttons.png";
	// this is for the big background image that goes behind the menu itself and takes up the full game window
	public static final String MENU_BACKGROUND_IMG = "background_menu.png";
	public static final String PLAYING_BG_IMG = "playing_bg_img.png";
	public static final String BIG_CLOUDS = "big_clouds.png";
	public static final String SMALL_CLOUDS = "small_clouds.png";
	public static final String CRABBY_SPRITE = "crabby_sprite.png";
	public static final String STATUS_BAR = "health_power_bar.png";
	public static final String COMPLETED_IMG = "completed_sprite.png";
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
	
	public static BufferedImage[] GetAllLevels() {
		// gets the path to the lvls folder
		URL url = LoadSave.class.getResource("/lvls");
		
		// access the folder and look at what's inside it
		File file = null;
		
		
		// URI is the identifier for the resource you are looking for
		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		// goes over all files in the folder and makes a list of them
		File[] files = file.listFiles();
		File[] filesSorted = new File[files.length];
		
		
		// sorting the files list
		for(int i = 0; i < filesSorted.length; i++)
			for(int j = 0; j < files.length; j++) { 	
				if(files[j].getName().equals("" + (i  + 1) + ".png"))
					filesSorted[i] = files[j];
			}
		
		
		BufferedImage[] imgs = new BufferedImage[filesSorted.length];
		
		
		// make an array of level data images
		for(int i = 0; i < imgs.length; i++)
			try {
				imgs[i] = ImageIO.read(filesSorted[i]);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		
		return imgs;
	}
	
	// using the green color of the RGB values encoded into the level data to spawn enemies
	public static ArrayList<Crabby> GetCrabs(){
		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
		ArrayList<Crabby> list = new ArrayList<>();
		
		for(int j = 0; j < img.getHeight(); j++)  
			for(int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j)); 
				int value = color.getGreen();
				
				// if the green value == CRABBY (which is 0)
				// put a Crab at this location in the level and store that location data in the Crab array
				if(value == CRABBY) 
					list.add(new Crabby(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
			}
		
		return list;
	}
	
	// size of array matches size of game window in terms of tiles of width and height
	public static int[][] GetLevelData(){
		
		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
		
		// level data is now the same size as the level image
		int[][] lvlData = new int[img.getHeight()][img.getWidth()]; // [ROWS][COLUMNS]
		
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
				
				// wait it actually makes sense, i think the level data is organized into 4x12 pixel PNG's 
				// so the data is encoded into it
				
				lvlData[j][i] = value;
			}
		}
					
				

		return lvlData;
	}
}
