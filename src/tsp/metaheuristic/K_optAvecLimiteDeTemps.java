package tsp.metaheuristic;

import java.util.ArrayList;
import java.util.List;

import tsp.Instance;
import tsp.Solution;

import tsp.util.Voisins;

/**
 * The Class K_opt.
 * @author Thomas Bazaille
 */
public class K_optAvecLimiteDeTemps extends AMetaheuristic{

	public K_optAvecLimiteDeTemps(Instance instance) throws Exception {
		super(instance, "K_optAvecLimiteDeTemps");
	}
	
	/**
	 * Reverse. On renvoie la liste l dans laquelle on a échangé les sous-listes l[i:j] et l[k:l].
	 * Exemple : reverse([0, 1, 2, 3, 4, 5, 6, 7, 8], 1, 3, 5, 6) renvoie [0, 5, 6, 4, 1, 2, 3, 7, 8].
	 *
	 * @param l : la liste que l'on va modifier
	 * @param i : l'indice de debut de la premiere sous-liste
	 * @param j : l'indice de fin de la premiere sous-liste
	 * @param k : l'indice de fin de la seconde sous-liste
	 * @param l : l'indice de fin de la seconde sous-liste
	 * @return la nouvelle liste
	 */
	public List<Integer> inverser(List<Integer> liste, int i, int j, int k, int l) {
		List<Integer> res = new ArrayList<Integer>();
		int ind = 0;
		for (int e : liste) {
			if (ind < i || ind > l || (ind > l - k + i && ind < l - j + i)) {
				res.add(e);
			}else {
				if (ind <= i + l - k) {
					res.add(liste.get(k - i + ind));
				}else {
					res.add(liste.get(ind - l + j));
				}
			}
			ind++;
		}
		return res;
	}
	
	
	/**
	 * Reverse. On renvoie la liste l dans laquelle on a retourne la sous-liste allant de l'indice i à j.
	 * Exemple : reverse([0, 1, 2, 3, 4, 5, 6, 7, 8], 2, 5) renvoie [0, 1, 5, 4, 3, 2, 6, 7, 8].
	 *
	 * @param l : la liste que l'on va modifier
	 * @param i : l'indice de debut
	 * @param j : l'indice de fin
	 * @return la nouvelle liste
	 */
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
	
	/**
	 * Cost.
	 *
	 * @param instance the instance
	 * @param solution the solution
	 * @return le coût du tour/solution
	 * @throws Exception the exception
	 */
	public long cost(Instance instance, List<Integer> solution) throws Exception {
		long c = 0;
		for(int i = 0; i < solution.size(); i++) {
			c += instance.getDistances(solution.get(i), solution.get((i+1)%solution.size()));
		}
		return c;
	}
	
	/**
	 * Two opt. On retire 2 aretes à notre chemin/solution et on essaie de trouver une nouvelle 
	 * reconnection qui améliore le tour.
	 *
	 * @param instance the instance
	 * @param sol : la solution initiale que l'on va améliorer (sol peut être la solution de plusProcheVoisin ...)
	 * @param v : liste des voisins (v.get(i) renvoie les plus proches voisins de la ville i)
	 * @return la solution amélioré par l'algo 2-opt
	 * @throws Exception the exception
	 */
	public List<Integer> two_opt(Instance instance, List<Integer> sol, Voisins v, long timeLimit) throws Exception {
		List<Integer> solution = sol;
		boolean amelioration = true;
		long startTime = System.currentTimeMillis();
		long spentTime = 0;
		do
		{
		amelioration = false;
		for (int i = 0; i < instance.getNbCities(); i++) {
			for (int k : v.get(i)) {
					
				int k1 = solution.get((solution.indexOf(k)+1)%instance.getNbCities());
				int i1 = solution.get((solution.indexOf(i)+1)%instance.getNbCities());
					
				if (k1 != i && k != i1) {
						
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
						//System.out.println(cost(this.m_instance, solution));
					}
				}
			}	
		}
		spentTime = System.currentTimeMillis() - startTime;
		}while(spentTime < (timeLimit * 1000 - 100) && amelioration);
		
