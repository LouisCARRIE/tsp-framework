package tsp.heuristic;

import tsp.Instance;
import tsp.Solution;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

public class ArbreCouvrant extends AHeuristic{
	
	List<Integer> villesMarquees = new ArrayList<Integer>();
	
	public ArbreCouvrant(Instance instance) throws Exception {
		super(instance, "Arbre Couvrant de poids minimal");
	}
	
	public static List<Integer> remove1(List<Integer> l, int point) {
		List<Integer> t = new ArrayList<Integer>();
		for(int e : l) {
			if(e != point) {
				t.add(e);
			}
		}
		return t;
	}
	
	/*return un arbre de recouvrement de poids minimal, arbre.get(i) renvoie les fils du sommet i, i est donc le 
	 * père des sommets de arbre.get(i)*/
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
	
	public void explorer(List<List<Integer>> arbre, int villeVisitee) throws Exception {
		this.villesMarquees.add(villeVisitee);
		System.out.println(villeVisitee);
		for(int villeFils : arbre.get(villeVisitee)) {
			if(!(this.villesMarquees.contains(villeFils))) {
				explorer(arbre, villeFils);
			}
		}
	}
	
	@Override
	public void solve() throws Exception {
		List<List<Integer>> arbre = algoPrim(this.m_instance);
		System.out.println(arbre);
		int debut = 4; //faire boucle for sur la ville de depart 'debut'
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
