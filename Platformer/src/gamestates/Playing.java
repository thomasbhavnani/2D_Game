package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.util.Random;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;
import static utilz.Constants.Environment.*;

public class Playing extends State implements Statemethods{
	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private ObjectManager objectManager;
	private PauseOverlay pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	private LevelCompletedOverlay levelCompletedOverlay;
	private boolean paused = false;
	
	// used to draw the level around the player moved left/right
	private int xLvlOffset;
	// left and right border is where the player has to reach on screen before level starts moving 
	private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);

//	// how many pixels is the maxTilesOffset
	private int maxLvlOffsetX;
	
	private BufferedImage backgroundImg, bigCloud, smallCloud;
	private int[] smallCloudsPos;
	private Random rnd = new Random();
	
	private boolean gameOver;
	private boolean lvlCompleted; 
	private boolean playerDying;
	
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
		
		calLvlOffset();
		loadStartLevel();
	}

	public void loadNextLevel() {
		levelManager.loadNextLevel();
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		resetAll();
	}
	
	private void loadStartLevel() {
		enemyManager.loadEnemies(levelManager.getCurrentLevel());
		objectManager.loadObjects(levelManager.getCurrentLevel());
		
	}

	private void calLvlOffset() {
		maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
		
	}

	private void initClasses() {
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this); // requires the playing class as argument
		objectManager = new ObjectManager(this);
		player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this);
		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		
		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);
	}
	
	
	
	@Override
	public void update() {
		if(paused) {
			pauseOverlay.update();
		} else if (lvlCompleted) {
			levelCompletedOverlay.update();
		} else if (gameOver) {
			gameOverOverlay.update();
		} else if (playerDying) {
			player.update();
		} else {
			levelManager.update();
			objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			player.update();
			enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			checkCloseToBorder();
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
		objectManager.draw(g, xLvlOffset);
		
		if(paused) {
			g.setColor(new Color(0,0,0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		} else if (gameOver)
			gameOverOverlay.draw(g);
		else if (lvlCompleted)
		levelCompletedOverlay.draw(g);
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
		gameOver = false;
		paused = false;
		lvlCompleted = false;
		playerDying = false;
		player.resetAll();
		enemyManager.resetAllEnemies();
		objectManager.resetAllObjects();
	}
	
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	public void checkObjectHit(Rectangle2D.Float attackBox) {
		objectManager.checkObjectHit(attackBox);
	}
	
	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHit(attackBox);
	}

	public void checkPotionTouched(Rectangle2D.Float hitbox) {
		objectManager.checkObjectTouched(hitbox);
	}
	
	public void checkSpikesTouched(Player p) {
		objectManager.checkSpikesTouched(p);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(!gameOver) {
			if(e.getButton() == MouseEvent.BUTTON1)
				player.setAttacking(true);
			else if(e.getButton() == MouseEvent.BUTTON3)
				player.powerAttack();
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		if(!gameOver)
			if(paused)
				pauseOverlay.mouseDragged(e);
	}



	@Override
	public void mousePressed(MouseEvent e) {
		if(!gameOver) {
			if(paused)
				pauseOverlay.mousePressed(e);
			else if(lvlCompleted)
				levelCompletedOverlay.mousePressed(e);
		} else 
			gameOverOverlay.mousePressed(e);
		
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		if(!gameOver) {
			if(paused)
				pauseOverlay.mouseReleased(e);
			else if(lvlCompleted)
				levelCompletedOverlay.mouseReleased(e);
		} else 
			gameOverOverlay.mouseReleased(e);
	}



	@Override
	public void mouseMoved(MouseEvent e) {
		if(!gameOver) {
			if(paused)
				pauseOverlay.mouseMoved(e);
			else if(lvlCompleted)
				levelCompletedOverlay.mouseMoved(e);
		} else 
			gameOverOverlay.mouseMoved(e);
	}
	
	public void setLevelCompleted(boolean levelCompleted) {
		this.lvlCompleted = levelCompleted;
		if(levelCompleted)
			game.getAudioPlayer().lvlCompleted();
	}
	
	public void setMaxLvlOffset(int lvlOffset) {
		this.maxLvlOffsetX = lvlOffset;
	}
	
	public void unpauseGame() {
		paused = false;
	}



	@Override
	public void keyPressed(KeyEvent e) {
		if(gameOver)
			gameOverOverlay.keyPressed(e);
		else
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
		
		if(!gameOver)
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
	
	public EnemyManager getEnemyManager() {
		return enemyManager;
	}
	
	public ObjectManager getObjectManager() {
		return objectManager;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

	public void setPlayerDying(boolean playerDying) {
		this.playerDying = playerDying;
		
	}




	
	
	

}
