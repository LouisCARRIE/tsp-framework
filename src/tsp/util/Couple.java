package tsp.util;

public class Couple {
	int x;
	int y;
	public Couple() {
		this(0,0);
	}
	public Couple(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	public boolean equals(Object o) {
		return (o != null) && (o instanceof Couple) 
				&& this.getX() == ((Couple)o).getX() && this.getY() == ((Couple)o).getY();
	}
}
