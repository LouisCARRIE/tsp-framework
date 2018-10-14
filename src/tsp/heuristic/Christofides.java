package tsp.heuristic;

import java.util.ArrayList;
import java.util.List;

import tsp.Instance;

public class Christofides extends AHeuristic{

	public Christofides(Instance instance) throws Exception {
		super(instance, "Christofides");
	}
	
	public static List<Integer> remove1(List<Integer> l, int point) {
		List<Integer> t = new ArrayList<Integer>();
		for(int e : l) {
			if(e != point) {
				t.add(e);
			}
		}
		return t;
	}
	
	/*return un arbre de recouvrement de poids minimal, arbre.get(i) renvoie les sommets suivant du sommet i*/
	public static List<List<Integer>> algoPrim(Instance instance) throws Exception{
		List<List<Integer>> arbre = new ArrayList<List<Integer>>();
		for(int i = 0; i<instance.getNbCities(); i++) {
			arbre.add(new ArrayList<Integer>());
		}
		List<Integer> villesMarquees = new ArrayList<Integer>();
		villesMarquees.add(0); //On commence à construire notre arbre depuis la ville 0.
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
	
	public static List<Integer> sommetsDeDegresImpairs(List<List<Integer>> arbre){
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
	
	/* return le graphe induit de l'ensemble des sommets de degré impair, 
	 * prend en entrée un arbre de recouvrement de poids minimal*/
	public static List<List<Integer>> grapheInduit(Instance instance, List<List<Integer>> arbre){
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
	
	/*calcule deux liste L et R de sorte que G = (L,R) soit un graphe biparti (nécessaire pour le couplage parfait)
	 * prend en entrée le graphe induit par l'ensemble des sommets de degre impair, et un arbre de recouvrement*/
	public static List<List<Integer>> creationGrapheBiparti(Instance instance, List<List<Integer>> graphe, List<List<Integer>> arbre) throws Exception{
		//List<List<Integer>> graphe = grapheInduit(instance, algoPrim(instance));
		List<List<Integer>> biparti = new ArrayList<List<Integer>>();
		biparti.add(new ArrayList<Integer>());
		biparti.add(new ArrayList<Integer>());
		List<Integer> villesMarquees = new ArrayList<Integer>();
		List<Integer> villesAChoisir = sommetsDeDegresImpairs(arbre);
		while (villesMarquees.size() < graphe.size()) {
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
			
			//imin dans get(1) et jmin dans get(0) ? heuristique, on compare des sommes
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
	
	public static List<List<Integer>> creationCouplageParfait(Instance instance, List<List<Integer>> biparti) throws Exception{
		List<List<Integer>> couplage = new ArrayList<List<Integer>>();
		long[][] matrice = new long[biparti.get(0).size()][biparti.get(1).size()];
		int nbLignes = biparti.get(0).size();
		int nbColonnes = biparti.get(1).size();
		//creation de la matrice
		for(int i = 0; i < biparti.get(0).size(); i++) {
			for(int j = 0; j < biparti.get(1).size(); j++) {
				matrice[i][j] = instance.getDistances(biparti.get(0).get(i), biparti.get(1).get(j));
			}
		}
		//On soustrait à chaque ligne le min de la ligne puis à chaque colonne le min de la colonne
		for(int i = 0; i < nbLignes; i++) {
			long min = matrice[i][0];
			for(int j = 0; j < nbColonnes; j++) {
				if(matrice[i][j] < min) {
					min = matrice[i][j];
				}
			}
			for(int j = 0; j < nbColonnes; j++) {
				matrice[i][j] -= min;
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
			}
		}
		//Puis on cherche une solution optimale
		
		return couplage;
	}
	
	@Override
	public void solve() throws Exception {
		
		
	}

}
