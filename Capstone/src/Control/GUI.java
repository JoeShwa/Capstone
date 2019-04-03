package Control;

import Items.Item;
import processing.core.PApplet;
import processing.core.PConstants;

public class GUI {

	Player player;
	static PApplet p;
	int pulse = 256;
	int pulseChange = 0;
	static final int GAME = 0;
	static final int INVENTORY = 1;
	public int guiState = GAME;
	boolean prevC = false;

	public GUI(PApplet p) {
		this.p = p;
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
		switch (guiState) {
		case GAME:
			p.stroke(255);
			p.noFill();
			p.strokeWeight(3);
			p.ellipse(p.width / 2, p.height / 2, 10, 10);
			if (player.selItem != null) {
				player.selItem.draw(0, 0);
			}
			if (pulse < 256 && pulse > -1) {
				p.stroke(255, 255, 255, 255 - pulse);
				p.ellipse(p.width / 2, p.height / 2, pulse / 10 + 10, pulse / 10 + 10);
			}
			break;
		case INVENTORY:
			p.background(0);
			Item[] inv = player.inventory.getItems();
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
		}
		p.hint(PConstants.ENABLE_DEPTH_TEST);
	}

	public void doGUI() {
		if (player.input['c']) {
			if (prevC == false) {
				if (guiState == GAME) {
					guiState = INVENTORY;
				} else {
					player.yawV -= Math.toRadians(p.mouseX - p.width / 2) * Main.MOUSE_SENSITIVITY;
					player.pitchV -= Math.toRadians(p.mouseY - p.height / 2) * Main.MOUSE_SENSITIVITY;
					guiState = GAME;
				}
			}
			prevC = true;
		} else {
			prevC = false;
		}
		p.hint(PConstants.DISABLE_DEPTH_TEST);
		switch (guiState) {
		case GAME:
			if (pulse < 256 && pulse > -1) {
				pulse += pulseChange;
			}
			break;
		case INVENTORY:

			break;
		}
	}

}
