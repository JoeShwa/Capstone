package Control;

import java.util.Iterator;
import java.util.LinkedList;

import Items.Item;
import processing.core.PApplet;
import processing.core.PConstants;

public class GUI {

	static PApplet p;
	// Globals for pulse effect when clicking
	int pulse = 256;
	int pulseChange = 0;
	// GUI's possible states
	public static final int GAME = 0;
	public static final int INVENTORY = 1;
	public static final int RESEARCH = 2;
	public int guiState = GAME;
	// Prevents unintentional spam-switching of GUI state
	boolean[] prevC;
	boolean canSwitchState = true;
	// Holds data logged on screen
	LinkedList<Log> logs;

	public GUI(PApplet p) {
		GUI.p = p;
		logs = new LinkedList<>();
		prevC = new boolean[Globals.player.input.length];
	}

	public void log(String log) {
		logs.add(new Log(log));
		if (logs.size() > 8) {
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

	public void drawGUI() {
		p.hint(PConstants.DISABLE_DEPTH_TEST);
		// Draw GUI depending on gui state
		switch (guiState) {
		case GAME:
			p.stroke(255);
			p.noFill();
			p.strokeWeight(3);
			p.ellipse(p.width / 2, p.height / 2, 10, 10);
			if (Globals.player.selItem != null) {
				Globals.player.selItem.draw(0, 0);
			}
			if (pulse < 256 && pulse > -1) {
				p.noFill();
				p.strokeWeight(3);
				p.stroke(255, 255, 255, 255 - pulse);
				p.ellipse(p.width / 2, p.height / 2, pulse / 10 + 10, pulse / 10 + 10);
			}
			break;
		case INVENTORY:
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
			break;
		case RESEARCH:
			p.background(0);
			Item[] res = Globals.player.inventory.getItems();
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
		}
		// Draw text log
		int y = 60;
		p.textSize(24);
		for (Iterator<Log> iter = logs.descendingIterator(); iter.hasNext();) {
			Log log = iter.next();
			p.fill(255, 255, 255, log.getAlpha());
			int height = (int) Math.max(64 * p.textWidth(log.msg) / 400, 48);
			p.text(log.msg, 1500, y, 400, height);
			y += height;
		}
		p.hint(PConstants.ENABLE_DEPTH_TEST);
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
		// Update GUI
		switch (guiState) {
		case GAME:
			if (pulse < 256 && pulse > -1) {
				pulse += pulseChange;
			}
			break;
		case INVENTORY:
			break;
		case RESEARCH:

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

}
