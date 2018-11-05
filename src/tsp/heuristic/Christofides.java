package tsp.heuristic;

import java.util.ArrayList;
import java.util.List;

import tsp.Instance;
import tsp.Solution;
import tsp.util.Couple;

/**
 * The Class Christofides.
 * @author Thomas Bazaille
 */
public class Christofides extends AHeuristic{
	
	
	
	/**
	 * Instantiates a new Christofides.
	 *
	 * @param instance the instance
	 * @throws Exception the exception
	 */
	public Christofides(Instance instance) throws Exception {
		super(instance, "Christofides");
	}
	
	/**
	 * Enlève l'élément point dans la liste l.
	 *
	 * @param l la liste
	 * @param point l'element à retirer
	 * @return une nouvelle liste sans l'element point
	 */
	public static List<Integer> remove1(List<Integer> l, int point) {
		List<Integer> t = new ArrayList<Integer>();
		for(int e : l) {
			if(e != point) {
				t.add(e);
			}
		}
		return t;
	}
	
	/**
	 * Enlève la premiere occurence de l'element point dans la liste l.get(index).
	 *
	 * @param l la liste de liste
	 * @param index
	 * @param point
	 * @return une nouvelle liste de liste sans la premiere occurence de l'element point dans l.get(index)
	 */
	public static List<List<Integer>> remove2(List<List<Integer>> l, int index, int point) {
		List<List<Integer>> t = new ArrayList<List<Integer>>();
		for(int i = 0; i < l.size(); i++) {
			t.add(new ArrayList<Integer>());
		}
		boolean trouve = false;
		int k = 0;
		for(List<Integer> v : l) {
			for(int e : v) {
				if (trouve) {
					t.get(k).add(e); 
				}else if ((k == index && e == point)) {
					trouve = true;
				}else if (k == index && e != point) {
					t.get(index).add(e);
				}else if (k != index) {
					t.get(k).add(e);
				}
			}
			k++;
		}
		return t;
	}
	
	/**
	 * Copy la liste de liste l.
	 *
	 * @param l une liste de liste d'Integer
	 * @return la copie profonde de l
	 */
	public static List<List<Integer>> copy(List<List<Integer>> l) {
		List<List<Integer>> t = new ArrayList<List<Integer>>();
		for(int i = 0; i < l.size(); i++) {
			t.add(new ArrayList<Integer>());
		}
		int k = 0;
		for(List<Integer> v : l) {
			for(int e : v) {
				t.get(k).add(e);
			}
			k++;
		}
		return t;
	}
	
	/**
	 * Copy la liste de liste l.
	 *
	 * @param l une liste de liste de Couple
	 * @return la copie profonde de l
	 */
	public static List<List<Couple>> copy1(List<List<Couple>> l) {
		List<List<Couple>> t = new ArrayList<List<Couple>>();
		for(int i = 0; i < l.size(); i++) {
			t.add(new ArrayList<Couple>());
		}
		int k = 0;
		for(List<Couple> v : l) {
			for(Couple e : v) {
				t.get(k).add(e);
			}
			k++;
		}
		return t;
	}
	
	/**
	 * Copy la liste l.
	 *
	 * @param l une liste de Couple
	 * @return la copie profonde de l
	 */
	public static List<Couple> copy2(List<Couple> l) {
		List<Couple> t = new ArrayList<Couple>();
		for(Couple v : l) {
			t.add(v);
		}
		return t;
	}
	
	/**
	 * Retire les Couple qui ont leur abscisse egale à ligne et les Couple qui ont leur ordonnee egal à colonne.
	 *
	 * @param l la liste de liste de Couple
	 * @param ligne
	 * @param colonne
	 * @return la nouvelle liste de liste de Couple
	 */
	public static List<List<Couple>> retireLigneColonne(List<List<Couple>> l, int ligne, int colonne) {
		List<List<Couple>> t = copy1(l);
		int k = 0;
		for(List<Couple> v : l) {
			for(Couple e : v) {
				if (e.getX() == ligne || e.getY() == colonne) {
					t.get(k).remove(e);
				}
			}
			k++;
		}
		return t;
	}
	
