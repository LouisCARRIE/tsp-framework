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
		if (V[i][j]!= null) {
			if (V[i][j].isVisite()) {
			
			}
			else {
				p=(V[i][j].getPheno()/V[i][j].getDistance());
			}
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
					if(V[k][j][l]!=null) {
					if(V[k][j][l].isVisite()==false) {
						somme+=V[k][j][l].getPheno();
					}
					}
				}
				i++;
			}
		}
		for (int k=0; k<V.length; k++) { 	  	    	  	   		 	
			for (int j=0; j<V[0].length; j++) { 	  	    	  	   		 	
				int i=1; 	  	    	  	   		 	
				for (int l =0; l<i; l++) { 
					if(V[k][j][l]!=null) {
					if(V[k][j][l].isVisite()==false) {
					V[k][j][l].setPheno(rho*V[k][j][l].getPheno()+somme);
					V[k][l][j].setPheno(rho*V[k][l][j].getPheno()+somme);
					}
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
		int [] L = new int [c[0].length];
		for (int k = 0; k < c.length; k++) {
			long distance=distanceMin;
			for (int j=0; j < c[0].length; j++) {
				L = new int [c[0].length];
				if (c[k][j]!=null) {
					L[j]=c[k][j].getX();
				}
			}
			distance = distance(L);
			if (distance<distanceMin) {
				indice=k;
				distanceMin=distance;
			}
		}
		return indice;
	}
	
	public long distance (int[] c)throws Exception {
		long distance=0;
		for (int k=0; k<c.length-2; k++) {
			distance+=this.m_instance.getDistances(k, k+1);
		}
		distance+=this.m_instance.getDistances(c.length-2,0);
		return distance;
	}
	
public  TripletPheroDistanceVisite[][][] InitianilisationArretes() throws Exception  {
	int nbvilles = this.m_instance.getNbCities();
	int nombreFourmis=100;
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
	return Arretes;
}
	public Couple[][] InitialinisationVillesFourmis(){
		int nbvilles = this.m_instance.getNbCities();
		int nombreFourmis=100;
		Couple[][] VillesFourmis = new Couple[nombreFourmis][nbvilles];	  	    	  	   		 	 	  	    	  	   		 	
	   		for (int l =0; l<nombreFourmis; l++) {
	   			VillesFourmis[l][0]= new Couple(0,0);
	   		}
	   		return VillesFourmis;	   		
	}
	
	public static int  VilleSuivante( double[][] proba) {
		int nbVillesNonVisitees = proba.length;
		double[] probaponderees = new double[nbVillesNonVisitees];
		for (int i=0; i<probaponderees.length; i++) {
			probaponderees[i]=proba[i][1]*i;
			}
		double rang = Math.random()*proba.length;
		int indice =(int)proba[0][0];
		int k=0;
		while (rang<probaponderees[k]) {
			indice =(int)proba[k][0];
			k++;
		}
		return indice;
		}
	
	@Override
	public void solve() throws Exception {
		int nbvilles = this.m_instance.getNbCities();;
		int nbFourmis=10;
		int quantitePhero=5;
		double [][] listeProba = new double [nbvilles][2];
		TripletPheroDistanceVisite[][][] Arretes = new TripletPheroDistanceVisite[nbFourmis][nbvilles][nbvilles]; 
  	   	long meilleuredistance=2*10^25;
  	   	Arretes=InitianilisationArretes();
  	   	int [] meilleurChemin = new int [nbvilles+1];
  	   	
  	   	while (quantitePhero>0) {
  	   	int ite =0;
  	   		for ( int f=0; f<Arretes.length; f++) {
  	   			int[] VillesVisitees = new int [nbvilles+1];
  	   			VillesVisitees[0]=0;
  	   			VillesVisitees[nbvilles]=0; 		
  	   			for (int k=0; k<nbvilles-1; k++) {
  	   				listeProba = new double [nbvilles-ite+1][2];
  	   				for (int j=0; j<nbvilles-ite+1; j++) {
  	   					if (Arretes[f][VillesVisitees[k]][k]!=null) {
  	   					if (Arretes[f][VillesVisitees[k]][k].isVisite()==false) {
  	   						listeProba[j][0]=j;
  	   						listeProba[j][1]=Arretes[f][VillesVisitees[k]][k].getPheno()/Arretes[f][VillesVisitees[k]][k].getDistance();
  	   					}
  	   					}
  	   				}
  	   				int VilleSuivante=VilleSuivante(listeProba);
  	   				VillesVisitees[k]=VilleSuivante;
  	   			}
  	   			long distanceParcourue=distance(VillesVisitees);
  	   			if (distanceParcourue<meilleuredistance) {
  	   				meilleuredistance=distanceParcourue;
  	   				meilleurChemin=VillesVisitees;
  	   			}
  	   		}
  	   		MajPhero(Arretes);
  	   		ite++;		
  	   		quantitePhero--;
		}
  	   	
  	   	//Elaboration de la solution 
  	   	Solution s = new Solution(m_instance);
		for (int e = 0; e<meilleurChemin.length ; e++) {
			s.setCityPosition(meilleurChemin[e], e);
		}
		m_solution = s;
			}
  	   	}

