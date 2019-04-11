package control;

import blocks.Block;
import entities.Particle;
import events.EventManager;
import items.Item;
import items.Tool;

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
	// Player's ability to move in the air
	static final double AIR_CONTROL = 0;
	// Disables rotation smoothing
	static final boolean INSTANT_ROTATION = false;
	// How much thrust the player's jetpack provides (gravity disabled)
	static final double JET_STREN = 0.0027;
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

	public Player(boolean[] input) {
		yaw = 0;
		pitch = 0;
		yawV = 0;
		pitchV = 0;
		xv = 0;
		yv = 0;
		zv = 0;
		mineCool = 0;
		maxFuel = 20;
		fuel = maxFuel;
		this.input = input;
		while (check()) {
			x = Globals.world.sizeX() * Math.random();
			y = Globals.world.sizeY() - 1;
			y = 100;
			z = Globals.world.sizeZ() * Math.random();
			int count = 0;
			while (count < 75 && check()) {
				y--;
				count++;
			}
		}
		inventory = new Inventory(100000);
		research = new Inventory(Integer.MAX_VALUE);
		inventory.addItem(new Tool());
		Item item = new items.Sludge();
		item.amount = 400;
		inventory.addItem(item);
		item = new items.Thermite();
		item.amount = 400;
		inventory.addItem(item);
		item = new items.Rock();
		item.amount = 400;
		inventory.addItem(item);
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
					tool.activate();
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
						Globals.gui.log("You can't place a block where you exist!");
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

	public void move(Main m) {
		// Update the tool if the player is selecting a tool
		if (selItem instanceof Tool) {
			Tool tool = (Tool) selItem;
			tool.update();
		}
		// Does mouse movement
		if (Globals.gui.guiState == GUI.GAME && Globals.p.focused) {
			yawV += Math.toRadians(m.mouseX - m.width / 2) * Main.MOUSE_SENSITIVITY;
			pitchV += Math.toRadians(m.mouseY - m.height / 2) * Main.MOUSE_SENSITIVITY;
			m.r.mouseMove(m.width / 2, m.height / 2);
			if (INSTANT_ROTATION) {
				yaw += yawV * 1.5;
				pitch += pitchV * 1.5;
				yawV = 0;
				pitchV = 0;
			}
		}
		// Moves player
		x += xv;
		y += yv;
		z += zv;
		wrapPos();
		// Bounces player back on collision
		int bestC = 4;
		int bI = 0;
		int bJ = 0;
		int bK = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					x -= xv * i;
					y -= yv * j;
					z -= zv * k;
					if (!check() && i + j + k < bestC) {
						bestC = i + j + k;
						bI = i;
						bJ = j;
						bK = k;

					}
					x += xv * i;
					y += yv * j;
					z += zv * k;
				}
			}
		}
		if (bestC == 4) {
			bI = 1;
			bJ = 1;
			bK = 1;
		}
		x -= xv * bI;
		y -= yv * bJ;
		z -= zv * bK;
		xv *= 1 - bI;
		yv *= 1 - bJ;
		zv *= 1 - bK;
		// Check if the player is on the ground
		boolean onGround = false;
		if (bJ == 1) {
			y++;
			if (check()) {
				onGround = true;
			}
			y--;
		}
		// Stop player from exceeding 1 block per frame
		double speed = Math.sqrt(xv * xv + yv * yv + zv * zv);
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
		// Movement from player input
		boolean usingJetpack = false;
		if (Globals.gui.guiState == GUI.GAME) {
			double buttons = 0;
			if (m.input['w']) {
				buttons++;
			}
			if (m.input['a']) {
				buttons++;
			}
			if (m.input['s']) {
				buttons++;
			}
			if (m.input['d']) {
				buttons++;
			}
			if (m.input[' '] && fuel > 0) {
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
			if (pitch > Math.toRadians(89)) {
				pitch = Math.toRadians(89);
			}
			if (pitch < Math.toRadians(-89)) {
				pitch = Math.toRadians(-89);
			}
			double xvc = 0;
			double yvc = 0;
			double zvc = 0;
			if (m.input['w']) {
				xvc += speed * Math.cos(yaw);
				zvc += speed * Math.sin(yaw);
			}
			if (m.input['s']) {
				xvc -= speed * Math.cos(yaw);
				zvc -= speed * Math.sin(yaw);
			}
			if (m.input['a']) {
				xvc -= speed * Math.cos(yaw + Math.toRadians(90));
				zvc -= speed * Math.sin(yaw + Math.toRadians(90));
			}
			if (m.input['d']) {
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
		// Change the mining cooldown
		if (mineCool > 0) {
			mineCool--;
			canMine = false;
		} else {
			canMine = true;
		}
		// Give fuel back while on the ground
		if (onGround && fuel < maxFuel) {
			fuel += maxFuel / 15;
			if (fuel > maxFuel) {
				fuel = maxFuel;
			}
		}
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
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					double bx = x - 0.4 + (double) i * 0.8;
					double by = y - 0.4 + (double) j * 0.8;
					double bz = z - 0.4 + (double) k * 0.8;
					Block block = Globals.world.getBlock(Globals.floor(bx), Globals.floor(by), Globals.floor(bz));
					if (block.isSolid()) {
						collide = true;
					}
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
