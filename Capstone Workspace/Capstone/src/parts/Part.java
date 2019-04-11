package parts;

import control.Globals;
import events.EventManager;
import items.Tool;

public abstract class Part implements Parts {

	public double xv;
	public double yv;
	int x;
	int y;
	int xt;
	int yt;
	public int nextMove;
	public boolean hasMoved;
	static final double GRAVITY = (double) 1 / 60;
	static final int[][] dirs2D = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

	public Part(int x, int y) {
		xv = 0;
		yv = 0;
		this.x = x;
		this.y = y;
		xt = 60;
		yt = 60;
		hasMoved = false;
	}

	public void draw(int x, int y, int scale) {
		if (getTexture() != null) {
			Globals.p.noStroke();
			Globals.p.beginShape();
			Globals.p.noTint();
			Globals.p.texture(getTexture());
			Globals.p.vertex(x, y, 0, 0);
			Globals.p.vertex(x, y + 16 * scale, 0, 1);
			Globals.p.vertex(x + 16 * scale, y + 16 * scale, 1, 1);
			Globals.p.vertex(x + 16 * scale, y, 1, 0);
			Globals.p.endShape();
		}
	}

	// Advances x by xv
	private void moveX(Tool tool) {
		if (xt < 1 && xv != 0) {
			int xc = (int) Math.signum(xv);
			if (tool.move(x, y, x + xc, y)) {
				x += xc;
				xt = (int) (60 / Math.abs(xv));
			} else {
				// Make parts run collision code
				Part part = tool.getPart(x + xc, y);
				double force = part.xv - xv;
				part.hitPart(-force, 0);
				hitPart(force, 0);
			}
		}
	}

	// Advances y by yv
	private void moveY(Tool tool) {
		if (yt < 1 && yv != 0) {
			int yc = (int) Math.signum(yv);
			if (tool.move(x, y, x, y + yc)) {
				y += yc;
				yt = (int) (60 / Math.abs(yv));
			} else {
				// Make parts run collision code
				Part part = tool.getPart(x, y + yc);
				double force = part.yv - yv;
				part.hitPart(0, -force);
				hitPart(0, force);
			}
		}
	}

	// Makes sure that it doesn't wait longer than it should to move (x)
	protected void pushXt() {
		xt = (int) Math.min(xt, (int) 60 / Math.abs(xv));
	}

	// Makes sure that it doesn't wait longer than it should to move (y)
	protected void pushYt() {
		yt = (int) Math.min(yt, (int) 60 / Math.abs(yv));
	}

	// Do motion
	public void move(Tool tool) {
		// Checks if the part is touching sludge
		boolean isStuck = false;
		if (!(this instanceof Thermite)) {
			for (int[] dir : dirs2D) {
				if (tool.getPart(x + dir[0], y + dir[1]) instanceof Sludge) {
					isStuck = true;
				}
			}
		}
		xt--;
		yt--;
		if (isStuck) {
			// Parts stick to sludge, giving them the same sticky properties (doesn't work
			// on thermite)
			xv *= 0.9;
			yv *= 0.9;
			if (Math.abs(xv) < 1) {
				xv = 0;
			}
			if (Math.abs(yv) < 1) {
				yv = 0;
			}
		}
		// Move in whichever direction first the part is moving fastest
		if (Math.abs(xv) > Math.abs(yv)) {
			moveX(tool);
			moveY(tool);
		} else {
			moveY(tool);
			moveX(tool);
		}
		yv += GRAVITY;
		pushXt();
		pushYt();
	}

	// Registers a collision with an incoming relative velocity
	public void hitPart(double hxv, double hyv) {
		xv += hxv;
		yv += hyv;
		pushXt();
		pushYt();
	}

	// To be overridden by parts that react to heat
	public void heat() {

	}

}
