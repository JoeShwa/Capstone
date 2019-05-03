package control;

import java.util.Iterator;
import java.util.LinkedList;

import blocks.Block;
import entities.Enemy;
import entities.Entity;
import entities.Particle;
import events.EventManager;
import items.Item;
import items.Tool;
import recipes.Recipe;

public class Player {

	// Player position, velocities, and rotation
	private double x;
	private double y;
	private double z;
	public double xv;
	public double yv;
	public double zv;
	private double yaw;
	private double pitch;
	double yawV;
	double pitchV;
	// Used to loop through all 6 directions
	static int[][] dirs = { { 0, 0, -1 }, { 0, 0, 1 }, { 1, 0, 0 }, { -1, 0, 0 }, { 0, -1, 0 }, { 0, 1, 0 } };
	// Player's internal inventory
	public Inventory inventory;
	// Items that have been discovered
	Inventory research;
	// List of available crafting recipes to player
	Craftables craftables;
	// Selected item
	public Item selItem;
	// Keyboard input
	boolean[] input;
	// Player reach length
	int reach = 7;
	// Scan step for detecting blocks in front of player
	static final double SCAN_STEP = 0.1;
	// Player run speed
	static final double SPEED = 0.02;
	// Player's ab[0]lity to move in the air
	static final double AIR_CONTROL = 0;
	// Disables rotation smoothing
	static final boolean INSTANT_ROTATION = false;
	// How much thrust the player's jetpack provides (gravity disabled)
	static final double JET_STREN = 0.003;
	// How strong the player's jump is
	static final double JUMP = 0.15;
	// How long the player's jetpack lasts
	int maxFuel;
	// Amount of jetpack fuel left
	double fuel;
	// Whether the player can mine or not
	public boolean canMine = true;
	// How long until the player can mine
	int mineCool;
	// Player's structural integrity
	int integrity;
	// Player's maximum structural integrity
	int maxInteg;
	// Player's energy level
	public int energy;
	// Player's maximum energy level
	public int maxEnergy;

	public Player(boolean[] input) {
		yaw = 0;
		pitch = 0;
		yawV = 0;
		pitchV = 0;
		xv = 0;
		yv = 0;
		zv = 0;
		mineCool = 0;
		maxFuel = 16;
		maxInteg = 1024;
		integrity = maxInteg;
		maxEnergy = 16384;
		energy = maxEnergy;
		fuel = maxFuel;
		this.input = input;
		while (check()) {
			x = Globals.world.sizeX() / 2 + 20 * Math.random() - 10;
			y = Globals.world.sizeY() - 1;
			x = 0;
			y = 100;
			z = Globals.world.sizeZ() / 2 + 20 * Math.random() - 10;
			int count = 0;
			while (count < 75 && check()) {
				y--;
				count++;
			}
		}
		inventory = new Inventory(150);
//		inventory.addItem(new items.Rock(40));
//		inventory.addItem(new items.Sludge(40));
//		inventory.addItem(new items.Thermite(40));
//		inventory.addItem(new items.Accelerator(1));
		research = new Inventory(Integer.MAX_VALUE);
		craftables = new Craftables();
	}
	
	

	// Activates code ran by enemies that are near by
	public void triggerEnemies() {
		LinkedList<Entity> ents = Globals.world.getNearEntities(Globals.floor(x), Globals.floor(y), Globals.floor(z),
				3);
		for (Entity e : ents) {
			if (e instanceof Enemy) {
				double[] rels = Globals.relPos(x, y, z, e.x, e.y, e.z);
				((Enemy) e).triggerNear(rels[0], rels[1], rels[2]);
			}
		}
	}

