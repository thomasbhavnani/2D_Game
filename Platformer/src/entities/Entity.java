package entities;

// abstract class is a class you cannot create an object of
// other classes extend this and store types of values that are shared among those classes
public abstract class Entity {
	
	// protected means only classes that extend entity can use x and y
	protected float x, y;
	protected int width, height;

	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}
