package Control;

import processing.core.PApplet;

public class Globals {
	
	public static Main main;
	public static PApplet p;
	public static Player player;
	public static World world;
	public static GUI gui;
	
	public static void add(Main main) {
		Globals.main = main;
	}
	
	public static void add(PApplet p) {
		Globals.p = p;
	}
	
	public static void add(Player player) {
		Globals.player = player;
	}
	
	public static void add(World world) {
		Globals.world = world;
	}
	
	public static void add(GUI gui) {
		Globals.gui = gui;
	}
}
