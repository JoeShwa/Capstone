package items;

import blocks.Block;
import control.Globals;
import parts.Part;
import parts.Trigger;

public class Tool extends Item {

	private String name;
	private Part[][] parts;

	static final int TOOL_SPEED = 20;
	static final parts.Rift rift = new parts.Rift(-1, -1);
	
	static int tools = 0;
	

	public Tool() {
		tools++;
		name = "Apparatus " + tools;
		parts = new Part[16][16];
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				setPart(x, y, new parts.Air(x, y));
			}
		}
	}

	// Move all of the parts
	public void update() {
		for (int x = 0; x < parts.length; x++) {
			for (int y = 0; y < parts[0].length; y++) {
				Part p = getPart(x, y);
				// Prevents parts from being moved multiple times in the same frame
				if (!p.hasMoved && !(p instanceof parts.Air)) {
					getPart(x, y).move(this);
				}
			}
		}
		// Flags all the parts as movable again
		for (int x = 0; x < parts.length; x++) {
			for (int y = 0; y < parts[0].length; y++) {
				Part p = getPart(x, y);
				p.hasMoved = false;
			}
		}
	}
	
	// Run activation on all activation parts of the tool
	public void activate() {
		for(int x = 0; x < parts.length; x++) {
			for(int y = 0; y < parts[0].length; y++) {
				if(getPart(x, y) instanceof Trigger) {
					Trigger trig = (Trigger) getPart(x, y);
					trig.activate();
				}
			}
		}
	}

	// Gets the part at the coords
	// MIGHT CAUSE ISSUES WITH TOOL CONSTRUCTION, Items INTERFACE
	public Part getPart(int x, int y) {
		if (x > parts.length - 1 || x < 0 || y > parts[0].length - 1 || y < 0) {
			return rift;
		}
		return parts[x][y];
	}

	public void setPart(int x, int y, Part p) {
		parts[x][y] = p;
	}

	public boolean move(int x, int y, int nx, int ny) {
		if (getPart(nx, ny) instanceof parts.Air) {
			setPart(nx, ny, getPart(x, y));
			setPart(x, y, new parts.Air(x, y));
			return true;
		}
		if (getPart(nx, ny) instanceof parts.Rift) {
			double xv = getPart(x, y).xv * Math.cos(Globals.player.getYaw()) / TOOL_SPEED + Globals.player.xv;
			double yv = getPart(x, y).yv / TOOL_SPEED + Globals.player.yv;
			double zv = getPart(x, y).xv * Math.sin(Globals.player.getYaw()) / TOOL_SPEED + Globals.player.zv;
			Globals.world.addEntity(new entities.Particle(getPart(x, y), Globals.player.getX(), Globals.player.getY(),
					Globals.player.getZ(), xv, yv, zv));
			setPart(x, y, new parts.Air(x, y));
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public String getLore() {
		return "How are you even viewing this screen?";
	}

	public Block getBlock() {
		return null;
	}

	public void drawBig() {
		// Determine top left corner's coordinates
		int cornX = p.width / 2 - 512;
		int cornY = p.height / 2 - 512;
		// Draw outline
		p.stroke(255);
		p.strokeWeight(1);
		p.noFill();
		p.rect(cornX + 1, cornY + 1, 1022, 1022);
		p.noStroke();
		// Draw each part
		for (int x = 0; x < parts.length; x++) {
			for (int y = 0; y < parts[0].length; y++) {
				Part part = getPart(x, y);
				part.draw(cornX + x * 64, cornY + y * 64, 4);
			}
		}
	}

	public void draw(int x, int y) {
		// Rescale and refit position to render
		x *= 256;
		y *= 256;
		y += 28;
		// Draw outline (useful for identifying empty tools)
		p.stroke(255);
		p.strokeWeight(1);
		p.noFill();
		p.rect(x + 1, y + 1, 254, 254);
		p.noStroke();
		p.fill(0);
		p.rect(x + 1, y + 1, 254, 254);
		// Draw each part
		for (int px = 0; px < parts.length; px++) {
			for (int py = 0; py < parts[0].length; py++) {
				Part part = getPart(px, py);
				part.draw(x + px * 16, y + py * 16, 1);
			}
		}
	}
}
