package tsp.heuristic;

import java.util.ArrayList;
import java.util.List;

import tsp.Instance;
import tsp.Solution;
import tsp.util.Couple;

public class Christofides extends AHeuristic{
	
	List<Integer> villesMarquees = new ArrayList<Integer>();
	
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
	public static List<List<Integer>> remove2(List<List<Integer>> l, int index, int point) {
		List<List<Integer>> t = new ArrayList<List<Integer>>();
		for(int i = 0; i < l.size(); i++) {
			t.add(new ArrayList<Integer>());
		}
		int k = 0;
		for(List<Integer> v : l) {
			for(int e : v) {
				if (k == index && e != point) {
					t.get(index).add(e);
				}else if (k != index) {
					t.get(k).add(e);
				}
			}
			k++;
		}
		return t;
	}
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
	
	/*return un arbre de recouvrement de poids minimal, arbre.get(i) renvoie les sommets suivant du sommet i*/
	public List<List<Integer>> algoPrim(Instance instance) throws Exception{
		List<List<Integer>> arbre = new ArrayList<List<Integer>>();
		for(int i = 0; i<instance.getNbCities(); i++) {
			arbre.add(new ArrayList<Integer>());
		}
		List<Integer> villesMarquees = new ArrayList<Integer>();
		villesMarquees.add(0); //On commence ‡ construire notre arbre depuis la ville 0.
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
	
	/* return le graphe induit de l'ensemble des sommets de degrÈ impair, 
	 * prend en entrÈe un arbre de recouvrement de poids minimal*/
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
	
	/*calcule deux liste L et R de sorte que G = (L,R) soit un graphe biparti (nÈcessaire pour le couplage parfait)
	 * prend en entrÈe le graphe induit par l'ensemble des sommets de degre impair, et un arbre de recouvrement*/
	public List<List<Integer>> creationGrapheBiparti(Instance instance, List<List<Integer>> graphe, List<List<Integer>> arbre) throws Exception{
		//List<List<Integer>> graphe = grapheInduit(instance, algoPrim(instance));
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
	public List<List<Integer>> creationGrapheBiparti1(Instance instance, List<List<Integer>> graphe, List<List<Integer>> arbre) throws Exception{
		//List<List<Integer>> graphe = grapheInduit(instance, algoPrim(instance));
		List<List<Integer>> biparti = new ArrayList<List<Integer>>();
		biparti.add(new ArrayList<Integer>());
		biparti.add(new ArrayList<Integer>());
		List<Integer> villesMarquees = new ArrayList<Integer>();
		List<Integer> villesAChoisir = sommetsDeDegresImpairs(arbre);
		int n = villesAChoisir.size();
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
	/*algorithme hongrois*/
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
				//(le plus ‡ gauche)
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
					if(nombreZerosNonBarres[i] < min && nombreZerosNonBarres[i] != 0) {//attention modif
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
				/*etape 3*/
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
					modif1 = true;
					modif2 = true;
					for(Couple c : zerosBarres) {
						if(lignesMarquees.contains(c.getX()) 
								&& !colonnesMarquees.contains(c.getY())) {
							colonnesMarquees.add(c.getY());
						}else {
							modif1 = false;
						}
					}
					for(Couple c : zerosEncadres) {
						if(colonnesMarquees.contains(c.getY()) && !lignesMarquees.contains(c.getX())) {
							lignesMarquees.add(c.getX());
						}else {
							modif2 = false;
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
					lignesNonMarquees.remove(k);
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
				while(sousMatrice[i][j] != 0) {
					if(j == nbColonnes - 1) {
						j = 0;
						i += 1;
					}else{
						j += 1;
					}
				}
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
				
			}
		}
		for(Couple c : zerosEncadres) {
			couplage.add(new Couple(biparti.get(0).get(c.getX()), biparti.get(1).get(c.getY())));
		}
		return couplage;
	}
	
	public List<List<Integer>> union(List<List<Integer>> arbre, List<Couple> couplage){
		List<List<Integer>> graphe = arbre;
		for(Couple c : couplage) {
			if(!graphe.get(c.getX()).contains(c.getY())) {
				graphe.get(c.getX()).add(c.getY());
				graphe.get(c.getY()).add(c.getX());
			}
		}
		return graphe;
	}
	
	public boolean isEmpty1(List<List<Integer>> G) {
		for(List<Integer> l : G) {
			if(!l.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	/*renvoie true si contient toutes les villes*/
	public boolean contient(Instance instance, List<Integer> villes) {
		for(int i = 0; i < instance.getNbCities(); i++) {
			if(!villes.contains(i)) {
				return false;
			}
		}
		return true;
	}
	public void explorer(List<List<Integer>> arbre, int villeVisitee) throws Exception {
		this.villesMarquees.add(villeVisitee);
		for(int villeFils : arbre.get(villeVisitee)) {
			if(!(this.villesMarquees.contains(villeFils))) {
				explorer(arbre, villeFils);
			}
		}
	}
	public List<Integer> tourEulerien(Instance instance, List<List<Integer>> G) throws Exception{
		List<Integer> chemin = new ArrayList<Integer>();
		List<List<Integer>> graphe = G;
		List<List<Integer>> copyGraphe = copy(G);
		int x = 0;
		chemin.add(x);
		while(!isEmpty1(graphe)) { //il faudrait tester avant si le graphe est eulerien, si il ne l'est pas modifier biparti
			
			for(int y : graphe.get(x)) {
				this.villesMarquees.clear();
				copyGraphe = remove2(copyGraphe, x, y);
				copyGraphe = remove2(copyGraphe, y, x);
				explorer(copyGraphe, y);
				copyGraphe.get(x).add(y);
				copyGraphe.get(y).add(x);
				
				if(contient(instance, this.villesMarquees)) {
					graphe = remove2(graphe, x, y);
					graphe = remove2(graphe, y, x);
					copyGraphe = remove2(copyGraphe, x, y);
					copyGraphe = remove2(copyGraphe, y, x);
					chemin.add(y);
					x = y;
					break;
				}
			}
		}
		return chemin;
	}
	
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
	
	@Override
	public void solve() throws Exception {
		List<List<Integer>> arbre = algoPrim(this.m_instance);
		List<List<Integer>> graphe = grapheInduit(this.m_instance, arbre);
		List<List<Integer>> biparti = creationGrapheBiparti1(this.m_instance, graphe, arbre);

		List<Couple> couplage = creationCouplageParfait(this.m_instance, biparti);
		/*System.out.println(couplage.get(0).getX());
		System.out.println(couplage.get(0).getY());
		System.out.println(couplage.get(1).getX());
		System.out.println(couplage.get(1).getY());
		System.out.println(couplage.get(2).getX());
		System.out.println(couplage.get(2).getY());*/
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