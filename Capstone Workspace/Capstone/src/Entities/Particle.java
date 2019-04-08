
package Entities;

import Control.Globals;
import Control.World;

public class Particle extends Entity {
	
	public Particle(double x, double y, double z, double xv, double yv, double zv) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.xv = xv;
		this.yv = yv;
		this.zv = zv;
	}
	
	public void draw() {
		Globals.p.pushMatrix();
		Globals.p.translate((float) x * 100, (float) y * 100, (float) z * 100);
		Globals.p.fill(100, 0, 0);
		Globals.p.box(16);
		Globals.p.popMatrix();
	}
	
	public void update() {
		x += xv;
		y += yv;
		z += zv;
		xv *= 0.98;
		yv *= 0.98;
		zv *= 0.98;
		wrapPos();
		yv += World.GRAVITY;
		if(Globals.world.getBlock(x, y, z).isSolid()) {
			kill();
		}
	}

}
