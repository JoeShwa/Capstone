package control;

public class Log {
	
	public String msg;
	public int time;
	
	public Log(String msg) {
		this.msg = msg;
		time = 600;
	}
	
	public int getAlpha() {
		if(time > 60) {
			return 255;
		} else {
			return (int) (time * 4.25);
		}
	}
	
	public void tick() {
		time--;
	}

}
