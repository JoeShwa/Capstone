package control;

import java.awt.Robot;
import java.util.Iterator;
import java.util.LinkedList;
import blocks.Air;
import blocks.Block;
import entities.Bug;
import entities.Entity;
import events.EventManager;
import events.LogEvent;
import items.Item;
import processing.core.PApplet;
import processing.opengl.PJOGL;

public class Main extends PApplet {

	// ----------------------------------------------------
	// ----------------------------------------------------

	static final int REND_DIST = 30;
	static final double MOUSE_SENSITIVITY = 0.3;
	Robot r;
	World world;
	Player player;
	boolean[] input;
	public WorldGen gen;
	static int[][] dirs = { { 0, 0, -1 }, { 0, 0, 1 }, { 1, 0, 0 }, { -1, 0, 0 }, { 0, -1, 0 }, { 0, 1, 0 } };
	boolean mouseVisible = true;
	int mouseCooldown = 0;
	LinkedList<BlockPos> renderList;
	int obsTime;

	static long bm = 0;
	static double avg = 0;

	// Tells the library to use this class as the main class
	public static void main(String args[]) {
		PApplet.main("control.Main");
	}

	// Sets the window size and type
	public void settings() {
		fullScreen(P3D);
		PJOGL.setIcon("textures/icon.png");
	}

	public static void benchmark(String id) {
		long dif = System.nanoTime() - bm;
		Globals.gui.log(id + ": " + (double) dif / 1000000);
		System.out.println(id + ": " + (double) dif / 1000000);
	}

	public static double benchmark() {
		long dif = System.nanoTime() - bm;
		return (double) dif / 1000000;
	}

	public static void bmStart() {
		bm = System.nanoTime();
	}

	public void setup() {
		surface.setTitle("Capstone");
		Globals.add(this);
		Globals.add((PApplet) this);
		try {
			r = new Robot();
		} catch (Exception e) {
			e.printStackTrace();
		}
		world = new World(100, 100 * World.DIM_COUNT, 100);
		Globals.add(world);
		gen = new WorldGen(world);
		input = new boolean[256];
		// Prepares the static block class for loading textures
		Block.prepBlocks(this, world);
		Item.prepItems(this);
		// Generates the world
		gen.start();
		// Create the event manager
		EventManager.init(this);
		// Pre-calculate common sqrts
		Globals.init();
		player = null;
	}

	// Initializes render list with initially visible blocks
	public void setupRenderList() {
		renderList = new LinkedList<>();
		for (int i = 0; i <= REND_DIST; i++) {
			addRenderBlocks(i);
		}
	}

	// Shifts coordinates of rendered blocks, used when the player loops over the
	// edge of the map
	public void shiftRenderList(int x, int y, int z) {
		for (BlockPos pos : renderList) {
			pos.x += x;
			pos.y += y;
			pos.z += z;
		}
	}

	public void draw() {
		if (gen.phase < World.DIM_COUNT + 1) {
			loadScreen();
		} else {
			if (player == null) {
				player = new Player(input);
				Globals.add(player);
				Globals.add(new GUI(this));
				setupRenderList();
				Globals.gui.log("System booting...");
				EventManager.addEvent(new LogEvent("Activating universal positioning system..."), 180);
				EventManager.addEvent(new LogEvent("Current location: _____"), 210);
				EventManager.addEvent(new LogEvent("Objective 1: Survive"), 240);
				EventManager.addEvent(new LogEvent("Objective 2: Research"), 240);
				Spawner.spawn();
			}
			runGame();
		}
	}

	public void loadScreen() {
		background(0);
		fill(255);
		textSize(128);
		textAlign(CENTER, CENTER);
		text("Loading... " + gen.phase + " / " + (World.DIM_COUNT + 1), width / 2, height / 2 - 100);
		fill(50, 200, 50);
		rect(0, height / 2, gen.phase * width / (World.DIM_COUNT + 1), 100);
	}

	public void updateEntities() {
		LinkedList<Entity> news = new LinkedList<Entity>();
		for (int x = 0; x < world.sizeX() / World.SUBDIV; x++) {
			for (int y = 0; y < world.sizeY() / World.SUBDIV; y++) {
				for (int z = 0; z < world.sizeZ() / World.SUBDIV; z++) {
					LinkedList<Entity> entities = world.getEntities(x, y, z);
					// Globals.gui.log(entities.size() + "");
					for (Iterator<Entity> iter = entities.iterator(); iter.hasNext();) {
						Entity entity = iter.next();
						entity.update();
						if (entity.isDead) {
							iter.remove();
						} else if (entity.newChunk) {
							iter.remove();
							news.add(entity);
							entity.newChunk = false;
						}
					}
				}
			}
		}
		for (Entity entity : news) {
			world.addEntity(entity);
		}
	}

