package tsp.heuristic;

import tsp.Instance;
import tsp.Solution;

public class AlgorithmeGenetique extends AHeuristic {

	public AlgorithmeGenetique(Instance instance) throws Exception {
		super(instance, "AlgorithmeGenetique");
		// TODO Auto-generated constructor stub
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
			performances[i] = evaluateIndividu(population[i])/performancecumulee; 
		}
		return performances;
	}
	
	
	//fonctionne
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
			nouvellepopulation[j] = population[indiceIndividuSelectionne];
			
		}
		return nouvellepopulation;
		
	}
	
	
	//a revoir
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
			
	@Override
	public void solve() throws Exception {
		//n = taille de la population ; n>=log(l) avec l la taille d'un invididu
		int n = 10;
		if (n%2!=0) {
			throw new Exception("la taille de la population doit etre un nombre pair");
		} else {
			int[][] p = genererPopulation(n);
			int[] g = genererIndividu();
			
			for (int i=0 ; i<=this.m_instance.getNbCities() ; i++) {
				this.m_solution.setCityPosition(g[i], i);;
			}
		}
		// TODO Auto-generated method stub
		
	}
	
	

}
