package events;

import control.Globals;

public class LogEvent extends Event {

	String msg;
	
	public LogEvent(String msg) {
		this.msg = msg;
	}
	
	public void trigger() {
		Globals.gui.log(msg);
	}

}
