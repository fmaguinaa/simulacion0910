package manager;

public class Contenido {

	private int x1;
	private int x2;
	private int y1;
	private int y2;
	
	public Contenido(int posX1,int posX2,int posY1,int posY2) {
		
		x1 = posX1;
		x2 = posX2;
		y1 = posY1;
		y2 = posY2;
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}

	
}