		return solution;
	}
	
	/**
	 * Union.
	 *
	 * @param l1 
	 * @param l2
	 * @return l'union des elements des deux listes l1 et l2
	 */
	public List<Integer> union(List<Integer> l1, List<Integer> l2){
		List<Integer> l = new ArrayList<Integer>(l1);
		for (int e : l2) {
			if (!l1.contains(e)) {
				l.add(e);
			}
		}
		return l;
	}
	
	/**
	 * Three opt. On retire 3 aretes à notre chemin/solution et on essaie de trouver une nouvelle 
	 * reconnection qui améliore le tour.
	 *
	 * @param instance the instance
	 * @param sol : la solution initiale que l'on va améliorer (sol peut être la solution de plusProcheVoisin ...)
	 * @param v : liste des voisins (v.get(i) renvoie les plus proches voisins de la ville i)
	 * @return la solution amélioré par l'algo 3-opt
	 * @throws Exception the exception
	 */
	public List<Integer> three_opt(Instance instance, List<Integer> sol, Voisins v, long timeLimit) throws Exception {
		List<Integer> solution = sol;
		boolean amelioration = true;
		boolean stop = false;
		long startTime = System.currentTimeMillis();
		long spentTime = 0;
		while (amelioration) {
			amelioration = false;
			for (int i = 0; i < instance.getNbCities(); i++) {
				int i_index = solution.indexOf(i);
				int i1_index = (i_index + 1)%instance.getNbCities();
				if (stop) {
					break;
				}
				for (int k : v.get(i)) {
					int k_index = solution.indexOf(k);
					int k1_index = (k_index + 1)%instance.getNbCities();
					List<Integer> union = union(v.get(i), v.get(k));
					if (stop) {
						break;
					}
					for (int l : union) {
						int l_index = solution.indexOf(l);
						
						int k1 = solution.get((k_index+1)%instance.getNbCities());
						int i1 = solution.get((i_index+1)%instance.getNbCities());
						int l1 = solution.get((l_index+1)%instance.getNbCities());
						
						if (k_index > i_index && l_index > k_index && l != k && k != i1 && k1 != i && k1 != l && k != l1 && l1 != i && i1 != l) {
							long d0 = instance.getDistances(i, i1) + instance.getDistances(k, k1) + instance.getDistances(l, l1);
							long d1 = instance.getDistances(i, k) + instance.getDistances(i1, l) + instance.getDistances(k1, l1);
							long d2 = instance.getDistances(i, k1) + instance.getDistances(i1, l) + instance.getDistances(k, l1);
							long d3 = instance.getDistances(i, l) + instance.getDistances(i1, k1) + instance.getDistances(k, l1);
							long d4 = instance.getDistances(i, k1) + instance.getDistances(k, l) + instance.getDistances(i1, l1);
							
							List<Integer> cop = new ArrayList<Integer>(solution);
							long choix1 = Math.max(d0 - d3, d0 - d2);
							long choix2 = Math.max(d0 - d4, d0 - d1);
							long choix = Math.max(choix1, choix2);
							if (choix>0) {
								if (d3 < d0 && d0 - d3 == choix) {
									solution = reverse(solution, k1_index, l_index);
									solution = inverser(solution, i1_index, k_index, k1_index, l_index);
									amelioration = true;
								}else if (d2 < d0 && d0 - d2 == choix) {
									solution = inverser(solution, i1_index, k_index, k1_index, l_index);
									amelioration = true;
								}else if (d1 < d0 && d0 - d1 == choix) {
									solution = reverse(solution, i1_index, k_index);
									solution = reverse(solution, k1_index, l_index);
									amelioration = true;
								}else if (d4 < d0 && d0 - d4 == choix) {
									solution = reverse(solution, i1_index, k_index);
									solution = inverser(solution, i1_index, k_index, k1_index, l_index);
									amelioration = true;
								}else {
									
								}
							}
							spentTime = System.currentTimeMillis() - startTime;
							if (cost(this.m_instance, solution) > cost(this.m_instance, cop) || spentTime > (timeLimit * 1000 - 100)) {
								solution = cop;
								stop = true;
								break;
								
							}
							
						}
					}
					
				}
			}
			
		}
		return solution;
	}
	
	/**
	 * Four opt. On retire 4 aretes à notre chemin/solution et on essaie de trouver une nouvelle 
	 * reconnection qui améliore le tour.
	 *
	 * @param instance the instance
	 * @param sol : la solution initiale que l'on va améliorer (sol peut être la solution de plusProcheVoisin ...)
	 * @param v : liste des voisins (v.get(i) renvoie les plus proches voisins de la ville i)
	 * @return la solution amélioré par l'algo 4-opt
	 * @throws Exception the exception
	 */
	public List<Integer> four_opt(Instance instance, List<Integer> sol, Voisins v, long timeLimit) throws Exception {
		List<Integer> solution = sol;
		boolean amelioration = true;
		boolean stop = false;
		long startTime = System.currentTimeMillis();
		long spentTime = 0;
		while (amelioration) {
			amelioration = false;
			for (int i = 0; i < instance.getNbCities(); i++) {
				int i_index = solution.indexOf(i);
				int i1_index = (i_index + 1)%instance.getNbCities();
				int i1 = solution.get((i_index+1)%instance.getNbCities());
				if (stop) {
					break;
				}
				for (int j : v.get(i)) {
					int j_index = solution.indexOf(j);
					int j1_index = (j_index + 1)%instance.getNbCities();
					int j1 = solution.get((j_index+1)%instance.getNbCities());
					List<Integer> union = union(v.get(i), v.get(j));
					if (stop) {
						break;
					}
					for (int k : union) {
						int k_index = solution.indexOf(k);
						int k1_index = (k_index + 1)%instance.getNbCities();
						int k1 = solution.get((k_index+1)%instance.getNbCities());
						List<Integer> union1 = union(union, v.get(k));
						if (stop) {
							break;
						}
						for (int l : union1) {
							int l_index = solution.indexOf(l);
							int l1 = solution.get((l_index+1)%instance.getNbCities());
							
							
							if (j_index > i_index && k_index > j_index && l_index > k_index 
									&& l != k && k != l1 && k1 != l 
									&& j != k && j != k1 && k != j1
									&& j != i && j != i1 && i != j1
									&& i != k && i1 != k && k1 != i
									&& i != l && i1 != l && l1 != i
									&& j != l && j1 != l && l1 != j) {
								long d0 = instance.getDistances(i, i1) + instance.getDistances(j, j1) + instance.getDistances(k, k1) + instance.getDistances(l, l1);
								
								long d1 = instance.getDistances(i, j) + instance.getDistances(i1, j1) + instance.getDistances(k, l) + instance.getDistances(k1, l1);
								long d2 = instance.getDistances(i, j) + instance.getDistances(i1, k) + instance.getDistances(j1, l) + instance.getDistances(k1, l1);
								long d3 = instance.getDistances(i, j) + instance.getDistances(i1, k1) + instance.getDistances(j1, l) + instance.getDistances(k, l1);
								long d4 = instance.getDistances(i, j) + instance.getDistances(i1, k1) + instance.getDistances(k, l) + instance.getDistances(j1, l1);
								
								long d5 = instance.getDistances(i, j1) + instance.getDistances(i1, k) + instance.getDistances(j, l) + instance.getDistances(k1, l1);
								long d6 = instance.getDistances(i, j1) + instance.getDistances(j, k) + instance.getDistances(i1, l) + instance.getDistances(k1, l1);
								long d7 = instance.getDistances(i, j1) + instance.getDistances(k, l) + instance.getDistances(j, k1) + instance.getDistances(i1, l1);
								long d8 = instance.getDistances(i, j1) + instance.getDistances(k, l) + instance.getDistances(i1, k1) + instance.getDistances(j, l1);
								
								long d9 = instance.getDistances(i, k) + instance.getDistances(i1, j1) + instance.getDistances(j, l) + instance.getDistances(k1, l1);
								long d10 = instance.getDistances(i, k) + instance.getDistances(j1, l1) + instance.getDistances(l, i1) + instance.getDistances(j, l1);
								long d11 = instance.getDistances(i, k) + instance.getDistances(j1, l1) + instance.getDistances(l, j) + instance.getDistances(i1, l1);
								long d12 = instance.getDistances(i, k) + instance.getDistances(j1, l) + instance.getDistances(k1, i1) + instance.getDistances(j, l1);
								long d13 = instance.getDistances(i, k) + instance.getDistances(j1, l) + instance.getDistances(k1, j) + instance.getDistances(i1, l1);
								
								long d14 = instance.getDistances(i, k1) + instance.getDistances(i1, l) + instance.getDistances(k, j) + instance.getDistances(j1, l1);
								long d15 = instance.getDistances(i, k1) + instance.getDistances(j, l) + instance.getDistances(i1, j1) + instance.getDistances(k, l1);
								long d16 = instance.getDistances(i, k1) + instance.getDistances(j, l) + instance.getDistances(i1, k) + instance.getDistances(j1, l1);
								long d17 = instance.getDistances(i, k1) + instance.getDistances(j1, l) + instance.getDistances(k, i1) + instance.getDistances(j, l1);
								long d18 = instance.getDistances(i, k1) + instance.getDistances(j1, l) + instance.getDistances(k, j) + instance.getDistances(i1, l1);
								long d19 = instance.getDistances(i, k1) + instance.getDistances(k, l) + instance.getDistances(i1, j1) + instance.getDistances(j, l1);
								
								long d20 = instance.getDistances(i, l) + instance.getDistances(i1, k1) + instance.getDistances(j, j1) + instance.getDistances(k, l1);
								long d21 = instance.getDistances(i, l) + instance.getDistances(i1, k1) + instance.getDistances(j, k) + instance.getDistances(j1, l1);
								long d22 = instance.getDistances(i, l) + instance.getDistances(j, k1) + instance.getDistances(i1, j1) + instance.getDistances(k, l1);
								long d23 = instance.getDistances(i, l) + instance.getDistances(j, k1) + instance.getDistances(i1, k) + instance.getDistances(j1, l1);
								long d24 = instance.getDistances(i, l) + instance.getDistances(j1, k1) + instance.getDistances(k, i1) + instance.getDistances(j, l1);
								long d25 = instance.getDistances(i, l) + instance.getDistances(j1, k1) + instance.getDistances(k, j) + instance.getDistances(i1, l1);
								
								List<Integer> cop = new ArrayList<Integer>(solution);
								
								long choix1 = Math.max(d0 - d1, d0 - d2);
								long choix2 = Math.max(d0 - d3, d0 - d4);
								long choix3 = Math.max(d0 - d5, d0 - d6);
								long choix4 = Math.max(d0 - d7, d0 - d8);
								long choix5 = Math.max(d0 - d9, d0 - d10);
								long choix6 = Math.max(d0 - d11, d0 - d12);
								long choix7 = Math.max(d0 - d13, d0 - d14);
								long choix8 = Math.max(d0 - d15, d0 - d16);
								long choix9 = Math.max(d0 - d17, d0 - d18);
								long choix10 = Math.max(d0 - d19, d0 - d20);
								long choix11 = Math.max(d0 - d21, d0 - d22);
								long choix12 = Math.max(d0 - d23, d0 - d24);
								
								long choix13 = Math.max(d0 - d25, 0);
								
								long choix14 = Math.max(choix1, choix2);
								long choix15 = Math.max(choix3, choix4);
								long choix16 = Math.max(choix5, choix6);
								long choix17 = Math.max(choix7, choix8);
								long choix18 = Math.max(choix9, choix10);
								long choix19 = Math.max(choix11, choix12);
								
								choix1= Math.max(choix14, choix15);
								choix2 = Math.max(choix16, choix17);
								choix3 = Math.max(choix18, choix19);
								
								choix10 = Math.max(choix1, choix2);
								choix11 = Math.max(choix3, choix13);
								
								long choix = Math.max(choix10, choix11);
								
								if (choix>0) {
									if (d0 - d1 == choix) {
										solution = reverse(solution, k1_index, l_index);
										solution = reverse(solution, i1_index, j_index);
										amelioration = true;
									}else if (d0 - d2 == choix) {
										solution = reverse(solution, k1_index, l_index);
										solution = reverse(solution, i1_index, j_index);
										solution = reverse(solution, j1_index, k_index);
										amelioration = true;
									}else if (d0 - d3 == choix) {
										solution = reverse(solution, i1_index, j_index);
										solution = inverser(solution, j1_index, k_index, k1_index, l_index);
										amelioration = true;
									}else if (d0 - d4 == choix) {
										solution = reverse(solution, j1_index, k_index);
										solution = reverse(solution, i1_index, j_index);
										solution = inverser(solution, j1_index, k_index, k1_index, l_index);
										amelioration = true;
									}else if (d0 - d5 == choix) {
										solution = reverse(solution, k1_index, l_index);
										solution = inverser(solution, i1_index, j_index, j1_index, k_index);
										amelioration = true;
									}else if (d0 - d6 == choix) {
										solution = reverse(solution, i1_index, j_index);
										solution = reverse(solution, k1_index, l_index);
										solution = inverser(solution, i1_index, j_index, j1_index, k_index);
										amelioration = true;
									}else if (d0 - d7 == choix) {
										solution = reverse(solution, k1_index, l_index);
										solution = reverse(solution, i1_index, j_index);
										solution = inverser(solution, i1_index, j_index, j1_index, l_index);
										amelioration = true;
									}else if (d0 - d8 == choix) {
										solution = reverse(solution, k1_index, l_index);
										solution = inverser(solution, i1_index, j_index, j1_index, l_index);
										amelioration = true;
									}else if (d0 - d9 == choix) {
										solution = reverse(solution, j1_index, k_index);
										solution = reverse(solution, k1_index, l_index);
										solution = inverser(solution, i1_index, j_index, j1_index, k_index);
										amelioration = true;
									}else if (d0 - d10 == choix) {
										solution = reverse(solution, j1_index, k_index);
										solution = inverser(solution, i1_index, j_index, j1_index, l_index);
										amelioration = true;
									}else if (d0 - d11 == choix) {
										solution = reverse(solution, j1_index, k_index);
										solution = reverse(solution, i1_index, j_index);
										solution = inverser(solution, i1_index, j_index, j1_index, l_index);
										amelioration = true;
									}else if (d0 - d12 == choix) {
										solution = reverse(solution, j1_index, k_index);
										solution = reverse(solution, k1_index, l_index);
										solution = inverser(solution, i1_index, j_index, j1_index, l_index);
										amelioration = true;
									}else if (d0 - d13 == choix) {
										solution = reverse(solution, j1_index, k_index);
										solution = reverse(solution, k1_index, l_index);
										solution = reverse(solution, i1_index, j_index);
										solution = inverser(solution, i1_index, j_index, j1_index, l_index);
										amelioration = true;
									}else if (d0 - d14 == choix) {
										solution = reverse(solution, j1_index, k_index);
										solution = inverser(solution, i1_index, k_index, k1_index, l_index);
										amelioration = true;
									}else if (d0 - d15 == choix) {
										solution = reverse(solution, i1_index, j_index);
										solution = inverser(solution, i1_index, k_index, k1_index, l_index);
										amelioration = true;
									}else if (d0 - d16 == choix) {
										solution = reverse(solution, i1_index, j_index);
										solution = reverse(solution, j1_index, k_index);
										solution = inverser(solution, i1_index, k_index, k1_index, l_index);
										amelioration = true;
									}else if (d0 - d17 == choix) {
										solution = inverser(solution, i1_index, j_index, k1_index, l_index);
										amelioration = true;
									}else if (d0 - d18 == choix) {
										solution = reverse(solution, i1_index, j_index);
										solution = inverser(solution, i1_index, j_index, k1_index, l_index);
										amelioration = true;
									}else if (d0 - d19 == choix) {
										solution = reverse(solution, j1_index, k_index);
										solution = inverser(solution, i1_index, j_index, k1_index, l_index);
										amelioration = true;
									}else if (d0 - d20 == choix) {
										solution = reverse(solution, k1_index, l_index);
										solution = inverser(solution, i1_index, k_index, k1_index, l_index);
										amelioration = true;
									}else if (d0 - d21 == choix) {
										solution = reverse(solution, k1_index, l_index);
										solution = reverse(solution, j1_index, k_index);
										solution = inverser(solution, i1_index, k_index, k1_index, l_index);
										amelioration = true;
									}else if (d0 - d22 == choix) {
										solution = reverse(solution, i1_index, j_index);
										solution = reverse(solution, k1_index, l_index);
										solution = inverser(solution, i1_index, k_index, k1_index, l_index);
										amelioration = true;
									}else if (d0 - d23 == choix) {
										solution = reverse(solution, i1_index, j_index);
										solution = reverse(solution, k1_index, l_index);
										solution = reverse(solution, j1_index, k_index);
										solution = inverser(solution, i1_index, k_index, k1_index, l_index);
										amelioration = true;
									}else if (d0 - d24 == choix) {
										solution = reverse(solution, k1_index, l_index);
										solution = inverser(solution, i1_index, j_index, k1_index, l_index);
										amelioration = true;
									}else if (d0 - d25 == choix) {
										solution = reverse(solution, k1_index, l_index);
										solution = reverse(solution, i1_index, j_index);
										solution = inverser(solution, i1_index, j_index, k1_index, l_index);
										amelioration = true;
									}else {
										
									}
								}
								//System.out.println(cost(this.m_instance, cop));
								spentTime = System.currentTimeMillis() - startTime;
								if (cost(this.m_instance, solution) > cost(this.m_instance, cop) || spentTime > (timeLimit * 1000 - 100)) {
									solution = cop;
									stop = true;
									break;
									
								}
							}
						
							
						}
					}
					
				}
			}
			
		}
		return solution;
	}
	
	/**
	 * Egal. Test si deux listes sont egales
	 *
	 * @param l1
	 * @param l2
	 * @return true si les deux listes sont de memes tailles et possedent les memes elements dans le meme ordre
	 */
	public boolean egal(List<Integer> l1, List<Integer> l2) {
		if (l1.size() != l2.size()) {
			return false;
		}else {
			for(int i = 0; i < l1.size(); i++) {
				if (l1.get(i) != l2.get(i)) {
					return false;
				}
			}
			return true;
		}
	}
	
	/**
	 * K opt. On améliore la solution en lui appliquant en cascade les algo 2-opt, 3-opt et 4-opt.
	 * Tant qu'il y a des améliorations, on fait tourner 2-opt puis 3-opt. Quand il n'y a plus d'amélioration
	 * on fait tourner 4-opt et on recommence l'étape précédente si 4-opt a amélioré la solution.
	 *
	 * @param instance the instance
	 * @param sol : la solution initiale que l'on va améliorer (sol peut être la solution de plusProcheVoisin ... )
	 * @param v : liste des voisins (v.get(i) renvoie les plus proches voisins de la ville i)
	 * @return la solution améliorer par l'algo K-opt
	 * @throws Exception the exception
	 */ 
	public List<Integer> k_opt(Instance instance, List<Integer> sol, Voisins v, long timeLimit) throws Exception{
		List<Integer> solution = two_opt(this.m_instance, sol, v, timeLimit);;
		List<Integer> solution1 = sol;
		List<Integer> solution2 = sol;
		boolean b = true;
		long startTime = System.currentTimeMillis();
		long spentTime = 0;
		//System.out.println(cost(instance, solution));
		while(!egal(solution2, solution) && spentTime < (timeLimit * 1000 - 100)) {
			while(!egal(solution, solution1) && spentTime < (timeLimit * 1000 - 100)) {
				if (b) {
					solution1 = new ArrayList<Integer>(solution);
					solution = three_opt(this.m_instance, solution, v, timeLimit * 1000 - spentTime);
					b = false;
				}else {
					solution1 = new ArrayList<Integer>(solution);
					solution = two_opt(this.m_instance, solution, v, timeLimit * 1000 - spentTime);
					b = true;
				}
			}
			solution2 = new ArrayList<Integer>(solution);
			solution = four_opt(this.m_instance, solution, v, timeLimit* 1000 - spentTime);
			spentTime = System.currentTimeMillis() - startTime;
		}
		
		return solution;
	}
	
	
	@Override
	public Solution solve(Solution s) throws Exception {
		
			
		List<Integer> solution = new ArrayList<Integer>();
		for (int i = 0; i < this.m_instance.getNbCities(); i++) {
			solution.add(s.getCity(i));
		}
		
		
		
		//int m = this.m_instance.getNbCities();
		int m = 40;
		long timeLimit = 10;
		
		
		//On récupère la liste des m plus proches voisins pour chaque node
		Voisins v = new Voisins(this.m_instance, m);
		
		for (int i = 0; i < this.m_instance.getNbCities(); i++) {
			for (int k = 0; k < this.m_instance.getNbCities(); k++) {
				if (i != k) {
					v.inserer(this.m_instance, i, k, m);
				}
				
			}
		}
		
		
		solution = k_opt(this.m_instance, solution, v, timeLimit);
		
		
		solution.add(solution.get(0));
		Solution sol = new Solution(this.m_instance);
		int k = 0;
		for(int i : solution) {
			sol.setCityPosition(i, k);
			k++;
		}
		return sol;
	}
}

