package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.GameOverOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;
import static utilz.Constants.Environment.*;

public class Playing extends State implements Statemethods{
	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private PauseOverlay pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	private boolean paused = false;
	
	// used to draw the level around the player moved left/right
	private int xLvlOffset;
	// left and right border is where the player has to reach on screen before level starts moving 
	private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
	// how many tiles wide the whole level is
	private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
	// difference how many tiles you can see and how many there actually are
	private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
	// how many pixels is the maxTilesOffset
	private int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;
	
	private BufferedImage backgroundImg, bigCloud, smallCloud;
	private int[] smallCloudsPos;
	private Random rnd = new Random();
	
	private boolean gameOver;
	
	public Playing(Game game) {
		super(game);
		initClasses();
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
		bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
		smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
		
		// for the vertical location of the small clouds
		smallCloudsPos = new int[8];
		for(int i = 0; i < smallCloudsPos.length; i++) {
			smallCloudsPos[i] = (int) (90 * Game.SCALE) + rnd.nextInt((int)(150 * Game.SCALE));
			
		}
		
	}

	
	
	private void initClasses() {
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this); // requires the playing class as argument
		player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this);
		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);
	}
	
	
	
	@Override
	public void update() {
		if(!paused) {
			levelManager.update();
			player.update();
			enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			checkCloseToBorder();
		} else {
			pauseOverlay.update();
		}
		
	}



	private void checkCloseToBorder() {
		int playerX = (int) player.getHitbox().x;
		// used to check if new player position is < 20% or > 80%
		int diff = playerX - xLvlOffset;
		
		if(diff > rightBorder)
			xLvlOffset += diff - rightBorder;
		// if playuerX is at pixel 85, level screen is offset 0 pixes, and the border is at pixel 80 on the screen
		// we know the player is beyond the x border because 85 - 0 > 80 
		// so the level screen has to offset 5 more pixels to keep the player at the border of the screen (offset += (85 - 80))
		else if (diff < leftBorder)
			xLvlOffset += diff - leftBorder;
		
		// makes sure xLvlOffset doesn't go beyond the max x size of the level
		if (xLvlOffset > maxLvlOffsetX)
			xLvlOffset = maxLvlOffsetX;
		else if (xLvlOffset < 0)
			xLvlOffset = 0;
		
	}



	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		
		drawClouds(g);
		levelManager.draw(g, xLvlOffset);
		// player is drawn in front of level
		player.render(g, xLvlOffset);
		enemyManager.draw(g, xLvlOffset);
		
		if(paused) {
			g.setColor(new Color(0,0,0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		}
	}



	private void drawClouds(Graphics g) {
		for(int i  = 0; i < 3; i++)
			// subtract the xLvlOffset from the x position to make the clouds move in the background as the player moves
			// smaller clouds should move quicker so xLvlOffset multiplied by a greater number in the x position of the small clouds
			g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.3), (int)(204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
		
		for(int i = 0; i < smallCloudsPos.length; i++)
			// add 4 clouds of width between each small cloud
			g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.7), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
		

	}
	
	public void resetAll() {
		
	}
	
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHit(attackBox);
	}



	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
			player.setAttacking(true);
		
	}
	
	public void mouseDragged(MouseEvent e) {
		if(paused)
			pauseOverlay.mouseDragged(e);
	}



	@Override
	public void mousePressed(MouseEvent e) {
		if(paused)
			pauseOverlay.mousePressed(e);
		
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		if(paused)
			pauseOverlay.mouseReleased(e);
		
	}



	@Override
	public void mouseMoved(MouseEvent e) {
		if(paused)
			pauseOverlay.mouseMoved(e);
		
	}
	
	public void unpauseGame() {
		paused = false;
	}



	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {

		case KeyEvent.VK_A:
			player.setLeft(true);
			break;
		case KeyEvent.VK_D:
			player.setRight(true);
			break;
		case KeyEvent.VK_SPACE:
			player.setJump(true);
			break;
		case KeyEvent.VK_ESCAPE:
			paused = !paused;
			break;
		}	
	}



	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {

		case KeyEvent.VK_A:
			player.setLeft(false);
			break;
		case KeyEvent.VK_D:
			player.setRight(false);
			break;
		case KeyEvent.VK_SPACE:
			player.setJump(false);
			break;
			
		}
		
	}
	
	public void windowFocusLost() {
		player.resetDirBooleans();
	}
	
	public Player getPlayer() {
		return player;
	}
}