	// Does player's generic movement and updating
	public void move() {
		doMouse();
		pushPlayer();
		int[] b = fixPos();
		// Deal impact damage to the player (falling, etc)
		double impactSpeed = Math.sqrt(xv * xv * b[0] + yv * yv * b[1] + zv * zv * b[2]);
		impact(impactSpeed);
		// Check if the player is on the ground
		boolean onGround = false;
		if (b[1] == 1) {
			y++;
			if (check()) {
				onGround = true;
			}
			y--;
		}
		double speed = Math.sqrt(xv * xv + yv * yv + zv * zv);
		updateVelPos(b, onGround, speed);
		control(b, onGround, speed);
		updateValues(onGround);
		triggerEnemies();
	}

	public void research(Item item) {
		item.amount = 0;
		if (!research.hasItem(item.getName())) {
			Globals.gui.log("New item discovered: " + item.getName());
		}
		research.addItem(item);
	}

	public void leftClick() {
		boolean didClick = true;
		switch (Globals.gui.guiState) {
		case GUI.GAME:
			// Scans to see which block the player is looking at
			if (canMine) {
				int[] hit = scan(false);
				// Breaks the selected block
				if (hit != null) {
					Block b = Globals.world.getBlock(hit[0], hit[1], hit[2]);
					mineCool = b.getHardness();
					if (mineCool > 0) {
						if (inventory.addItem(b.getItem())) {
							research(b.getItem());
							for (Iterator<Recipe> iter = Craftables.recipes.iterator(); iter.hasNext();) {
								Recipe recipe = iter.next();
								if (recipe.canResearch(inventory)) {
									iter.remove();
									research(recipe.getResult());
									craftables.addRecipe(recipe);
								}
							}
							Globals.world.breakBlock(hit[0], hit[1], hit[2]);
							// Pushes an event to try to mine again when possible, allowing the player to
							// hold left click
							EventManager.addEvent(new events.MineEvent(this, EventManager.m), mineCool + 2);
						} else {
							Globals.gui.log("Not enough space in inventory!");
						}
					}
				}
			} else {
				didClick = false;
			}
			break;
		case GUI.INVENTORY:
			int x = (Globals.p.mouseX) / 256;
			int y = (Globals.p.mouseY - 28) / 256;
			int ind = x + y * Inventory.REND_X;
			Item[] items = inventory.getItems();
			if (ind > -1 && ind < items.length) {
				selItem = items[ind];
			} else {
				selItem = null;
			}
			break;
		case GUI.RESEARCH:
			x = (Globals.p.mouseX) / 256;
			y = (Globals.p.mouseY - 28) / 256;
			ind = x + y * Inventory.REND_X;
			items = research.getItems();
			if (ind > -1 && ind < items.length) {
				Globals.gui.showResearch(items[ind]);
			}
			break;
		case GUI.CRAFT:
			Recipe[] recipes = craftables.getRecipes();
			int mx = (Globals.p.mouseX) / 256;
			int my = (Globals.p.mouseY - 28) / 256;
			ind = mx + my * Craftables.REND_X;
			Recipe selRec = null;
			if (ind > -1 && ind < recipes.length) {
				selRec = recipes[ind];
			}
			if (selRec != null) {
				if (selRec.canCraft(inventory)) {
					inventory.addItem(selRec.craft(inventory));
				}
			}
			break;
		}
		// Do gui's left click
		if (didClick) {
			Globals.gui.leftClick();
		}
	}

	public void rightClick() {
		switch (Globals.gui.guiState) {
		case GUI.GAME:
			if (selItem != null) {
				if (selItem instanceof Tool) {
					Tool tool = (Tool) selItem;
					tool.rightClick();
				} else {
					// Get coords where player is looking
					int[] hit = scan(true);
					// Place block if it can, and use the item
					if (hit != null && selItem.getBlock() != null && inventory.useItem(selItem.getName(), 1)) {
						Globals.world.setBlock(hit[0], hit[1], hit[2], selItem.getBlock());
						if (selItem.amount == 0) {
							selItem = null;
						}
					}
					if (check()) {
						inventory.addItem(Globals.world.getBlock(hit[0], hit[1], hit[2]).getItem());
						Globals.world.breakBlock(hit[0], hit[1], hit[2]);
					}
				}
			}
			break;
		}
		Globals.gui.rightClick();
	}

