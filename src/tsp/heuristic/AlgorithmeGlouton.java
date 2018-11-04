package tsp.heuristic;

import tsp.Instance;

/**
 * The Class AlgorithmeGlouton.
 */

public class AlgorithmeGlouton extends AHeuristic {

	/**
	 * Instantiates a new algorithme glouton.
	 *
	 * @param instance the instance
	 * @throws Exception the exception
	 */
	public AlgorithmeGlouton(Instance instance) throws Exception {
		super(instance, "Algorithme Glouton");
	}
	

	
	/** Mise en application de l'heuristique pour construire m_solution 
	 *  Exemple simpliste ou le sommet i est insere en position i dans la tournee.
	 */
	public void solve() throws Exception {
		for (int i=0 ; i<m_instance.getNbCities() ; i++) {
			m_solution.setCityPosition(i, i);
		}
		
	}

}
