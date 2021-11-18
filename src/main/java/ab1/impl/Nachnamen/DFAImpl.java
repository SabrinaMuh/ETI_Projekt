package ab1.impl.Nachnamen;

import ab1.DFA;
import ab1.exceptions.IllegalCharacterException;

import java.util.Set;

public class DFAImpl extends NFAImpl implements DFA {
    //constructor (calls the constructor of the superclass)
    public DFAImpl(int numStates, Set<Character> alphabet, Set<Integer> acceptingStates, int initialState) {
        super(numStates, alphabet, acceptingStates, initialState);
    }

    @Override
    public void reset() {

    }

    @Override
    public int getCurrentState() {
        return 0;
    }

    @Override
    public int doStep(char c) throws IllegalCharacterException, IllegalStateException {
        return 0;
    }

    @Override
    public Integer getNextState(int s, char c) throws IllegalCharacterException, IllegalStateException {
        return null;
    }

    @Override
    public boolean isInAcceptingState() {
        return false;
    }
}
