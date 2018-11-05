package tsp.heuristic;

import tsp.Instance;
import tsp.Solution;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;


/**
 * The Class ArbreCouvrant.
 * @author Thomas Bazaille
 */
public class ArbreCouvrant extends AHeuristic{
	
	/** The villes marquees. On en fera l'utilisation dans la fonction recursive explorer */
	List<Integer> villesMarquees = new ArrayList<Integer>();
	
	/**
	 * Instantiates a new arbre couvrant.
	 *
	 * @param instance the instance
	 * @throws Exception the exception
	 */
	public ArbreCouvrant(Instance instance) throws Exception {
		super(instance, "Arbre Couvrant de poids minimal");
	}
	
	/**
	 * Removes l'element point dans la liste l.
	 *
	 * @param l the list
	 * @param point the point
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
	
	/**
	 * Algo de Prim.
	 *
	 * @param instance the instance
	 * @return une liste correspondant à un arbre de recouvrement de poids minimal
	 * arbre.get(i) renvoie les fils du sommet i, i est donc le père des sommets de arbre.get(i)
	 * @throws Exception the exception
	 */
	public static List<List<Integer>> algoPrim(Instance instance) throws Exception{
		List<List<Integer>> arbre = new ArrayList<List<Integer>>();
		for(int i = 0; i<instance.getNbCities(); i++) {
			arbre.add(new ArrayList<Integer>());
		}
		List<Integer> villesMarquees = new ArrayList<Integer>();
		villesMarquees.add(0); //On commence à construire notre arbre depuis la ville 0.
		List<Integer> villesATraitees = new ArrayList<Integer>();
		for(int i = 1; i<instance.getNbCities(); i++) {
			villesATraitees.add(i);
		}
		for(int k = 0; k < instance.getNbCities() - 1; k++) {
			long distance = instance.getDistances(villesMarquees.get(0), villesATraitees.get(0));
			int vP = villesMarquees.get(0);
			int vF = villesATraitees.get(0);
			for(int villePere : villesMarquees) {
				for(int villeFils : villesATraitees) {
					if(instance.getDistances(villePere, villeFils)<distance) {
						distance = instance.getDistances(villePere, villeFils);
						vP = villePere;
						vF = villeFils;
					}
				}
			}
			villesATraitees = remove1(villesATraitees, vF);
			villesMarquees.add(vF);
			arbre.get(vP).add(vF);
			arbre.get(vF).add(vP);
		}
		return arbre;
	}
	
	/**
	 * Explorer. On fait un parcours en profondeur de notre arbre.
	 *
	 * @param arbre : l'arbre couvrant que l'on a fabriqué avec l'algo de prim
	 * @param villeVisitee the ville visitee
	 * @throws Exception the exception
	 */
	public void explorer(List<List<Integer>> arbre, int villeVisitee) throws Exception {
		this.villesMarquees.add(villeVisitee);
		for(int villeFils : arbre.get(villeVisitee)) {
			if(!(this.villesMarquees.contains(villeFils))) {
				explorer(arbre, villeFils);
			}
		}
	}
	
	@Override
	public void solve() throws Exception {
		List<List<Integer>> arbre = algoPrim(this.m_instance);
		int debut = 0;
		explorer(arbre, debut);
		Solution sol = new Solution(this.m_instance);
		int k = 0;
		for(int i : this.villesMarquees) { 	 	 	  		  		   	 	
			sol.setCityPosition(i, k); 	 	 	  		  		   	 	
			k++; 	 	 	  		  		   	 	
		}
		sol.setCityPosition(debut, this.m_instance.getNbCities());
		this.m_solution = sol;
	}
	
}
