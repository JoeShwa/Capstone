
package entities;

import java.util.LinkedList;

import blocks.Block;
import control.Globals;
import control.World;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public abstract class Particle extends Entity {
	
	int movement = 0;
	
	public Particle(double x, double y, double z, double xv, double yv, double zv) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.xv = xv;
		this.yv = yv;
		this.zv = zv;
	}
	
	public PImage getTexture() {
		return null;
	}
	
	public void draw() {
		Globals.p.pushMatrix();
		doTranslation();
		int px = (int) Math.signum(Globals.player.getX() - x);
		int py = (int) Math.signum(Globals.player.getY() - y);
		int pz = (int) Math.signum(Globals.player.getZ() - z);
		for (int i = 0; i < 6; i++) {
			if ((Globals.dirs[i][0] == 0 || Globals.dirs[i][0] == px) && (Globals.dirs[i][1] == 0 || Globals.dirs[i][1] == py)
					&& (Globals.dirs[i][2] == 0 || Globals.dirs[i][2] == pz)) {
				Globals.p.beginShape(PConstants.QUADS);
				Globals.p.texture(getTexture());
				Globals.p.textureMode(PConstants.NORMAL);
				for (int j = 0; j < 4; j++) {
					PVector vec = Block.sh.getVertex(i * 4 + j);
					float mapX;
					float mapY;
					mapX = vec.x * Math.max(Math.abs(Globals.dirs[i][2]), Math.abs(Globals.dirs[i][1]))
							+ vec.z * Math.abs(Globals.dirs[i][0]);
					mapY = vec.y * Math.max(Math.abs(Globals.dirs[i][2]), Math.abs(Globals.dirs[i][0]))
							+ vec.z * Math.abs(Globals.dirs[i][1]);
					mapX = mapX / 100 + 0.5f;
					mapY = mapY / 100 + 0.5f;
					Globals.p.vertex(vec.x / 8, vec.y / 8, vec.z / 8, mapX, mapY);
				}
				Globals.p.endShape(PConstants.CLOSE);
			}
		}
		Globals.p.popMatrix();
	}
	
	public void update() {
		if(xv * xv + yv * yv + zv * zv > 0.01 * 0.01) {
			movement = 30;
		}
		yv += World.GRAVITY;
		x += xv;
		y += yv;
		z += zv;
		if(check()) {
			hit();
		}
		Entity e = checkEntity();
		if(e != null) {
			hit(e);
		}
		movement--;
		if(movement == 0) {
			kill();
		}
		super.update();
	}
	
	public boolean check() {
		return Globals.world.getBlock(x, y, z).isSolid();
	}
	
	// Overrideable to give particles special properties on impact with block
	public void hit() {
		kill();
	}
	
	// Overrideable to give particles special properties on impact with entity
	public void hit(Entity e) {
		kill();
	}
	
	public boolean onPoint(double x, double y, double z) {
		return this.x == x && this.y == y && this.z == z;
	}
	
	//Checks if the particle is touching an entity
	public Entity checkEntity() {
		LinkedList<Entity> ents = Globals.world.getNearEntities(Globals.floor(x), Globals.floor(y), Globals.floor(z), 1);
		for(Entity e : ents) {
			if(this != e && e.onPoint(x, y, z)) {
				return e;
			}
		}
		return null;
	}

}
