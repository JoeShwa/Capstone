package entities;

public abstract class Enemy extends Entity {
	
	public Enemy(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void triggerNear(double relX, double relY, double relZ) {
		
	}
}
