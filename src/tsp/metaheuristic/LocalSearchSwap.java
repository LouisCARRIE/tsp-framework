package tsp.metaheuristic;

import java.util.ArrayList;
import java.util.List;

import tsp.Instance;
import tsp.Solution;

public class LocalSearchSwap extends AMetaheuristic {


	public LocalSearchSwap(Instance instance) throws Exception {
		super(instance, "Algorithme LocalSearchSwap");
		
	}
	
	
	public static void echangerVilles(int[] tab, int i, int j) throws Exception  {
		// i, j les positions des villes visitees tab[i] est la ieme ville visitee
		if (i<=0||i>=tab.length-1) {
			throw new Exception("indice(s) non valide(s)");
		} else if (j<=0||j>=tab.length-1) {
			throw new Exception("indice(s) non valide(s)");
		} else {
			int k = tab[i];
			tab[i] = tab[j];
			tab[j] = k;
		}
	}
	
	
	public int[] genererIndividu() throws Exception {
		int[] individu = new int[this.m_instance.getNbCities() + 1];
		for (int i = 1 ; i<this.m_instance.getNbCities() ; i++) {
			individu[i] = i;
		}
		return individu;
		
	}
	
	
	public int[] copyOf(int[] individu) {
		int[] copyOfIndividu = new int[individu.length];
		for (int i=0 ; i<individu.length ; i++) {
			copyOfIndividu[i] = individu[i];
		}
		return copyOfIndividu;
	}
	
	
	public List<int[]> genererVoisinage(int[] individu) throws Exception {
		//on choisit un voisinage par inversion de couples d'indices
		List<int[]> voisinage = new ArrayList<int[]>();
		for (int i=1 ; i<individu.length-1 ; i++) {
			for (int j=i+1 ; j<individu.length-1 ; j++) {
					int[] voisin = copyOf(individu);
					echangerVilles(voisin, i, j);
					voisinage.add(voisin);
			}
		}
		return voisinage;
	}
	
	
	public double evaluateIndividu(int[] individu) throws Exception {
		int m_objectiveValue = 0;
		for (int i = 0; i < this.m_instance.getNbCities(); i++) {
			m_objectiveValue += m_instance.getDistances(individu[i], individu[i + 1]);
		}
		return m_objectiveValue;
	}
	
	
	public int[] meilleureSolutionVoisinage(List<int[]> voisinage) throws Exception {
		int[] meilleureSolution = voisinage.get(0);
		for (int[] element : voisinage) {
			if (evaluateIndividu(element)<evaluateIndividu(meilleureSolution)) {
				meilleureSolution = element;
			}
		}
		return meilleureSolution;
	}
	
	
	public int[] localSearchSwap(int[] individu) throws Exception {
		double delta = Double.MAX_VALUE;
		//for (int i = 0 ; i<10 ; i++) {
		while (delta>0) {
			int[] voisin = meilleureSolutionVoisinage(genererVoisinage(individu));
			delta = evaluateIndividu(individu) - evaluateIndividu(voisin);
			if (delta>0) {
				individu = voisin;
			}
		}
		return individu;
		
	}
	

	@Override
	//méthode trop longue pour les problemes contenant plus de 320 villes !
	//dans la méthode localSearchSwap remplacer la boucle while par un for ?
	public Solution solve(Solution sol) throws Exception {
		int[] individu = genererIndividu();
		individu = localSearchSwap(individu);
		for (int i = 0; i<individu.length ; i++) {
			sol.setCityPosition(individu[i], i);
		}
		return sol;
	}
	
	

}
