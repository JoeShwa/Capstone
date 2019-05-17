package portals;

import java.util.HashMap;

public class PortalManager {
	
	public static HashMap<Integer, Pocket> coords;
	
	static {
		coords = new HashMap<>();
	}
	
	public static Pocket getPocket(int x, int y, int z) {
		if(isPocket(x, y, z)) {
			return coords.get(toIndex(x, y, z));
		} else {
			return null;
		}
	}
	
	public static boolean isPocket(int x, int y, int z) {
		return coords.containsKey(toIndex(x, y, z));
	}
	
	private static int toIndex(int x, int y, int z) {
		return x + y * 1290 + z * 3686400;
	}

}
