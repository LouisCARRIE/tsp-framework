package tsp.heuristic;



import tsp.Instance;
import tsp.Solution;
import tsp.util.Couple;
import tsp.util.TripletPheroDistanceVisite;



public class ColonieDeFourmis extends AHeuristic  {

	public ColonieDeFourmis(Instance instance) throws Exception {
		super(instance, "Colonie de fourmis");
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
	
	public int indiceMinDistance (Couple[][] c) throws Exception {
		int indice=0;
		long distanceMin=10^100;
		for (int k = 0; k < c.length; k++) {
			long distance=distanceMin;
			for (int j=0; j < c[0].length-1; j++) {
				distance+=this.m_instance.getDistances(c[k][j].getX(), c[k][j+1].getX());
				}
			distance+=this.m_instance.getDistances(c[k][0].getX(), c[k][c[0].length].getX());
			if (distance<distanceMin) {
				indice=k;
				distanceMin=distance;
			}
		}
		return indice;
	}
	

	
	
	@Override
	public void solve() throws Exception {
		// TODO Auto-generated method stub
		
		int nbvilles = this.m_instance.getNbCities();
		int nombreFourmis=100;
		int quantitePhero=100;
		TripletPheroDistanceVisite[][][] Arretes = new TripletPheroDistanceVisite[nombreFourmis][nbvilles][nbvilles];
		for (TripletPheroDistanceVisite [][] fourmis : Arretes) { 	  	    	  	   		 	
			for (int ligne=0; ligne< fourmis.length; ligne ++) { 	  	    	  	   		 		  	    	  	   		 	
				for (int colonne=0; colonne < fourmis[0].length; colonne++) { 	  	    	  	   		 	
					if (ligne!=colonne) { 
						fourmis[ligne][colonne]=new  TripletPheroDistanceVisite(5, this.m_instance.getDistances(ligne, colonne), false); 	  	    	  	   		 						
					} 	  	    	  	   		 	 	  	    	  	   		 	
				} 	  	    	  	   		 				 	  	    	  	   		 	
			} 	  	    	  	   		 	
		} 
		
		Couple[][] VillesFourmis = new Couple[nombreFourmis][nbvilles];	  	    	  	   		 	 	  	    	  	   		 	
  	   	for (int l =0; l<nombreFourmis; l++) {
  	   		VillesFourmis[l][0]= new Couple(0,0);
  	   	}
		while (quantitePhero>0) {
			int ite=0;
			for (int f=0 ; f<nombreFourmis; f++) {
	  	   		double[][] proba = new double[nbvilles][nbvilles];
	  	   		for (int m=0; m<nbvilles; m++) {
	  	   			for (int n=0; n<nbvilles; n++) { 
	  	   				if (m!=n) {
	  	   					proba[m][n]=ProbaVille(m,n,Arretes[ite]);
	  	   				}
	  	   			}
	  	   		}
	  	   	Couple c =  MaxTableauSup(proba);
	  	 	VillesFourmis[f][ite]=c;
			}
	  	   	ite++;		
			quantitePhero--;
		}
	
  	   	int indiceMeilleureFourmi = indiceMinDistance(VillesFourmis);
  	   	int [] meilleurChemin = new int[nbvilles];
  	   	for (int z = 0; z<nbvilles; z++) {
  	   		meilleurChemin[z]=VillesFourmis[indiceMeilleureFourmi][z].getX();
  	   	}
  	   	Solution s = new Solution(m_instance);
		for (int e = 0; e<meilleurChemin.length ; e++) {
			s.setCityPosition(meilleurChemin[e], e);
		}
		m_solution = s;
	}
}
