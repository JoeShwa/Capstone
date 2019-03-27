package Control;
import processing.core.PApplet;
import processing.core.PConstants;

public class GUI {

	Player player;
	static PApplet p;
	int pulse = 256;

	public GUI(Player player, PApplet p) {
		this.p = p;
	}

	public void leftClick() {
		pulse = 0;
	}

	public void drawGUI() {
		p.hint(PConstants.DISABLE_DEPTH_TEST);
		p.stroke(255);
		p.noFill();
		p.strokeWeight(3);
		p.ellipse(p.width / 2, p.height / 2, 10, 10);
		if (pulse < 255) {
			pulse += 8;
			p.stroke(255, 255, 255, 255 - pulse);
			p.ellipse(p.width / 2, p.height / 2, pulse / 10 + 10, pulse / 10 + 10);
		}
		p.hint(PConstants.ENABLE_DEPTH_TEST);
	}

}
