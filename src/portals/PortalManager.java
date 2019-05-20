package portals;

import java.util.HashMap;

public class PortalManager {

	public static HashMap<Integer, PocketPos> coords;

	static {
		coords = new HashMap<>();
	}

	public static Pocket getPocket(int x, int y, int z) {
		if (isPocket(x, y, z)) {
			return coords.get(toIndex(x, y, z)).pocket;
		} else {
			return null;
		}
	}
	
	public static PocketPos getPos(int x, int y, int z) {
		return coords.get(toIndex(x, y, z));
	}
	
	public static void addPos(PocketPos pos, int x, int y, int z) {
		coords.put(toIndex(x, y, z), pos);
	}

	public static boolean isPocket(int x, int y, int z) {
		return coords.containsKey(toIndex(x, y, z));
	}

	private static int toIndex(int x, int y, int z) {
		return x + y * 1290 + z * 3686400;
	}

}
