package tsp.heuristic;

import java.util.ArrayList;

import tsp.Instance;
import tsp.util.Couple;
import tsp.util.CouplePhenoDistance;
import tsp.util.CoupleVisite;
import tsp.util.TripletPheroDistanceVisite;



public class ColonieDeFourmis extends AHeuristic  {

	public ColonieDeFourmis(Instance instance, String name) throws Exception {
		super(instance, name);
		// TODO Auto-generated constructor stub
	}
	
	public double ProbaVille (int i, int j, TripletPheroDistanceVisite[][] V) {
		double p =0.0;
		if (V[i][j].isVisite()) {
			
		}
		else {
			p=(V[i][j].getPheno()/V[i][j].getDistance());
		}
		return p;
		
	}
	
	public static void MajPhero(TripletPheroDistanceVisite[][][] V ) {
		int somme = 0;
		double rho =0.5;
		for (int k=0; k<V.length; k++) {
			for (int j=0; j<V[0].length; j++) {
				int i=1;
				for (int l =0; l<i; l++) {
					if(V[k][j][l].isVisite()==false) {
						somme+=V[k][j][l].getPheno();
					}
				}
				i++;
			}
		}
		for (int k=0; k<V.length; k++) { 	  	    	  	   		 	
			for (int j=0; j<V[0].length; j++) { 	  	    	  	   		 	
				int i=1; 	  	    	  	   		 	
				for (int l =0; l<i; l++) { 
					if(V[k][j][l].isVisite()==false) {
					V[k][j][l].setPheno(rho*V[k][j][l].getPheno()+somme);
					V[k][l][j].setPheno(rho*V[k][l][j].getPheno()+somme);
					}
				} 	  	    	  	   		 	
				i++; 	  	    	  	   		 	
			} 	  	    	  	   		 	
		} 	  	    	  	   		 	

		
	}
	
	public Couple MaxTableauSup(double [][] t) {
		int ligne =0;
		double max=0.0;
		Couple c=new Couple();
		while (ligne<t.length) {
			int colonne =ligne;
			while (colonne<ligne) {
				if (t[ligne][colonne]>max) {
					c.setX(ligne);
					c.setY(colonne);
				}
			}
		}
		return c;
	}
	

	
	
	@Override
	public void solve() throws Exception {
		// TODO Auto-generated method stub
		
		int nbvilles = this.m_instance.getNbCities();
		int nombreFourmis=100;
		int quantitePhero=100;
		TripletPheroDistanceVisite[][][] Arretes = new TripletPheroDistanceVisite[nombreFourmis][nbvilles][nbvilles];
		int i=0;
		for (TripletPheroDistanceVisite [][] fourmis : Arretes) {
			for (TripletPheroDistanceVisite [] ligne : fourmis) {
				int j=0;
				for (TripletPheroDistanceVisite colonne : ligne) {
					if (i!=j) {
						colonne.setDistance(this.m_instance.getDistances(i, j));
						colonne.setPheno(1);
						colonne.setVisite(false);
					}
					j+=1;
				}
				i+=1;			
			}
		}
		
		Couple[][] VillesFourmis = new Couple[nombreFourmis][nbvilles];	  	    	  	   		 	 	  	    	  	   		 	
  	   	for (int l =0; l<nombreFourmis; l++) {
  	   		int x = (int) Math.random()*nbvilles;
  	   		
  	   		int y = (int) Math.random()*nbvilles%10; 	  	    	  	   		 	
  	   		while (x==y) {
  	   			y = (int) Math.random()*nbvilles%10;  	  	    	  	   		 	
  	   		}
  	   		VillesFourmis[l][0].setX(x);
  	   		VillesFourmis[l][0].setX(y);
		while (quantitePhero>0) {
			int ite=0;
			for (int f=0 ; f<nombreFourmis; f++) {
	  	   		double[][] proba = new double[nbvilles][nbvilles];
	  	   		for (int m=0; m<nbvilles; m++) {
	  	   			for (int n=0; n<nbvilles; n++) { 	  	    	  	   		 	
	  	   				proba[m][n]=ProbaVille(m,n,Arretes[ite]);
	  	   				
	  	   			}
	  	   		}
	  	   	Couple c =  MaxTableauSup(proba);
	  	 	VillesFourmis[l][ite].setX(c.getX());
	   		VillesFourmis[l][ite].setX(c.getY());
			}
	  	   	ite++;		
		}
			quantitePhero--;
		
	}
	}
}
