package ab1;

import java.util.Set;

import ab1.exceptions.IllegalCharacterException;

/**
 * Schnittstelle eines NFA. Der Automat numeriert die Zustände von 0 * bis
 * numStates-1 (siehe {@link ab1.Ab1}).
 */
public interface NFA {
	/**
	 * @return Menge der möglichen Zeichen
	 */
	public Set<Character> getAlphabet();

	/**
	 * @return Menge der Endzustände
	 */
	public Set<Integer> getAcceptingStates();

	/**
	 * @return Startzustand
	 */
	public int getInitialState();

	/**
	 * @param s zu testender Zustand
	 * @throws IllegalArgumentException Wenn es den Zustand nicht gibt
	 * @return true, wenn der Zustand s ein Endzustand ist. Ansonsten false.
	 */
	public boolean isAcceptingState(int s) throws IllegalStateException;

	/**
	 * Liefert die Transitionsmatrix. Jeder Eintrag der Matrix ist eine
	 * Menge, welche die Zeichen angibt, für die dieser Übergang definiert
	 * sind. "null" entspricht dem leeren Wort.
	 * 
	 * @return Die Transiationsmatrix mit allen Übergängen
	 */
	public Set<Character>[][] getTransitions();

	/**
	 * Setzt einen Übergang.
	 * 
	 * @param fromState Ausgangszustand
	 * @param c gelesenes Zeichen. "null" entspricht dem leeren Wort
	 * @param toState Folgezustand
	 * @throws IllegalStateException Falls die Zustände nicht existieren
	 * @throws IllegalCharacterException Falls das Zeichen nicht erlaubt ist.
	 */
	public void setTransition(int fromState, Character c, int toState) throws IllegalStateException, IllegalCharacterException;

	/**
	 * Löscht alle Transitionen mit dem Zeichen c, welche von dem gegebenen Zustand ausgehen
	 * 
	 * @param fromState Ausgangszustand
	 * @param c das Zeichen
	 * @throws IllegalStateException Falls der Zustand nicht erlaubt ist.
	 */
	public void clearTransitions(int fromState, Character c) throws IllegalStateException;

	/**
	 * Liefert die Menge aller Zustände, zu denen man durch Abarbeitung des Zeichens
	 * kommt.
	 * 
	 * @param state Ausgangszustand
	 * @param c das Zeichen
	 * @return die Menge der "nächsten" Zustände
	 * @throws IllegalCharacterException Falls das Zeichen nicht erlaubt ist.
	 * @throws IllegalStateException Falls der Zustand nicht erlaubt ist.
	 */
	public Set<Integer> getNextStates(int state, Character c) throws IllegalCharacterException, IllegalStateException;
	
	/**
	 * Liefert die Anzahl der Zustände des Automaten
	 *
	 * @return die Anzahl der Zustände des Automaten
	 */
	public int getNumStates();
	
	/**
	 * Erzeugt einen Automaten, der die Vereinigung des Automaten mit dem übergebenen Automaten darstellt.
	 *
	 * @param a der zu vereinigende Automat
	 * @return neuer Automat
	 */
	public NFA union(NFA a);
	
	/**
	 * Erzeugt einen Automaten, der den Durchschnitt des Automaten mit dem übergebenen Automaten darstellt.
	 * @param a der zu schneidende Automat
	 * @return neuer Automat
	 */
	public NFA intersection(NFA a);
	
	/**
	 * Erzeugt die Differenz zu dem übergebenen Automaten
	 * @return neuer Automat
	 */
	public NFA minus(NFA a);
	
	/**
	 * Hängt den Automaten a an den Automaten an
	 *
	 * @param a der anzuhängende Automat
	 * @return neuer Automat
	 */
	public NFA concat(NFA a);
	
	/**
	 * Erzeugt das Komplement des Automaten
	 * @return neuer Automat
	 */
	public NFA complement();
	
	/**
	 * Bildet den Kleene-Stern des Automaten
	 * @return neuer Automat
	 */
	public NFA kleeneStar();
	
	/**
	 * Erzeugt L(a)+
	 * @return neuer Automat
	 */
	public NFA plus();

	/**
	 * Erzeugt einen DFA, der die selbe Sprache akzeptiert
	 * @return den DFA
	 */
	public DFA toDFA();
	
	/**
	 * Prüft, ob das Wort w durch den Automaten akzeptiert wird.
	 *
	 * @param w das abzuarbeitende Wort
	 * @return true, wenn das Wort w akzeptiert wird, andernfalls false.
	 * @throws IllegalCharacterException falls das Wort aus nicht erlaubten Zeichen besteht
	 */
	public Boolean accepts(String w) throws IllegalCharacterException;

	/**
	 * Überprüft, ob der Automat kein einziges Wort (also die leere Sprache) aktzeptiert.
	 *
	 * @return true, wenn der Automat nichts akzeptiert, andernfalls false.
	 */
	public Boolean acceptsNothing();

	/**
	 * Überprüft, ob der Automat nur das leere Wort aktzeptiert
	 *
	 * @return true, wenn nur das leere Wort akzeptiert wird, andernfalls false.
	 */
	public Boolean acceptsEpsilonOnly();

	/**
	 * Überprft, ob der Automat das leere Wort aktzeptiert (und womöglich auch mehr).
	 *
	 * @return true, wenn das leere Wort aktzeptiert wird, andernfalls false.
	 */
	public Boolean acceptsEpsilon();
		
	/**
	 * Überprüft, ob der übergebene Automat eine Übermenge der Sprache
	 * dieses Automaten erkennt. Tipp: Wird von einer Menge (bzw. Sprache)
	 * eine Übermenge abgezogen (Mengendifferenz), so ist die Ergebnismenge
	 * leer.
	 *
	 * @return True, wenn jedes Wort, dass dieser Automat akzeptiert, auch
	 *         von b akzeptiert wird. Sonst false.
	 */
	public boolean subSetOf(NFA b);

	/**
	 * Überprüft, ob der übergebene Automat die selbe Sprache akzeptiert.
	 *
	 * @return True, wenn das übergebene Objekt ein Automat ist, der die selbe Sprache akzeptiert.
	 */
	@Override
	public boolean equals(Object b);
}
