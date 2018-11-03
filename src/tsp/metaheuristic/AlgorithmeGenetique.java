package tsp.metaheuristic;

import tsp.Instance;
import tsp.Solution;


/**
 * The Class AlgorithmeGenetique.
 */
public class AlgorithmeGenetique extends AMetaheuristic {
	
	//JAutoDoc

	/**
	 * Instantiates a new algorithme genetique.
	 *
	 * @param instance the instance
	 * @throws Exception the exception
	 */
	public AlgorithmeGenetique(Instance instance) throws Exception {
		super(instance, "AlgorithmeGenetique");
	}
	
	
	/**
	 * Generer individu.
	 *
	 * @return the int[]
	 * @throws Exception the exception
	 */
	//partir de indiv = plusProcheVoisin puis faire les modifications
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
	
	
	/**
	 * Generer individu semi aleatoirement.
	 *
	 * @return the int[]
	 * @throws Exception the exception
	 */
	public int[] genererIndividuSemiAleatoirement() throws Exception {
		int[] individu = new int[this.m_instance.getNbCities() + 1];
		for (int i = 1 ; i<this.m_instance.getNbCities() ; i++) {
			individu[i] = i;
		}
		for (int i = 0 ; i<10 ; i++) {
			int c1 = 1 + (int) (Math.random()*(individu.length-2));
			int c2;
			do {
				c2 = 1 + (int) (Math.random()*(individu.length-2));
			} while ((c1==c2)||(c1==(c2+1))||(c2==(c1+1)));
			individu = copyOrdreDifferent(individu, c1, c2);
			
		}
		return individu;
		
	}
	
	
	/**
	 * Generer population.
	 *
	 * @param n the n
	 * @return the int[][]
	 * @throws Exception the exception
	 */
	public int[][] genererPopulation(int n) throws Exception {
		//n = taille de la population ; n>=log(l) avec l la taille d'un invididu
		//attention : nombre d'individus pair ?
		int[][] population = new int[n][1];
		for (int i=0 ; i<n ; i++) {
			population[i] = genererIndividu();
		}
		return population;
	}
	
	
	/**
	 * Generer population semi aleatoirement.
	 *
	 * @param n the n
	 * @return the int[][]
	 * @throws Exception the exception
	 */
	public int[][] genererPopulationSemiAleatoirement(int n) throws Exception {
		//n = taille de la population ; n>=log(l) avec l la taille d'un invididu
		//attention : nombre d'individus pair ?
		int[][] population = new int[n][1];
		for (int i=0 ; i<n ; i++) {
			population[i] = genererIndividuSemiAleatoirement();
		}
		return population;
	}
	
	
	/**
	 * Selection sous ensemble population.
	 *
	 * @param population the population
	 * @return the int[][]
	 * @throws Exception the exception
	 */
	public int[][] selectionSousEnsemblePopulation(int[][] population) throws Exception {
		//selection par tournoi
		int m = 10; //m pair !
		//int m = 200;
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
	
	
	/**
	 * Cross over.
	 *
	 * @param selectionSousEnsemblePopulation the selection sous ensemble population
	 * @return the int[][]
	 * @throws Exception the exception
	 */
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
	
	
	public int[][] collisionCrossOver(int[][] selectionSousEnsemblePopulation) throws Exception {
		int[][] populationenfant = new int[selectionSousEnsemblePopulation.length][1];
		double probadhybridation = 0.66;
		for (int i=0 ; i<selectionSousEnsemblePopulation.length-1 ; i+=2) {
			
			double r = Math.random(); //r entre 0 inclus et 1 exclus --> il faut les 2 inclus !!	
			if (r<=probadhybridation) {
				populationenfant[i] = collisionEnfant1(selectionSousEnsemblePopulation[i], selectionSousEnsemblePopulation[i+1]);
				populationenfant[i+1] = collisionEnfant2(selectionSousEnsemblePopulation[i+1], selectionSousEnsemblePopulation[i]);		
			} else {
				populationenfant[i] = copyOf(selectionSousEnsemblePopulation[i]);
				populationenfant[i+1] = copyOf(selectionSousEnsemblePopulation[i+1]);
			}
			
			
		}

		return populationenfant;
	}
	
	
	/**
	 * Mutation individu.
	 *
	 * @param individu the individu
	 * @throws Exception the exception
	 */
	public static void mutationIndividu(int[] individu) throws Exception {
		//methode "2 echanges consecutifs"
		int i = 1 + (int) (Math.random()*(individu.length-3));
		int j = i + 1;
		echangerVilles(individu, i, j);
	}
	
	
	/**
	 * Mutation individu 2 opt.
	 *
	 * @param individu the individu
	 * @return the int[]
	 * @throws Exception the exception
	 */
	public static int[] mutationIndividu2opt(int[] individu) throws Exception {
		//methode "2 opt"
		int c1 = 1 + (int) (Math.random()*(individu.length-2));
		int c2;
		do {
			c2 = 1 + (int) (Math.random()*(individu.length-2));
		} while ((c1==c2)||(c1==(c2+1))||(c2==(c1+1)));
		return copyOrdreDifferent(individu, c1, c2);
	}
	
	
	/**
	 * Mutation population enfant.
	 *
	 * @param populationEnfant the population enfant
	 * @throws Exception the exception
	 */
	public static void mutationPopulationEnfant(int[][] populationEnfant) throws Exception {
		for (int i=0 ; i<populationEnfant.length ; i++) {
			double r = Math.random();
			if (r<1) {
			//if (r<0.2) {
				mutationIndividu(populationEnfant[i]);
			}
		}
	}
	
	
	/**
	 * Mutation population enfant 2 opt.
	 *
	 * @param populationEnfant the population enfant
	 * @throws Exception the exception
	 */
	public static void mutationPopulationEnfant2opt(int[][] populationEnfant) throws Exception {
		for(int i=0 ; i<populationEnfant.length ; i++) {
			double r = Math.random();
			//if (r<1) {
			if (r<0.2) {
				populationEnfant[i] = mutationIndividu2opt(populationEnfant[i]);
			}
		}
	}
	
	
	/**
	 * Nouvelle population.
	 *
	 * @param population the population
	 * @param populationenfant the populationenfant
	 * @throws Exception the exception
	 */
	public void nouvellePopulation(int[][] population, int[][] populationenfant) throws Exception {
		for (int i=0 ; i<populationenfant.length ; i++) {
			int indicePireIndividu = indicePireIndividu(population);
			population[indicePireIndividu] = populationenfant[i];
		}
	}
	
	
	/**
	 * Meilleur individu.
	 *
	 * @param population the population
	 * @return the int[]
	 * @throws Exception the exception
	 */
	public int[] meilleurIndividu(int[][] population) throws Exception {
		int indicemeilleurindiv = 0;
		for(int i=1 ; i<population.length ; i++) {
			if (evaluateIndividu(population[i])<evaluateIndividu(population[indicemeilleurindiv])) {
				indicemeilleurindiv = i;	
			}
		}
		return population[indicemeilleurindiv];
	}
	
	
	/**
	 * Indice pire individu.
	 *
	 * @param population the population
	 * @return the int
	 * @throws Exception the exception
	 */
	public int indicePireIndividu(int[][] population) throws Exception {
		int indicePireIndividu= 0;
		for(int i=1 ; i<population.length ; i++) {
			if (evaluateIndividu(population[i])>evaluateIndividu(population[indicePireIndividu])) {
				indicePireIndividu = i;	
			}
		}
		return indicePireIndividu;
	}
	
	
	/**
	 * Echanger villes.
	 *
	 * @param tab the tab
	 * @param i the i
	 * @param j the j
	 * @throws Exception the exception
	 */
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
	
	
	/**
	 * Trouver ville.
	 *
	 * @param tab the tab
	 * @param i the i
	 * @return the int
	 * @throws Exception the exception
	 */
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
	
	
	/**
	 * Echanger villes 2.
	 *
	 * @param tab the tab
	 * @param i the i
	 * @param j the j
	 * @throws Exception the exception
	 */
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
	
		
	/**
	 * Copy of.
	 *
	 * @param individu the individu
	 * @return the int[]
	 */
	public static int[] copyOf(int[] individu) {
		int[] copyOfIndividu = new int[individu.length];
		for (int i=0 ; i<individu.length ; i++) {
			copyOfIndividu[i] = individu[i];
		}
		return copyOfIndividu;
	}
	
	
	/**
	 * Evaluate individu.
	 *
	 * @param individu the individu
	 * @return the int
	 * @throws Exception the exception
	 */
	public int evaluateIndividu(int[] individu) throws Exception {
	//recopie du code de evaluate() de Solution car evaluate() ne s'applique qu'à des objets
	//de type solution ; renvoie int ou double ???
		int m_objectiveValue = 0;
		for (int i = 0; i < this.m_instance.getNbCities(); i++) {
			m_objectiveValue += m_instance.getDistances(individu[i], individu[i + 1]);
		}
		return m_objectiveValue;
	}
	
	

	/**
	 * Individu au hasard.
	 *
	 * @param population the population
	 * @return the int[]
	 * @throws Exception the exception
	 */
	public static int[] individuAuHasard(int[][] population) throws Exception {
		//selection par tournoi
		int r = (int) (Math.random()*population.length);
		return population[r];
		
	}
	
	
	/**
	 * Annexe.
	 *
	 * @param individu1 the individu 1
	 * @param individu2 the individu 2
	 * @param c the c
	 * @return the int[]
	 * @throws Exception the exception
	 */
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
	
	/**
	 * Est present.
	 *
	 * @param individu the individu
	 * @param entier the entier
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public static boolean estPresent(int[] individu, int entier) throws Exception {
		int i = 0;
		while (i<individu.length&&individu[i]!=entier) {
			i = i + 1;
		}
		return i<individu.length;

	}
	
	/**
	 * Copy ordre different.
	 *
	 * @param individu the individu
	 * @param c1 the c 1
	 * @param c2 the c 2
	 * @return the int[]
	 * @throws Exception the exception
	 */
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
	
	
	public long[] tableauMassesChromosomesIndividu(int[] individu) throws Exception {
		long[] tableauMassesChromosomesIndividu = new long[individu.length];
		for (int i=1 ; i<individu.length-1 ; i++) {
			tableauMassesChromosomesIndividu[i] = m_instance.getDistances(individu[i-1], individu[i]) + m_instance.getDistances(individu[i], individu[i+1]);
		}
		return tableauMassesChromosomesIndividu;
	}
	
	
	public double velociteChromosomesIndividu(int[] individu) throws Exception {
		return evaluateIndividu(individu);
	}
	
	
	public int[] collisionEnfant1(int[] individu1, int[] individu2) throws Exception {
		int[] enfant1 = new int[individu1.length];
		for (int i=1 ; i<enfant1.length-1 ; i++) {
			double v1 = (tableauMassesChromosomesIndividu(individu1)[i] - tableauMassesChromosomesIndividu(individu2)[i])*velociteChromosomesIndividu(individu1)
					   /(tableauMassesChromosomesIndividu(individu1)[i] + tableauMassesChromosomesIndividu(individu2)[i])
					 + 2*tableauMassesChromosomesIndividu(individu2)[i]*velociteChromosomesIndividu(individu2)
					   /(tableauMassesChromosomesIndividu(individu1)[i] + tableauMassesChromosomesIndividu(individu2)[i]);
			System.out.println("v1 vaut " + v1);	
			if (v1>=0) {
				enfant1[i] = individu1[i];
			}
		}
		for (int i=1 ; i<enfant1.length-1 ; i++) {
			if (enfant1[i]==0) {
				enfant1[i] = premierNonPresent(enfant1, individu2);
			}
		}
		return enfant1;
	}
	
	
	
	public int[] collisionEnfant2(int[] individu1, int[] individu2) throws Exception {
		int[] enfant2 = new int[individu1.length];
		for (int i=1 ; i<enfant2.length-1 ; i++) {
			double v2 = 2*tableauMassesChromosomesIndividu(individu1)[i]*velociteChromosomesIndividu(individu1)
					   /(tableauMassesChromosomesIndividu(individu1)[i] + tableauMassesChromosomesIndividu(individu2)[i])
					 - (tableauMassesChromosomesIndividu(individu1)[i] - tableauMassesChromosomesIndividu(individu2)[i])*velociteChromosomesIndividu(individu2)
					   /(tableauMassesChromosomesIndividu(individu1)[i] + tableauMassesChromosomesIndividu(individu2)[i]);
			System.out.println("v2 vaut " + v2);		 
			if (v2>=0) {
				enfant2[i] = individu2[i];
			}
		}
		for (int i=1 ; i<enfant2.length-1 ; i++) {
			if (enfant2[i]==0) {
				enfant2[i] = premierNonPresent(enfant2, individu1);
			}
		}
		return enfant2;
	}
	
	
	public static int premierNonPresent(int[] individu1, int[] individu2) throws Exception {
		int i = 1;
		while (estPresent(individu1, individu2[i])) {
			i++;
		}
		return individu2[i];
	}

	/* (non-Javadoc)
	 * @see tsp.metaheuristic.AMetaheuristic#solve(tsp.Solution)
	 */
	@Override
	public Solution solve(Solution sol) throws Exception {
		
		int[][] population;
		population = genererPopulation(20);
		//population = genererPopulationSemiAleatoirement(20);
		
		//population = genererPopulation(1000);
		
		//for (int i=0 ; i<100000 ; i++) {
		for (int i=0 ; i<10000 ; i++) {
			int [][] selectionSousEnsemblePopulation = selectionSousEnsemblePopulation(population);
			//int [][] populationEnfant = crossOver(selectionSousEnsemblePopulation);
			int [][] populationEnfant = collisionCrossOver(selectionSousEnsemblePopulation);
			mutationPopulationEnfant2opt(populationEnfant);
			nouvellePopulation(population, populationEnfant);
			
		}
		
		int[] meilleurIndividu = meilleurIndividu(population);
		//meilleurIndividu = localSearch(meilleurIndividu);
		for (int i = 0; i<meilleurIndividu.length ; i++) {
			sol.setCityPosition(meilleurIndividu[i], i);
		}
		return sol;
	}

}
