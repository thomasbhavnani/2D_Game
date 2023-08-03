package main;

import java.awt.Graphics;


import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;


public class Game implements Runnable{
	
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;
	
	private Playing playing;
	private Menu menu;

	
	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 1.5f; // multiplies tile size
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
	
	
	
	public Game() {
		// need to initialize classes first 
		initClasses();
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		// input focuses on the jpanel
		gamePanel.requestFocus();
		
		startGameLoop();
		
		
	}
	
	private void initClasses() {
		menu = new Menu(this);
		playing = new Playing(this);
		
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		// executes the new thread and calls the run method of that thread
		gameThread.start();
	}
	
	// to update player or level
	public void update() {
		// depending on what state we're in we send the updates to that class/state
		switch(Gamestate.state) {
		case MENU:
			menu.update();
;			break;
		case PLAYING:
			playing.update();
			break;
		case OPTIONS:
		case QUIT:
		default:
			// terminates program
			System.exit(0);
			break;
		
		}
	}
	
	public void render(Graphics g){
		// depending on what state we're in we send render things from that class/state
		switch(Gamestate.state) {
		case MENU:
			menu.draw(g);
			break;
		case PLAYING:
			playing.draw(g);
			break;
		default:
			break;
		
		}

	}
	
	@Override
	public void run() {
		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;
		
		
		long previousTime = System.nanoTime();
		
		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();
		
		double deltaU =0;
		double deltaF = 0;
		
		while(true) {
			
			long currentTime = System.nanoTime();
			// check if it has been long enough since last update to update again
			deltaU+=(currentTime - previousTime) / timePerUpdate;
			deltaF+=(currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;
			
			
			if(deltaU >= 1) {
				update();
				updates++;
				deltaU--;
				// for example, if deltaU is 1.1 (more than 100% of the time between updates)
				// next update will come quicker because now deltaU == 0.1
				// increases overall UPS
				// allows the UPS to catch up if there's lag
			}
			if (deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}
					
			// print the FPS and UPS every second
			if(System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;
			}
			
		}
		
	}
	public void windowFocusLost() {
		if(Gamestate.state == Gamestate.PLAYING) {
			playing.getPlayer().resetDirBooleans();
		}
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	public Playing getPlaying() {
		return playing;
	}
	

	
}
