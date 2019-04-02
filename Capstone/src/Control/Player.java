package Control;

import Blocks.Block;
import Items.Item;
import processing.core.PApplet;

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
	PApplet p;
	World world;
	GUI gui;
	Inventory inventory;
	Item selItem;
	boolean[] input;
	static final int REACH = 7;
	static final double SCAN_STEP = 0.1;

	private double mod(double n1, double n2) {
		if (n1 < 0) {
			n1 += n2;
		}
		return n1 % n2;
	}

	public Player(PApplet p, World world, GUI gui, boolean[] input) {
		yaw = 0;
		pitch = 0;
		yawV = 0;
		pitchV = 0;
		this.p = p;
		this.world = world;
		this.gui = gui;
		xv = 0;
		yv = 0;
		zv = 0;
		this.input = input;
		x = world.sizeX() / 2;
		y = world.sizeY() / 2;
		z = world.sizeZ() / 2;
		while (check()) {
			x += Math.random() * 2 - 1;
			y += Math.random() * 2 - 1;
			z += Math.random() * 2 - 1;
			if (x < 0)
				x += world.sizeX();
			x %= world.sizeX();
			if (y < 0)
				y += world.sizeY();
			y %= world.sizeY();
			if (z < 0)
				z += world.sizeZ();
			z %= world.sizeZ();
		}
		inventory = new Inventory(50);
	}

	public void leftClick() {
		switch (gui.guiState) {
		case GUI.GAME:
			// Scans to see which block the player is looking at
			int[] hit = scan(false);
			// Breaks the selected block
			if (hit != null) {
				inventory.addItem(world.getBlock(hit[0], hit[1], hit[2]).getItem());
				world.breakBlock(hit[0], hit[1], hit[2]);
			}
			break;
		case GUI.INVENTORY:
			int x = (p.mouseX) / 256;
			int y = (p.mouseY - 28) / 256;
			selItem = inventory.getItems()[x + y * Inventory.REND_X];
			break;
		}
		// Do gui's left click
		gui.leftClick();
	}

	public void rightClick() {
		switch (gui.guiState) {
		case GUI.GAME:
			if (selItem != null) {
				// Get coords where player is looking
				int[] hit = scan(true);
				//Place block if it can, and use the item
				if (hit != null && selItem.getBlock() != null && inventory.useItem(selItem.getName(), 1)) {
					world.setBlock(hit[0], hit[1], hit[2], selItem.getBlock());
					if(selItem.amount == 0) {
						selItem = null;
					}
				}
			}
			break;
		}
		gui.rightClick();
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
			if (world.getBlock((int) rx, (int) ry, (int) rz).isBreakable()) {
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
		if (gui.guiState == GUI.GAME) {
			yawV += Math.toRadians(m.mouseX - m.width / 2) / 3;
			pitchV += Math.toRadians(m.mouseY - m.height / 2) / 3;
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
		// Movement dampening
		xv *= 0.5;
		yv *= 0.5;
		zv *= 0.5;
		yaw += yawV;
		pitch += pitchV;
		yawV *= 0.3;
		pitchV *= 0.3;
		double buttons = 0;
		// Movement from player input
		if (gui.guiState == GUI.GAME) {
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
			if (m.input['q']) {
				buttons++;
			}
			if (m.input['e']) {
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
			if (m.input['q']) {
				yvc += speed;
			}
			if (m.input['e']) {
				yvc -= speed;
			}
			xv += xvc;
			yv += yvc;
			zv += zvc;
		}
		x = mod(x, world.sizeX());
		y = mod(y, world.sizeY());
		z = mod(z, world.sizeZ());
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
					Block block = world.getBlock((int) bx, (int) by, (int) bz);
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
