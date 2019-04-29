package events;

import items.Tool;

public class ReloadEvent extends Event {

	Tool tool;
	
	public ReloadEvent(Tool tool) {
		this.tool = tool;
	}
	
	public void trigger() {
		tool.canUse = true;
	}

}
