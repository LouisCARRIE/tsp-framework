package tsp.heuristic;

import tsp.Instance;
import tsp.util.CoupleVisite;



public class ColonieDeFourmis extends AHeuristic {

	public ColonieDeFourmis(Instance instance, String name) throws Exception {
		super(instance, name);
		// TODO Auto-generated constructor stub
	}

	
	
	
	public static double phenoromone ( double pheno, double longeurChemin) {
		int G=1;
		return (pheno+G/longeurChemin);
	}
	
	public static boolean convergence () {
		return true;
	}
	
	
	@Override
	public void solve() throws Exception {
		// TODO Auto-generated method stub
		int nombreFourmis=100;
		int quantitePhero=100;
		CoupleVisite villes = new CoupleVisite();
		while (quantitePhero>0|| convergence()==true) {
			for (int k=0; k < nombreFourmis; k++) {
				
				
				
			}
		}
		
	}
	
}
