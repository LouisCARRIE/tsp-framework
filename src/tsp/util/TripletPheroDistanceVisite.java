package tsp.util;

public class TripletPheroDistanceVisite {
	long distance; 	  	    	  	   		 	
	double pheno; 
	boolean visite;
	
	
	public TripletPheroDistanceVisite(long distance, double pheno, boolean visite) {
		super();
		this.distance = distance;
		this.pheno = pheno;
		this.visite = visite;
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


	public boolean isVisite() {
		return visite;
	}


	public void setVisite(boolean visite) {
		this.visite = visite;
	}
	
	

}
