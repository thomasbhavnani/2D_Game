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
	public static final String POTION_ATLAS = "potions_sprites.png";
	public static final String CONTAINER_ATLAS = "objects_sprites.png";
	public static final String TRAP_ATLAS = "trap_atlas.png";
	public static final String CANNON_ATLAS = "cannon_atlas.png";
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
	
	
	
}
