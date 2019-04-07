package Control;

import Blocks.Block;
import Events.EventManager;
import Items.Item;

public class Player {

	private double x;
	private double y;
	private double z;
	private double xv;
	private double yv;
	private double zv;
	private double yaw;
	private double pitch;
	double yawV;
	double pitchV;
	static int[][] dirs = { { 0, 0, -1 }, { 0, 0, 1 }, { 1, 0, 0 }, { -1, 0, 0 }, { 0, -1, 0 }, { 0, 1, 0 } };
	Inventory inventory;
	Item selItem;
	boolean[] input;
	static final int REACH = 7;
	static final double SCAN_STEP = 0.1;
	static final double JUMP = 0.3;
	public boolean canMine = true;
	int mineCool = 0;

	private double mod(double n1, double n2) {
		if (n1 < 0) {
			n1 += n2;
		}
		return n1 % n2;
	}

	public Player(boolean[] input) {
		yaw = 0;
		pitch = 0;
		yawV = 0;
		pitchV = 0;
		xv = 0;
		yv = 0;
		zv = 0;
		this.input = input;
		x = Globals.world.sizeX() / 2;
		y = Globals.world.sizeY() - 1;
		z = Globals.world.sizeZ() / 2;
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
		inventory = new Inventory(50);
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
						inventory.addItem(b.getItem());
						Globals.world.breakBlock(hit[0], hit[1], hit[2]);
						// Pushes an event to try to mine again when possible, allowing the player to
						// hold left click
						EventManager.addEvent(new Events.MineEvent(this, EventManager.m), mineCool + 2);
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
				// Get coords where player is looking
				int[] hit = scan(true);
				// Place block if it can, and use the item
				if (hit != null && selItem.getBlock() != null && inventory.useItem(selItem.getName(), 1)) {
					Globals.world.setBlock(hit[0], hit[1], hit[2], selItem.getBlock());
					if (selItem.amount == 0) {
						selItem = null;
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
		while (dist < REACH) {
			dist += SCAN_STEP;
			rx += rxv;
			ry += ryv;
			rz += rzv;
			if (Globals.world.getBlock((int) rx, (int) ry, (int) rz).isBreakable()) {
				hitBlock = true;
				dist = REACH;
			}
		}
		if (needAir) {
			rx -= rxv;
			ry -= ryv;
			rz -= rzv;
		}
		if (hitBlock) {
			int[] out = { (int) rx, (int) ry, (int) rz };
			return out;
		}
		return null;
	}

	public void move(Main m) {
		// Does mouse movement
		if (Globals.gui.guiState == GUI.GAME) {
			yawV += Math.toRadians(m.mouseX - m.width / 2) * Main.MOUSE_SENSITIVITY;
			pitchV += Math.toRadians(m.mouseY - m.height / 2) * Main.MOUSE_SENSITIVITY;
			m.r.mouseMove(m.width / 2, m.height / 2);
		}
		// Moves player
		x += xv;
		y += yv;
		z += zv;
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
		// Movement dampening
		xv *= 0.5;
		yv *= 0.98;
		zv *= 0.5;
		yaw += yawV;
		pitch += pitchV;
		yawV *= 0.3;
		pitchV *= 0.3;
		double buttons = 0;
		// Movement from player input
		if (Globals.gui.guiState == GUI.GAME) {
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
			double speed = 0.08f / Math.sqrt(buttons);
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
			if (m.input[' ']) {
				yvc = -JUMP * bJ;
			}
			xv += xvc;
			yv += yvc + World.GRAVITY;
			zv += zvc;
		}
		double oldX = x;
		double oldY = y;
		double oldZ = z;
		x = mod(x, Globals.world.sizeX());
		y = mod(y, Globals.world.sizeY());
		z = mod(z, Globals.world.sizeZ());
		if (oldX != x || oldY != y || oldZ != z) {
			Globals.main.shiftRenderList((int) (x - oldX), (int) (y - oldY), (int) (z - oldZ));
		}
		if (mineCool > 0) {
			mineCool--;
			canMine = false;
		} else {
			canMine = true;
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
					Block block = Globals.world.getBlock((int) bx, (int) by, (int) bz);
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
