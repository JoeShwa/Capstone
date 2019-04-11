package control;

import java.util.LinkedList;

import blocks.Block;
import entities.Entity;

public class World {

	// Stores block data
	private Block[][][] blocks;
	// Stores entities
	private LinkedList<Entity>[][][] entities;

	public static final double GRAVITY = 0.01;
	public static final int DIM_COUNT = 2;
	public static final int SUBDIV = 5;

	@SuppressWarnings("unchecked")
	public World(int x, int y, int z) {
		if (x % SUBDIV != 0 || y % SUBDIV != 0 || z % SUBDIV != 0) {
			throw new IllegalArgumentException();
		}
		blocks = new Block[x][y][z];
		entities = (LinkedList<Entity>[][][]) new LinkedList<?>[x / SUBDIV][y / SUBDIV][z / SUBDIV];
		for (x = 0; x < entities.length; x++) {
			for (y = 0; y < entities[0].length; y++) {
				for (z = 0; z < entities[0][0].length; z++) {
					entities[x][y][z] = new LinkedList<Entity>();
				}
			}
		}

	}

	// Adds the entity at the correct chunk
	public void addEntity(Entity entity) {
		getEntities(Globals.floor(entity.x), Globals.floor(entity.y), Globals.floor(entity.z)).add(entity);
	}

	// Gets all the entities in all the chunks in the cubic radius around xyz
	public LinkedList<Entity> getEntities(int x, int y, int z, int rad) {
		x /= SUBDIV;
		y /= SUBDIV;
		z /= SUBDIV;
		LinkedList<Entity> out = new LinkedList<Entity>();
		for (int sx = -rad; sx < rad + 1; sx++) {
			for (int sy = -rad; sy < rad + 1; sy++) {
				for (int sz = -rad; sz < rad + 1; sz++) {
					out.addAll(getEntities(sx + x, sy + y, sz + z));
				}
			}
		}
		return out;
	}

	// Get all entities in general
	public LinkedList<Entity> getEntities() {
		LinkedList<Entity> out = new LinkedList<Entity>();
		for (int x = 0; x < entities.length; x++) {
			for (int y = 0; y < entities[0].length; y++) {
				for (int z = 0; z < entities[0][0].length; z++) {
					out.addAll(getEntities(x, y, z));
				}
			}
		}
		return out;
	}

	// Gets all the entities in the chunk xyz
	public LinkedList<Entity> getEntities(int x, int y, int z) {
		x = Globals.mod(x, entities.length);
		y = Globals.mod(y, entities[0].length);
		z = Globals.mod(z, entities[0][0].length);
		return entities[x][y][z];
	}

	public void updateAllVisibilty() {
		for (int x = 0; x < sizeX(); x++) {
			for (int y = 0; y < sizeY(); y++) {
				for (int z = 0; z < sizeZ(); z++) {
					getBlock(x, y, z).updateVisibilty(x, y, z);
				}
			}
		}
	}

	public int sizeX() {
		return blocks.length;
	}

	public int sizeY() {
		return blocks[0].length;
	}

	public int sizeZ() {
		return blocks[0][0].length;
	}

	public Block getBlock(int x, int y, int z) {
		return blocks[Globals.mod(x, sizeX())][Globals.mod(y, sizeY())][Globals.mod(z, sizeZ())];
	}

	public Block getBlock(double x, double y, double z) {
		return getBlock(Globals.floor(x), Globals.floor(y), Globals.floor(z));
	}

	public void setBlock(int x, int y, int z, Block b) {
		Block prev = getBlock(x, y, z);
		blocks[Globals.mod(x, sizeX())][Globals.mod(y, sizeY())][Globals.mod(z, sizeZ())] = b;
		b.placeEvent(x, y, z, prev);
	}

	public void setBlock(double x, double y, double z, Block b) {
		setBlock(Globals.floor(x), Globals.floor(y), Globals.floor(z), b);
	}

	public void newBlock(int x, int y, int z, Block b) {
		blocks[Globals.mod(x, sizeX())][Globals.mod(y, sizeY())][Globals.mod(z, sizeZ())] = b;
	}

	public void breakBlock(int x, int y, int z) {
		getBlock(x, y, z).breakEvent(x, y, z);
	}

	public void breakBlock(double x, double y, double z) {
		breakBlock(Globals.floor(x), Globals.floor(y), Globals.floor(z));
	}
}