	public void drawEntities() {
		noTint();
		LinkedList<Entity> entities = world.getEntities(Globals.floor(Globals.player.getX()),
				Globals.floor(Globals.player.getY()), Globals.floor(Globals.player.getZ()), REND_DIST / World.SUBDIV);
		for (Iterator<Entity> iter = entities.iterator(); iter.hasNext();) {
			Entity entity = iter.next();
			entity.draw();
		}
	}

	public void drawBlocks() {
		bmStart();
		for (Iterator<BlockPos> iterator = renderList.iterator(); iterator.hasNext();) {
			BlockPos pos = iterator.next();
			// Remove blocks from the render list if they fall out of range
			int dist = Math.abs(pos.x - Globals.floor(player.getX())) + Math.abs(pos.y - Globals.floor(player.getY()))
					+ Math.abs(pos.z - Globals.floor(player.getZ()));
			Block b = world.getBlock(pos.x, pos.y, pos.z);
			if (dist <= REND_DIST && b.isVisible && !(b instanceof Air)) {
				b.draw(pos.x, pos.y, pos.z);
			} else {
				// Remove block from draw list if it shouldn't be drawn
				iterator.remove();
				b.isDrawn = false;
			}
		}
		avg += benchmark();
		if (frameCount % 300 == 0) {
			// Globals.gui.log("Average render time: " + avg / 300);
			avg = 0;
		}
	}

	public void runGame() {
		if (Globals.gui.guiState != GUI.OBSERVE) {
			// Reset observation mode
			obsTime = -1;
			EventManager.runEvents();
			if (mousePressed && mouseCooldown < 1) {
				mousePressed();
			}
			mouseCooldown--;
			noStroke();
			player.move();
			updateEntities();
			if (Globals.gui.guiState == GUI.GAME) {
				if (mouseVisible) {
					noCursor();
					mouseVisible = false;
				}
				pushMatrix();
				doCamera(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
				// Fixes clipping
				perspective(PI / 3, (float) width / height, 0.01f, 10000);
				// Adds the blocks at the edges of the render distance to be rendered
				// Requires loop to account for (literal) corner cases with movement
				for (int i = 0; i < 3; i++) {
					addRenderBlocks(REND_DIST - i); // POTENTIAL LAG CAUSE
				}
				background(0);
				drawBlocks();
				drawEntities();
				popMatrix();
			} else {
				if (!mouseVisible) {
					cursor();
					mouseVisible = true;
				}
				// Adds the blocks to be rendered even when the gui state isn't at game
				// This prevents motion while in a menu from causing rendering bugs
				for (int i = 0; i < 3; i++) {
					addRenderBlocks(REND_DIST - i);
				}
			}
		}
		Globals.gui.doGUI();
		if (Globals.gui.guiState != GUI.OBSERVE) {
			Globals.gui.drawGUI();
		} else {
			showObservation();
		}
	}

	public void showObservation() {
		if (obsTime == -1) {
			background(0);
			obsTime = 0;
			pushMatrix();
			doCamera(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
			// Fixes clipping
			perspective(PI / 3, (float) width / height, 0.01f, 1000000000);
			int dist = 150;
			for (int x = -dist; x <= dist; x++) {
				for (int y = -dist; y <= dist; y++) {
					for (int z = -dist; z <= dist; z++) {
						Block b = world.getBlock(x + player.getX(), y + player.getY(), z + player.getZ());
						if (b.isVisible) {
							b.draw(x + Globals.floor(player.getX()), y + Globals.floor(player.getY()),
									z + Globals.floor(player.getZ()));
						}
					}
				}
			}
			popMatrix();
		}
	}

	// Adds blocks to the render list if they just came in range
	public void addRenderBlocks(int dist) {
		for (int x = -dist; x <= dist; x++) {
			for (int y = -dist; y <= dist; y++) {
				int z = dist - (Math.abs(x) + Math.abs(y));
				for (int i = 0; i < 2; i++) {
					int rx = x + Globals.floor(player.getX());
					int ry = y + Globals.floor(player.getY());
					int rz = z + Globals.floor(player.getZ());
					Block b = world.getBlock(rx, ry, rz);
					if (b.isVisible && !(b instanceof Air)) {
						addRenderBlock(new BlockPos(rx, ry, rz));
					}
					z *= -1;
				}
			}
		}
	}

	public void addRenderBlock(BlockPos pos) {
		if (renderList != null && !world.getBlock(pos.x, pos.y, pos.z).isDrawn) {
			renderList.add(pos);
			world.getBlock(pos.x, pos.y, pos.z).isDrawn = true;
		}
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
		if (player != null) {
			mouseCooldown = 12;
			switch (mouseButton) {
			case LEFT:
				player.leftClick();
				break;
			case RIGHT:
				player.rightClick();
			}
		}
	}

	public void keyPressed() {
		if (key < 256) {
			input[key] = true;
		}
		// Stops the window from closing on ESC pressed (ESC == 27)
		if (key == ESC) {
			key = 0;
		}
		// Quit if P is pressed
		if (key == 'p') {
			key = ESC;
		}
	}

	public void keyReleased() {
		if (key < 256) {
			input[key] = false;
		}
	}
}
