package control;

import blocks.Block;
import processing.core.PApplet;

public class Globals {
	
	// Accessible objects for everyone
	public static Main main;
	public static PApplet p;
	public static Player player;
	public static World world;
	public static GUI gui;
	// Pre-calculated commonly used sqrts for efficiency
	static double[] sqrts;
	
	public static double sqrt(int a) {
		return sqrts[a];
	}
	
	public static void initSqrts() {
		int num = Block.BIGGEST_LIGHT * 2 + 1;
		num *= num;
		sqrts = new double[num * 3];
		for(int i = 0; i < sqrts.length; i++) {
			sqrts[i] = Math.sqrt(i);
		}
	}
	
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