	/**
	 * Algo de Prim.
	 *
	 * @param instance the instance
	 * @return une liste correspondant a un arbre de recouvrement de poids minimal
	 * arbre.get(i) renvoie les sommets suivant du sommet i
	 * @throws Exception the exception
	 */
	public List<List<Integer>> algoPrim(Instance instance) throws Exception{
		List<List<Integer>> arbre = new ArrayList<List<Integer>>();
		for(int i = 0; i<instance.getNbCities(); i++) {
			arbre.add(new ArrayList<Integer>());
		}
		List<Integer> villesMarquees = new ArrayList<Integer>();
		villesMarquees.add(0); //On commence par construire notre arbre depuis la ville 0.
		List<Integer> villesATraitees = new ArrayList<Integer>();
		for(int i = 1; i<instance.getNbCities(); i++) {
			villesATraitees.add(i);
		}
		for(int k = 0; k < instance.getNbCities() - 1; k++) {
			long distance = instance.getDistances(villesMarquees.get(0), villesATraitees.get(0));
			int vP = villesMarquees.get(0);
			int vF = villesATraitees.get(0);
			for(int villePere : villesMarquees) {
				for(int villeFils : villesATraitees) {
					if(instance.getDistances(villePere, villeFils)<distance) {
						distance = instance.getDistances(villePere, villeFils);
						vP = villePere;
						vF = villeFils;
					}
				}
			}
			villesATraitees = remove1(villesATraitees, vF);
			villesMarquees.add(vF);
			arbre.get(vP).add(vF);
			arbre.get(vF).add(vP);
		}
		return arbre;
	}
	
	/**
	 * Sommets de degres impairs.
	 *
	 * @param arbre : un arbre de recouvrement de poids minimal obtenu avec l'ago de Prim
	 * @return la liste des sommets de degres impairs dans l'arbre
	 */
	public List<Integer> sommetsDeDegresImpairs(List<List<Integer>> arbre){
		List<Integer> degresImpairs = new ArrayList<Integer>();
		int k = 0;
		for(List<Integer> l : arbre) {
			if (l.size()%2 == 1) {
				degresImpairs.add(k);
			}
			k++;
		}
		return degresImpairs;
	}
	
	/**
	 * Graphe induit.
	 *
	 * @param instance the instance
	 * @param arbre : arbre de recouvrement de poids minimal
	 * @return le graphe induit de l'ensemble des sommets de degre impair
	 */
	public List<List<Integer>> grapheInduit(Instance instance, List<List<Integer>> arbre){
		List<Integer> degresImpairs = sommetsDeDegresImpairs(arbre);
		List<List<Integer>> graphe = new ArrayList<List<Integer>>();
		for(int i = 0; i<instance.getNbCities(); i++) {
			graphe.add(new ArrayList<Integer>());
		}
		for(int i : degresImpairs) {
			for(int j : degresImpairs) {
				if(i != j) {
					graphe.get(i).add(j);
					graphe.get(j).add(i);
				}
			}
		}
		return graphe;
	}
	
