package tsp.heuristic;

import tsp.Instance;

public class AlgorithmeGlouton extends AHeuristic {

	public AlgorithmeGlouton(Instance instance) throws Exception {
		super(instance, "Algorithme Glouton");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void solve() throws Exception {
		for (int i=0 ; i<m_instance.getNbCities() ; i++) {
			m_solution.setCityPosition(i, i);
		}
		// TODO Auto-generated method stub
		
	}

}
