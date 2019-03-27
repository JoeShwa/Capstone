package Control;
import java.awt.Robot;

import processing.core.PApplet;

public class Main extends PApplet {

	// Tells the library to use this class as the main class
	public static void main(String[] args) {
		PApplet.main("Control.Main");
	}

	// Sets the window size and type
	public void settings() {
		fullScreen(P3D);
	}

	static final int REND_DIST = 20;

	Robot r;
	World world;
	Player player;
	GUI gui;
	boolean[] input;
	WorldGen gen;
	static int[][] dirs = { { 0, 0, -1 }, { 0, 0, 1 }, { 1, 0, 0 }, { -1, 0, 0 }, { 0, -1, 0 }, { 0, 1, 0 } };

	public void setup() {
		noCursor();
		try {
			r = new Robot();
		} catch (Exception e) {
			e.printStackTrace();
		}
		world = new World(100, 100, 100);
		gen = new WorldGen(world);
		input = new boolean[256];
		// Prepares the static block class for loading textures
		Block.prepBlocks(this, world);
		gen.start();
	}

	public void draw() {
		if(gen.phase < 4) {
			loadScreen();
		} else {
			if(player == null) {
				player = new Player(this, world, new GUI(player, this), input);
				gui = player.gui;
			}
			runGame();
		}
	}
	
	public void loadScreen() {
		background(0);
		fill(255);
		textSize(128);
		textAlign(CENTER, CENTER);
		text("Loading... " + gen.phase + " / 4" , width / 2, height / 2 - 100);
		fill(50, 200, 50);
		rect(0, height / 2, gen.phase * width / 4, 100);
		
	}
	
	public void runGame() {
		noStroke();
		// System.out.println(frameRate);
		player.move(this);
		pushMatrix();
		doCamera(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
		// Fixes clipping
		perspective(PI / 3, (float) width / height, 0.01f, 10000);
		background(0);
		for (int x = (int) player.getX() - REND_DIST; x < player.getX() + REND_DIST; x++) {
			for (int y = (int) player.getY() - REND_DIST; y < player.getY() + REND_DIST; y++) {
				for (int z = (int) player.getZ() - REND_DIST; z < player.getZ() + REND_DIST; z++) {
					world.getBlock(x, y, z).draw(x, y, z);
				}
			}
		}
		popMatrix();
		gui.drawGUI();
	}

	// Positions camera based on position and direction
	public void doCamera(double x, double y, double z, double yaw, double pitch) {
		x *= 100;
		y *= 100;
		z *= 100;
		camera((float) x, (float) y, (float) z, (float) x + cos((float) yaw) * cos((float) pitch),
				(float) y + sin((float) pitch), (float) z + sin((float) yaw) * cos((float) pitch), 0, 1, 0);
	}

	public void mousePressed() {
		switch (mouseButton) {
		case LEFT:
			player.leftClick();
			break;
		}

	}

	public void keyPressed() {
		if (key < 256) {
			input[key] = true;
		}
	}

	public void keyReleased() {
		if (key < 256) {
			input[key] = false;
		}
	}
}