	/**
	 * Creation graphe biparti. 
	 * Calcule deux liste L et R de sorte que G = (L,R) soit un graphe biparti (necessaire pour l'algo du couplage parfait)
	 *
	 * @param instance the instance
	 * @param graphe : le graphe induit par l'ensemble des sommets de degre impair
	 * @param arbre : un arbre de recouvrement
	 * @return les listes L et R decrites ci-dessus
	 * @throws Exception the exception
	 */
	public List<List<Integer>> creationGrapheBiparti(Instance instance, List<List<Integer>> graphe, List<List<Integer>> arbre) throws Exception{
		List<List<Integer>> biparti = new ArrayList<List<Integer>>();
		biparti.add(new ArrayList<Integer>());
		biparti.add(new ArrayList<Integer>());
		List<Integer> villesMarquees = new ArrayList<Integer>();
		List<Integer> villesAChoisir = sommetsDeDegresImpairs(arbre);
		int n = villesAChoisir.size();
		while (villesMarquees.size() < n) {
			long distance = instance.getDistances(villesAChoisir.get(0), villesAChoisir.get(1));
			int imin = villesAChoisir.get(0);
			int jmin = villesAChoisir.get(1);
			for(int i : villesAChoisir) {
				for(int j : villesAChoisir) {
					if(i != j && instance.getDistances(i, j) < distance){
						distance = instance.getDistances(i, j);
						imin = i;
						jmin = j;
					}
				}
			}
			villesMarquees.add(imin);
			villesMarquees.add(jmin);
			villesAChoisir = remove1(villesAChoisir, imin);
			villesAChoisir = remove1(villesAChoisir, jmin);
			
			//imin dans get(1) et jmin dans get(0). Heuristique, on compare des sommes
			int somme1 = 0;
			int somme2 = 0;
			for(int i0 : biparti.get(0)) {
				somme1 += instance.getDistances(i0, jmin);
			}
			for(int j1 : biparti.get(1)) {
				somme1 += instance.getDistances(imin, j1);
			}
			for(int j0 : biparti.get(0)) {
				somme2 += instance.getDistances(j0, imin);
			}
			for(int i1 : biparti.get(1)) {
				somme2 += instance.getDistances(jmin, i1);
			}
			if(somme1 < somme2) {
				biparti.get(0).add(imin);
				biparti.get(1).add(jmin);
			}else {
				biparti.get(0).add(jmin);
				biparti.get(1).add(imin);
			}
		}
		return biparti;
	}
	
	/**
	 * Creation graphe biparti1. Une autre manière de fabriquer un graphe biparti. Heuristique.
	 * On essaie de creer nos deux listes L et R de maniere à ce que pout tout element l de L et pour tout element r de R
	 * on ait l'aretes (l,r) qui n'appartient pas deja a l'arbre couvrant de poids minimal, ie on va chercher a creer de nouvelles liaisons  
	 * Calcule deux liste L et R de sorte que G = (L,R) soit un graphe biparti (necessaire pour l'algo du couplage parfait)
	 *
	 * @param instance the instance
	 * @param graphe : le graphe induit par l'ensemble des sommets de degre impair
	 * @param arbre : un arbre de recouvrement
	 * @return les listes L et R decrites ci-dessus
	 * @throws Exception the exception
	 */
	public List<List<Integer>> creationGrapheBiparti1(Instance instance, List<List<Integer>> graphe, List<List<Integer>> arbre) throws Exception{
		List<List<Integer>> biparti = new ArrayList<List<Integer>>();
		biparti.add(new ArrayList<Integer>());
		biparti.add(new ArrayList<Integer>());
		List<Integer> villesAChoisir = sommetsDeDegresImpairs(arbre);
		
		for (int i : villesAChoisir) {
			boolean voisin1 = false;
			boolean voisin0 = false;
			for(int j : biparti.get(0)) {
				if(arbre.get(i).contains(j)) {
					voisin0 = true;
					break;
				}
			}
			for(int j : biparti.get(1)) {
				if(arbre.get(i).contains(j)) {
					voisin1 = true;
					break;
				}
			}
			if(voisin0) {
				biparti.get(0).add(i);
			}else if(voisin1) {
				biparti.get(1).add(i);
			}else {
				if(biparti.get(0).size() < biparti.get(1).size()) {
					biparti.get(0).add(i);
				}else {
					biparti.get(1).add(i);
				}
			}
		}
		while(biparti.get(0).size() != biparti.get(1).size()) {
			if(biparti.get(0).size() < biparti.get(1).size()) {
				int x = biparti.get(1).get(0);
				biparti.get(1).remove(0);
				biparti.get(0).add(x);
			}else {
				int x = biparti.get(0).get(0);
				biparti.get(0).remove(0);
				biparti.get(1).add(x);
			}
		}
		return biparti;
	}
	
