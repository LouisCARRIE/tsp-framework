package tsp.heuristic;

import tsp.Instance;
import tsp.Solution;

public class AlgorithmeGenetique extends AHeuristic {
	
	//JAutoDoc

	public AlgorithmeGenetique(Instance instance) throws Exception {
		super(instance, "AlgorithmeGenetique");
	}
	
	
	//fonctionne
	/*public static void echangerVilles(int[] tab, int i, int j) throws Exception  {
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
	
	
	
	public double evaluatePopulationenproportioncumulee(int[][] population) throws Exception {
		double[] performancesenproportion = evaluateIndividuenproportion(population);
		double performanceenproportioncumulee = 0;
		for (int i=0 ; i<population.length ; i++) {
			//performances[i] = evaluateIndividu(population[i])/performancecumulee;
			performanceenproportioncumulee = performanceenproportioncumulee + performancesenproportion[i]; 
		}
		return performanceenproportioncumulee;
	}
	
	
	
	//(a revoir (si on genere une pop de 100.000 ne fonctionne pas ?))
	public int[][] selectionIndividus(int[][] population) throws Exception {
		//processus de la "roulette wheel selection"
		//performance : la performance d'un individu
		double[] performances = evaluateIndividuenproportion(population);
		int[][] nouvellepopulation = new int[population.length][1];
		
		System.out.println("somme en proportion " + evaluatePopulationenproportioncumulee(population));
		for (int j=0 ; j<population.length ; j++) {
			double r = Math.random()*evaluatePopulationenproportioncumulee(population); //r entre 0 inclus et 1 exclus --> il faut les 2 inclus !!
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
	
	
	public static int[][] crossingOver(int[][] population) throws Exception {
		
		double probadhybridation = 0.66;
		int n = population[0].length;
		for (int i=0 ; i<population.length-1 ; i+=2) {
			
			double r = Math.random(); //r entre 0 inclus et 1 exclus --> il faut les 2 inclus !!
			
			if (r<=probadhybridation) {
				
				for (int j=1 ; j<=5 ; j++) {
					int a = 1 + (int) (Math.random()*(n-3));
					echangerVilles(population[i], a, a+1);
					int b = 1 + (int) (Math.random()*(n-3));
					echangerVilles(population[i+1], b, b+1);
				}
					
			}
			
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
	}*/
	
	
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
		population = genererPopulation(50);
		
		for (int i=0 ; i<10000 ; i++) {
			int [][] selectionSousEnsemblePopulation = selectionSousEnsemblePopulation(population);
			int [][] populationEnfant = crossOver(selectionSousEnsemblePopulation);
			mutationPopulationEnfant(populationEnfant);
			nouvellePopulation(population, populationEnfant);
			
		}
		
		int[] meilleurIndividu = meilleurIndividu(population);
		Solution s = new Solution(m_instance);
		for (int i = 0; i<meilleurIndividu.length ; i++) {
			s.setCityPosition(meilleurIndividu[i], i);
		}
		m_solution = s;
		
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
	
	
	public int[][] genererPopulation(int n) throws Exception {
		//n = taille de la population ; n>=log(l) avec l la taille d'un invididu
		//attention : nombre d'individus pair ?
		int[][] population = new int[n][1];
		for (int i=0 ; i<n ; i++) {
			population[i] = genererIndividu();
		}
		return population;
	}

	
	public int[][] selectionSousEnsemblePopulation(int[][] population) throws Exception {
		//selection par tournoi
		int m = 40; //m pair !
		int[][] selectionSousEnsemblePopulation = new int[m][1];
		for (int i=0 ; i<m ; i++) {
			int[] competiteur1 = individuAuHasard(population);
			int[] competiteur2 = individuAuHasard(population);
			if (evaluateIndividu(competiteur1)<evaluateIndividu(competiteur2)) {
				selectionSousEnsemblePopulation[i] = competiteur1;
			} else {
				selectionSousEnsemblePopulation[i] = competiteur2;
			}
		}
		return selectionSousEnsemblePopulation;
		
	}
	
	
	public static int[][] crossOver(int[][] selectionSousEnsemblePopulation) throws Exception {
		int[][] populationenfant = new int[selectionSousEnsemblePopulation.length][1];
		double probadhybridation = 0.66;
		int n = selectionSousEnsemblePopulation[0].length;
		for (int i=0 ; i<selectionSousEnsemblePopulation.length-1 ; i+=2) {
			
			double r = Math.random(); //r entre 0 inclus et 1 exclus --> il faut les 2 inclus !!	
			if (r<=probadhybridation) {
				int c1 = 1 + (int) (Math.random()*(n-2));
				populationenfant[i] = annexe(selectionSousEnsemblePopulation[i], selectionSousEnsemblePopulation[i+1], c1);
				populationenfant[i+1] = annexe(selectionSousEnsemblePopulation[i+1], selectionSousEnsemblePopulation[i], c1);		
			} else {
				populationenfant[i] = copyOf(selectionSousEnsemblePopulation[i]);
				populationenfant[i+1] = copyOf(selectionSousEnsemblePopulation[i+1]);
			}
			
			
		}

		return populationenfant;
	}

	
	
