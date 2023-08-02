package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

// an interface is a collection of methods that we want 
// each class to have to create if they implement this interface
// the methods will be different in each class but they each need it
// what's the difference between that and implementing an abstract class?
public interface Statemethods {

	public void update();
	
	public void draw(Graphics g);
	
	public void mouseClicked(MouseEvent e);
	
	public void mousePressed(MouseEvent e);
	
	public void mouseReleased(MouseEvent e);
	
	public void mouseMoved(MouseEvent e);
	
	public void keyPressed(KeyEvent e);
	
	public void keyReleased(KeyEvent e);
	
	
}
