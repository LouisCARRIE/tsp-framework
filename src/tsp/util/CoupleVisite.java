package tsp.util;

public class CoupleVisite {
	double phero;
	boolean b;
	public CoupleVisite() {
		this(0, false);
	}
	
	public CoupleVisite(double phero, boolean b) {
		this.phero = phero;
		this.b = b;
	}
	public double getPhero() {
		return phero;
	}
	public void setPhero(double phero) {
		this.phero = phero;
	}
	public boolean getB() {
		return b;
	}
	public void setB(boolean b) {
		this.b = b;
	}
}