	public int[] scan(boolean needAir) {
		double rx = x;
		double ry = y;
		double rz = z;
		double rxv = Math.cos(yaw) * Math.cos(pitch) * SCAN_STEP;
		double ryv = Math.sin(pitch) * SCAN_STEP;
		double rzv = Math.sin(yaw) * Math.cos(pitch) * SCAN_STEP;
		double dist = 0;
		boolean hitBlock = false;
		while (dist < reach) {
			dist += SCAN_STEP;
			rx += rxv;
			ry += ryv;
			rz += rzv;
			if (Globals.world.getBlock(rx, ry, rz).isBreakable()) {
				hitBlock = true;
				dist = reach;
			}
		}
		if (needAir) {
			rx -= rxv;
			ry -= ryv;
			rz -= rzv;
		}
		if (hitBlock) {
			int[] out = { Globals.floor(rx), Globals.floor(ry), Globals.floor(rz) };
			return out;
		}
		return null;
	}

	// Does mouse movement
	public void doMouse() {
		if (Globals.gui.guiState == GUI.GAME && Globals.p.focused) {
			yawV += Math.toRadians(Globals.main.mouseX - Globals.main.width / 2) * Main.MOUSE_SENSITIVITY;
			pitchV += Math.toRadians(Globals.main.mouseY - Globals.main.height / 2) * Main.MOUSE_SENSITIVITY;
			Globals.main.r.mouseMove(Globals.main.width / 2, Globals.main.height / 2);
			if (INSTANT_ROTATION) {
				yaw += yawV * 1.5;
				pitch += pitchV * 1.5;
				yawV = 0;
				pitchV = 0;
			}
		}
	}

	// Moves player
	public void pushPlayer() {
		x += xv;
		y += yv;
		z += zv;
		wrapPos();
	}

