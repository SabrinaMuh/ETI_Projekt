package ab1;

import java.util.Set;

/**
 * Schnittstelle zur Erzeugung von endlichen Automaten (Factory Pattern)
 */
public interface Ab1 {

	/**
	 * Erzeugt einen NFA. Die Zustände des Automaten sind 0-indiziert, dh der erste
	 * Zustand hat den Wert 0.
	 * 
	 * @param numStates
	 *            Anzahl Zustände des Automaten
	 * @param alphabet
	 *            Mögliche Zeichenmenge
	 * @param acceptingStates
	 *            Akzeptierender Zustand (0-indiziert)
	 * @param initialState
	 *            Startzutand (0-indiziert)
	 * @return einen NFA
	 */
	public NFA createNFA(int numStates, Set<Character> alphabet, Set<Integer> acceptingStates, int initialState);

	/**
	 * Erzeugt einen DFA. Die Zustände des Automaten sind 0-indiziert, dh der erste
	 * Zustand trägt den Wert 0
	 * 
	 * @param numStates
	 *            Anzahl Zustände des Automaten
	 * @param alphabet
	 *            Mögliche Zeichenmenge
	 * @param acceptingStates
	 *            Akzeptierender Zustand (0-indiziert)
	 * @param initialState
	 *            Startzutand (0-indiziert)
	 * @return einen DFA
	 */
	public DFA createDFA(int numStates, Set<Character> alphabet, Set<Integer> acceptingStates, int initialState);
}
