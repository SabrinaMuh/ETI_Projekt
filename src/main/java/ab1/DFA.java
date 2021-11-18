package ab1;
import ab1.exceptions.IllegalCharacterException;


/**
 * Schnittstelle eines DFA. Der Automat numeriert die Zustände von 0 * bis
 * numStates-1 (siehe {@link ab1.Ab1}).
 */
public interface DFA extends NFA {

	/**
	 * Setzt den Automaten wieder auf den Startzustand.
	 */
	public void reset();

	/**
	 * 
	 * @return den aktuell aktiven Zustand des Automaten
	 */
	public int getCurrentState();

	/**
	 * Veranlasst den Automaten, ein Zeichen abzuarbeiten. Ausgehend vom aktuellen
	 * Zustand wird das Zeichen c abgearbeitet und der Automat befindet sich danach
	 * im Folgezustand. Ist das Zeichen c kein erlaubtes Zeichen, so wird eine
	 * IllegalArgumentException geworfen. Andernfalls liefert die Methode den neuen
	 * aktuellen Zustand. Ist kein Folgezustand definiert, wird eine
	 * IllegalStateException geworfen und der aktuell Zustand bleibt erhalten.
	 * 
	 * @param c das abzuarbeitende Zeichen
	 * @return den aktuellen Zustand nach der Abarbeitung des Zeichens
	 * @throws IllegalCharacterException Falls das Zeichen nicht erlaubt ist.
	 * @throws IllegalStateException Falls kein Folgezustand definiert ist.
	 */
	public int doStep(char c) throws IllegalCharacterException, IllegalStateException;

	/**
	 * Liefert den Zustand, der erreicht wird, wenn im Zustand s das Zeichen c
	 * gelesen wird. Ist das Zeichen c kein erlaubtes Zeichen, so wird eine
	 * IllegalCharacterException geworfen. Ist der Zustand s kein erlaubter Zustand,
	 * so wird eine IllegalStateException geworfen.
	 * 
	 * @param s der Zustand
	 * @param c das Zeichen
	 * @return Folgezustand, oder null, wenn es keinen Folgezustand gibt
	 * @throws IllegalCharacterException Falls das Zeichen nicht erlaubt ist.
	 * @throws IllegalStateException Falls der Zustand s nicht erlaubt ist.
	 */
	public Integer getNextState(int s, char c) throws IllegalCharacterException, IllegalStateException;

	/**
	 * Prüft, ob der aktuelle Zustand ein aktzeptierender Zustand ist.
	 * 
	 * @return true, wenn der aktuelle Zustand ein Endzustand ist. Ansonsten false.
	 */
	public boolean isInAcceptingState();

	/**
	 * Setzt einen Übergang. Das übergebene Zeichen darf nicht "null" sein
	 * (keine Epsilon-Übergänge in DFAs). Falls doch, so soll eine
	 * IllegalCharacterException geworfen werden. Ist ein Übergang für den
	 * angegebenen Ausgangszustand und das angegebene Zeichen bereits
	 * definiert, so soll dieser durch den neuen ersetzt werden (ein DFA
	 * hat nur einen Übergang pro Zustand und Zeichen).
	 * 
	 * @param fromState Ausgangszustand
	 * @param c gelesenes Zeichen
	 * @param toState Folgezustand
	 * @throws IllegalStateException wenn der Zustand nicht erlaubt ist
	 * @throws IllegalCharacterException wenn das übergebene Zeichen nicht erlaubt, oder null ist.
	 */
	public void setTransition(int fromState, Character c, int toState)
			throws IllegalStateException, IllegalCharacterException;
}
