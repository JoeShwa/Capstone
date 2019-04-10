package events;

import control.Globals;
import items.Tool;
import parts.Part;

public class PartMotionEvent extends Event {
	
	Tool tool;
	Part part;
	char dir;

	public PartMotionEvent(Tool tool, Part part, char dir) {
		this.tool = tool;
		this.part = part;
		this.dir = dir;
	}
	
	public void trigger() {
		if(part.nextMove == Globals.p.frameCount) {
			part.move(tool, dir);
		}
	}

}
