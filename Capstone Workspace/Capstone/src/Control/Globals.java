package Control;

import processing.core.PApplet;

public class Globals {
	
	public static Main main;
	public static PApplet p;
	public static Player player;
	public static World world;
	public static GUI gui;
	
	public static int mod(int a, int b) {
		if(a > 0) {
			return a % b;
		}
		while(a < 0) {
			a += b;
		}
		return a;
	}
	
	public static double mod(double a, double b) {
		if(a > 0) {
			return a % b;
		}
		while(a < 0) {
			a += b;
		}
		return a;
	}
	
	public static int floor(double n) {
		if(n < 0) {
			n--;
		}
		return (int) n;
	}
	
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
