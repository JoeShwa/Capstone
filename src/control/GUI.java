package control;

import java.util.Iterator;
import java.util.LinkedList;
import items.Item;
import processing.core.PApplet;
import recipes.Recipe;

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
	public static final int DEATH = 6;
	public static final int CRAFT = 7;
	// GUI's current state
	public int guiState = GAME;
	// Prevents unintentional spam-switching of GUI state
	boolean[] prevC;
	boolean canSwitchState = true;
	// Holds data logged on screen
	LinkedList<Log> logs;
	// Item currently being worked with in the GUI
	Item workItem;
	// Countdown to world regeneration after death
	int deathTimer = 0;

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
		p.rect(0, 14, (float) Globals.player.energy / Globals.player.maxEnergy * p.width, 13);
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
		case DEATH:
			p.background(0);
			p.textSize(128);
			p.fill(255, 180, 180);
			p.text("You died!\nStarting over in " + deathTimer / 60, p.width / 2, p.height / 2);
			break;
		case CRAFT:
			p.background(0);
			Recipe[] recipes = Globals.player.craftables.getRecipes();
			if (recipes.length > 0) {
				for (int i = 0; i < Craftables.REND_X; i++) {
					for (int j = 0; j < Craftables.REND_Y; j++) {
						int index = i + j * Craftables.REND_X;
						if (index < recipes.length) {
							Item item = recipes[index].getResult();
							item.draw(i, j);
						} else {
							break;
						}
					}
				}
				int x = (Globals.p.mouseX) / 256;
				int y = (Globals.p.mouseY - 28) / 256;
				int ind = x + y * Craftables.REND_X;
				Recipe selRec = null;
				if (ind > -1 && ind < recipes.length) {
					selRec = recipes[ind];
				}
				if (selRec != null) {
					Item[] reqs = selRec.getReqs();
					for (int i = 0; i < reqs.length; i++) {
						reqs[i].draw(6, i);
						Item inInv = Globals.player.inventory.getItem(reqs[i].getName());
						if(inInv == null || inInv.amount < reqs[i].amount) {
							Globals.p.stroke(255, 0, 0);
							Globals.p.strokeWeight(10);
							Globals.p.line(256 * 6 + 10, i * 256 + 38, 256 * 7 - 10, i * 256 + 256 + 18);
						}
					}
				}
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

	@SuppressWarnings("static-access")
	public void doGUI() {
		// Open/close the inventory
		canSwitchState = true;
		stateKey('e', INVENTORY);
		stateKey('r', RESEARCH);
		stateKey(PApplet.ESC, GAME);
		stateKey('f', OBSERVE);
		stateKey('t', TOOL);
		stateKey('c', CRAFT);
		// Update GUI
		switch (guiState) {
		case GAME:
			if (pulse < 256 && pulse > -1) {
				pulse += pulseChange;
			}
			break;
		case DEATH:
			deathTimer--;
			if (deathTimer == 0) {
				Globals.main.setup();
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
