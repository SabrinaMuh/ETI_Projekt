package ab1.impl.Nachnamen;

import ab1.DFA;
import ab1.exceptions.IllegalCharacterException;

import java.util.Set;

public class DFAImpl extends NFAImpl implements DFA {
    private int currentState;

    //constructor (calls the constructor of the superclass)
    public DFAImpl(int numStates, Set<Character> alphabet, Set<Integer> acceptingStates, int initialState) {
        super(numStates, alphabet, acceptingStates, initialState);
        currentState = initialState;
    }

    @Override
    public void reset() {
        currentState = getInitialState();
    }

    @Override
    public int getCurrentState() {
        return currentState;
    }

    @Override
    public int doStep(char c) throws IllegalCharacterException, IllegalStateException {
        if (!isExistingChar(c)) {
            throw new IllegalCharacterException();
        }
        if (getNextState(currentState, c) == null) {
            throw new IllegalStateException();
        }
        currentState = getNextState(currentState, c);
        return currentState;
    }

    @Override
    public Integer getNextState(int s, char c) throws IllegalCharacterException, IllegalStateException {
        if (!isExistingChar(c)) {
            throw new IllegalCharacterException();
        }
        if (!isExistingState(s)) {
            throw new IllegalStateException();
        }

        Set<Character>[][] transitions = getTransitions();
        for (int i = 0; i < transitions.length; i++) {
            if (transitions[s][i].contains(c)) return i;
        }
        //wenn kein Folgezustand gefunden wurde, return null
        return null;
    }

    @Override
    public boolean isInAcceptingState() {
        if (getAcceptingStates().contains(currentState)) return true;
        return false;
    }

    @Override
    public void setTransition(int fromState, Character c, int toState) throws IllegalStateException, IllegalCharacterException {
        if (!isExistingChar(c) || c == null) {
            throw new IllegalCharacterException();
        }
        if (!isExistingState(fromState) || !isExistingState(toState)){
            throw new IllegalStateException();
        }
        Set<Character>[][] transitions = getTransitions();
        clearTransitions(fromState, c);
        transitions[fromState][toState].add(c);
    }
}
