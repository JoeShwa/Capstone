package Items;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class Item implements Items {
	
	static PApplet p;
	public int amount;
	
	public static void prepItems(PApplet p_) {
		p = p_;
	}
	
	public Item() {
		amount = 1;
	}
	
	public Item(int amt) {
		amount = amt;
	}
	
	public void draw(PImage tex, int x, int y) {
		x *= 256;
		y *= 256;
		p.beginShape();
		p.noTint();
		p.texture(tex);
		p.vertex(x, y, 0, 0);
		p.vertex(x, y + 256, 0, 1);
		p.vertex(x + 256, y + 256, 1, 1);
		p.vertex(x + 256, y, 1, 0);
		p.endShape();
		p.fill(255);
		p.textSize(32);
		p.text(amount, x + 32, y + 222);
	}
}
