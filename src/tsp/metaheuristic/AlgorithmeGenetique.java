package tsp.metaheuristic;

import tsp.Instance;
import tsp.Solution;


/**
 * The Class AlgorithmeGenetique.
 */

public class AlgorithmeGenetique extends AMetaheuristic {

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
	 * Echanger villes. 
	 * Echange, dans un chemin emprunte par le voyageur de commerce, 
	 * les villes visitees en i-eme et j-ieme positions.
	 * Exemple : si tab = [0, 2, 1, 3, 0] alors apres l'appel
	 			echangerVilles(tab, 2, 3), tab = [0, 2, 3, 1, 0] 
	 *
	 * @param tab : un chemin possible pour le voyageur de commerce sous forme de tableau
	 * @param i : indice tel que tab[i] correspond a la i-eme ville visitee
	 * @param j : indice tel que tab[j] correspond a la j-eme ville visitee
	 * @throws Exception the exception
	 */
	public static void echangerVilles(int[] tab, int i, int j) throws Exception  {
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
	 * Generer individu.
	 * Genere aleatoirement un chemin emprunte par le voyageur de commerce
	 * Ici, sommet i est insere en position i dans la tournee.
	 * Puis, on échange l'ordre des villes visitees aleatoirement 
	 *
	 * @return individu : un chemin aleatoire sous forme de tableau
	 * @throws Exception the exception
	 */
	public int[] genererIndividu() throws Exception {
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
	 * Generer population.
	 * Genere aleatoirement une population de chemins empruntes par le voyageur de commerce
	 *
	 * @param n : entier egal a la taille de la population generee ;
	 * @return population : type = un tableau de tableau de int ;
	 						population = un tableau de n chemins aleatoires 
	 * @throws Exception the exception
	 */
	public int[][] genererPopulation(int n) throws Exception {
		int[][] population = new int[n][1];
		for (int i=0 ; i<n ; i++) {
			population[i] = genererIndividu();
		}
		return population;
	}
	
	
	/**
	 * Individu au hasard.
	 * Renvoie un chemin tire aleatoirement dans une population
	 *
	 * @param population : un tableau de chemins aleatoires
	 * @return un chemin (sous forme de tableau) tire aleatoirement au sein population
	 * @throws Exception the exception
	 */
	public static int[] individuAuHasard(int[][] population) throws Exception {
		//r = indice du chemin tire aleatoirement
		int r = (int) (Math.random()*population.length);
		return population[r];
		
	}
	
	
	/**
	 * Selection sous ensemble population.
	 * 
	 * Selection d'un certain nombre de chemins parmi 
	 * tous les chemins de la population population
	 * 
	 * La selection doit aboutir a un nombre pair de chemins selectionnes
	 * car les croisements a l'origine d'un enfant1 et d'un enfant2
	 * font intervenir un parent1 et un parent2
	 * 
	 * La selection choisie est une selection par tournoi, i.e
	 * deux chemins sont tires au sort et celui qui sera selectionne
	 * sera celui ayant le cout le moins important (dans notre cas,
	 * le cout est egal a la distance parcourue)
	 * 
	 * 
	 * @param population : un tableau de chemins
	 * @return selectionSousEnsemblePopulation : un tableau de chemins apparaissant 
	 											dans population ;
	 										    N.B : 1) tous les chemins de population
	 										    n'apparaissent pas dans selectionSousEnsemblePopulation
	 										    		  2) un chemin de population peut apparaitre
	 										    	plusieurs fois dans selectionSousEnsemblePopulation
	 * @throws Exception the exception
	 */
	public int[][] selectionSousEnsemblePopulation(int[][] population) throws Exception {
		int m = 10; //m pair !
		int[][] selectionSousEnsemblePopulation = new int[m][1];
		for (int i=0 ; i<m ; i++) {
			int[] competiteur1 = individuAuHasard(population);
			int[] competiteur2 = individuAuHasard(population);
			//choix de l'individu au cout le plus bas
			if (evaluateIndividu(competiteur1)<evaluateIndividu(competiteur2)) {
				selectionSousEnsemblePopulation[i] = competiteur1;
			} else {
				selectionSousEnsemblePopulation[i] = competiteur2;
			}
		}
		return selectionSousEnsemblePopulation;
		
	}
	
	
	/**
	 * Copy of.
	 * Cree une copie en profondeur d'un chemin
	 *
	 * @param individu : tableau d'entiers et chemin que l'on souhaite recopier
	 * @return copyOfIndividu : la copie en profondeur de individu
	 */
	public static int[] copyOf(int[] individu) {
		int[] copyOfIndividu = new int[individu.length];
		for (int i=0 ; i<individu.length ; i++) {
			copyOfIndividu[i] = individu[i];
		}
		return copyOfIndividu;
	}
	
	
	/**
	 * Est present.
	 * Savoir si un entier est present dans un tableau
	 *
	 * @param individu : un tableau d'entiers
	 * @param entier : un entier
	 * @return true si l'entier est present dans le tableau individu ; false sinon
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
	 * Annexe.
	 * Cree un chemin enfant appele copyPartielleOfIndividus a partir de deux chemins
	 * et d'un indice de cassure

	 * Fonctionne de la maniere suivante : 

	 		- pour tout entier i de [0, c] : copyPartielleOfIndividus[i] = individu1[i]
	 		
	 		- pour tout entier i de [c+1, copyPartielleOfIndividus.length-2] : 
	 										   copyPartielleOfIndividus[i] = premiere ville
	 		                                    rencontree de individu2 n'apparaissant pas 
	 		                                    deja dans copyPartielleOfIndividus
	 *
	 * @param individu1 : un chemin sous forme de tableau
	 * @param individu2 : un autre chemin sous forme de tableau
	 * @param c : indice de "cassure" 
	 * @return copyPartielleOfIndividus : un chemin enfant issu de individu1 et individu2
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
	 * Cross over.
	 * Renvoie une population enfant. Cette population enfant est
	 * constituee de chemins issus de croisements entre les chemins 
	 * d'une partie de la population initiale
	 * 
	 * Afin de laisser place au hasard, on applique le cross over
	 * de maniere aleatoire ; probabilite d'appliquer un 
	 * cross over egale a 2/3 ; si on n'applique pas un cross over
	 * alors les deux chemins enfants sont des copies profondes 
	 * des deux parents 
	 *
	 * @param selectionSousEnsemblePopulation : un tableau de chemins apparaissant 
	 										   dans une population initiale ;
	 										   N.B : 1) tous les chemins de la population initiale
	 										            n'apparaissent pas dans selectionSousEnsemblePopulation  
	 										    		 2) un chemin de la population initiale peut 
	 										            apparaitre plusieurs fois dans 
	 										            selectionSousEnsemblePopulation
	 * 
	 * @return populationenfant : un tableau de chemins issus du crossOver des
	 							individus selectionnes parmi la population initiale
	 * @throws Exception the exception
	 */
	public static int[][] crossOver(int[][] selectionSousEnsemblePopulation) throws Exception {
		int[][] populationenfant = new int[selectionSousEnsemblePopulation.length][1];
		double probaCrossOver = 0.66;
		int n = selectionSousEnsemblePopulation[0].length;
		for (int i=0 ; i<selectionSousEnsemblePopulation.length-1 ; i+=2) {
			
			double r = Math.random(); //r entre 0 inclus et 1 exclus
									 //theoriquement il faudrait les deux inclus
			//probabilite d'appliquer un cross over
			if (r<=probaCrossOver) {
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
	
	
	/**
	 * Tableau masses chromosomes individu
	 * 
	 * @param individu : un chemin sous forme de tableau
	 * @return tableauMassesChromosomesIndividu : 
	 					un tableau de long tel que tableauMassesChromosomesIndividu[i]
	 					represente la masse de la ville en position i ; la masse etant
	 					definie comme la somme des distances a ses voisins (villes i-1 et i+1)
	 					
	 * @throws Exception the exception
	 */
	public long[] tableauMassesChromosomesIndividu(int[] individu) throws Exception {
		long[] tableauMassesChromosomesIndividu = new long[individu.length];
		for (int i=1 ; i<individu.length-1 ; i++) {
			tableauMassesChromosomesIndividu[i] = m_instance.getDistances(individu[i-1], individu[i]) + m_instance.getDistances(individu[i], individu[i+1]);
		}
		return tableauMassesChromosomesIndividu;
	}
	
	
	/**
	 * Velocite chromosomes individu
	 * 
	 * @param individu : un chemin sous forme de tableau
	 * @return la velocite d'une ville (appelee aussi chromosome) ;
	 			la velocite est egale au cout de l'individu					
	 * @throws Exception the exception
	 */
	public double velociteChromosomesIndividu(int[] individu) throws Exception {
		return evaluateIndividu(individu);
	}
	
	
	/**
	 * Premier non present
	 * Retourne la premiere ville rencontree d'un chemin complet n'apparaissant pas 
	   deja dans un chemin2. Dans ce chemin2 toutes les villes n'apparaissent
	   pas encore ; Exemple : dans [0, 0, 1, 3, 6, 0, 5, 0] il manque les villes 2 et 4
	 *
	 * @param tab : un tableau representant un chemin dans lequel 
	 				certaines villes manquent
	 				Exemple : dans [0, 0, 1, 3, 6, 0, 5, 0] il manque les villes 2 et 4
	 				          ; [0, 2, 1, 3, 6, 4, 5, 0] serait un chemin valide
	 * @param individu2 : un chemin (complet) sous forme de tableau
	 * @return premiere ville rencontree de individu2 n'apparaissant pas 
	 		   deja dans tab
	 * @throws Exception the exception
	 */
	public static int premierNonPresent(int[] tab, int[] individu2) throws Exception {
		int i = 1;
		while (estPresent(tab, individu2[i])) {
			i++;
		}
		return individu2[i];
	}
	
	/**
	 * Collision enfant 1
	 * Cree un chemin enfant appele enfant1 a partir de la collision deux chemins
	 * Methode inspiree de la theorie des chocs elastiques entre deux particules 
	 * et lorsqu'on se place a une dimension

	 * Fonctionne de la maniere suivante :

	 		- on cree un nouveau tableau d'entiers de meme longueur que individu1
	 		- pour tout entier i de [1, enfant1.length-2] :
	 		        on calcule la nouvelle velocite v1 de la ville situee en position i 
	 		        dans individu1 apres le choc entre individu1 et individu2 ;
	 		        v1 depend des velocites avant le choc de la ville situee en position i 
	 		        dans individu1 et de la ville situee en position i dans individu2
	 		        ainsi que des masses des deux villes
	 		 - si v1<=0, cela signifie que la ville situee en position i dans individu1
	 		   est un "bon gene", 
	 		         
	 * 
	 * @param individu1 : un chemin sous forme de tableau
	 * @param individu2 : un autre chemin sous forme de tableau
	 * @return enfant1 : chemin issu de la collision de individu1 et individu2	
	 * @throws Exception the exception
	 */
	public int[] collisionEnfant1(int[] individu1, int[] individu2) throws Exception {
		int[] enfant1 = new int[individu1.length];
		for (int i=1 ; i<enfant1.length-1 ; i++) {
			double v1 = (tableauMassesChromosomesIndividu(individu1)[i] - tableauMassesChromosomesIndividu(individu2)[i])*velociteChromosomesIndividu(individu1)
					   /(tableauMassesChromosomesIndividu(individu1)[i] + tableauMassesChromosomesIndividu(individu2)[i])
					 + 2*tableauMassesChromosomesIndividu(individu2)[i]*velociteChromosomesIndividu(individu2)
					   /(tableauMassesChromosomesIndividu(individu1)[i] + tableauMassesChromosomesIndividu(individu2)[i]);
			System.out.println("v1 vaut " + v1);	
			if (v1<=0) {
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
	
	
	/**
	 * Collision enfant 2
	 * @param individu1
	 * @param individu2
	 * @return
	 * @throws Exception
	 */
	public int[] collisionEnfant2(int[] individu1, int[] individu2) throws Exception {
		int[] enfant2 = new int[individu1.length];
		for (int i=1 ; i<enfant2.length-1 ; i++) {
			double v2 = 2*tableauMassesChromosomesIndividu(individu1)[i]*velociteChromosomesIndividu(individu1)
					   /(tableauMassesChromosomesIndividu(individu1)[i] + tableauMassesChromosomesIndividu(individu2)[i])
					 - (tableauMassesChromosomesIndividu(individu1)[i] - tableauMassesChromosomesIndividu(individu2)[i])*velociteChromosomesIndividu(individu2)
					   /(tableauMassesChromosomesIndividu(individu1)[i] + tableauMassesChromosomesIndividu(individu2)[i]);
			System.out.println("v2 vaut " + v2);		 
			if (v2<=0) {
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
	
	
	/**
	 * Collision cross over.
	 * Renvoie une population enfant. Cette population enfant est
	 * constituee de chemins issus de croisements entre les chemins 
	 * d'une partie de la population initiale
	 * Methode de croisements utilisee : "collision cross over"
	 * 
	 * Afin de laisser place au hasard, on applique le cross over
	 * de maniere aleatoire ; probabilite d'appliquer un 
	 * cross over egale a 2/3 ; si on n'applique pas un cross over
	 * alors les deux chemins enfants sont des copies profondes 
	 * des deux parents 
	 *
	 * @param selectionSousEnsemblePopulation : un tableau de chemins apparaissant 
	 										   dans une population initiale ;
	 										   N.B : 1) tous les chemins de la population initiale
	 										            n'apparaissent pas dans selectionSousEnsemblePopulation  
	 										    		 2) un chemin de la population initiale peut 
	 										            apparaitre plusieurs fois dans 
	 										            selectionSousEnsemblePopulation
	 * 
	 * @return populationenfant : un tableau de chemins issus du collisionCrossOver des
	 							individus selectionnes parmi la population initiale
	 * @throws Exception the exception
	 */
	public int[][] collisionCrossOver(int[][] selectionSousEnsemblePopulation) throws Exception {
		int[][] populationenfant = new int[selectionSousEnsemblePopulation.length][1];
		double probadhybridation = 0.66;
		for (int i=0 ; i<selectionSousEnsemblePopulation.length-1 ; i+=2) {
			
			double r = Math.random(); //r entre 0 inclus et 1 exclus
									 //theoriquement il faudrait les deux inclus
			//probabilite d'appliquer un cross over
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
	 * Echange deux villes consecutives dans un chemin
	 * N.B : mutationIndividu prend en compte le fait que la ville 0 (qui est visitee au debut
	         et a la fin) ne peut pas etre changee de place
	 * Exemple : si individu = [0, 2, 1, 3, 0] alors apres l'appel
	 			mutationIndividu(individu), individu = [0, 1, 2, 3, 0]
	 									   || in	dividu = [0, 2, 3, 1, 0] 
	 *
	 * @param individu : un chemin sous forme de tableau
	 * @throws Exception the exception
	 */
	// on privilegie par la suite mutationIndividu2opt a mutationIndividu
	/*public static void mutationIndividu(int[] individu) throws Exception {
		int i = 1 + (int) (Math.random()*(individu.length-3));
		int j = i + 1;
		echangerVilles(individu, i, j);
	}*/
	
	/**
	 * Copy ordre different.
	 * Inverse en partie un chemin
	 * Renvoie une copie copie du chemin individu telle que :
	 * pour i entier dans [0, c1] : copie[i] = individu[i];
	 * pour j entier dans [c2, individu.length-1] : copie[j] = individu[j];
	 * on retourne le sous tableau de individu d'indice de debut c1+1 et d'indice de fin c2-1
	 * Exemple : si individu = [0, 2, 1, 4, 5, 3, 6, 0] alors
	 			copyOrdreDifferent(individu, 1, 5) retourne 
	 			[0, 2, 5, 4, 1, 3, 6, 0]
	 *
	 * @param individu : un chemin sous forme de tableau
	 * @param c1 : un entier ; c1+1 correspond à l'indice à partir duquel on 
	 			   souhaite inverser le chemin individu
	 * @param c2 : un entier ; c2-1 correspond à l'indice à partir duquel on
	 			   souhaite arreter d'inverser le chemin individu
	 * @return copyOrdreDifferent : le tableau partiellement inverse par 
	 								rapport a individu
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
	
	
	/**
	 * Mutation individu 2 opt.
	 * Inverse en partie un chemin
	 * 1) Creation de deux entiers c1, c2 aleatoirement
	 * 2) Application de copyOrdreDifferent(individu, c1, c2)
	 *
	 * @param individu : un chemin sous forme de tableau
	 * @return le tableau partiellement inverse par rapport a individu
	 		   Exemple : si individu = [0, 2, 1, 4, 5, 3, 6, 0], c1 = 1, c2 = 5
	 		             alors copyOrdreDifferent(individu, 1, 5) retourne 
	 			         [0, 2, 5, 4, 1, 3, 6, 0]
	 * @throws Exception the exception
	 */
	public static int[] mutationIndividu2opt(int[] individu) throws Exception {
		int c1 = 1 + (int) (Math.random()*(individu.length-2));
		int c2;
		do {
			c2 = 1 + (int) (Math.random()*(individu.length-2));
		} while ((c1==c2)||(c1==(c2+1))||(c2==(c1+1)));
		return copyOrdreDifferent(individu, c1, c2);
	}
	
	
	/**
	 * Mutation population enfant.
	 * Chaque enfant issu de crossOver est mute en utilisant
	 * la methode mutationIndividu
	 *
	 * @param populationEnfant : un tableau de chemins issus du crossOver
	 							des individus selectionnes parmi la population
	 							initiale
	 * @throws Exception the exception
	 */
	// on privilegie par la suite mutationPopulationEnfant2opt a mutationPopulationEnfant
	/*public static void mutationPopulationEnfant(int[][] populationEnfant) throws Exception {
		for (int i=0 ; i<populationEnfant.length ; i++) {
			double r = Math.random();
			if (r<0.2) {
				mutationIndividu(populationEnfant[i]);
			}
		}
	}*/
	
	
	/**
	 * Mutation population enfant 2 opt.
	 * Chaque enfant issu de crossOver est mute en utilisant
	 * la methode mutationIndividu2opt
	 *
	 * @param populationEnfant : un tableau de chemins issus du crossOver
	 							des individus selectionnes parmi la population
	 							initiale
	 * @throws Exception the exception
	 */
	public static void mutationPopulationEnfant2opt(int[][] populationEnfant) throws Exception {
		for(int i=0 ; i<populationEnfant.length ; i++) {
			double r = Math.random();
			if (r<0.2) {
				populationEnfant[i] = mutationIndividu2opt(populationEnfant[i]);
			}
		}
	}
	
	
	/**
	 * Evaluate individu.
	 * Renvoie le cout d'un chemin emprunte par le voyageur de commerce,
	 * i.e la distance totale
	 *
	 * @param individu : un chemin emprunte par le voyageur de commerce code dans un tableau
	 * @return m_objectiveValue : la distance totale du chemin
	 * @throws Exception the exception
	 */
	public int evaluateIndividu(int[] individu) throws Exception {
		int m_objectiveValue = 0;
		for (int i = 0; i < this.m_instance.getNbCities(); i++) {
			m_objectiveValue += m_instance.getDistances(individu[i], individu[i + 1]);
		}
		return m_objectiveValue;
	}
	
	
	/**
	 * Indice pire individu.
	 *
	 * @param population : un tableau de chemins
	 * @return indicePireIndividu : indice de l'individu ayant le cout le plus eleve, i.e
	 							    indice de l'individu ayant la distance la plus grande
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
	 * Nouvelle population.
	 * Modifie la population initiale en une population
	 * hybride constituee de la population enfant et de 
	 * certains chemins de la population initiale 
	 *
	 * @param population : type = tableau de tableau d'entiers
	 					   population initiale de chemins
	 					   
	 * @param populationenfant : type = tableau de tableau d'entiers

	 							 population enfant constituee des Enfants
	 							 les Enfants sont issus du crossOver des 
	 							 individus selectionnes parmi la population
	 							 initiale et ont subi des mutations
	 							
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
	 * Renvoie le chemin ayant le plus faible cout parmi un ensemble de chemins
	 *
	 * @param population : un tableau de chemins
	 * @return population[indicemeilleurindiv] : le chemin au sein de la
	  											population population ayant 
	  											le plus faible cout 
	 							                 
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
	 * Trouver ville.
	 * 
	 *
	 * @param tab : un tableau d'entiers ;
	 			    correspond a un chemin emprunte par le voyageur de commerce 
	 * @param i : un entier ; correspond au numero de la ville que l'on veut trouver dans tab
	 * @return k : l'indice de la ville i dans le tableau tab 
	 * @throws Exception the exception
	 */
	public static int trouverVille(int[] tab, int i) throws Exception  {
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
	 * Echange de position, dans un chemin emprunte par le voyageur de commerce, 
	 * les villes portant les numeros i et j.
	 * Exemple : si tab = [0, 2, 1, 3, 0] alors apres l'appel
	 	         echangerVilles(tab, 2, 3), tab = [0, 3, 1, 2, 0] 
	 	         
	 * @param tab : un chemin possible pour le voyageur de commerce sous forme de tableau
	 * @param i : numero de ville que l'on souhaite changer de position dans la tournee
	 * @param j : autre numero numero de ville que l'on souhaite changer de position dans la tournee
	 * @throws Exception the exception
	 */
	public static void echangerVilles2(int[] tab, int i, int j) throws Exception  {
		if (i<=0||i>=tab.length-1) {
			throw new Exception("indice(s) non valide(s)");
		} else if (j<=0||j>=tab.length-1) {
			throw new Exception("indice(s) non valide(s)");
		} else {
			//k est l'indice de la ville i dans tab
			int k = trouverVille(tab, i);
			//n est l'indice de la ville j dans tab
			int n = trouverVille(tab, j);
			tab[k] = j;
			tab[n] = i;
		}
	}

	
	/** Application de la metaheuristique sur une solution
	 *  pour construire une solution optimale locale
	 *  
	 *  @return sol : la solution dont l'ordre des villes correspond a celui dans 
	 *  				  le tableau meilleurIndividu retourne par l'algorithme genetique
	 */
	public Solution solve(Solution sol) throws Exception {
		
		int[][] population;
		population = genererPopulation(20);

		//repetition du schema d'un algorithme genetique 100.000 fois
		//le schema d'un algorithme genetique simplifie est : 
		// selection puis croisements puis mutations
		
		for (int i=0 ; i<100000 ; i++) {
		//for (int i=0 ; i<10000 ; i++) {
			int [][] selectionSousEnsemblePopulation = selectionSousEnsemblePopulation(population);
			int [][] populationEnfant = crossOver(selectionSousEnsemblePopulation);
			//int [][] populationEnfant = collisionCrossOver(selectionSousEnsemblePopulation);
			mutationPopulationEnfant2opt(populationEnfant);
			nouvellePopulation(population, populationEnfant);
		}
		
		int[] meilleurIndividu = meilleurIndividu(population);
		for (int i = 0; i<meilleurIndividu.length ; i++) {
			sol.setCityPosition(meilleurIndividu[i], i);
		}
		return sol;
	}

}
