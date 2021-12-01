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
    private Set<Character>[][] transitions;

    //constructor
    public NFAImpl(int numStates, Set<Character> alphabet, Set<Integer> acceptingStates, int initialState) {
        this.numStates = numStates;
        this.alphabet = alphabet;
        this.acceptingStates = acceptingStates;
        this.initialState = initialState;
        this.transitions = new Set[numStates][numStates];
        for (int i = 0; i < numStates; i++) {
            for (int j = 0; j < numStates; j++) {
                transitions[i][j] = new HashSet<Character>();
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
        if (!isExistingState(s)) {
            throw new IllegalStateException("The state does not exist!");
        }
        return acceptingStates.contains(s);
    }

    @Override
    public void setTransition(int fromState, Character c, int toState) throws IllegalStateException, IllegalCharacterException {
        if (!isExistingChar(c) && c != null) {
            throw new IllegalCharacterException();
        }
        if (!isExistingState(fromState) || !isExistingState(toState)){
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
    public int getNumStates() {
        return numStates;
    }

    //ueberpruefen, ob der Zustand existiert
    public boolean isExistingState(int s) {
        if (s < 0 || s > getNumStates() - 1) return false;
        return true;
    }

    //ueberpruefen, ob der Character in dem Alphabet vorhanden ist
    public boolean isExistingChar(Character c) {
        if (getAlphabet().contains(c)) {
            return true;
        }
        return false;
    }

    @Override
    public void clearTransitions(int fromState, Character c) throws IllegalStateException {
        if (!isExistingState(fromState)) throw new IllegalStateException();
        for (int i = 0; i < transitions.length; i++) {
            transitions[fromState][i].remove(c);
        }
    }

    @Override
    public Set<Integer> getNextStates(int state, Character c) throws IllegalCharacterException, IllegalStateException {
        Set<Integer> epsStates = getEpsilonStates(state);
        Set<Integer> nextStates = getNextDeterministicStates(state, c);

        boolean changed = false;

        do {
            int nextStatesSizeBefore = nextStates.size();
            int epsStatesSizeBefore = epsStates.size();

            Set<Integer> epsStatesNew = new HashSet<Integer>();

            for (Integer s : epsStates) {
                nextStates.addAll(getNextDeterministicStates(s, c));
                epsStatesNew.addAll(getEpsilonStates(s));
            }
            epsStates.addAll(epsStatesNew);

            changed = nextStatesSizeBefore != nextStates.size() || epsStatesSizeBefore != epsStates.size();
        } while (changed);

        Set<Integer> epsAfter = new HashSet<>();
        for (int s : nextStates) {
            epsAfter.addAll(getEpsilonStates(s));
        }
        nextStates.addAll(epsAfter);

        return nextStates;
    }

    public Set<Integer> getNextDeterministicStates(int state, Character c) throws IllegalCharacterException, IllegalStateException {
        if (!isExistingChar(c) && c != null) {
            throw new IllegalCharacterException();
        }
        if (!isExistingState(state)) throw new IllegalStateException();

        Set<Integer> nextStates = new HashSet<>();
        for (int i = 0; i < transitions[state].length; i++) {
            if (transitions[state][i].contains(c)) {
                nextStates.add(i);
            }
        }
        return nextStates;
    }

    public Set<Integer> getEpsilonStates(int state) throws IllegalStateException {
        if (!isExistingState(state)) throw new IllegalStateException();
        Set<Integer> nextEpsStates = new HashSet<>();
        for (int i = 0; i < transitions[state].length; i++) {
            if (transitions[state][i].contains(null)) {
                nextEpsStates.add(i);
            }
        }
        return nextEpsStates;
    }

    /*
    1) Fueg einen neuen Zustand 0 hinzu.
    2) Erstell 2 neuen Epsilon-Verbindungen zwischen den neuen Zustand und den Startzustaenden beider Automaten
     */
    @Override
    public NFA union(NFA a) {
        //neues Alphabet
        Set<Character> unionAlphabet = new HashSet<>();
        unionAlphabet.addAll(a.getAlphabet());
        unionAlphabet.addAll(this.getAlphabet());

        //akzeptierende Endzustaende
        Set<Integer> unionAccStates = new HashSet<>();
        //unionAccStates.addAll(this.getAcceptingStates());
        for (int accState : this.getAcceptingStates()) {
            unionAccStates.add(accState + 1);
        }
        for (int accState : a.getAcceptingStates()) {
            unionAccStates.add(accState + this.numStates + 1);
        }

        //create new NFA
        NFA unionFA = new NFAImpl(this.numStates + a.getNumStates() + 1, unionAlphabet, unionAccStates, 0);

        //set transition matrix (the matrix was already created in the constructor)
        //1) set 2 new Epsilon-transitions
        unionFA.setTransition(0, null, 1);
        unionFA.setTransition(0, null, this.numStates + 1);

        //2) copy first transition matrix
        Set<Character>[][] unionMatrix = unionFA.getTransitions();
        Set<Character>[][] matrix1 = this.getTransitions();
        for (int i = 0; i < this.numStates; i++) {
            for (int j = 0; j < this.numStates; j++) {
                if (!matrix1[i][j].isEmpty()) {
                    unionMatrix[i + 1][j + 1].addAll(matrix1[i][j]);
                }
            }
        }

        //3) copy second matrix
        Set<Character>[][] matrix2 = a.getTransitions();
        for (int i = 0; i < matrix2.length; i++) {
            for (int j = 0; j < matrix2.length; j++) {
                if (!matrix2[i][j].isEmpty()) {
                    unionMatrix[i + 1 + matrix1.length][j + 1 + matrix1[0].length].addAll(matrix2[i][j]);
                }
            }
        }

        return unionFA;
    }

    //L1 complement in Vereinigung nit L2 complement
    @Override
    public NFA intersection(NFA a) {
        if (!this.getAcceptingStates().isEmpty()) {
            //1) Komplement von L1
            NFA complement1 = this.complement();

            //2) Komplement von L2
            NFA complement2 = a.complement();

            //TODO: Eine Lösung für das n4 Probleme finden (unereichbare Zustände löschen)

            //3) Vereinigung von Komplement L1 und Komplement L2
            NFA union = complement1.union(complement2);

            NFA complement = union.complement();

            //4) Komplement von dieser Vereinigung
            return complement;
        }else return this;
    }

    //complement(complement L1 vereinigt mit L2)
    @Override
    public NFA minus(NFA a) {
        if (this.acceptsNothing()) {return this;}
        NFA l1 = this.complement();
        NFA l1vl2 = l1.union(a);

        return l1vl2.complement();
    }

    //von jedem Endzustand mach einen Epsilon Uebergang zu dem Startzustand des zweiten Ausomates
    @Override
    public NFA concat(NFA a) {
        //neues Alphabet ist die Vereinigung von zwei Automaten
        Set<Character> concatAlphabet = new HashSet<>();
        concatAlphabet.addAll(a.getAlphabet());
        concatAlphabet.addAll(this.getAlphabet());

        //ausrechne akzeptierende Zustaende
        Set<Integer> concatAccStates = new HashSet<>();
        for (int accState : a.getAcceptingStates()) {
            concatAccStates.add(accState + this.numStates);
        }

        //erstell einen neuen NFA
        NFA concatFA = new NFAImpl(this.numStates + a.getNumStates(), concatAlphabet, concatAccStates, 0);

        //fuell die Transitionsmatrix aus
        //1) kopiere die Transitionsmatrix von dem ersten Automaten
        Set<Character>[][] concatMatrix = concatFA.getTransitions();
        Set<Character>[][] matrix1 = this.getTransitions();
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1.length; j++) {
                concatMatrix[i][j].addAll(matrix1[i][j]);
            }
        }

        //2) kopiere die Transitionsmatrix von dem zweiten Automaten
        Set<Character>[][] matrix2 = a.getTransitions();
        for (int i = 0; i < matrix2.length; i++) {
            for (int j = 0; j < matrix2.length; j++) {
                concatMatrix[i + numStates][j + numStates].addAll(matrix2[i][j]);
            }
        }

        //3) erstelle Epsilon - Uebergaenge
        for (int accState : acceptingStates) {
            concatFA.setTransition(accState, null, numStates);
        }

        return concatFA;
    }

    public void setTransitions(Set<Character>[][] transitions){
        this.transitions = transitions;
    }

    @Override
    public NFA complement() {
        DFA dfa = this.toDFA();
        //Schon jetzt in eine NFA umwandeln, damit die setTransition Funktion von DFA nicht die vorhandenen Verbindungen umschreibt
        //numStates + 1, weil man sonst nicht richtig die Falle einbauen kann
        NFAImpl nfa = new NFAImpl(dfa.getNumStates()+1, dfa.getAlphabet(), dfa.getAcceptingStates(), dfa.getInitialState());

        Set<Character> [][] transDfa = dfa.getTransitions();
        //Befüllt nfa mit den Transitions von dfa
        for (int i = 0; i < dfa.getNumStates(); i++) {
            for (int j = 0; j < dfa.getNumStates(); j++) {
                nfa.transitions[i][j] = transDfa[i][j];
            }
        }

        //1) Falle
        int trapstate = nfa.getNumStates() - 1;

        //Dann alle Zustände mit dem Fallenzustand verbinden
        //und es dürfen nur die Buchstaben eingelesen werden, die nicht zu einem gültigen Pfad führen
        for (int i = 0; i < nfa.getNumStates(); i++) {
            //verwendete Buchstaben sammeln
            List<Character> used = new ArrayList<>();
            for (char c: nfa.getAlphabet()) {
                for (int j = 0; j < nfa.getNumStates(); j++) {
                    if(nfa.transitions[i][j].contains(c)) used.add(c);
                }
            }
            for (char c: nfa.getAlphabet()) {
                if(!used.contains(c)) nfa.setTransition(i, c, trapstate);
            }
        }

        for (char c: dfa.getAlphabet()) {
            nfa.setTransition(trapstate, c, trapstate);
        }

        //2) Umwandlung akzeptierende Zustände in nicht akzeptierende Zustände
        //Sammeln von nicht akzeptierbare Zustände
        Set <Integer> notAcceptingStates = new HashSet<>();
        for (int i = 0; i < nfa.getNumStates(); i++) {
            if(!nfa.getAcceptingStates().contains(i)) notAcceptingStates.add(i);
        }

        if(!notAcceptingStates.contains(trapstate)) notAcceptingStates.add(trapstate);

        NFAImpl complement_NFA = new NFAImpl(nfa.getNumStates(), nfa.getAlphabet(), notAcceptingStates, 0);

        complement_NFA.setTransitions(nfa.getTransitions());

        return complement_NFA;

    }

    @Override
    public NFA kleeneStar() {
        //akzeptierende Endzustaende
        Set<Integer> acceptingStatesNew = new HashSet<>();
        acceptingStatesNew.add(0);
        for (int state: acceptingStates) {
            acceptingStatesNew.add(state + 1);
        }
        for (int i : acceptingStatesNew) {
            System.out.print(i + " ");
        }

        //erstelle NFA
        NFA nfaStar = new NFAImpl(numStates + 1, alphabet, acceptingStatesNew, 0);

        //befuelle Transitionsmatrix
        //1) kopiere die urspruengliche Matrix
        Set<Character>[][] starMatrix = nfaStar.getTransitions();
        for (int i = 0; i < transitions.length; i++) {
            for (int j = 0; j < transitions.length; j++) {
                starMatrix[i + 1][j + 1].addAll(transitions[i][j]);
            }
        }

        //2) erstelle Epsilon - Uebergaenge
        nfaStar.setTransition(0, null, 1);
        for (int state : acceptingStatesNew) {
            nfaStar.setTransition(state, null, 0);
        }

        return nfaStar;
    }

    //L+ = L konkateniert mit L *
    @Override
    public NFA plus() {
        NFA lStar = kleeneStar();
        return concat(lStar);
    }

    public DFA toDFAOld() {
        LinkedList<Integer> toExplore = new LinkedList<>();     //here I will write the new "composed" nodes
        toExplore.add(0);   //I will explore 0 (initial node) at first
        int currentExplored = 0;  //I start with the first element (0)
        //write combinated states in the HashMap, otherwise we can't know which states are "hidden" inside
        HashMap<Integer, Set<Integer>> hiddenStates = new HashMap<>();

        Set<Integer> startSet = new HashSet<>();
        startSet.add(0);
        hiddenStates.put(0, startSet);     //0 in NFA corresponds 0 in DFA

        int numNewStates = 1;   //amount of the states in DFA
        //accepting states of DFA
        Set<Integer> accStates = new HashSet<>();
        if (acceptingStates.contains(initialState)) {
            accStates.add(initialState);
        }
        Set<Integer> nextStates = new HashSet<>();
        //till there is smt to explore, do
        while (currentExplored < toExplore.size()) {
            //for each character in alphabet search for a possible transitions
            for (Character ch : alphabet) {
                //get next states for each possible state
                for (int i = 0; i < hiddenStates.get(currentExplored).size(); i++) {
                    nextStates.addAll(getNextStates(i, ch));   //get next states
                }

                if (!hiddenStates.containsValue(nextStates) && !nextStates.isEmpty()) {
                    hiddenStates.put(numNewStates, nextStates);
                    toExplore.add(numNewStates);    //add a new state to explore
                    for (int state : nextStates) {
                        if (this.acceptingStates.contains(state)) {
                            accStates.add(numNewStates);
                        }
                    }
                    numNewStates++;
                }
            }
            currentExplored++;  //explore the next element
        }
        //create DFA
        DFA dfa = new DFAImpl(numNewStates, alphabet, accStates, 0);
        //now we have the number of states in the new DFA and we know, which states of NFA are "hidden" ind the states of DFA
        //now we can fill in transtition matrix
        for (int i = 0; i < hiddenStates.size(); i++) {
            Set<Integer> tempSet = hiddenStates.get(i);
            Set<Integer> tempSetNext = new HashSet<>();
            for (Character ch : alphabet) {
                for (int num : tempSet) {
                    tempSetNext.addAll(getNextStates(num, ch));
                }
                    for (int key : hiddenStates.keySet()) {
                        if (hiddenStates.get(key).equals(tempSetNext)) {
                            dfa.setTransition(i, ch, key);
                        }
                    }

                tempSetNext.clear();
            }

        }

        return dfa;
    }

    /*
    public DFA toDFA() {
        //create DFA
        DFAImpl dfa = new DFAImpl(this.numStates, alphabet, null, 0);
        //write combinated states in the HashMap, otherwise we can't know which states are "hidden" inside
        HashMap<Integer, Set<Integer>> hiddenStates = new HashMap<>();
        Set<Integer> startSet = new HashSet<>();
        startSet.add(0);
        hiddenStates.put(0, startSet);     //0 in NFA corresponds 0 in DFA

        Set<Integer> nextStates = new HashSet<>();
        //accepting states of DFA
        Set<Integer> accStates = new HashSet<>();
        //till there is smt to explore, do
        int hiddenStatesSize = hiddenStates.size();
        for (int i = 0; i < hiddenStatesSize; i++) {
            //for each character in alphabet search for a possible transitions
            for (Character ch : alphabet) {
                //get next states for each possible state
                for (int state : hiddenStates.get(i)) {
                    nextStates.addAll(getNextStates(state, ch));
                }

                System.out.println(nextStates.size());

                if (!hiddenStates.containsValue(nextStates) && !nextStates.isEmpty()) {
                    int pos = hiddenStatesSize;
                    Set<Integer> copy = new HashSet<>(nextStates);
                    hiddenStates.put(pos, copy);
                    hiddenStatesSize++;
                }
                //set new transition
                if (!nextStates.isEmpty()) {
                    dfa.setTransition(i, ch, hiddenStates.size() - 1);
                }

                nextStates.clear();
            }
        }

        for (int i = 0; i < hiddenStates.size(); i++) {
            for (int state : hiddenStates.get(i)) {
                if (this.acceptingStates.contains(state)) {
                    accStates.add(state);
                }
            }
        }

        dfa.setNumStates(hiddenStates.size());
        dfa.setAcceptingStates(accStates);

        return dfa;
    }
    */

    public DFA toDFA() {

        //copy starting state
        //NFAImpl toDFA = this.copy();

        //look where to go from state
        //for each char in alphabet: generate a new Set<states> (state node) with all the states that are reached from the state
        // (if null, go one step further), repeat this step till not null (for each state inside the state node)
        //repeat for each state-node

        DFAImpl dfa = new DFAImpl(this.numStates, alphabet,null,0); //NUMSTATES HAS TO BE CHANGED LATER

        //Repeat until everything is finished

        ArrayList<Set<Integer>> list = new ArrayList<>();
        //add start state
        Set<Integer> startSet = new HashSet<>();

        startSet.add(initialState);
        startSet.addAll(getEpsilonStates(initialState));
        list.add(startSet);

        Set<Integer> fromStateNode = new HashSet<>();

        int l = list.size();
        for(int i = 0; i < l; i++) {

            //load in startNode
            fromStateNode = list.get(i);

            for (char c : alphabet) {
                Set<Integer> toStateNode = new HashSet<>();

                for (int y : fromStateNode) {
                    toStateNode.addAll(getNextStates(y, c));
                }

                if(!toStateNode.isEmpty()) {
                    if(!list.contains(toStateNode)){
                        l++;
                        list.add(toStateNode);
                    }
                    //Vermeidet bei n7 eine IllegalStateException
                    if(list.size() <= dfa.getNumStates()) dfa.setTransition(list.indexOf(fromStateNode), c, list.indexOf(toStateNode));
                    //dfa.setTransition(list.indexOf(fromStateNode), c, list.indexOf(toStateNode));
                }
            }

        }

        /*
        //delete unneeded matrix fields
        Set<Character> [][] newTrans = new Set[list.size()][list.size()];;
        if (dfa.getTransitions().length > list.size()) {
            Set<Character> [][] dfaTrans = dfa.getTransitions();
            int diff = dfa.getTransitions().length - list.size();

            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    newTrans[i][j] = dfaTrans[i][j];
                }
            }
        }

        dfa.setTransitions(newTrans);
        
         */

//check for each of those new states if contains any accepting states
        Set<Integer> newAcceptingStates = new HashSet<Integer>();
        int idx = 0;
        for(Set<Integer> set : list){
            for(int i : set){
                if(this.acceptingStates.contains(i)){
                    newAcceptingStates.add(idx);
                }

            }
            idx++;
        }

        dfa.setAcceptingStates(newAcceptingStates);


        //dfa.numStates=(int)list.stream().count();
        dfa.setNumStates(numStates);

        //now we have essentially our DFA, problem being our states are still a "set"
        //iterate through set of states, add a new state Set<"counter"> and duplicate all it's transitions (and check for accepting states)
        //repeat for all states

        //done (hopefully)

        return dfa;
    }


    public void setNumStates(int numStates) {
        this.numStates = numStates;
    }

    public void setAcceptingStates(Set<Integer> newAccStates) {
        this.acceptingStates = newAccStates;
    }


    @Override
    public Boolean accepts(String w) throws IllegalCharacterException {
        if (acceptsNothing()) return false;  //es wird nichts akzeptiert
        if (w.length() == 0) {
            return acceptsEpsilon();
        }

        Set<Integer> currentStates = new HashSet<>();
        Set<Integer> newStates = new HashSet<>();
        currentStates.add(0);   //wir starten mit dem Zustand 0

        for (int i = 0; i < w.length(); i++) {
            char c = w.charAt(i);
            if (!isExistingChar(c)) throw new IllegalCharacterException();

            for (Integer state : currentStates) {
                newStates.addAll(getNextStates(state, c));
            }

            currentStates.clear();
            currentStates.addAll(newStates);
            newStates.clear();
        }

        for (Integer state : currentStates) {
            if (acceptingStates.contains(state)) return true;
        }
        return false;
    }

    //kann Fehler beinhalten - bzw. kann die Methode Unreachable die Fehler beinhalten
    @Override
    public Boolean acceptsNothing() {
        //wenn wir keine akzeptierende Zustaende haben, dann akzeptiert Automat nichts
        if (acceptingStates.isEmpty()) return true;
        Set<Integer> unreachable = getUnreachableStatesNew();
        for (int i : unreachable) {
            System.out.print(i + " ");
        }
        //wenn wir keine unerreichbare Zustaende haben und die Menge der akzeptiernde Zustaende ist nicht leer, dann return false
        if (unreachable.isEmpty()) return false;
        //manchmal kann das sein, dass die akzeptierende Zustaende unerreichbar sind
        int counter = 0;
        for (int state : acceptingStates) {
            if (unreachable.contains(state)) counter++;
        }
        if (counter == acceptingStates.size()) return true;
        return false;
    }

    @Override
    public Boolean acceptsEpsilonOnly() {
        //if (acceptsNothing()) return false;
        Set<Integer> epsilonStates = new HashSet<>();
        epsilonStates.add(0);
        epsilonStates.addAll(getEpsilonStates(0));
        int epsilonStatesSizeBefore;

        do {
            epsilonStatesSizeBefore = epsilonStates.size();
            Set<Integer> epsStatesNew = new HashSet<>();
            for (Integer state : epsilonStates) {
                epsStatesNew.addAll(getEpsilonStates(state));
            }
            epsilonStates.addAll(epsStatesNew);
        } while (epsilonStatesSizeBefore != epsilonStates.size());      //wenn keine neue Epsilon Zustaende hinzugefuegt werden, dann bricht die Schleife ab

        //ueberpruefe, ob das leere Wort ueberhaupt akzeptiert wird
        Set<Integer> acceptedEpsStates = new HashSet<>();
        for (Integer state : epsilonStates) {
            if (acceptingStates.contains(state)) acceptedEpsStates.add(state);
        }
        if (acceptedEpsStates.size() == 0) return false;

        //neue Ueberpruefung
        Set<Integer> unreachable = getUnreachableStatesNew();
        if (acceptingStates.size() > acceptedEpsStates.size()) {
            for (int state : acceptingStates) {
                if (!acceptedEpsStates.contains(state) && !unreachable.contains(state)) return false;
            }
        }

        //ueberpruefe, ob es irgendeinen anderen Weg gibt zu dem akzeptierenden Zustand zu kommen
        for (Integer state : acceptedEpsStates) {
            for (int i = 0; i < numStates; i++) {
                if (transitions[i][state].size() > 0 && !transitions[i][state].contains(null) && !getUnreachableStatesNew().contains(i)) return false;
                if (transitions[i][state].size() > 1) return false;
            }
        }

        return true;
    }

    @Override
    public Boolean acceptsEpsilon() {
        //if (acceptsNothing()) return false;
        Set<Integer> epsilonStates = new HashSet<>();
        epsilonStates.add(0);
        epsilonStates.addAll(getEpsilonStates(0));
        int epsilonStatesSizeBefore;

        do {
            epsilonStatesSizeBefore = epsilonStates.size();
            Set<Integer> epsStatesNew = new HashSet<>();
            for (Integer state : epsilonStates) {
                epsStatesNew.addAll(getEpsilonStates(state));
            }
            epsilonStates.addAll(epsStatesNew);
        } while (epsilonStatesSizeBefore != epsilonStates.size());      //wenn keine neue Epsilon Zustaende hinzugefuegt werden, dann bricht die Schleife ab

        for (Integer state : epsilonStates) {
            if (acceptingStates.contains(state)) return true;
        }

        return false;
    }

    @Override
    public boolean subSetOf(NFA b) {
        List<Integer> listacceptingStates = new ArrayList<>(acceptingStates);
        for (Integer listacceptingState : listacceptingStates) {
            if (!b.getAcceptingStates().contains(listacceptingState)) return false;
        }
        return true;
    }

    //die Methode ueberprueft, ob es unerreichbare Zustaende gibt, und gibt true oder false zurueck
    public boolean unreachableStates() {
        int counter = 0;
        //wir starten mit 1, weil 0 immer erreichbar ist
        for (int i = 1; i < numStates; i++) {
            for (int j = 0; j < numStates; j++) {
                if (transitions[j][i].isEmpty()) counter++;     //falls wir nicht zum Zustand kommen koennen, erhoehe den Counter
            }
            if (counter == numStates) return true;
        }
        return false;
    }

    public Set<Integer> getUnreachableStatesNew() {
        Set<Integer> nextStates = new HashSet<>();
        Set<Integer> reachable = new HashSet<>();
        nextStates.add(0);
        nextStates.addAll(getEpsilonStates(0));
        reachable.addAll(nextStates);
        int reachableSizeBefore;
        do {
            reachableSizeBefore = reachable.size();

            Set<Integer> nextStatesNew = new HashSet<>();
            for (int state : nextStates) {
                for (char ch : alphabet) {
                    nextStatesNew.addAll(getNextDeterministicStates(state, ch));
                    nextStatesNew.addAll(getEpsilonStates(state));
                }

            }
            nextStates.clear();
            nextStates.addAll(nextStatesNew);
            reachable.addAll(nextStatesNew);       //make a copy because of Concurent Modification Error

        } while (reachableSizeBefore != reachable.size());

        Set<Integer> unreachable = new HashSet<>();

        for (int i = 0; i < numStates; i++) {
            if (reachable.contains(i))
                continue;
            else {
                unreachable.add(i);
            }
        }

        return unreachable;
    }

    //kann Fehler beinhalten
    public Set<Integer> getUnreachableStates() {
        Set<Integer> unreachable = new HashSet<>();
        int counter = 0;
        for (int i = 1; i < numStates; i++) {
            for (int j = 0; j < numStates; j++) {
                if (transitions[j][i].isEmpty()) counter++;     //falls wir nicht zum Zustand kommen koennen, erhoehe den Counter
            }
            if (counter == numStates) unreachable.add(i);
            counter = 0;
        }

        if (unreachable.isEmpty()) return unreachable;

        //////////////////////////////////////////////

        Set<Integer> toProve = new HashSet<>();
        int toProveSizeBefore;
        int unreachableSizeBefore;
        do {
            toProveSizeBefore = toProve.size();
            unreachableSizeBefore = unreachable.size();
            for (int state : unreachable) {
                for (int i = 1; i < numStates; i++) {
                    if (transitions[state][i].size() != 0) toProve.add(i);
                }
            }

            boolean foundNewWay = false;

            for (int state : toProve) {
                for (int i = 1; i < numStates; i++) {
                    if (transitions[i][state].size() != 0 && !unreachable.contains(i)) {
                        foundNewWay = true;
                        toProve.add(i);
                    }
                }
                if (!foundNewWay) unreachable.add(state);
                foundNewWay = false;
            }
        } while(toProveSizeBefore != toProve.size() || unreachableSizeBefore != unreachable.size());
        return unreachable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        NFAImpl nfa = (NFAImpl) o;
        //compare transition matrix
        if (transitions.length != nfa.transitions.length) return false;
        if (transitions[0].length != nfa.transitions[0].length) return false;
        for (int i = 0; i < transitions.length; i++) {
            for (int j = 0; j < transitions.length; j++) {
                if (!transitions[i][j].equals(nfa.transitions[i][j])) return false;
            }
        }
        return numStates == nfa.numStates &&
                initialState == nfa.initialState &&
                Objects.equals(alphabet, nfa.alphabet) &&
                Objects.equals(acceptingStates, nfa.acceptingStates);

    }

}