	/**
	 * Creation couplage parfait. Algorithme hongrois
	 *
	 * @param instance the instance
	 * @param biparti : un graphe biparti
	 * @return une liste de Couple correspondant a un couplage parfait
	 * @throws Exception the exception
	 */
	public List<Couple> creationCouplageParfait(Instance instance, List<List<Integer>> biparti) throws Exception{
		List<Couple> couplage = new ArrayList<Couple>();
		long[][] matrice = new long[biparti.get(0).size()][biparti.get(1).size()];
		int nbLignes = biparti.get(0).size();
		int nbColonnes = biparti.get(1).size();
		
		List<Couple> zeros = new ArrayList<Couple>();
		//creation de la matrice
		for(int i = 0; i < biparti.get(0).size(); i++) {
			for(int j = 0; j < biparti.get(1).size(); j++) {
				matrice[i][j] = instance.getDistances(biparti.get(0).get(i), biparti.get(1).get(j));
			}
		}
		
		
		/*On soustrait à chaque ligne le min de la ligne puis à chaque colonne le min de la colonne

		/*On soustrait à chaque ligne le min de la ligne puis à chaque colonne le min de la colonne
		 * ie reduction du tableau initial (etape 1)*/
		for(int i = 0; i < nbLignes; i++) {
			long min = matrice[i][0];
			for(int j = 0; j < nbColonnes; j++) {
				if(matrice[i][j] < min) {
					min = matrice[i][j];
				}
			}
			for(int j = 0; j < nbColonnes; j++) {
				matrice[i][j] -= min;
				if(matrice[i][j] == 0) {
					zeros.add(new Couple(i,j));
				}
			}
		}
		for(int i = 0; i < nbColonnes; i++) {
			long min = matrice[0][i];
			for(int j = 0; j < nbLignes; j++) {
				if(matrice[j][i] < min) {
					min = matrice[j][i];
				}
			}
			for(int j = 0; j < nbLignes; j++) {
				matrice[j][i] -= min;
				if(matrice[j][i] == 0 && !zeros.contains(new Couple(j,i))) {
					zeros.add(new Couple(j,i));
				}
			}
		}
		
		
		/*Puis on cherche une solution optimale (etape 2)*/
		List<Couple> zerosEncadres = new ArrayList<Couple>();
		List<Couple> zerosBarres = new ArrayList<Couple>();
		while (zerosEncadres.size() < nbLignes) {
			
			//recreer zeros
			if(zeros.size() == 0) {
				for(int i = 0; i < nbLignes; i++) {
					for(int j = 0; j < nbColonnes; j++) {
						if(matrice[i][j] == 0) {
							zeros.add(new Couple(i,j));
						}
					}
				}
			}
			while(zeros.size() > 0) {
				//On commence par chercher la ligne ayant le moins de zeros non barres et on encadre un zero 
				//(le plus à gauche)
				int ligneContenantLeMoinsDeZerosNonBarres = 0;
				int[] nombreZerosNonBarres = new int[nbLignes];
				List<List<Couple>> potentielZerosEncadres = new ArrayList<List<Couple>>();
				for(int k = 0; k < nbLignes; k++) {
					potentielZerosEncadres.add(new ArrayList<Couple>());
				}
				for(Couple c : zeros) {
					nombreZerosNonBarres[c.getX()] += 1; //pas besoin de mettre de if car les zeros dans zeros sont forcement non barres ni encadres
					potentielZerosEncadres.get(c.getX()).add(c);	
				}
				int min = nombreZerosNonBarres[0] + 1;
				for(int i = 1; i < nombreZerosNonBarres.length; i++) {
					min += nombreZerosNonBarres[i];
				}
				for(int i = 0; i < nombreZerosNonBarres.length; i++) {
					if(nombreZerosNonBarres[i] < min && nombreZerosNonBarres[i] != 0) {
						min = nombreZerosNonBarres[i];
						ligneContenantLeMoinsDeZerosNonBarres = i;
					}
				}
				
				
				Couple zeroCourant = potentielZerosEncadres.get(ligneContenantLeMoinsDeZerosNonBarres).get(0);
				zerosEncadres.add(zeroCourant);
				zeros.remove(zeroCourant);
				List<Couple> temp = new ArrayList<Couple>();
				for(Couple c : zeros) {
					if (c.getX() == zeroCourant.getX() || c.getY() == zeroCourant.getY()) {
						zerosBarres.add(c);
						temp.add(c);
					}
				}
				for(Couple c : temp) {
					zeros.remove(c);
				}
				
			}
			if(zerosEncadres.size() < nbLignes) {
				/*etape 3 : On a pas trouvé de solution optimale, on va donc effectue des modifications sur la matrice*/
				List<Integer> lignesMarquees = new ArrayList<Integer>();
				List<Integer> colonnesMarquees = new ArrayList<Integer>();
				boolean modif1 = true;
				boolean modif2 = true;
				for(int i = 0; i < nbLignes; i++) {
					lignesMarquees.add(i);
				}
				for(Couple c : zerosEncadres) {
					if (lignesMarquees.contains(c.getX())) {
						lignesMarquees = remove1(lignesMarquees, c.getX());
					}
				}
				while(modif1 || modif2) {
					modif1 = false;
					modif2 = false;
					
					for(Couple c : zerosBarres) {
						if(lignesMarquees.contains(c.getX()) 
								&& !colonnesMarquees.contains(c.getY())) {
							colonnesMarquees.add(c.getY());
							modif1 = true;
						}
					}
					for(Couple c : zerosEncadres) {
						if(colonnesMarquees.contains(c.getY()) && !lignesMarquees.contains(c.getX())) {
							lignesMarquees.add(c.getX());
							modif2 = true;
						}
					}
					
				}
			
				zerosEncadres.clear();
				zerosBarres.clear();
				List<Integer> lignesNonMarquees = new ArrayList<Integer>();
				for(int i = 0; i < nbLignes; i++) {
					lignesNonMarquees.add(i);
				}
				for(int k : lignesMarquees) {
					lignesNonMarquees = remove1(lignesNonMarquees, k);
				}
				
				int[][] sousMatrice = new int[nbLignes][nbColonnes];
				int barre = 1;
				int etoile = 2;
				for(int i = 0; i < nbLignes; i++) {
					for(int j = 0; j < nbColonnes; j++) {
						if (lignesNonMarquees.contains(i) && colonnesMarquees.contains(j)) {
							sousMatrice[i][j] = etoile;
						}else if(lignesNonMarquees.contains(i) || colonnesMarquees.contains(j)) {
							sousMatrice[i][j] = barre;
						}else {
							sousMatrice[i][j] = 0;
						}
					}
				}
				
				int i = 0;
				int j = 0;
				boolean sortir = false;
				while(sousMatrice[i][j] != 0 && !sortir) {
					if (i == nbLignes - 1 && j == nbColonnes - 1) {
						sortir = true;
						i = 0;
						j = 0;
					}else if(j == nbColonnes - 1) {
						j = 0;
						i += 1;
					}else{
						j += 1;
					}
				}
				if (!sortir) {
					long min = matrice[i][j];
					for(int k = 0; k < nbLignes; k++) {
						for(int l = 0; l < nbColonnes; l++) {
							if (sousMatrice[k][l] == 0 && matrice[k][l] < min) {
								min = matrice[k][l];
							}
						}
					}
					
					for(int k = 0; k < nbLignes; k++) {
						for(int l = 0; l < nbColonnes; l++) {
							if (sousMatrice[k][l] == 0) {
								matrice[k][l] -= min;
							}else if (sousMatrice[k][l] == etoile) {
								matrice[k][l] += min;
							}else {
								
							}
						}
					}
				}else { /*On devrait trouver un couplage parfait de poids minimal si on arrive ici*/
					/* on recree zeros*/
					for(int i1 = 0; i1 < nbLignes; i1++) {
						for(int j1 = 0; j1 < nbColonnes; j1++) {
							if(matrice[i1][j1] == 0) {
								zeros.add(new Couple(i1,j1));
							}
						}
					}
					int[] nombreZerosNonBarres = new int[nbLignes];
					List<List<Couple>> zerosParLigne = new ArrayList<List<Couple>>(); //zerosParLigne.get(i) renvoie les zeros non barres de la ligne i
					int[] nombreZerosNonBarresParColonne = new int[nbLignes];
					List<List<Couple>> zerosParColonne = new ArrayList<List<Couple>>();
					for(int k = 0; k < nbLignes; k++) {
						zerosParLigne.add(new ArrayList<Couple>());
						zerosParColonne.add(new ArrayList<Couple>());
					}
					for(Couple c : zeros) {
						nombreZerosNonBarres[c.getX()] += 1;
						zerosParLigne.get(c.getX()).add(c);
						nombreZerosNonBarresParColonne[c.getY()] += 1;
						zerosParColonne.get(c.getY()).add(c);
					}
					List<Integer> lignesContenantUnSeulZero = new ArrayList<Integer>();
					List<Integer> colonnesContenantUnSeulZero = new ArrayList<Integer>();
					List<Integer> lignesZerosEncadres = new ArrayList<Integer>();
					List<Integer> colonnesZerosEncadres = new ArrayList<Integer>();
					
					
					while(contains1(nombreZerosNonBarres) || contains1(nombreZerosNonBarresParColonne)) {
						lignesZerosEncadres.clear();
						colonnesZerosEncadres.clear();
						for(int k = 0; k < nbLignes; k++) {
							if (nombreZerosNonBarres[k] == 1) {
								Couple z = zerosParLigne.get(k).get(0);
								if (!zerosEncadres.contains(z)) {
									zerosEncadres.add(z);
								}
								zeros.remove(z);
								zerosParLigne.get(k).remove(z);
								lignesContenantUnSeulZero.add(k);
								colonnesZerosEncadres.add(z.getY());
							}
							if (nombreZerosNonBarresParColonne[k] == 1) {
								Couple z = zerosParColonne.get(k).get(0);
								if (!zerosEncadres.contains(z)) {
									zerosEncadres.add(z);
								}
								zeros.remove(z);
								zerosParColonne.get(k).remove(z);
								colonnesContenantUnSeulZero.add(k);
								lignesZerosEncadres.add(z.getX());
							}
						}
						for (int e : lignesContenantUnSeulZero) {
							nombreZerosNonBarres[e] = 0;
						}
						for (int e : colonnesContenantUnSeulZero) {
							nombreZerosNonBarresParColonne[e] = 0;
						}
						for (Couple c : zeros) {
							
								if (lignesZerosEncadres.contains(c.getX())) {
									zerosBarres.add(c);
									zerosParLigne.get(c.getX()).remove(c);
									nombreZerosNonBarres[c.getX()] -= 1;
								}else if (colonnesZerosEncadres.contains(c.getY())) {
									zerosBarres.add(c);
									zerosParColonne.get(c.getY()).remove(c);
									nombreZerosNonBarresParColonne[c.getY()] -= 1;
								}else {
									
								}
							
						}
						for (Couple c : zerosBarres) {
							if (zeros.contains(c)) {
								zeros.remove(c);
							}
						}
					}
					int n = zerosEncadres.size();
					for (Couple c : zerosEncadres) { //Rajout : on nettoie zerosParLigne
						zerosParLigne = retireLigneColonne(zerosParLigne, c.getX(), c.getY());
					}
					match(retireListesVides(zerosParLigne), nbLignes-n);
					for (Couple c : this.matchFinal) {
						zerosEncadres.add(c);
					}
					
					
				}
				
				
			}
		}
		for(Couple c : zerosEncadres) {
			couplage.add(new Couple(biparti.get(0).get(c.getX()), biparti.get(1).get(c.getY())));
		}
		return couplage;
	}
	
