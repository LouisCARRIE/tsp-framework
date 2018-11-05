package tsp.heuristic;

import tsp.Instance;
import tsp.Solution;
import java.util.List;
import java.util.ArrayList;

/**
 * The Class PlusProcheVoisin.
 * @author Thomas Bazaille
 */
public class PlusProcheVoisin extends AHeuristic{

	/**
	 * Instantiates a new plus proche voisin.
	 *
	 * @param instance the Instance
	 * @throws Exception the exception
	 */
	public PlusProcheVoisin(Instance instance) throws Exception {
		super(instance, "plusProcheVoisin");
	}
	
	/**
	 * Removes l'element point dans la liste l.
	 *
	 * @param l the list
	 * @param point : un int
	 * @return la liste l sans l'element point
	 */
	public static List<Integer> remove1(List<Integer> l, int point) {
		List<Integer> t = new ArrayList<Integer>();
		for(int e : l) {
			if(e != point) {
				t.add(e);
			}
		}
		return t;
	}
	

	@Override
	public void solve() throws Exception {
		int nbVilles = this.m_instance.getNbCities();
		long[] distances = new long[nbVilles]; // liste des distances en fontion du point de depart
		List<List<Integer>> solutions = new ArrayList<List<Integer>>(); // liste des solutions en fonction du point de départ
		for(int i = 0; i<nbVilles; i++) {
			solutions.add(new ArrayList<Integer>());
		}
		/*On va regarder pour chaque point de depart la distance du parcours*/
		for(int pointDepart = 0; pointDepart < nbVilles; pointDepart ++) {
			int pointCourant = pointDepart;
			solutions.get(pointDepart).add(pointDepart);
			long distance = 0;
			List<Integer> villesRestantes = new ArrayList<Integer>();
			for (int j = 0; j<nbVilles; j++) {
				villesRestantes.add(j);
			}
			villesRestantes = remove1(villesRestantes, pointDepart);
			for (int k = 0; k<nbVilles-1; k++) { //On fabrique un chemin qui va passer par tous les points 
				long distanceTemp = this.m_instance.getDistances(pointCourant, villesRestantes.get(0));
				int pointSuivant = villesRestantes.get(0);
				for(int i : villesRestantes) { // On cherche la ville la plus proche du point courant
					if (this.m_instance.getDistances(pointCourant, i) < distanceTemp) {
						distanceTemp = this.m_instance.getDistances(pointCourant, i);
						pointSuivant = i;
					}
				}
				villesRestantes = remove1(villesRestantes, pointSuivant);
				pointCourant = pointSuivant;
				distance += distanceTemp;
				solutions.get(pointDepart).add(pointSuivant);
			}
			distance += this.m_instance.getDistances(pointCourant, pointDepart);
			solutions.get(pointDepart).add(pointDepart);
			distances[pointDepart] = distance;
		}
		/*On cherche à présent à partir de quel point de départ la distance est minimum*/
		long distMin = distances[0];
		int depart = 0;
		for (int i = 1; i < nbVilles; i++) {
			if (distances[i] != 0 && distMin > distances[i]) {
				distMin = distances[i];
				depart = i;
			}
		}


		//des lignes 93-102, creation d'une tournee ou la ville 0 n'est
		//pas forcement en debut et en fin de tournee
		//si l'on veut que la ville 0 soit en debut et fin de tournee,
		//il faut mettre les lignes 93-102 en commentaires 
		//et decommenter les lignes 106-120
		Solution sol = new Solution(this.m_instance);
		int k = solutions.get(depart).indexOf(0);
		
		
		k = 0;
		for(int i : solutions.get(depart)) {
			sol.setCityPosition(i, k);
			k++;
		}
		this.m_solution = sol;
		
		//partie a decommenter si l'on veut que la ville 0
		//soit visitee en debut et en fin de tournee
		/*Solution sol = new Solution(this.m_instance);
		int k = solutions.get(depart).indexOf(0);
		
		int j = 0;
		
		for (int i=k ; i<solutions.get(depart).size() ; i++) {
			sol.setCityPosition(solutions.get(depart).get(i), j);
			j++;
		}
		
		for (int i=1 ; i<=k ; i++) {
			sol.setCityPosition(solutions.get(depart).get(i), j);
			j++;
		}
		this.m_solution = sol;*/
	}
}
