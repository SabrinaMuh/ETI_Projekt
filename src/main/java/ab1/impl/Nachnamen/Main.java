package ab1.impl.Nachnamen;

import ab1.DFA;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        //Test
        Ab1Impl factory = new Ab1Impl();
        Set<Character> alphabet = new HashSet<>();
        alphabet.add('a');
        alphabet.add('b');
        Set<Integer> acceptingStates = new HashSet<>();
        acceptingStates.add(1);

        DFA dfa1 = factory.createDFA(2, alphabet, acceptingStates, 0);
        dfa1.setTransition(0, 'b', 1);
        dfa1.setTransition(1, 'a', 1);
    }
}
