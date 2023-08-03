package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.PauseOverlay;
import utilz.LoadSave;

public class Playing extends State implements Statemethods{
	private Player player;
	private LevelManager levelManager;
	private PauseOverlay pauseOverlay;
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
	
	public Playing(Game game) {
		super(game);
		initClasses();
		
	}

	
	
	private void initClasses() {
		levelManager = new LevelManager(game);
		player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE));
		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		pauseOverlay = new PauseOverlay(this);
	}
	
	
	
	@Override
	public void update() {
		if(!paused) {
			levelManager.update();
			player.update();
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
		levelManager.draw(g, xLvlOffset);
		// player is drawn in front of level
		player.render(g, xLvlOffset);
		
		if(paused) {
			g.setColor(new Color(0,0,0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		}
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
