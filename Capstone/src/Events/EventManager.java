package Events;

import java.util.HashMap;
import java.util.LinkedList;

import Control.Main;

public class EventManager {

	public static Main m;
	static HashMap<Integer, LinkedList<Event>> events;

	public static void init(Main m) {
		EventManager.m = m;
		events = new HashMap<>();
	}

	// Finds and run all events set to run on that specific frame
	public static void runEvents() {
		if (events.containsKey(m.frameCount)) {
			LinkedList<Event> cur = events.get(m.frameCount);
			while (cur.size() > 0) {
				Event e = cur.remove();
				e.trigger();
			}
			events.remove(m.frameCount);
		}
	}

	// Adds an event to be ran in time frames from the time of the method call
	public static void addEvent(Event e, int time) {
		time += m.frameCount;
		if (!events.containsKey(time)) {
			events.put(time, new LinkedList<Event>());
		}
		events.get(time).add(e);
	}
}
