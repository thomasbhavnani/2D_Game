package main;

import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;


public class GamePanel extends JPanel{
	// MouseInputs implements both MouseListener and MouseMotionListener
	private MouseInputs mouseInputs;
	private int xDelta = 100, yDelta = 100;

	public GamePanel() {
		mouseInputs = new MouseInputs(this);
		//pass GamePanel into the Keyboard inputs constructor
		addKeyListener( new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}
	
	public void changeXDelta(int value) {
		this.xDelta += value;
		// display the rectangle at the new x position
		repaint();
	}
	
	public void changeYDelta(int value) {
		this.yDelta += value;
		// display the rectangle at the new y position
		repaint();
	}
	
	public void setRectPos(int x, int y) {
		this.xDelta = x;
		this.yDelta = y;
		repaint();
	}
	
	
	public void paintComponent(Graphics g) {
		// JComponent is the superclass of JPanel
		// calling JComponent's paintComponent
		// cleans the surface so you can draw
		super.paintComponent(g); 
		
		//x position, y position, width, height
		// xDelta and yDelta dynamically change the rectangle's position based on keyboard inputs
		g.fillRect(xDelta, yDelta, 200, 50);
		
	}
}
