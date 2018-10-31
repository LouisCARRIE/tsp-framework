package tsp.heuristic;

import java.util.ArrayList;
import java.util.List;

import tsp.Instance;
import tsp.Solution;
import tsp.heuristic.*;

import tsp.util.Voisins;

public class K_opt extends AHeuristic{

	public K_opt(Instance instance) throws Exception {
		super(instance, "K_opt");
	}
	
	public List<Integer> reverse(List<Integer> l, int i, int j) {
		List<Integer> res = new ArrayList<Integer>();
		int k = 0;
		for (int e : l) {
			if (k < i || k > j) {
				res.add(e);
			}else {
				res.add(l.get(j+i-k));
			}
			k++;
		}
		return res;
	}
	
	public List<Integer> two_opt(Instance instance, List<Integer> solution, Voisins v) throws Exception {
		boolean amelioration = true;
		while (amelioration) {
			amelioration = false;
			for (int i = 0; i < instance.getNbCities(); i++) {
				for (int k : v.get(i)) {
					if (k != i && k != i-1 && k != i+1) {
						int k1 = (k+1)%instance.getNbCities();
						int i1 = (i+1)%instance.getNbCities();
						long d1 = instance.getDistances(i, i1);
						long d2 = instance.getDistances(k, k1);
						long d3 = instance.getDistances(i, k);
						long d4 = instance.getDistances(i1, k1);
						if (d1 + d2 > d3 + d4) {
							if (solution.indexOf(i1) < solution.indexOf(k)) {
								solution = reverse(solution, solution.indexOf(i1), solution.indexOf(k));
							}else {
								solution = reverse(solution, solution.indexOf(k1), solution.indexOf(i));
							}
							amelioration = true;
						}
					}
				}
			}
			
		}
		return solution;
	}
	
	@Override
	public void solve() throws Exception {
		PlusProcheVoisin s = new PlusProcheVoisin(this.m_instance);
		List<Integer> solution = new ArrayList<Integer>();
		for (int i = 0; i < this.m_instance.getNbCities(); i++) {
			solution.add(s.getSolution().getCity(i));
		}
		int m = this.m_instance.getNbCities();
		//int m = 20;
		//On récupère la liste des m plus proches voisins pour chaque node
		Voisins v = new Voisins(this.m_instance, m);
		
		for (int i = 0; i < this.m_instance.getNbCities(); i++) {
			for (int k = 0; k < this.m_instance.getNbCities(); k++) {
				if (i != k) {
					v.inserer(this.m_instance, i, k, m);
				}
				
			}
		}
		solution = two_opt(this.m_instance, solution, v);
		
		Solution sol = new Solution(this.m_instance);
		int k = 0;
		for(int i : solution) {
			sol.setCityPosition(i, k);
			k++;
		}
		this.m_solution = sol;
		
	}
}
