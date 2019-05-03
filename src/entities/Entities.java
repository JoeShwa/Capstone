package entities;

public interface Entities {

	public void draw();
	
	public void update();
	
	public boolean check();
	
	public boolean onPoint(double x, double y, double z);
}