	// Makes player not be stuck in wall
	public int[] fixPos() {
		int bestC = 4;
		int[] b = new int[3];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					x -= xv * i;
					y -= yv * j;
					z -= zv * k;
					if (!check() && i + j + k < bestC) {
						bestC = i + j + k;
						b[0] = i;
						b[1] = j;
						b[2] = k;

					}
					x += xv * i;
					y += yv * j;
					z += zv * k;
				}
			}
		}
		if (bestC == 4) {
			b[0] = 1;
			b[1] = 1;
			b[2] = 1;
		}
		x -= xv * b[0];
		y -= yv * b[1];
		z -= zv * b[2];
		return b;
	}

	public void updateVelPos(int[] b, boolean onGround, double speed) {
		xv *= 1 - b[0];
		yv *= 1 - b[1];
		zv *= 1 - b[2];
		// Stop player from exceeding 1 block per frame
		if (speed > 1) {
			xv /= speed;
			yv /= speed;
			zv /= speed;
		}
		// Apply friction when on ground
		if (onGround) {
			xv *= 0.9;
			zv *= 0.9;
		}
		// Accelerate yaw/pitch by velocities
		yaw += yawV;
		pitch += pitchV;
		// Dampen rotation
		yawV *= 0.3;
		pitchV *= 0.3;
	}

	// Movement from player input
	public void control(int[] b, boolean onGround, double speed) {
		boolean usingJetpack = false;
		if (Globals.gui.guiState == GUI.GAME) {
			double buttons = 0;
			if (Globals.main.input['w']) {
				buttons++;
			}
			if (Globals.main.input['a']) {
				buttons++;
			}
			if (Globals.main.input['s']) {
				buttons++;
			}
			if (Globals.main.input['d']) {
				buttons++;
			}
			if (Globals.main.input[' '] && fuel > 0) {
				fuel--;
				usingJetpack = true;
			}
			if (onGround) {
				speed = SPEED / Math.sqrt(buttons);
			} else {
				if (usingJetpack) {
					speed = JET_STREN / Math.sqrt(buttons);
				} else {
					speed = AIR_CONTROL / Math.sqrt(buttons);
				}
			}
			// Use energy to move around
			if (buttons > 0 && energy > 0) {
				energy--;
			} else {
				speed = 0;
			}
			if (pitch > Math.toRadians(89)) {
				pitch = Math.toRadians(89);
			}
			if (pitch < Math.toRadians(-89)) {
				pitch = Math.toRadians(-89);
			}
			double xvc = 0;
			double yvc = 0;
			double zvc = 0;
			if (Globals.main.input['w']) {
				xvc += speed * Math.cos(yaw);
				zvc += speed * Math.sin(yaw);
			}
			if (Globals.main.input['s']) {
				xvc -= speed * Math.cos(yaw);
				zvc -= speed * Math.sin(yaw);
			}
			if (Globals.main.input['a']) {
				xvc -= speed * Math.cos(yaw + Math.toRadians(90));
				zvc -= speed * Math.sin(yaw + Math.toRadians(90));
			}
			if (Globals.main.input['d']) {
				xvc += speed * Math.cos(yaw + Math.toRadians(90));
				zvc += speed * Math.sin(yaw + Math.toRadians(90));
			}
			if (usingJetpack) {
				yvc = -JET_STREN;
			}
			if (onGround && input[' ']) {
				yvc -= JUMP;
			}
			xv += xvc;
			yv += yvc;
			zv += zvc;
		}
		// Apply gravity
		if (!usingJetpack) {
			yv += World.GRAVITY;
		}
	}

	// Uses/restores player's internal resources
	public void updateValues(boolean onGround) {
		// Change the mining cooldown
		if (mineCool > 0) {
			mineCool--;
			canMine = false;
		} else {
			canMine = true;
		}
		// Give fuel back while on the ground
		if (onGround && fuel < maxFuel && energy > 0) {
			fuel += 4;
			if (fuel > maxFuel) {
				energy += fuel - maxFuel;
				fuel = maxFuel;
			}

			energy -= 4;
		}
		// Repair integrity at the cost of energy
		if (energy > maxEnergy / 4 && integrity < maxInteg) {
			energy -= 32;
			integrity++;
		}
		// Kill the player when integrity runs out
		if (integrity < 1 && Globals.gui.guiState != GUI.DEATH) {
			Globals.gui.guiState = GUI.DEATH;
			Globals.gui.deathTimer = 600;
		}
	}

	// Do impact damage
	private void impact(double speed) {
		speed -= 0.5;
		if (speed > 0) {
			hurt((int) (speed * 2048));
		}
	}

	public void hurt(int dmg) {
		integrity -= dmg;
	}

	// Wraps the player's position around the world
	public void wrapPos() {
		double oldX = x;
		double oldY = y;
		double oldZ = z;
		x = Globals.mod(x, Globals.world.sizeX());
		y = Globals.mod(y, Globals.world.sizeY());
		z = Globals.mod(z, Globals.world.sizeZ());
		if (oldX != x || oldY != y || oldZ != z) {
			Globals.main.shiftRenderList((int) (x - oldX), (int) (y - oldY), (int) (z - oldZ));
		}
	}

	// Checks for generic block collisions
	private boolean check() {
		boolean collide = false;
		for (double ys = 0; ys < Math.PI * 2; ys += Math.PI / 32) {
			for (double ps = -0.2; ps <= 0.2; ps += 0.4) {
				double bx = x + Math.cos(ys) * 0.4;
				double by = y + ps;
				double bz = z + Math.sin(ys) * 0.4;
				Block block = Globals.world.getBlock(Globals.floor(bx), Globals.floor(by), Globals.floor(bz));
				if (block.isSolid()) {
					collide = true;
				}
			}
		}
		return collide;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public double getYaw() {
		return yaw;
	}

	public double getPitch() {
		return pitch;
	}
}
