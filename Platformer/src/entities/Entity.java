package entities;

// abstract class is a class you cannot create an object of
// other classes extend this and store types of values that are shared among those classes
public abstract class Entity {
	
	// protected means only classes that extend entity can use x and y
	protected float x, y;
	public Entity(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
