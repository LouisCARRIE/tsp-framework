package tsp.util;

public class CouplePhenoDistance {
	long distance;
	double pheno;
	
	
	public CouplePhenoDistance(long distance, double pheno) {
		super();
		this.distance = distance;
		this.pheno = pheno;
	}


	public long getDistance() {
		return distance;
	}


	public void setDistance(long distance) {
		this.distance = distance;
	}


	public double getPheno() {
		return pheno;
	}


	public void setPheno(double pheno) {
		this.pheno = pheno;
	}
	

}
