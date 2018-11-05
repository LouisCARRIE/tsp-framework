/*
 * 
 */
package tsp.metaheuristic;


import tsp.Instance;
import tsp.Solution;
import tsp.util.Couple;
import tsp.util.TripletPheroDistanceVisite;




// TODO: Auto-generated Javadoc
/**
 * The Class ColonieDeFourmis.
 * @author Brendan Becaert
 */


public class ColonieDeFourmis extends AMetaheuristic  {
	
	
	/**
	 * Instantiates a new colonie de fourmis.
	 *
	 * @param instance the instance
	 * @throws Exception the exception
	 */

	public ColonieDeFourmis(Instance instance) throws Exception {
		super(instance, "Colonie de fourmis");
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Initianilisation arretes.
	 *
	 * @return l'initianilisation de la liste des matrices caractéristiques de chaque fourmis.
	 * @throws Exception the exception
	 */
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
	
	
	/**
	 * Proba ville.
	 *
	 * @param i indice de ville
	 * @param j indice de ville
	 * @param V the v
	 * @return l'initianilisation de la liste des matrices caractéristiques de chaque fourmis.
	 */
	
	public static double ProbaVille (int i, int j, TripletPheroDistanceVisite[][] V) {
		double p =0.0;
		if (V[i][j]!=null) {
			if (V[i][j].isVisite()) {
			
			}
			else {
				p=(V[i][j].getPheno()/V[i][j].getDistance());
			}
		}
		return p;
		
	}
	
	/**
	 * Maj phero.
	 *
	 * @param V the v
	 * @return la meme matrice avec les caractéristiques mis à jour
	 */
	
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
					if(V[k][j][l].isVisite()==false) {
					V[k][j][l].setPheno(rho*V[k][j][l].getPheno()+somme);
					V[k][l][j].setPheno(rho*V[k][l][j].getPheno()+somme);
					}
				} 	  	    	  	   		 	
				i++; 	  	    	  	   		 	
			} 	  	    	  	   		 	
		} 
	}
	
	/**
	 * Distance.
	 *
	 * @param c un tableau de villes
	 * @return la distance parcourant le chemin passsant par les villes de c
	 * @throws Exception the exception
	 */
	
	public long distance (int[] c)throws Exception {
		long distance=0;
		for (int k=0; k<c.length-2; k++) {
			distance+=this.m_instance.getDistances(k, k+1);
		}
		distance+=this.m_instance.getDistances(c.length-2,0);
		return distance;
	}
	



	/**
	 * Ville suivante.
	 *
	 * @param proba tableau de double ayant pour arguments, l'indice de la ville et la probabilité qui lui correspond
	 * @return l'indice de la ville suivante à visiter en prenant en compte les probabilités que chaque ville soient choisies
	 */
	
	public static int   VilleSuivante( double[][] proba) {
		int nbVillesNonVisitees = proba.length;
		double[] probaponderees = new double[nbVillesNonVisitees];
		for (int i=0; i<probaponderees.length; i++) {
			probaponderees[i]=proba[i][1]*i;
			}
		double rang = Math.random()*proba[proba.length-1][0];
		int indice =(int)proba[0][0];
		int k=0;
		while ( k<probaponderees.length&&probaponderees[k]<rang) {
			indice =(int)proba[k][0];
			k++;
		}
		return indice;
		}
	
	/**
	 * Solve.
	 *
	 * @return l'initianilisation de la liste des matrices caractéristiques de chaque fourmis.
	 * @throws Exception the exception
	 */
	
	

	@Override
	public Solution solve(Solution sol) throws Exception {
		long tempsIni = System.currentTimeMillis();
		int nbvilles = this.m_instance.getNbCities();;
		int nbFourmis=5;
		TripletPheroDistanceVisite[][][] Fourmis = new TripletPheroDistanceVisite[nbFourmis][nbvilles][nbvilles]; 
  	   	
  	   	Fourmis=InitianilisationArretes();
  	   	int [] meilleurChemin = new int [nbvilles+1];
  	   	for (int i = 0 ; i< meilleurChemin.length; i++) {
  	   		meilleurChemin[i]=i;
  	   	}
  	   	meilleurChemin[meilleurChemin.length-1]=0;
  	  long meilleuredistance=distance(meilleurChemin);
  	   	int[] VillesVisitees = new int [nbvilles+1];
  	   	VillesVisitees[0]=0;
		VillesVisitees[nbvilles]=0;
		int VilleSuivante=1;
		double [][] listeProba = new double [nbvilles][2];
		
  	   	while (System.currentTimeMillis()-tempsIni<50000) {
  	   		for ( int f=0; f<Fourmis.length; f++) {
  	   			for (int k=0; k<nbvilles-1; k++) { 
  	   				listeProba = new double [nbvilles-k-1][2];
  	   				double i =0.0;
  	   				for (double [] v : listeProba) {
  	   					if (i<Fourmis[f].length) {
  	   						
  	   						v[0]=i;
  	   						v[1]=ProbaVille(k, (int)i, Fourmis[f]) ;	
  	   					}
 						i+=1.0;	
  	   				}
  	   				
  	   				
  	   				VilleSuivante=VilleSuivante(listeProba);  			
  	   				VillesVisitees[k]=VilleSuivante;
  	   				
  	   			
  	   		
  	   			long distanceParcourue=distance(VillesVisitees);
	   			if (distanceParcourue<meilleuredistance) {
	   				meilleuredistance=distanceParcourue;
	   				meilleurChemin=VillesVisitees;
	   				}
  	   			}
  	   		}
  	   	}
  	   	
  	   	//Elaboration de la solution 
		for (int e = 0; e<meilleurChemin.length ; e++) {
			sol.setCityPosition(meilleurChemin[e], e);
		}
		return sol;
			}
}

