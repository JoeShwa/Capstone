package control;

import java.util.Iterator;
import java.util.LinkedList;
import items.Item;
import items.Tool;
import parts.Part;
import processing.core.PApplet;

public class GUI {

	static PApplet p;
	// Globals for pulse effect when clicking
	int pulse = 256;
	int pulseChange = 0;
	// GUI's possible states
	public static final int GAME = 0;
	public static final int INVENTORY = 1;
	public static final int RESEARCH = 2;
	public static final int RES_ITEM = 3;
	public static final int OBSERVE = 4;
	public static final int TOOL = 5;
	// GUI's current state
	public int guiState = GAME;
	// Prevents unintentional spam-switching of GUI state
	boolean[] prevC;
	boolean canSwitchState = true;
	// Holds data logged on screen
	LinkedList<Log> logs;
	// Item currently being worked with in the GUI
	Item workItem;

	public GUI(PApplet p) {
		GUI.p = p;
		logs = new LinkedList<>();
		prevC = new boolean[Globals.player.input.length];
	}

	public void log(String log) {
		logs.add(new Log(log));
		if (logs.size() > 20) {
			logs.remove();
		}
	}

	public void leftClick() {
		switch (guiState) {
		case GAME:
			pulse = 0;
			pulseChange = 51;
			break;
		case INVENTORY:
			break;
		}
	}

	public void rightClick() {
		switch (guiState) {
		case GAME:
			pulse = 255;
			pulseChange = -51;
			break;
		case INVENTORY:
			break;
		}
	}
	
	// Shows the player's integrity and energy
	private void drawStats() {
		p.noStroke();
		p.fill(50, 255, 50, 127);
		p.rect(0, 0, (float) Globals.player.integrity / Globals.player.maxInteg * p.width, 13);
		p.fill(255, 255, 50, 127);
		p.rect(0, 14, (float ) Globals.player.energy / Globals.player.maxEnergy * p.width, 13);
	}

	public void drawGUI() {
		p.hint(PApplet.DISABLE_DEPTH_TEST);
		p.textAlign(PApplet.CENTER, PApplet.CENTER);
		// Draw GUI depending on gui state
		switch (guiState) {
		case GAME:
			p.stroke(255);
			p.noFill();
			p.strokeWeight(3);
			p.ellipse(p.width / 2, p.height / 2, 10, 10);
			if (pulse < 256 && pulse > -1) {
				p.stroke(255, 255, 255, 255 - pulse);
				p.ellipse(p.width / 2, p.height / 2, pulse / 10 + 10, pulse / 10 + 10);
			}
			p.stroke(255);
			p.rect(3, 287, 20, 100);
			p.noStroke();
			p.fill(150, 150, 200);
			float fuel = (float) Globals.player.fuel / Globals.player.maxFuel * 100;
			p.rect(3, 387 - fuel, 20, fuel);
			if (Globals.player.selItem != null) {
				Globals.player.selItem.draw(0, 0);
			}
			drawStats();
			break;
		case INVENTORY:
			p.noStroke();
			p.background(0);
			Item[] inv = Globals.player.inventory.getItems();
			if (inv.length > 0) {
				for (int i = 0; i < Inventory.REND_X; i++) {
					for (int j = 0; j < Inventory.REND_Y; j++) {
						int index = i + j * Inventory.REND_X;
						if (index < inv.length) {
							inv[index].draw(i, j);
						} else {
							break;
						}
					}
				}
			}
			double fullness = Globals.player.inventory.getFullness() * p.width;
			p.fill(70, 70, 255);
			p.rect(0, 0, (float) fullness, 27);
			break;
		case RESEARCH:
			p.background(0);
			Item[] res = Globals.player.research.getItems();
			if (res.length > 0) {
				for (int i = 0; i < Inventory.REND_X; i++) {
					for (int j = 0; j < Inventory.REND_Y; j++) {
						int index = i + j * Inventory.REND_X;
						if (index < res.length) {
							res[index].draw(i, j);
						} else {
							break;
						}
					}
				}
			}
			break;
		case RES_ITEM:
			p.background(0);
			workItem.draw(0, 0);
			p.fill(255);
			p.textSize(64);
			p.text(workItem.getName(), p.width / 2, 128);
			p.textSize(32);
			p.text(workItem.getLore(), 240, p.height / 2 - 256, 1440, 512);
			break;
		case TOOL:
			p.background(0);
			Tool tool = (Tool) workItem;
			tool.drawBig();
			if (Globals.player.selItem != null) {
				Globals.player.selItem.draw(0, 0);
			}
			break;
		}
		// Draw text log
		int y = 60;
		p.textSize(24);
		p.noStroke();
		p.textAlign(PApplet.LEFT, PApplet.CENTER);
		for (Iterator<Log> iter = logs.descendingIterator(); iter.hasNext();) {
			Log log = iter.next();
			p.fill(0, 0, 0, log.getAlpha() / 3);
			String msg = "";
			for (int i = 0; i < log.msg.length(); i++) {
				char ch;
				if (log.msg.charAt(i) == '_') {
					ch = (char) (Math.random() * ('z' - 'a') + 'a');
				} else {
					ch = log.msg.charAt(i);
				}
				msg += ch;
			}
			int height = (int) Math.max(64 * p.textWidth(msg) / 400, 48);
			p.rect(1500, y, 400, height);
			p.fill(255, 255, 255, log.getAlpha());
			p.text(msg, 1500, y, 400, height);
			y += height;
		}
		p.hint(PApplet.ENABLE_DEPTH_TEST);
	}

