package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

// all code necessary for enemies to work, patrol, attack, etc.
public class EnemyManager {

	private Playing playing;
	private BufferedImage[][] crabbyArr;
	// list of all the crab enemies 
	private ArrayList<Crabby> crabbies = new ArrayList<>();
	
	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
		addEnemies();
	}
	
	private void addEnemies() {
		crabbies = LoadSave.GetCrabs();
		System.out.println("size of crabs: " + crabbies.size());
		
	}

	public void update(int[][] lvlData) {
		// update all the crab enemies
		// uses the update method found in Enemy.java
		for(Crabby c : crabbies)
			// enemies have access to the level data so they know where to patrol and don't fall off edges
			c.update(lvlData);
	}
	
	public void draw(Graphics g, int xLvlOffset) {
		drawCrabs(g, xLvlOffset);
	}
	
	private void drawCrabs(Graphics g, int xLvlOffset) {
		for(Crabby c : crabbies) {
			g.drawImage(crabbyArr[c.getEnemyState()][c.getAniIndex()], (int) c.getHitbox().x - xLvlOffset - CRABBY_DRAWOFFSET_X, (int) c.getHitbox().y - CRABBY_DRAWOFFSET_Y, CRABBY_WIDTH, CRABBY_HEIGHT, null);
			c.drawHitbox(g, xLvlOffset);
		}
	}

	private void loadEnemyImgs() {
		crabbyArr = new BufferedImage[5][9];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
		
		// iterate through the row of images
		for(int j = 0; j < crabbyArr.length; j++)
			for(int i = 0; i < crabbyArr[j].length; i++)
				crabbyArr[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
		
	}
}
