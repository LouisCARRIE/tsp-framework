package tsp.heuristic;

import tsp.Instance;
import java.util.ArrayList; 

public class ColonieDeFourmis extends AHeuristic {

	public ColonieDeFourmis(Instance instance, String name) throws Exception {
		super(instance, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void solve() throws Exception {
		// TODO Auto-generated method stub
		
	}
	public static double phenoromone ( double pheno, double longeurChemin) {
		int G=1;
		return (pheno+G/longeurChemin);
	}
	
	ArrayList L = new ArrayList<>(5);
	
	
		
	}
	

}
