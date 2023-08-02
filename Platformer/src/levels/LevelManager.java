package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import utilz.LoadSave;

public class LevelManager {

	private Game game;
	private BufferedImage[] levelSprite;
	private Level levelOne;
	
	public LevelManager(Game game) {
		this.game = game;
		//levelSprite = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		importOutsideSprites();
		levelOne = new Level(LoadSave.GetLevelData());
	}
	private void importOutsideSprites() {
		levelSprite = new BufferedImage[48]; // 12 (width) x 48 (height) sprites
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		for(int j = 0; j < 4; j++)
			for(int i = 0; i < 12; i++) {
				int index = j * 12 + i; 	// because we're putting all level sprites in a single array, 
				levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);					 	// access row index * 12 to get to a new sprite atlas row in a single array
			}
		
	}
	public void draw(Graphics g) {
		for(int j = 0; j < Game.TILES_IN_HEIGHT; j++) {
			for(int i = 0; i < Game.TILES_IN_WIDTH; i++) {
				int index = levelOne.getSpriteIndex(i, j);
				g.drawImage(levelSprite[index], Game.TILES_SIZE * i, Game.TILES_SIZE * j, Game.TILES_SIZE, Game.TILES_SIZE, null);
			}
		}
		
	}
	
	public void update() {
		
	}
	
	public Level getCurrentLevel() {
		return levelOne;
	}
}
