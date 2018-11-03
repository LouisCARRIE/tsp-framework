package tsp.metaheuristic;

import java.util.ArrayList;
import java.util.List;

import tsp.Instance;
import tsp.Solution;

/**
 * The Class TwoOpt.
 */

public class TwoOpt extends AMetaheuristic {

	/**
	 * Instantiates a new two opt.
	 *
	 * @param instance the instance
	 * @throws Exception the exception
	 */
	public TwoOpt(Instance instance) throws Exception {
		super(instance, "2_Opt");
	}
	
	/**
	 * Echanger villes. 
	 * Echange dans un chemin emprunte par le voyageur de commerce 
	 * les villes visitees en i-eme et j-ieme positions.
	 * Exemple : si tab = [0, 2, 1, 3, 0] alors apres l'appel
	 			echangerVilles(tab, 2, 3), tab = [0, 2, 3, 1, 0] 
	 *
	 * @param tab : un chemin possible pour le voyageur de commerce
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
	 * Two opt.
	 * Inverse en partie un chemin
	 * 1) Creation de deux entiers c1, c2 aleatoirement
	 * 2) Application de copyOrdreDifferent(individu, c1, c2)
	 *
	 * @param individu : un chemin sous forme de tableau
	 * @return le tableau partiellement inverse par 
	 		   rapport a individu
	 		   Exemple : si individu = [0, 2, 1, 4, 5, 3, 6, 0], c1 = 1, c2 = 5
	 		             alors copyOrdreDifferent(individu, 1, 5) retourne 
	 			         [0, 2, 5, 4, 1, 3, 6, 0]
	 * @throws Exception the exception
	 */
	public int[] Two_Opt(int[] individu) throws Exception {
		int c1 = 1 + (int) (Math.random()*(individu.length-2));
		int c2;
		do {
			c2 = 1 + (int) (Math.random()*(individu.length-2));
		} while ((c1==c2)||(c1==(c2+1))||(c2==(c1+1)));
		return copyOrdreDifferent(individu, c1, c2);
	}
	
	
	/**
	 * Copy ordre different.
	 * Inverse en partie un chemin
	 * Renvoie une copie copie du chemin individu telle que :
	 * pour i entier dans [0, c1] : copie[i] = individu[i];
	 * pour j entier dans [c2, individu.length-1] : copie[j] = individu[j];
	 * on retourne le sous tableau d'indice de debut c1+1 et d'indice de fin c2-1
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
	 * Generer voisinage partiel.
	 * Genere le voisinage partiel d'un chemin individu.
	 * Les voisins sont obtenus par la méthode Two-Opt
	 * 
	 *
	 * @param individu : le chemin sous forme de tableau pour lequel on genere le voisinage
	 * @return voisinagePartiel : une liste de 10000 voisins de individu 
	 							 obtenus par la methode Two_opt
	 * @throws Exception the exception
	 */
	public List<int[]> genererVoisinagePartiel(int[] individu) throws Exception {
		List<int[]> voisinagePartiel = new ArrayList<int[]>();
		for (int i=0 ; i<10000 ; i++) {
			int [] voisin = Two_Opt(individu);
			voisinagePartiel.add(voisin);
		}
		return voisinagePartiel;
	}
	
	
	/**
	 * Meilleure solution voisinage.
	 * Renvoie le chemin ayant le plus faible cout parmi une liste de chemins
	 *
	 * @param voisinage : une liste de chemins voisins (au sens du Two_Opt) d'un autre chemin
	 * @return meilleureSolution : le chemin ayant le plus faible cout 
	 							   parmi le voisinage voisinage
	 * @throws Exception the exception
	 */
	public int[] meilleureSolutionVoisinage(List<int[]> voisinagePartiel) throws Exception {
		int[] meilleureSolution = voisinagePartiel.get(0);
		for (int[] element : voisinagePartiel) {
			if (evaluateIndividu(element)<evaluateIndividu(meilleureSolution)) {
				meilleureSolution = element;
			}
		}
		return meilleureSolution;
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
	 * Local search 2 opt.
	 * Remplace la solution courante par la meilleure solution choisie dans le voisinage 
	 * de la solution courante
	 * La recherche s’arrete lorsqu’aucune nouvelle meilleure solution n'est trouvee
	 *
	 * @param individu : le chemin sous forme de tableau actuellement emprunte 
	 					 par le voyageur de commerce
	 * @return le chemin optimum local au sens du Two_Opt
	 * @throws Exception the exception
	 */
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
	
	
	/** Mise en application de l'heuristique pour construire m_solution
	 *  
	 *  @return sol : la solution dont l'ordre des villes correspond a celui
	 *  				  trouve dans le tableau individu retourne par localSearch2_opt
	 */
	public Solution solve(Solution sol) throws Exception {
		int[] individu = genererIndividu();
		individu = localSearch2_Opt(individu);
		for (int i = 0; i<individu.length ; i++) {
			sol.setCityPosition(individu[i], i);
		}
		return sol;
	}

}
