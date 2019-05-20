package items;

import blocks.Block;
import control.Globals;
import control.Inventory;
import control.Player;
import entities.RockParticle;
import events.EventManager;
import events.ReloadEvent;
import processing.core.PImage;

public class Accelerator extends Tool {

	public Accelerator(int amt) {
		super(amt);
	}

	public Accelerator() {
		super(0);
	}

	static PImage tex = Globals.p.loadImage("textures/accelerator.png");

	public void rightClick() {
		if (canUse && Globals.player.energy >= 128) {
			Globals.player.energy -= 128;
			Inventory inv = Globals.player.inventory;
			if (inv.hasItem("Thermite") && inv.hasItem("Sludge") && inv.hasItem("Rock")) {
				inv.useItem("Thermite", 1);
				inv.useItem("Sludge", 1);
				inv.useItem("Rock", 1);
				Player pl = Globals.player;
				Globals.world.addEntity(new RockParticle(pl.getX(), pl.getY(), pl.getZ(),
						(Math.cos(pl.getYaw()) * Math.cos(pl.getPitch())) / 2, Math.sin(pl.getPitch()) / 2,
						(Math.sin(pl.getYaw()) * Math.cos(pl.getPitch())) / 2));
			}
			canUse = false;
			EventManager.addEvent(new ReloadEvent(this), 30);
		}
	}

	public String getName() {
		return "Accelerator";
	}

	public String getLore() {
		return "A tool that uses the reaction between Thermite and Sludge to launch Rocks at high velocities.";
	}

	public Block getBlock() {
		return null;
	}

	public void draw(int x, int y) {
		super.draw(tex, x, y);
	}

}