	public static void mutationIndividu(int[] individu) throws Exception {
		//methode "2 echanges consecutifs"
		int i = 1 + (int) (Math.random()*(individu.length-3));
		int j = i + 1;
		echangerVilles(individu, i, j);
	}
	
	
	public static void mutationPopulationEnfant(int[][] populationenfant) throws Exception {
		for (int i=0 ; i<populationenfant.length ; i++) {
			double r = Math.random();
			if (r<0.2) {
				mutationIndividu(populationenfant[i]);
			}
		}
	}
	
	
	public void nouvellePopulation(int[][] population, int[][] populationenfant) throws Exception {
		for (int i=0 ; i<populationenfant.length ; i++) {
			int indicePireIndividu = indicePireIndividu(population);
			population[indicePireIndividu] = populationenfant[i];
		}
	}
	
	
	public int[] meilleurIndividu(int[][] population) throws Exception {
		int indicemeilleurindiv = 0;
		for(int i=1 ; i<population.length ; i++) {
			if (evaluateIndividu(population[i])<evaluateIndividu(population[indicemeilleurindiv])) {
				indicemeilleurindiv = i;	
			}
		}
		return population[indicemeilleurindiv];
	}
	
	
	public int indicePireIndividu(int[][] population) throws Exception {
		int indicePireIndividu= 0;
		for(int i=1 ; i<population.length ; i++) {
			if (evaluateIndividu(population[i])>evaluateIndividu(population[indicePireIndividu])) {
				indicePireIndividu = i;	
			}
		}
		return indicePireIndividu;
	}
	
	
	public static void echangerVilles(int[] tab, int i, int j) throws Exception  {
		//i, j sont les indices qu'on veut changer
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
	
	

	public static int[] individuAuHasard(int[][] population) throws Exception {
		//selection par tournoi
		int r = (int) (Math.random()*population.length);
		return population[r];
		
	}
	
	
	public static int[] annexe(int[] individu1, int[] individu2, int c) throws Exception {
		if ((individu1.length!=individu2.length)) {
			throw new Exception("les tailles des parents ne sont pas les memes");
		} else {
			if (c<=0||c>=individu1.length-1) {
				throw new Exception("l'indice demande de la cassure n'est pas possible");
			} else {
				int[] copyPartielleOfIndividus = new int[individu1.length];
				for (int i=1 ; i<=c ; i++) {
					copyPartielleOfIndividus[i] = individu1[i];
				}
				int k = c + 1;
				for (int j=1 ; j<individu2.length-1 ; j++) {
					if (!estPresent(copyPartielleOfIndividus, individu2[j])) {
						copyPartielleOfIndividus[k] = individu2[j];
						k = k + 1;
					}
				}
				return copyPartielleOfIndividus;
			}
		}

	}
	
	public static boolean estPresent(int[] individu, int entier) throws Exception {
		int i = 0;
		while (i<individu.length&&individu[i]!=entier) {
			i = i + 1;
		}
		return i<individu.length;

	}
	

	

	
	

}
