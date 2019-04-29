package items;

public abstract class Tool extends Item {
	
	public boolean canUse = true;

	public Tool(int amt) {
		super(amt);
	}
	
	public void rightClick() {
		
	}

}
