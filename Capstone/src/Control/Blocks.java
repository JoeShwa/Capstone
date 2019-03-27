package Control;

public interface Blocks {

	boolean isTrans();

	boolean isSolid();

	boolean isLight();

	void draw(int x, int y, int z);
}
