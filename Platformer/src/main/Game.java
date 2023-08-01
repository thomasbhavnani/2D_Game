package main;

import java.awt.Graphics;

import entities.Player;

public class Game implements Runnable{
	
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;
	
	private Player player;
	
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
		player = new Player(200, 200);
		
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		// executes the new thread and calls the run method of that thread
		gameThread.start();
	}
	
	// to update player or level
	public void update() {
		player.update();
	}
	
	public void render(Graphics g){
		player.render(g);
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
		player.resetDirBooleans();
	}
	
	public Player getPlayer() {
		return player;
	}
	
}
