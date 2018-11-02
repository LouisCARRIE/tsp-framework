package tsp.metaheuristic;

import java.util.ArrayList;
import java.util.List;

import tsp.Instance;
import tsp.Solution;

import tsp.heuristic.*;

public class TwoOpt extends AMetaheuristic {

	public TwoOpt(Instance instance) throws Exception {
		super(instance, "2_Opt");
		// TODO Auto-generated constructor stub
	}
	
	public int[] genererIndividu() throws Exception {
		//génère aléatoirement un chemin 
		int[] individu = new int[this.m_instance.getNbCities() + 1];
		for (int i = 1 ; i<this.m_instance.getNbCities() ; i++) {
			individu[i] = i;
		}
		for (int j = 1 ; j<this.m_instance.getNbCities() ; j++) {
			int k = 1 + (int) (Math.random()*(this.m_instance.getNbCities()-1));
			echangerVilles(individu, j, k);
		}
		return individu;
		
	}
	
	public List<int[]> genererVoisinagePartiel(int[] individu) throws Exception {
		//on choisit un voisinage par inversion de couples d'indices
		List<int[]> voisinage = new ArrayList<int[]>();
		for (int i=0 ; i<10 ; i++) {
			int [] voisin = TwoOpt(individu);
			voisinage.add(voisin);
		}
		return voisinage;
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
	
	
	public int[] TwoOpt(int[] individu) throws Exception {
		//methode "2 opt"
		int c1 = 1 + (int) (Math.random()*(individu.length-2));
		int c2;
		do {
			c2 = 1 + (int) (Math.random()*(individu.length-2));
		} while ((c1==c2)||(c1==(c2+1))||(c2==(c1+1)));
		return copyOrdreDifferent(individu, c1, c2);
	}
	
	public static int[] copyOrdreDifferent(int[] individu, int c1, int c2) throws Exception {
		if ((c1<=0)||(c1>=(individu.length-1))||(c2<=0)||(c2>=(individu.length-1))||(c1==c2)||(c1==(c2+1))||(c2==(c1+1))) {
			throw new Exception("les indices sont non valides");
		} else {
			int min = Math.min(c1, c2);
			int max = Math.max(c1, c2);
			int[] copyOrdreDifferent = new int[individu.length];
			for (int i=1 ; i<=min ; i++) {
				copyOrdreDifferent[i] = individu[i];
			}
			for (int i=max ; i<individu.length-1 ; i++) {
				copyOrdreDifferent[i] = individu[i];
			}
			int j = 1;
			for (int i=min + 1 ; i<=(min+1 + max-1)/2 ; i++) {
				copyOrdreDifferent[i] = individu[max-j];
				copyOrdreDifferent[max-j] = individu[i];
				j++;
				
			}
			
			return copyOrdreDifferent;
		}
	}
	
	public static int[] copyOf(int[] individu) {
		int[] copyOfIndividu = new int[individu.length];
		for (int i=0 ; i<individu.length ; i++) {
			copyOfIndividu[i] = individu[i];
		}
		return copyOfIndividu;
	}
	
	public int evaluateIndividu(int[] individu) throws Exception {
	//recopie du code de evaluate() de Solution car evaluate() ne s'applique qu'à des objets
	//de type solution ; renvoie int ou double ???
		int m_objectiveValue = 0;
		for (int i = 0; i < this.m_instance.getNbCities(); i++) {
			m_objectiveValue += m_instance.getDistances(individu[i], individu[i + 1]);
		}
		return m_objectiveValue;
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
	
	
	public int[] localSearch2_Opt(int[] individu) throws Exception {
		double delta = Double.MAX_VALUE;
		//for (int i = 0 ; i<10 ; i++) {
		while (delta>0) {
			int[] voisin = meilleureSolutionVoisinage(genererVoisinagePartiel(individu));
			delta = evaluateIndividu(individu) - evaluateIndividu(voisin);
			if (delta>0) {
				individu = voisin;
			}
		}
		return individu;
		
	}
	

	@Override
	public Solution solve(Solution sol) throws Exception {
		int[] individu = genererIndividu();
		individu = localSearch2_Opt(individu);
		for (int i = 0; i<individu.length ; i++) {
			sol.setCityPosition(individu[i], i);
		}
		return sol;
	}

}
