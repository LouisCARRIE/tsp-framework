package tsp.heuristic;

import tsp.Instance;
import tsp.Solution;

public class AlgorithmeGenetique extends AHeuristic {

	public AlgorithmeGenetique(Instance instance) throws Exception {
		super(instance, "AlgorithmeGenetique");
	}
	
	
	//fonctionne
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
	
	
	//fonctionne
	public static int trouverVille(int[] tab, int i) throws Exception  {
		//i est la ville qu'on veut changer
		if (i<=0||i>=tab.length-1) {
			throw new Exception("indice(s) non valide(s)");
		} else {
			int k = 0;
			while (tab[k]!=i) {
				k = k + 1;
			}
			return k;
		}	
	}
	
	
	//fonctionne
	public static void echangerVilles2(int[] tab, int i, int j) throws Exception  {
		//i, j sont les villes qu'on veut changer
		if (i<=0||i>=tab.length-1) {
			throw new Exception("indice(s) non valide(s)");
		} else if (j<=0||j>=tab.length-1) {
			throw new Exception("indice(s) non valide(s)");
		} else {
			int k = trouverVille(tab, i);
			int n = trouverVille(tab, j);
			tab[k] = j;
			tab[n] = i;
		}
	}
	
	
	//fonctionne
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
	
	
	//fonctionne
	public int[][] genererPopulation(int n) throws Exception {
		//n = taille de la population ; n>=log(l) avec l la taille d'un invididu
		//attention : nombre d'individus pair ?
		int[][] population = new int[n][1];
		for (int i=0 ; i<n ; i++) {
			population[i] = genererIndividu();
		}
		return population;
	}
	
	
	//fonctionne
	public int evaluateIndividu(int[] individu) throws Exception {
	//recopie du code de evaluate() de Solution car evaluate() ne s'applique qu'à des objets
	//de type solution ; renvoie int ou double ???
		int m_objectiveValue = 0;
		for (int i = 0; i < this.m_instance.getNbCities(); i++) {
			m_objectiveValue += m_instance.getDistances(individu[i], individu[i + 1]);
		}
		return m_objectiveValue;
	}
	
	
	//fonctionne
	public double evaluatePopulation(int[][] population) throws Exception {
		int performancecumulee = 0;
		for (int k=0 ; k<population.length ; k++) {
			performancecumulee = performancecumulee + evaluateIndividu(population[k]); 
		}
		return performancecumulee;
	}
	
	
	//fonctionne
	public double[] evaluateIndividuenproportion(int[][] population) throws Exception {
		double[] performances = new double[population.length];
		double performancecumulee = evaluatePopulation(population);
		for (int i=0 ; i<population.length ; i++) {
			//performances[i] = evaluateIndividu(population[i])/performancecumulee;
			performances[i] = performancecumulee/evaluateIndividu(population[i]);
		}
		return performances;
	}
	
	
	//(a revoir (si on genere une pop de 100.000 ne fonctionne pas ?))
	public int[][] selectionIndividus(int[][] population) throws Exception {
		//processus de la "roulette wheel selection"
		//performance : la performance d'un individu
		
		double[] performances = evaluateIndividuenproportion(population);
		int[][] nouvellepopulation = new int[population.length][1];
		
		
		for (int j=0 ; j<population.length ; j++) {
			double r = Math.random(); //r entre 0 inclus et 1 exclus --> il faut les 2 inclus !!
			int indiceIndividuSelectionne = 0;
			double t = performances[0];
			while (t<r) {
				indiceIndividuSelectionne = indiceIndividuSelectionne + 1;
				t = t + performances[indiceIndividuSelectionne];
			}
			//il ne faut pas faire nouvellepopulation[j] = population[indiceIndividuSelectionne];
			//car on peut avoir dans ce cas nouvellepopulation[j] == nouvellepopulation[j'] avec j!=j'
			//alors qu'on voudrait avoir nouvellepopulation[j] equals nouvellepopulation[j'] !!
			nouvellepopulation[j] = copyOf(population[indiceIndividuSelectionne]);
			
		}
		return nouvellepopulation;
		
	}
	
	
	//fonctionne
	public static int[] copyOf(int[] individu) {
		int[] copyOfIndividu = new int[individu.length];
		for (int i=0 ; i<individu.length ; i++) {
			copyOfIndividu[i] = individu[i];
		}
		return copyOfIndividu;
	}
	
	
	//fonctionne
	public static boolean test1(int[] individu, int[] copyOfIndividu) throws Exception {
		return individu==copyOfIndividu;
	}
	
	
	//fonctionne
	public static boolean test2(int[] individu, int[] copyOfIndividu) throws Exception {
		int j = 0;
		if (individu.length==copyOfIndividu.length) {
			while (j<individu.length&&individu[j]==copyOfIndividu[j]) {
				j = j + 1;
			}
			return j==individu.length;
		} else {
			return false;
		}
	}
	
	
	//a revoir ; static ou pas ?
	public static int[][] hybridation(int[][] population) throws Exception {
		double probahybridation = 0.66;
		int n = population[0].length;
		for (int i=0 ; i<population.length-1 ; i+=2) {
			System.out.println("i vaut " + i);
			int[] parent1 = population[i];
			int[] parent2 = population[i+1];
			int[] enfant1;
			int[] enfant2;
			double r = Math.random(); //r entre 0 inclus et 1 exclus --> il faut les 2 inclus !!
			System.out.println("le nombre tire au hasard vaut " + r);
			if (r<=probahybridation) {
				enfant1 = parent1;
				enfant2 = parent2;
				int a = 1 + (int) (Math.random()*(n-2));
				int b = 1 + (int) (Math.random()*(n-2));
				int tranchemin = Math.min(a, b);
				System.out.println("tranchemin vaut " + tranchemin);
				int tranchemax = Math.max(a, b);
				System.out.println("tranchemax vaut " + tranchemax);
				for (int j=tranchemin ; j<=tranchemax ; j++) {
					int v1 = trouverVille(enfant1, parent1[j]);
					if (!(v1>=tranchemin&&v1<j)) {
						echangerVilles2(enfant1, parent1[j], parent2[j]);
					}
					int v2 = trouverVille(enfant2, parent2[j]);
					if (!(v2>=tranchemin&&v2<j)) {
						echangerVilles2(enfant2, parent2[j], parent1[j]);
					}
					
				}
			} else {
				enfant1 = parent1;
				enfant2 = parent2;	
			}
			population[i] = enfant1;
			population[i+1] = enfant2;
			
		}
		return population;
		
	}
	
	
	//fonctionne
	public static void mutationIndividu(int[] individu) throws Exception {
		int i = 1 + (int) (Math.random()*(individu.length-3));
		int j = i + 1;
		echangerVilles(individu, i, j);
	}
	
	
	//fonctionne 
	public static void mutationPopulation(int[][] population) throws Exception {
		for(int i=0 ; i<population.length ; i++) {
			double r = Math.random();
			if (r<0.2) {
				mutationIndividu(population[i]);
			}
		}
	}
	
	
	//fonctionne
	public int[] meilleurIndividu(int[][] population) throws Exception {
		int indicemeilleurindiv = 0;
		for(int i=1 ; i<population.length ; i++) {
			if (evaluateIndividu(population[i])<evaluateIndividu(population[indicemeilleurindiv])) {
				indicemeilleurindiv = i;	
			}
		}
		return population[indicemeilleurindiv];
	}
	
	
	//a revoir
	@Override
	public void solve() throws Exception {
		/*//n = taille de la population ; n>=log(l) avec l la taille d'un invididu
		int n = 10;
		if (n%2!=0) {
			throw new Exception("la taille de la population doit etre un nombre pair");
		} else {
			int[][] p = genererPopulation(n);
			int[] g = genererIndividu();
			
			for (int i=0 ; i<=this.m_instance.getNbCities() ; i++) {
				this.m_solution.setCityPosition(g[i], i);;
			}
		}*/
		
		
		int[][] population;
		population = genererPopulation(20);
		population = selectionIndividus(population);

		//population = hybridation(population);
		//System.out.println("la population hybridee est " + toString2(population));
		
		mutationPopulation(population);
		int[] meilleurIndividu = meilleurIndividu(population);
		Solution s = new Solution(m_instance);
		for (int i = 0; i<meilleurIndividu.length ; i++) {
			s.setCityPosition(meilleurIndividu[i], i);
		}
		m_solution = s;
		
	}
	
	

}
