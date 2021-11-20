package ab1.impl.Nachnamen;

import ab1.DFA;
import ab1.NFA;
import ab1.exceptions.IllegalCharacterException;

import java.util.*;

public class NFAImpl implements NFA {
    private int numStates;
    private Set<Character> alphabet;
    private Set<Integer> acceptingStates;
    private int initialState;
    private Set[][] transitions;

    //constructor
    public NFAImpl(int numStates, Set<Character> alphabet, Set<Integer> acceptingStates, int initialState) {
        this.numStates = numStates;
        this.alphabet = alphabet;
        this.acceptingStates = acceptingStates;
        this.initialState = initialState;
        this.transitions = new Set[numStates][numStates];
        for (int i = 0; i < numStates; i++) {
            for (int j = 0; j < numStates; j++) {
                transitions[i][j] = new HashSet();
            }
        }
    }

    @Override
    public Set<Character> getAlphabet() {
        return alphabet;
    }

    @Override
    public Set<Integer> getAcceptingStates() {
        return acceptingStates;
    }

    @Override
    public int getInitialState() {
        return initialState;
    }

    @Override
    public boolean isAcceptingState(int s) throws IllegalStateException {
        if (s > numStates - 1 || s < 0) {
            throw new IllegalStateException("The state does not exist!");
        }
        return acceptingStates.contains(s);
    }

    @Override
    public void setTransition(int fromState, Character c, int toState) throws IllegalStateException, IllegalCharacterException {
        if (!alphabet.contains(c) && c != null) {
            throw new IllegalCharacterException();
        }
        if (fromState>numStates || toState>numStates || fromState < 0 || toState < 0){
            throw new IllegalStateException();
        }
        transitions[fromState][toState].add(c);
    }

    /* Beispiel fuers bessere Verstaendnis :
          0 1 2
        0 a b -
        1 - b a
        2 - - a,b
    */
    @Override
    public Set<Character>[][] getTransitions() {
        return transitions;
    }

    @Override
    public void clearTransitions(int fromState, Character c) throws IllegalStateException {
        if (isAcceptingState(fromState)) {
            for (int i = 0; i < transitions[fromState].length; i++) {
                transitions[fromState][i].remove(c);
            }
        }
    }

    @Override
    public Set<Integer> getNextStates(int state, Character c) throws IllegalCharacterException, IllegalStateException {
        Set<Integer> nextStates = new HashSet<>();
        if (!alphabet.contains(c) && c != null) {
            throw new IllegalCharacterException();
        }
        for (int i = 0; i < transitions[state].length; i++) {
            if (transitions[state][i].contains(c)) {
                nextStates.add(i);
            }
        }
        for (int i = 0; i < transitions[state].length; i++) {
            if(transitions[state][i].contains(null)){
                nextStates.add(i);
            }
        }
        return nextStates;
    }

    @Override
    public int getNumStates() {
        return numStates;
    }

    @Override
    public NFA union(NFA a) {
        return null;
    }

    @Override
    public NFA intersection(NFA a) {
        return null;
    }

    @Override
    public NFA minus(NFA a) {
        return null;
    }

    @Override
    public NFA concat(NFA a) {
        return null;
    }

    @Override
    public NFA complement() {
        return null;
    }

    @Override
    public NFA kleeneStar() {
        return null;
    }

    @Override
    public NFA plus() {
        return null;
    }

    public DFA toDFA() {
        return null;
    }

    @Override
    public Boolean accepts(String w) throws IllegalCharacterException {
        if (acceptingStates.size() == 0) return false;
        if(w.length() == 0){
            return isAcceptingState(0);
        }else {
            if (!alphabet.contains(w.charAt(0))) throw new IllegalCharacterException();

            boolean anotherStateTrue = false;
            int anotherState = 0;
            int state = 0;
            int indexOfCharforAnotherState = 0;

            List <Integer> nextStates = new ArrayList<>(getNextStates(0, w.charAt(0)));
            if (nextStates.isEmpty()) return false;
            for (int i = 1; i < w.length(); i++) {
                if (nextStates.isEmpty()) return false;
                if (!alphabet.contains(w.charAt(i))) throw new IllegalCharacterException();
                if(nextStates.size() > 1){
                    anotherStateTrue = true;
                    anotherState = nextStates.get(1);
                    indexOfCharforAnotherState = i;
                }
                state = nextStates.get(0);
                nextStates = new ArrayList<>(getNextStates(state, w.charAt(i)));
            }

            if(!nextStates.isEmpty()) {
                state = nextStates.get(0);
                if (nextStates.size() > 1 && w.length()>1) {
                    anotherStateTrue = true;
                    anotherState = nextStates.get(1);
                    indexOfCharforAnotherState = w.length() - 2;
                }
            }

            if (!isAcceptingState(state) && anotherStateTrue){
                state = anotherState;
                for (int i = indexOfCharforAnotherState; i < w.length(); i++) {
                    if (!alphabet.contains(w.charAt(i))) throw new IllegalCharacterException();
                    nextStates = new ArrayList<>(getNextStates(state, w.charAt(i)));
                    if (nextStates.isEmpty()) return false;
                    if(!isAcceptingState(state)) state = nextStates.get(0);
                }
                return isAcceptingState(state);
            }else return isAcceptingState(state);
        }
    }

    @Override
    public Boolean acceptsNothing() {
        return acceptingStates.isEmpty();
    }

    @Override
    public Boolean acceptsEpsilonOnly() {
        if (acceptingStates.size() != 1) return false;
        if (!acceptingStates.contains(0)) return false;
        List <Integer> accepting = new ArrayList<>(acceptingStates);
        for (int i = 0; i < accepting.size(); i++) {
            for (int j = 0; j < numStates; j++) {
                if (transitions[accepting.get(i)][j].size() != 0) {
                    for (int k = 0; i < numStates; i++) {
                        for (int l = 0; j < numStates; j++) {
                            if (!transitions[k][l].contains(null)) return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public Boolean acceptsEpsilon() {
        for (int i = 0; i < numStates; i++) {
            for (int j = 0; j < numStates; j++) {
                if (transitions[i][j].contains(null)&&isAcceptingState(j)) return true;
            }
        }
        return isAcceptingState(0);
    }

    @Override
    public boolean subSetOf(NFA b) {
        List<Integer> listacceptingStates = new ArrayList<>(acceptingStates);
        for (Integer listacceptingState : listacceptingStates) {
            if (!b.getAcceptingStates().contains(listacceptingState)) return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NFAImpl nfa = (NFAImpl) o;
        return numStates == nfa.numStates &&
                initialState == nfa.initialState &&
                Objects.equals(alphabet, nfa.alphabet) &&
                Objects.equals(acceptingStates, nfa.acceptingStates) &&
                Arrays.equals(transitions, nfa.transitions);
    }

}
