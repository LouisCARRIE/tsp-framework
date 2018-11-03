package tsp.metaheuristic;

import java.util.ArrayList;
import java.util.List;

import tsp.Instance;
import tsp.Solution;


/**
 * The Class LocalSearchSwap.
 */

public class LocalSearchSwap extends AMetaheuristic {


	/**
	 * Instantiates a new local search swap.
	 *
	 * @param instance the instance
	 * @throws Exception the exception
	 */
	public LocalSearchSwap(Instance instance) throws Exception {
		super(instance, "Algorithme LocalSearchSwap");
		
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
	 * Genere un chemin arbitraire emprunte par le voyageur de commerce
	 * Ici, sommet i est insere en position i dans la tournee.
	 *
	 * @return un chemin arbitraire
	 * @throws Exception the exception
	 */
	public int[] genererIndividu() throws Exception {
		int[] individu = new int[this.m_instance.getNbCities() + 1];
		for (int i = 1 ; i<this.m_instance.getNbCities() ; i++) {
			individu[i] = i;
		}
		return individu;
		
	}
	
	
	/**
	 * Copy of.
	 * Cree une copie en profondeur d'un chemin
	 *
	 * @param individu : le chemin que l'on souhaite recopier
	 * @return copyOfIndividu : la copie en profondeur de individu
	 */
	public int[] copyOf(int[] individu) {
		int[] copyOfIndividu = new int[individu.length];
		for (int i=0 ; i<individu.length ; i++) {
			copyOfIndividu[i] = individu[i];
		}
		return copyOfIndividu;
	}
	
	
	/**
	 * Generer voisinage.
	 * Genere le voisinage d'un chemin individu par
	 * inversion de couples d'indices du chemin individu
	 * 
	 *
	 * @param individu : le chemin sous forme de tableau pour lequel on genere le voisinage
	 * @return voisinage : la liste de tableaux des voisins de individu obtenus par inversion 
	 * 					   de couples d'indices de individu
	 * @throws Exception the exception
	 */
	public List<int[]> genererVoisinage(int[] individu) throws Exception {
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
	
	
	/**
	 * Evaluate individu.
	 * Renvoie le cout d'un chemin emprunte par le voyageur de commerce,
	 * i.e la distance totale
	 *
	 * @param individu : un chemin emprunte par le voyageur de commerce code dans un tableau
	 * @return m_objectiveValue : la distance totale du chemin
	 * @throws Exception the exception
	 */
	public double evaluateIndividu(int[] individu) throws Exception {
		int m_objectiveValue = 0;
		for (int i = 0; i < this.m_instance.getNbCities(); i++) {
			m_objectiveValue += m_instance.getDistances(individu[i], individu[i + 1]);
		}
		return m_objectiveValue;
	}
	
	
	/**
	 * Meilleure solution voisinage.
	 * Renvoie le chemin ayant le plus faible cout parmi une liste de chemins
	 *
	 * @param voisinage : une liste de chemins voisins (au sens du Swap) d'un autre chemin
	 * @return meilleureSolution : le chemin ayant le plus faible cout
	 							   parmi le voisinage voisinage
	 * @throws Exception the exception
	 */
	public int[] meilleureSolutionVoisinage(List<int[]> voisinage) throws Exception {
		int[] meilleureSolution = voisinage.get(0);
		for (int[] element : voisinage) {
			if (evaluateIndividu(element)<evaluateIndividu(meilleureSolution)) {
				meilleureSolution = element;
			}
		}
		return meilleureSolution;
	}
	
	
	/**
	 * Local search swap.
	 * Remplace la solution courante par la meilleure solution choisie dans le voisinage 
	 * de la solution courante
	 * La recherche s’arrete lorsqu’aucune nouvelle meilleure solution n'est trouvee
	 *
	 * @param individu : le chemin sous forme de tableau actuellement emprunte 
	 					 par le voyageur de commerce
	 * @return le chemin optimum local au sens du Swap
	 * @throws Exception the exception
	 */
	public int[] localSearchSwap(int[] individu) throws Exception {
		//méthode trop longue pour les problemes contenant plus de 320 villes !
		//dans la méthode localSearchSwap remplacer la boucle while par un for ?
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
	

	/** Application de la metaheuristique sur une solution
	 *  pour construire une solution optimale locale
	 *  
	 *  @return sol : la solution dont l'ordre des villes correspond a celui
	 *  				  trouve dans le tableau individu retourne par localSearchSwap
	 */
	public Solution solve(Solution sol) throws Exception {
		int[] individu = genererIndividu();
		individu = localSearchSwap(individu);
		for (int i = 0; i<individu.length ; i++) {
			sol.setCityPosition(individu[i], i);
		}
		return sol;
	}
	
	

}
