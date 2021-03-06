package tsp;

import tsp.heuristic.AlgorithmeGlouton;
import tsp.heuristic.ArbreCouvrant;
import tsp.heuristic.Christofides;
import tsp.metaheuristic.ColonieDeFourmis;
import tsp.heuristic.PlusProcheVoisin;
import tsp.metaheuristic.LocalSearchSwap;
import tsp.metaheuristic.TwoOpt;
import tsp.metaheuristic.AlgorithmeGenetique;
import tsp.metaheuristic.K_opt;
import tsp.metaheuristic.K_optAvecLimiteDeTemps;

/**
 * 
 * This class is the place where you should enter your code and from which you can create your own objects.
 * 
 * The method you must implement is solve(). This method is called by the programmer after loading the data.
 * 
 * The TSPSolver object is created by the Main class.
 * The other objects that are created in Main can be accessed through the following TSPSolver attributes: 
 * 	- #m_instance :  the Instance object which contains the problem data
 * 	- #m_solution : the Solution object to modify. This object will store the result of the program.
 * 	- #m_timeLimit : the maximum time limit (in seconds) given to the program.
 *  
 * @author Damien Prot, Fabien Lehuede, Axel Grimault
 * @version 2017
 * 
 */
public class TSPSolver {

	// -----------------------------
	// ----- ATTRIBUTS -------------
	// -----------------------------

	/**
	 * The Solution that will be returned by the program.
	 */
	private Solution m_solution;

	/** The Instance of the problem. */
	private Instance m_instance;

	/** Time given to solve the problem. */
	private long m_timeLimit;

	
	// -----------------------------
	// ----- CONSTRUCTOR -----------
	// -----------------------------

	/**
	 * Creates an object of the class Solution for the problem data loaded in Instance
	 * @param instance the instance of the problem
	 * @param timeLimit the time limit in seconds
	 */
	public TSPSolver(Instance instance, long timeLimit) {
		m_instance = instance;
		m_solution = new Solution(m_instance);
		m_timeLimit = timeLimit;
	}

	// -----------------------------
	// ----- METHODS ---------------
	// -----------------------------

	/**
	 * **TODO** Modify this method to solve the problem.
	 * 
	 * Do not print text on the standard output (eg. using `System.out.print()` or `System.out.println()`).
	 * This output is dedicated to the result analyzer that will be used to evaluate your code on multiple instances.
	 * 
	 * You can print using the error output (`System.err.print()` or `System.err.println()`).
	 * 
	 * When your algorithm terminates, make sure the attribute #m_solution in this class points to the solution you want to return.
	 * 
	 * You have to make sure that your algorithm does not take more time than the time limit #m_timeLimit.
	 * 
	 * @throws Exception may return some error, in particular if some vertices index are wrong.
	 */
	public void solve() throws Exception
	{
		m_solution.print(System.err);
		
		// Example of a time loop
		long startTime = System.currentTimeMillis();
		long spentTime = 0;
		/*do
		{
			// TODO
			// Code a loop base on time here
			spentTime = System.currentTimeMillis() - startTime;
		}while(spentTime < (m_timeLimit * 1000 - 100) );*/
		
		
	
		/*ArbreCouvrant method = new ArbreCouvrant(m_instance);
		method.solve();
		this.setSolution(method.getSolution());*/

		/*PlusProcheVoisin method = new PlusProcheVoisin(m_instance);
		method.solve();
		this.setSolution(method.getSolution());*/
		 
		
		/*AlgorithmeGlouton glouton = new AlgorithmeGlouton(m_instance);
		glouton.solve();
		this.setSolution(glouton.getSolution());*/
		
		/*AlgorithmeGenetique genetique = new AlgorithmeGenetique(m_instance);
		Solution s = genetique.solve(new Solution(m_instance));
		this.setSolution(s);*/

		/*LocalSearchSwap localSearchSwap = new LocalSearchSwap(m_instance);
		Solution s = localSearchSwap.solve(new Solution(m_instance));
		this.setSolution(s);*/
		
		/*Christofides Christofides = new Christofides(m_instance);
		Christofides.solve();
		this.setSolution(Christofides.getSolution());*/
		
		/*ColonieDeFourmis fourmis = new ColonieDeFourmis(m_instance);
		Solution s = fourmis.solve(new Solution( m_instance));
		this.setSolution(s);*/
		
		/*TwoOpt opt = new TwoOpt(m_instance);
		Solution s = opt.solve(new Solution(m_instance));
		this.setSolution(s);*/

		
		/*On choisit une heuristique qui donne une solution approchée en premier lieu
		 * C'est cette solution que l'on va améliorer*/
		/*PlusProcheVoisin s = new PlusProcheVoisin(this.m_instance);
		s.solve();
		K_opt method = new K_opt(m_instance);
		Solution sol = method.solve(s.getSolution());
		this.setSolution(sol);*/
		
		PlusProcheVoisin s = new PlusProcheVoisin(this.m_instance);
		s.solve();
		K_optAvecLimiteDeTemps method = new K_optAvecLimiteDeTemps(m_instance);
		Solution sol = method.solve(s.getSolution());
		this.setSolution(sol);
	}

	// -----------------------------
	// ----- GETTERS / SETTERS -----
	// -----------------------------

	/** @return the problem Solution */
	public Solution getSolution() {
		return m_solution;
	}

	/** @return problem data */
	public Instance getInstance() {
		return m_instance;
	}

	/** @return Time given to solve the problem */
	public long getTimeLimit() {
		return m_timeLimit;
	}

	/**
	 * Initializes the problem solution with a new Solution object (the old one will be deleted).
	 * @param solution : new solution
	 */
	public void setSolution(Solution solution) {
		this.m_solution = solution;
	}

	/**
	 * Sets the problem data
	 * @param instance the Instance object which contains the data.
	 */
	public void setInstance(Instance instance) {
		this.m_instance = instance;
	}

	/**
	 * Sets the time limit (in seconds).
	 * @param time time given to solve the problem
	 */
	public void setTimeLimit(long time) {
		this.m_timeLimit = time;
	}

}
