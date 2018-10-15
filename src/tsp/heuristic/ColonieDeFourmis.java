package tsp.heuristic;

import java.util.ArrayList;

import tsp.Instance;
import tsp.util.CouplePhenoDistance;
import tsp.util.CoupleVisite;



public class ColonieDeFourmis extends AHeuristic {

	public ColonieDeFourmis(Instance instance, String name) throws Exception {
		super(instance, name);
		// TODO Auto-generated constructor stub
	}

	
	
	
	public static CouplePhenoDistance[][] matricepheno (CouplePhenoDistance[][] Arretes) {
		int Q=1;
		for (CouplePhenoDistance [] ligne : Arretes) {
			for (CouplePhenoDistance colonne : ligne) {
				colonne.setPheno(colonne.getPheno()+Q/colonne.getDistance());
			}
		}
		return Arretes;
	}
	
	public static boolean convergence (ArrayList<CoupleVisite> Villes) {
		boolean conv = true;
		for (CoupleVisite e : Villes) {
			if (e.getB()==false) {
				conv=false;
			}
		}
		return conv;
	}
	
	
	@Override
	public void solve() throws Exception {
		// TODO Auto-generated method stub
		double p = 0.1;
		int nombreFourmis=100;
		int quantitePhero=100;
		int nbvilles = this.m_instance.getNbCities();
		CouplePhenoDistance[][] Arretes = new CouplePhenoDistance[nbvilles][nbvilles];
		int i=0;
		for (CouplePhenoDistance [] ligne : Arretes) {
			int j=0;
			for (CouplePhenoDistance colonne : ligne) {
				colonne.setDistance(this.m_instance.getDistances(i, j));
				colonne.setPheno(1);
				j+=1;
			}
			i+=1;			
		}
		while (quantitePhero>0) {
			for (int k=0; k < nombreFourmis; k++) {
				
				
				
			}
			
		}
		
	}
	
}
