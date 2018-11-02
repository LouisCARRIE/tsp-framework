package tsp.util;

import java.util.ArrayList;
import java.util.List;

import tsp.Instance;

public class Voisins {
	List<List<Integer>> voisins = new ArrayList<List<Integer>>(); //voisins.get(i) représente la liste des m plus proches voisins de i
	
	public Voisins(Instance instance, int m) throws Exception {
		for (int i = 0; i < instance.getNbCities(); i++) {
			this.voisins.add(new ArrayList<Integer>());
		}
	}

	/**
	 * Inserer : inserer la ville k dans la liste des plus proches voisins de i
	 *de sorte que les plus proches voisins de i soient classés dans l'ordre croissant de leur distance à i,
	 *ainsi voisins.get(i).get(0) correspond à la ville la plus proche de la ville i.
	 * @param instance the instance
	 * @param i : une ville de l'instance
	 * @param k : la ville à insérer
	 * @param m : le nombre maximal de plus proches voisins de i stocké dans la liste 
	 * @throws Exception the exception
	 */
	public void inserer(Instance instance, int i, int k, int m) throws Exception {
		int n = this.voisins.get(i).size();
		
		int indice = 0;
		while (indice < n && instance.getDistances(this.voisins.get(i).get(indice), i) < instance.getDistances(i, k)) {
			indice++;
		}
		this.voisins.get(i).add(indice, k);
		n = this.voisins.get(i).size();
		if (n > m) {
			this.voisins.get(i).remove(n - 1);
		}
	}	
		
	
	
	/**
	 * Get.
	 *
	 * @param i : une ville de l'instance
	 * @return la liste des plus proches voisins de i
	 */
	public List<Integer> get(int i) {
		return this.voisins.get(i);
	}
}