	/**
	 * Union.
	 *
	 * @param arbre : arbre couvrant de poids minimal
	 * @param couplage : couplage parfait
	 * @return l'union de l'arbre et du couplage
	 */
	public List<List<Integer>> union(List<List<Integer>> arbre, List<Couple> couplage){
		List<List<Integer>> graphe = arbre;
		for(Couple c : couplage) {
			graphe.get(c.getX()).add(c.getY());
			graphe.get(c.getY()).add(c.getX());
		}
		return graphe;
	}
	
	/**
	 * Checks if is empty.
	 *
	 * @param G un graphe
	 * @return true, si G est vide
	 */
	public boolean isEmpty1(List<List<Integer>> G) {
		for(List<Integer> l : G) {
			if(!l.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * List to string.
	 *
	 * @param l the l
	 * @return the string
	 */
	public String listToString(List<Couple> l) {
		String res = "[";
		for (Couple c : l) {
			res = res + c.toString() + ", ";
		}
		res += "]";
		return res;
	}
	
	/**
	 * Retire listes vides.
	 *
	 * @param zerosParLigne une liste de liste de Couple
	 * @return une nouvelle liste où on a supprimer les listes vides de zerosParLigne
	 */
	public List<List<Couple>> retireListesVides(List<List<Couple>> zerosParLigne){
		List<List<Couple>> c = new ArrayList<List<Couple>>();
		for (List<Couple> l : zerosParLigne) {
			if (l.size() != 0) {
				c.add(l);
			}
		}
		return c;
	}
	
	/** The match. */
	List<Couple> match = new ArrayList<Couple>();
	
	/** The match final. */
	List<Couple> matchFinal = new ArrayList<Couple>();
	
	/** The fin. */
	boolean fin = false;
	
	/**
	 * Match. C'est la fonction recursive qui va tester si il existe un couplage parfait alors que notre matrice n'evolue plus
	 * dans l'algo hongrois
	 *
	 * @param zerosParLigneNettoye
	 * @param nbLignes
	 */
	public void match(List<List<Couple>> zerosParLigneNettoye, int nbLignes) {
		if (!this.fin) {
			if (this.match.size() == nbLignes) {
				this.fin = true;
				this.matchFinal = copy2(this.match);
			}else if (this.match.size() < nbLignes && zerosParLigneNettoye.size() == 0) {
				this.match.remove(this.match.size()-1);
			}else {
				int n = zerosParLigneNettoye.get(0).size();
				int k = 0;
				for (Couple c : zerosParLigneNettoye.get(0)) {
					k++;
					List<List<Couple>> l = retireListesVides(retireLigneColonne(zerosParLigneNettoye, c.getX(), c.getY()));
					this.match.add(c);
					match(l, nbLignes);
				}
				if (k == n && this.match.size() != 0) {
					this.match.remove(this.match.size() - 1);
				}
			}
		}
	}
	
	
	/**
	 * Contains 1.
	 *
	 * @param v
	 * @return true, si le vecteur contient 1
	 */
	
	public boolean contains1(int[] v) {
		for(int i = 0; i < v.length; i++) {
			if (v[i] == 1) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Contient.
	 *
	 * @param instance the instance
	 * @param villes
	 * @param visitees
	 * @return true, si villes contient visitees
	 */
	public boolean contient(Instance instance, List<Integer> villes, List<Integer> visitees) {
		for(int i = 0; i < visitees.size(); i++) {
			if(!villes.contains(visitees.get(i))) {
				return false;
			}
		}
		return true;
	}
	
	
	/** The villes marquees. */
	List<Integer> villesMarquees = new ArrayList<Integer>();
	
	
	/**
	 * Explorer. Parcours en profondeur.
	 *
	 * @param arbre
	 * @param villeVisitee
	 * @throws Exception the exception
	 */
	public void explorer(List<List<Integer>> arbre, int villeVisitee) throws Exception {
		this.villesMarquees.add(villeVisitee);
		for(int villeFils : arbre.get(villeVisitee)) {
			if(!(this.villesMarquees.contains(villeFils))) {
				explorer(arbre, villeFils);
			}
		}
	}
	
	/** The villes pas visitees. */
	List<Integer> villesPasVisitees = new ArrayList<Integer>();
	
	/**
	 * Tour eulerien.
	 *
	 * @param instance the instance
	 * @param G un graphe eulerien
	 * @return un tour eulerien
	 * @throws Exception the exception
	 */
	public List<Integer> tourEulerien(Instance instance, List<List<Integer>> G) throws Exception{
		List<Integer> chemin = new ArrayList<Integer>();
		List<List<Integer>> graphe = G;
		List<List<Integer>> copyGraphe = copy(G);
		int x = 0;
		for(int i = 0; i < instance.getNbCities(); i++) {
			if (i != x) {
				this.villesPasVisitees.add(i);
			}
		}
		chemin.add(x);
		while(!isEmpty1(graphe)) { //il faudrait tester avant si le graphe est eulerien, si il ne l'est pas modifier biparti
			for(int y : graphe.get(x)) {
				this.villesMarquees.clear();
				copyGraphe = remove2(copyGraphe, x, y);
				copyGraphe = remove2(copyGraphe, y, x);
				explorer(copyGraphe, y);
				copyGraphe.get(x).add(y);
				copyGraphe.get(y).add(x);
				
				if(contient(instance, this.villesMarquees, this.villesPasVisitees)) {
					graphe = remove2(graphe, x, y);
					graphe = remove2(graphe, y, x);
					copyGraphe = remove2(copyGraphe, x, y);
					copyGraphe = remove2(copyGraphe, y, x);
					chemin.add(y);
					x = y;
					this.villesPasVisitees = remove1(this.villesPasVisitees, x);
					break;
				}
			}
		}
		return chemin;
	}
	
	/**
	 * Supprimer doublons.
	 *
	 * @param l
	 * @return une liste sans doublons dans ses elements
	 */
	public List<Integer> supprimerDoublons(List<Integer> l){
		List<Integer> sansDoublons = new ArrayList<Integer>();
		for(int e : l) {
			if(!sansDoublons.contains(e)) {
				sansDoublons.add(e);
			}
		}
		sansDoublons.add(sansDoublons.get(0));
		return sansDoublons;
	}
	
	/**
	 * Afficher matrice.
	 *
	 * @param mat the mat
	 * @return the string
	 */
	public String afficherMatrice(long[][] mat) {
		String res = "[";
		for(int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				res += mat[i][j] + " ";
			}
			res += "\n";
		}
		return res + "]";
	}
	
	/**
	 * Afficher matrice 1.
	 *
	 * @param mat the mat
	 * @return the string
	 */
	public String afficherMatrice1(int[][] mat) {
		String res = "[";
		for(int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				res += mat[i][j] + " ";
			}
			res += "\n";
		}
		return res + "]";
	}
	
	/**
	 * Creer matrice test.
	 *
	 * @return the long[][]
	 */
	public long[][] creerMatriceTest(){
		long[][] m = new long[5][5];
		m[0][0] = 17;
		m[0][1] = 15;
		m[0][2] = 9;
		m[0][3] = 5;
		m[0][4] = 12;
		m[1][0] = 16;
		m[1][1] = 16;
		m[1][2] = 10;
		m[1][3] = 5;
		m[1][4] = 10;
		m[2][0] = 12;
		m[2][1] = 15;
		m[2][2] = 14;
		m[2][3] = 11;
		m[2][4] = 5;
		m[3][0] = 4;
		m[3][1] = 8;
		m[3][2] = 14;
		m[3][3] = 17;
		m[3][4] = 13;
		m[4][0] = 13;
		m[4][1] = 9;
		m[4][2] = 2;
		m[4][3] = 7;
		m[4][4] = 11;
		return m;
	}
	
	/**
	 * Est eulerien.
	 *
	 * @param graphe
	 * @return true, si le graphe est eulerien
	 */
	public boolean estEulerien(List<List<Integer>> graphe){
		int k = 0;
		for(List<Integer> l : graphe) {
			if (l.size()%2 == 1) {
				return false;
			}
			k++;
		}
		return true;
	}
	
	@Override
	public void solve() throws Exception {
		
		
		List<List<Integer>> arbre = algoPrim(this.m_instance);
		List<List<Integer>> graphe = grapheInduit(this.m_instance, arbre);
		List<List<Integer>> biparti = creationGrapheBiparti1(this.m_instance, graphe, arbre);

		List<Couple> couplage = creationCouplageParfait(this.m_instance, biparti);
		
		List<List<Integer>> union = union(arbre, couplage);

		List<Integer> tour = tourEulerien(this.m_instance, union);
		List<Integer> solution = supprimerDoublons(tour);
		Solution sol = new Solution(this.m_instance);
		int k = 0;
		for(int i : solution) {
			sol.setCityPosition(i, k);
			k++;
		}
		this.m_solution = sol;
	}
}






/*Je voulais juste atteindre les 1000 lignes de codes btw*/