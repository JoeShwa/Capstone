
package entities;

import blocks.Block;
import control.Globals;
import control.World;
import parts.Part;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class Particle extends Entity {
	
	Part part;
	
	public Particle(Part part, double x, double y, double z, double xv, double yv, double zv) {
		this.part = part;
		this.x = x;
		this.y = y;
		this.z = z;
		this.xv = xv;
		this.yv = yv;
		this.zv = zv;
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
				Globals.p.texture(part.getTexture());
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
					Globals.p.vertex(vec.x / 16, vec.y / 16, vec.z / 16, mapX, mapY);
				}
				Globals.p.endShape(PConstants.CLOSE);
			}
		}
		Globals.p.popMatrix();
	}
	
	public void update() {
		x += xv;
		y += yv;
		z += zv;
		xv *= 0.98;
		yv *= 0.98;
		zv *= 0.98;
		yv += World.GRAVITY;
		if(Globals.world.getBlock(x, y, z).isSolid()) {
			kill();
		}
		super.update();
	}

}
