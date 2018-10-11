package tsp.heuristic;

import tsp.Instance;
import java.util.List;
import java.util.ArrayList;

public class plusProcheVoisin extends AHeuristic{

	public plusProcheVoisin(Instance instance) throws Exception {
		super(instance, "plusProcheVoisin");
	}

	@Override
	public void solve() throws Exception {
		int nbVilles = this.m_instance.getNbCities(); // super à la place de this ?
		long[] distances = new long[nbVilles]; // liste des distances en fontion du point de départ
		List<List<Integer>> solutions = new ArrayList<List<Integer>>(); // liste des solutions en fonction du point de départ
		for(int pointDepart = 0; pointDepart < nbVilles; pointDepart ++) {
			int pointCourant = pointDepart;
			solutions.get(pointDepart).add(pointDepart);
			long distance = 0;
			for (int k = 0; k<nbVilles; k++) {
				long distanceTemp = this.m_instance.getDistances(pointCourant, (pointCourant+1)%nbVilles);
				int pointSuivant = (pointCourant+1)%nbVilles;
				for(int i = 0; i < nbVilles; i++) { // on cherche la ville la plus proche du point courant
					if (i != pointCourant && this.m_instance.getDistances(pointCourant, i) < distanceTemp) {
						distanceTemp = this.m_instance.getDistances(pointCourant, i);
						pointSuivant = i;
					}
				}
				distance += distanceTemp;
			}
		}
		
	}
}