	// Toggles given gui state when given key is pressed
	public void stateKey(char key, int state) {
		if (Globals.player.input[key]) {
			canSwitchState = false;
			if (prevC[key] == false) {
				if (guiState != state) {
					guiState = state;
				} else {
					Globals.player.yawV -= Math.toRadians(p.mouseX - p.width / 2) * Main.MOUSE_SENSITIVITY;
					Globals.player.pitchV -= Math.toRadians(p.mouseY - p.height / 2) * Main.MOUSE_SENSITIVITY;
					guiState = GAME;
				}
			}
			prevC[key] = true;
		} else {
			prevC[key] = false;
		}
	}

	public void doGUI() {
		// Open/close the inventory
		canSwitchState = true;
		stateKey('e', INVENTORY);
		stateKey('r', RESEARCH);
		stateKey(PApplet.ESC, GAME);
		stateKey('f', OBSERVE);
		stateKey('t', TOOL);
		// Update GUI
		switch (guiState) {
		case GAME:
			if (pulse < 256 && pulse > -1) {
				pulse += pulseChange;
			}
			break;
		case TOOL:
			// Tool selection
			if (Globals.player.selItem instanceof Tool) {
				workItem = Globals.player.selItem;
				Globals.player.selItem = null;
			} else {
				if (workItem == null || !(workItem instanceof Tool)) {
					guiState = GAME;
					log("Select a tool to modify it!");
					break;
				}
			}
			// Tool editing
			Tool tool = (Tool) workItem;
			int mx = (p.mouseX - p.width / 2 + 512) / 64;
			int my = (p.mouseY - p.height / 2 + 512) / 64;
			if (p.mousePressed && !(tool.getPart(mx, my) instanceof parts.Rift)) {
				switch (p.mouseButton) {
				case PApplet.RIGHT:
					if (tool.getPart(mx, my) instanceof parts.Air && Globals.player.selItem != null) {
						if (Globals.player.inventory.useItem(Globals.player.selItem.getName(), 1)) {
							Part newPart = Globals.player.selItem.getPart(mx, my);
							tool.setPart(mx, my, newPart);
							if (Globals.player.selItem.amount < 1) {
								Globals.player.selItem = null;
							}
						}
					}
					break;
				case PApplet.LEFT:
					Part part = tool.getPart(mx, my);
					if (!(part instanceof parts.Air)) {
						Globals.player.inventory.addItem(part.getItem());
						tool.setPart(mx, my, new parts.Air(mx, my));
					}
					break;
				case PApplet.CENTER:
					if (tool.getPart(mx, my) instanceof parts.Air && Globals.player.selItem != null) {
						if (Globals.player.inventory.useItem(Globals.player.selItem.getName(), 1)) {
							parts.Spawner newPart = new parts.Spawner(mx, my, tool, Globals.player.selItem.getPart(-1, -1));
							tool.setPart(mx, my, newPart);
							if (Globals.player.selItem.amount < 1) {
								Globals.player.selItem = null;
							}
						}
					}
					break;
				}
			}
			break;
		}
		// Update text log decay
		for (Iterator<Log> iter = logs.descendingIterator(); iter.hasNext();) {
			Log log = iter.next();
			log.tick();
			if (log.getAlpha() == 0) {
				iter.remove();
			}
		}
	}

	public void showResearch(Item item) {
		workItem = item;
		guiState = RES_ITEM;
	}
}
