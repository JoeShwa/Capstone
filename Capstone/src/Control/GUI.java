package Control;

import Items.Item;
import processing.core.PApplet;
import processing.core.PConstants;

public class GUI {

	Player player;
	static PApplet p;
	int pulse = 256;
	static final int GAME = 0;
	static final int INVENTORY = 1;
	public int guiState = GAME;

	public GUI(PApplet p) {
		this.p = p;
	}

	public void leftClick() {
		switch (guiState) {
		case GAME:
			pulse = 0;
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
			break;
		case INVENTORY:
			p.background(0);
			Item[] inv = player.inventory.getItems();
			if (inv.length > 0) {
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 5; j++) {
						int index = i + j * 10;
						if (index < inv.length) {
							inv[index].draw(i, j);
							System.out.println(i + " " + j);
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
			guiState = INVENTORY;
		} else {
			guiState = GAME;
		}
		p.hint(PConstants.DISABLE_DEPTH_TEST);
		switch (guiState) {
		case GAME:
			if (pulse < 255) {
				pulse += 8;
				p.stroke(255, 255, 255, 255 - pulse);
				p.ellipse(p.width / 2, p.height / 2, pulse / 10 + 10, pulse / 10 + 10);
			}
			break;
		case INVENTORY:

			break;
		}
	}

}
