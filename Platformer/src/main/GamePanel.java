package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.Random;

import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;


public class GamePanel extends JPanel{
	// MouseInputs implements both MouseListener and MouseMotionListener
	private MouseInputs mouseInputs;
	private float xDelta = 100, yDelta = 100;
	private float xDir = 0.03f, yDir = 0.03f;
	private Color color = new Color(150, 20, 90);
	private Random random;
	private int frames = 0;
	private long lastCheck = 0;

	public GamePanel() {
		mouseInputs = new MouseInputs(this);
		//pass GamePanel into the Keyboard inputs constructor
		addKeyListener( new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
		random =  new Random();
	}
	
	public void changeXDelta(int value) {
		this.xDelta += value;
		// display the rectangle at the new x position
		
	}
	
	public void changeYDelta(int value) {
		this.yDelta += value;
		// display the rectangle at the new y position
		
	}
	
	public void setRectPos(int x, int y) {
		this.xDelta = x;
		this.yDelta = y;
		
	}
	
	
	public void paintComponent(Graphics g) {
		// JComponent is the superclass of JPanel
		// calling JComponent's paintComponent
		// cleans the surface so you can draw
		super.paintComponent(g); 
		updateRectangle();
		g.setColor(color);
		//x position, y position, width, height
		// xDelta and yDelta dynamically change the rectangle's position based on keyboard inputs
		g.fillRect((int) xDelta, (int) yDelta, 200, 50);
		frames++;
		if(System.currentTimeMillis() - lastCheck >= 1000) {
			lastCheck = System.currentTimeMillis();
			System.out.println("FPS: " + frames);
			frames = 0;
		}
		// repaint calls paintComponent so it creates a recursive loop
		repaint();
	}
	private void updateRectangle() {
		//reverse the x or y directions when the rectangle reaches the x or y axis limit
		xDelta += xDir;
		if(xDelta > 400 || xDelta < 0) {
			xDir*=-1;
			color = getRndColor();
		}
		yDelta += yDir;
		if(yDelta > 400 || yDelta < 0) {
			yDir*=-1;
			color = getRndColor();
		}
		
	}
	
	private Color getRndColor() {
		// random.nextInt(x) returns a number between 0 and x
		int r = random.nextInt(255);
		int g = random.nextInt(255);
		int b = random.nextInt(255);
		
		return new Color(r, g, b);
		//TODO ended video 3 at 14:30
	}
}
