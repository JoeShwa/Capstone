package parts;

import control.Globals;
import events.EventManager;
import events.PartMotionEvent;
import items.Tool;

public abstract class Part implements Parts {

	double xv;
	double yv;
	int x;
	int y;
	public int nextMove;

	public Part(int x, int y) {
		xv = 0;
		yv = 0;
		this.x = x;
		this.y = y;
	}

	public void draw(int x, int y, int scale) {
		if (getTexture() != null) {
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
	// Do the motion and queue the next motion
	public void move(Tool tool, char dir) {
		if (dir == 'x') {
			x += Math.signum(xv);
			if (xv != 0) {
				//nextX = (int) (60 / xv);
			}
		} else {
			y += Math.signum(yv);
		}
		// Determine the next axis it will move along
		int nextX = Integer.MAX_VALUE;
		if (xv != 0) {
			nextX = (int) (60 / xv);
		}
		int nextY = Integer.MAX_VALUE;
		if (yv != 0) {
			nextY = (int) (60 / yv);
		}
		if (nextX < nextY) {
			EventManager.addEvent(new PartMotionEvent(tool, this, 'x'), nextX);
			nextMove = nextX + Globals.p.frameCount;
		} else {
			EventManager.addEvent(new PartMotionEvent(tool, this, 'y'), nextY);
			nextMove = nextY + Globals.p.frameCount;
		}
	}

}
