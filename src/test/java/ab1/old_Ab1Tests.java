package ab1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ab1.impl.Nachnamen.NFAImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import ab1.Ab1;
import ab1.NFA;
import ab1.DFA;

import ab1.impl.Nachnamen.Ab1Impl;

public class old_Ab1Tests {
	private NFA n1; // leere Menge
	private DFA d1; // leere Menge
	private NFA n2; // epsilon
	private DFA d2; // epsilon
	private NFA n3; // a*
	private DFA d3; // a*
	private NFA n4; // [ab]*
	private DFA d4; // [ab]*
	private NFA n5; // [ab]*c*
	private DFA d5; // [ab]*c*
	private NFA n6; // [abc]*
	private DFA d6; // [abc]*
	private NFA n7; // Beispiel
	private NFA n8; // Beispiel
	private NFA n9; // Beispiel
	private NFA n10; // abc
	private NFA n11; // (abc)+
	private NFA n12; // (abc|ab)+

	private static int gesamtPunkte = 0;

	Ab1Impl factory = new Ab1Impl();

	public static final Set<Character> chars = new HashSet<>();

	static {
		chars.add('a');
		chars.add('b');
		chars.add('c');
	}

	@Test
	public void myTest() {
		Ab1Impl factory = new Ab1Impl();
		Set<Character> alphabet = new HashSet<>();
		alphabet.add('a');
		alphabet.add('b');
		Set<Integer> acceptingStates = new HashSet<>();
		acceptingStates.add(1);

		NFA nfa1 = factory.createNFA(2, alphabet, acceptingStates, 0);
		nfa1.setTransition(0, 'b', 1);
		nfa1.setTransition(1, 'a', 1);
		//assertThrows(IllegalStateException.class, () -> {nfa1.setTransition(5, 'a', 3); });
		for (int s : nfa1.getNextStates(1, 'a')) {
			System.out.print(s + " ");
		}
		System.out.println(nfa1.accepts("ba"));
	}

	@Test
	public void myTestNFAtoDFA() {
		DFA dfa = n1.toDFA();
		printFA(dfa);
		printFA(n1);
		//System.out.println(dfa.equals(n2));
	}

	@Test
	public void myTestUnreachableStates() {
		NFA n = n1.union(n2);
		printFA(n);
		System.out.println(n.acceptsEpsilonOnly());
	}

	@Test
	public void myTestUnion() {
		System.out.println(n6.accepts("abc"));
		NFA n = n6.union(n7);
		printFA(n);
		System.out.println(n.accepts("abc"));
	}

	@Test
	public void myTestConcat() {
		NFA n = n6.concat(n7);
		printFA(n);
		System.out.println(n.acceptsEpsilon());
	}

	@Test
	public void myTestStar() {
		NFA n = n7.kleeneStar();
		printFA(n);
		System.out.println(n.acceptsEpsilonOnly());
	}

	@Test
	public void myTestPlus() {
		NFA n = n1.plus();
		//printFA(n);
		System.out.println(n.acceptsNothing());
	}

	public void printFA(NFA a) {
		System.out.println("NumStates: " + a.getNumStates() + "Initial state: " + a.getInitialState());
		System.out.println("Alphabet: ");
		for (Character c : a.getAlphabet()) {
			System.out.print(c + " ");
		}
		System.out.println("Accepting states: ");
		for (Integer i : a.getAcceptingStates()) {
			System.out.print(i + " ");
		}
		System.out.println("Transition matrix: ");
		Set[][] transMatrix = a.getTransitions();
		for (int i = 0; i < transMatrix.length; i++) {
			for (int j = 0; j < transMatrix.length; j++) {
				System.out.print(transMatrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	@BeforeEach
	public void InitializeNFA1() {
		Set<Integer> accept = new TreeSet<Integer>();

		n1 = factory.createNFA(1, chars, accept, 0);
	}

	@BeforeEach
	public void InitializeDFA1() {
		Set<Integer> accept = new TreeSet<Integer>();

		d1 = factory.createDFA(1, chars, accept, 0);
	}

	@BeforeEach
	public void InitializeNFA2() {
		Set<Integer> accept = new TreeSet<Integer>();
		accept.add(0);

		n2 = factory.createNFA(5, chars, accept, 0);

		n2.setTransition(1, 'a', 2);
		n2.setTransition(2, 'a', 3);
		n2.setTransition(3, 'a', 4);
		n2.setTransition(4, 'a', 0);
	}

	@BeforeEach
	public void InitializeDFA2() {
		Set<Integer> accept = new TreeSet<Integer>();
		accept.add(0);

		d2 = factory.createDFA(5, chars, accept, 0);

		d2.setTransition(1, 'a', 2);
		d2.setTransition(2, 'a', 3);
		d2.setTransition(3, 'a', 4);
		d2.setTransition(4, 'a', 0);
	}

	@BeforeEach
	public void InitializeNFA3() {
		Set<Integer> accept = new TreeSet<Integer>();
		accept.add(0);

		n3 = factory.createNFA(1, chars, accept, 0);

		n3.setTransition(0, 'a', 0);
	}

	@BeforeEach
	public void InitializeDFA3() {
		Set<Integer> accept = new TreeSet<Integer>();
		accept.add(0);

		d3 = factory.createDFA(1, chars, accept, 0);

		d3.setTransition(0, 'a', 0);
	}

	@BeforeEach
	public void InitializeNFA4() {
		Set<Integer> accept = new TreeSet<Integer>();
		accept.add(0);

		n4 = factory.createNFA(5, chars, accept, 0);

		n4.setTransition(0, 'a', 0);
		n4.setTransition(0, 'b', 0);
	}

	@BeforeEach
	public void InitializeDFA4() {
		Set<Integer> accept = new TreeSet<Integer>();
		accept.add(0);

		d4 = factory.createDFA(5, chars, accept, 0);

		d4.setTransition(0, 'a', 0);
		d4.setTransition(0, 'b', 0);
	}

	@BeforeEach
	public void InitializeNFA5() {
		Set<Integer> accept = new TreeSet<Integer>();
		accept.add(0);
		accept.add(1);

		n5 = factory.createNFA(2, chars, accept, 0);

		n5.setTransition(0, 'a', 0);
		n5.setTransition(0, 'b', 0);
		n5.setTransition(1, 'c', 1);
		n5.setTransition(0, null, 1);
	}

	@BeforeEach
	public void InitializeDFA5() {
		Set<Integer> accept = new TreeSet<Integer>();
		accept.add(0);
		accept.add(1);

		d5 = factory.createDFA(2, chars, accept, 0);

		d5.setTransition(0, 'a', 0);
		d5.setTransition(0, 'b', 0);
		d5.setTransition(0, 'c', 1);
		d5.setTransition(1, 'c', 1);
	}

	@BeforeEach
	public void InitializeNFA6() {
		Set<Integer> accept = new TreeSet<Integer>();
		accept.add(0);
		accept.add(1);

		n6 = factory.createNFA(2, chars, accept, 0);

		n6.setTransition(0, 'a', 0);
		n6.setTransition(0, 'b', 0);
		n6.setTransition(1, 'c', 1);
		n6.setTransition(0, null, 1);
		n6.setTransition(1, null, 0);
	}

	@BeforeEach
	public void InitializeDFA6() {
		Set<Integer> accept = new TreeSet<Integer>();
		accept.add(0);
		accept.add(1);

		d6 = factory.createDFA(2, chars, accept, 0);

		d6.setTransition(0, 'a', 0);
		d6.setTransition(0, 'b', 0);
		d6.setTransition(0, 'c', 1);
		d6.setTransition(1, 'c', 1);
		d6.setTransition(1, 'a', 0);
		d6.setTransition(1, 'b', 0);
	}


	@BeforeEach
	public void InitializeNFA7() {
		Set<Integer> accept = new TreeSet<Integer>();
		accept.add(4);

		n7 = factory.createNFA(5, chars, accept, 0);

		n7.setTransition(0, 'b', 1);
		n7.setTransition(0, 'a', 2);
		n7.setTransition(0, 'a', 3);
		n7.setTransition(1, 'b', 2);
		n7.setTransition(1, 'a', 4);
		n7.setTransition(2, 'a', 2);
		n7.setTransition(2, 'b', 4);
		n7.setTransition(3, 'b', 2);
	}

	@BeforeEach
	public void InitializeNFA8() {
		Set<Integer> accept = new TreeSet<Integer>();
		accept.add(3);
		accept.add(4);

		n8 = factory.createNFA(5, chars, accept, 0);

		n8.setTransition(0, 'a', 1);
		n8.setTransition(0, 'b', 2);
		n8.setTransition(1, 'b', 4);
		n8.setTransition(2, 'b', 2);
		n8.setTransition(2, 'a', 1);
		n8.setTransition(2, 'a', 3);
		n8.setTransition(4, 'b', 2);
		n8.setTransition(4, 'a', 3);

		n8.setTransition(1, null, 0);
		n8.setTransition(4, null, 0);
		n8.setTransition(3, null, 1);
		n8.setTransition(4, null, 2);
	}

	@BeforeEach
	public void InitializeNFA9() {
		Set<Integer> accept = new TreeSet<Integer>();
		accept.add(7);

		n9 = factory.createNFA(8, chars, accept, 0);

		n9.setTransition(5, 'a', 6);
		n9.setTransition(5, 'b', 1);
		n9.setTransition(6, 'b', 7);
		n9.setTransition(6, 'a', 3);
		n9.setTransition(7, 'b', 7);
		n9.setTransition(7, 'a', 7);
		n9.setTransition(3, 'b', 4);
		n9.setTransition(3, 'a', 0);
		n9.setTransition(0, 'b', 1);
		n9.setTransition(0, 'a', 0);
		n9.setTransition(2, 'a', 3);
		n9.setTransition(2, 'b', 1);
		n9.setTransition(1, 'a', 2);
		n9.setTransition(1, 'b', 1);
		n9.setTransition(4, 'b', 5);
		n9.setTransition(4, 'a', 2);
		n9.setTransition(1, 'c', 4);
		n9.setTransition(2, 'c', 6);
		n9.setTransition(5, 'c', 3);
		n9.setTransition(1, null, 6);
		n9.setTransition(2, null, 4);
		n9.setTransition(7, null, 3);
	}

	@BeforeEach
	public void InitializeNFA10() {
		Set<Integer> accept = new TreeSet<Integer>();
		accept.add(3);

		n10 = factory.createNFA(4, chars, accept, 0);

		n10.setTransition(0, 'a', 1);
		n10.setTransition(1, 'b', 2);
		n10.setTransition(2, 'c', 3);
	}

	@BeforeEach
	public void InitializeNFA11() {
		Set<Integer> accept = new TreeSet<Integer>();
		accept.add(3);

		n11 = factory.createNFA(4, chars, accept, 0);

		n11.setTransition(0, 'a', 1);
		n11.setTransition(1, 'b', 2);
		n11.setTransition(2, 'c', 3);
		n11.setTransition(3, null, 0);
	}

	@BeforeEach
	public void InitializeNFA12() {
		Set<Integer> accept = new TreeSet<Integer>();
		accept.add(3);
		accept.add(2);

		n12 = factory.createNFA(4, chars, accept, 0);

		n12.setTransition(0, 'a', 1);
		n12.setTransition(1, 'b', 2);
		n12.setTransition(2, 'c', 3);
		n12.setTransition(2, null, 0);
		n12.setTransition(3, null, 0);
	}

	@Test
	public void NFA_Language() {
		testLanguageNFA1(n1);
		testLanguageNFA2(n2);
		testLanguageNFA3(n3);
		testLanguageNFA4(n4);
		testLanguageNFA5(n5);
		testLanguageNFA6(n6);
		testLanguageNFA7(n7);
		testLanguageNFA8(n8);
		testLanguageNFA9(n9);
		testLanguageNFA10(n10);
		testLanguageNFA11(n11);
		testLanguageNFA12(n12);
		
		gesamtPunkte++;
	}

	@Test
	public void NFA_Properties() {
		testPropertiesNFA1(n1);
		testPropertiesNFA2(n2);
		testPropertiesNFA3(n3);
		testPropertiesNFA4(n4);
		testPropertiesNFA5(n5);
		testPropertiesNFA6(n6);
		testPropertiesNFA7(n7);
		testPropertiesNFA8(n8);
		testPropertiesNFA9(n9);
		testPropertiesNFA10(n10);
		testPropertiesNFA11(n11);
		testPropertiesNFA12(n12);
		
		gesamtPunkte++;
	}

	@Test
	public void NFA_Operations_Union() {
		////////////////////// Vereinigung ///////////////////

		NFA n = n1.union(n2);
		assertTrue(n.acceptsEpsilon());
		assertTrue(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n2.union(n3);
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		//////////////////////////

		n = n3.union(n4);
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n4.union(n5);
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		///////////////////////

		n = n5.union(n6);
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n6.union(n7);
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n7.union(n8);
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n8.union(n9);
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n9.union(n10);
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n10.union(n11);
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n10.union(n11);
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n11.union(n12);
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertTrue(n.accepts("abc"));
		
		gesamtPunkte++;
	}

	@Test
	public void NFA_Operations_Intersection() {
		////////////////////// Durchschnitt ///////////////////

		NFA n = n1.intersection(n2);
		assertTrue(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n2.intersection(n3);
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertTrue(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		//////////////////////////

		n = n3.intersection(n4);
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n4.intersection(n5);
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		///////////////////////

		n = n5.intersection(n6);
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n6.intersection(n7);
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n7.intersection(n8);
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n8.intersection(n9);
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n9.intersection(n10);
		assertTrue(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n10.intersection(n11);
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n11.intersection(n12);
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertTrue(n.accepts("abc"));
		
		gesamtPunkte++;
	}

	@Test
	public void NFA_Operations_Minus() {

		////////////////////// Ohne ///////////////////

		NFA n = n1.minus(n2);
		assertTrue(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n2.minus(n3);
		assertTrue(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		//////////////////////////

		n = n3.minus(n4);
		assertTrue(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n4.minus(n5);
		assertTrue(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		///////////////////////

		n = n5.minus(n6);
		assertTrue(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n6.minus(n7);
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n7.minus(n8);
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n8.minus(n9);
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n9.minus(n10);
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n10.minus(n11);
		assertTrue(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n11.minus(n12);
		assertTrue(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));
		
		gesamtPunkte++;
	}

	@Test
	public void NFA_Operations_Concat() {

		////////////////////// Concat ///////////////////

		NFA n = n1.concat(n2);
		assertTrue(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n2.concat(n3);
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		//////////////////////////

		n = n3.concat(n4);
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n4.concat(n5);
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		///////////////////////

		n = n5.concat(n6);
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n6.concat(n7);
		assertFalse(n.acceptsNothing());
		//nicht verstaendlich warum sollte Epsilon nicht akzeptiert werden!!!
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n7.concat(n8);
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));

		////////////////////////

		n = n8.concat(n9);
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n9.concat(n10);
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n10.concat(n11);
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n11.concat(n12);
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));
		
		gesamtPunkte++;
	}

	@Test
	public void NFA_Operations_Star() {
		////////////////////// Stern ///////////////////

		NFA n = n1.kleeneStar();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertTrue(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n2.kleeneStar();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertTrue(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		//////////////////////////

		n = n3.kleeneStar();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n4.kleeneStar();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		///////////////////////

		n = n5.kleeneStar();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n6.kleeneStar();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n7.kleeneStar();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n8.kleeneStar();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		//////////////////////

		n = n9.kleeneStar();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n10.kleeneStar();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n11.kleeneStar();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n12.kleeneStar();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		gesamtPunkte++;
	}

	@Test
	public void NFA_Operations_Plus() {
		////////////////////// Plus ///////////////////

		NFA n = n1.plus();
		assertTrue(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n2.plus();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertTrue(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		//////////////////////////

		n = n3.plus();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n4.plus();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		///////////////////////

		n = n5.plus();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n6.plus();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n7.plus();
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n8.plus();
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		///////////////////////

		n = n9.plus();
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n10.plus();
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n11.plus();
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n12.plus();
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertTrue(n.accepts("abc"));
		
		gesamtPunkte++;
	}

	@Test
	public void NFA_Operations_Complement() {
		////////////////////// Complement ///////////////////

		NFA n = n1.complement();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n2.complement();
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		//////////////////////////

		n = n3.complement();
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n4.complement();
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		///////////////////////

		n = n5.complement();
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n6.complement();
		assertTrue(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n7.complement();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n8.complement();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertTrue(n.accepts("abc"));

		////////////////////////

		n = n10.complement();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n11.complement();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("ab"));
		assertFalse(n.accepts("abc"));

		////////////////////////

		n = n12.complement();
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertFalse(n.accepts("ab"));
		assertFalse(n.accepts("abc"));
		
		gesamtPunkte++;
	}
	
	@Test
	public void NFA_Equals() {

		List<NFA> nfas = Arrays.asList(n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12);

		// Jeder Automat ist zu sich selbst equivalent
		for (int i = 0; i < nfas.size(); i++)
			assertTrue(nfas.get(i).equals(nfas.get(i)));

		// Kein gegebener Automat ist gleich einem anderen
		for (int i = 0; i < nfas.size(); i++)
			for (int j = i + 1; j < nfas.size(); j++)
				assertFalse(nfas.get(i).equals(nfas.get(j)));
		
		gesamtPunkte++;
	}

	@Test
	public void ToDFA() {
		testToDFA(n1);
		testToDFA(n2);
		testToDFA(n3);
		testToDFA(n4);
		testToDFA(n5);
		testToDFA(n6);
		testToDFA(n7);
		testToDFA(n8);
		testToDFA(n9);
		testToDFA(n10);
		testToDFA(n11);
		testToDFA(n12);

		testPropertiesNFA1(n1.toDFA());
		testPropertiesNFA2(n2.toDFA());
		testPropertiesNFA3(n3.toDFA());
		testPropertiesNFA4(n4.toDFA());
		testPropertiesNFA5(n5.toDFA());
		testPropertiesNFA6(n6.toDFA());
		testPropertiesNFA7(n7.toDFA());
		testPropertiesNFA8(n8.toDFA());
		testPropertiesNFA9(n9.toDFA());
		testPropertiesNFA10(n10.toDFA());
		testPropertiesNFA11(n11.toDFA());
		testPropertiesNFA12(n12.toDFA());

		testLanguageNFA1(n1.toDFA());
		testLanguageNFA2(n2.toDFA());
		testLanguageNFA3(n3.toDFA());
		testLanguageNFA4(n4.toDFA());
		testLanguageNFA5(n5.toDFA());
		testLanguageNFA6(n6.toDFA());
		testLanguageNFA7(n7.toDFA());
		testLanguageNFA8(n8.toDFA());
		testLanguageNFA9(n9.toDFA());
		testLanguageNFA10(n10.toDFA());
		testLanguageNFA11(n11.toDFA());
		testLanguageNFA11(n12.toDFA());

		gesamtPunkte += 4;
	}

	@Test
	public void DFA_equals_NFA() {
		assertTrue(n1.equals(d1));
		assertTrue(n2.equals(d2));
		assertTrue(n3.equals(d3));
		assertTrue(n4.equals(d4));
		assertTrue(n5.equals(d5));
		assertTrue(n6.equals(d6));

		gesamtPunkte++;
	}

	private static boolean isValidDFA(DFA d) {
		for (int s = 0; s < d.getNumStates(); s++) {
			for (Character c : d.getAlphabet()) {
				int numNextStates = d.getNextStates(s, c).size();
				// Für jedes Symbol muss es null oder einen Folgezustand geben.
				if (numNextStates > 1)
					return false;
			}

			int numNextStatesEpsilon = d.getNextStates(s, null).size();
			// DFA dürfen keine epsilon-Übergänge haben
			if (numNextStatesEpsilon > 0)
				return false;
		}

		return true;
	}

	private void testPropertiesNFA1(NFA n) {
		assertTrue(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
	}

	private void testPropertiesNFA2(NFA n) {
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertTrue(n.acceptsEpsilonOnly());
	}

	private void testPropertiesNFA3(NFA n) {
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
	}

	private void testPropertiesNFA4(NFA n) {
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
	}

	private void testPropertiesNFA5(NFA n) {
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
	}

	private void testPropertiesNFA6(NFA n) {
		assertFalse(n.acceptsNothing());
		assertTrue(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
	}

	private void testPropertiesNFA7(NFA n) {
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
	}

	private void testPropertiesNFA8(NFA n) {
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
	}

	private void testPropertiesNFA9(NFA n) {
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
	}

	private void testPropertiesNFA10(NFA n) {
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
	}

	private void testPropertiesNFA11(NFA n) {
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
	}

	private void testPropertiesNFA12(NFA n) {
		assertFalse(n.acceptsNothing());
		assertFalse(n.acceptsEpsilon());
		assertFalse(n.acceptsEpsilonOnly());
	}

	private void testLanguageNFA1(NFA n) {
		assertFalse(n.accepts(""));
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("aaa"));
		assertFalse(n.accepts("b"));
		assertFalse(n.accepts("c"));
	}

	private void testLanguageNFA2(NFA n) {
		assertTrue(n.accepts(""));
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("aaa"));
		assertFalse(n.accepts("b"));
		assertFalse(n.accepts("c"));
	}

	private void testLanguageNFA3(NFA n) {
		assertTrue(n.accepts(""));
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("aaa"));
		assertFalse(n.accepts("b"));
		assertFalse(n.accepts("c"));
	}

	private void testLanguageNFA4(NFA n) {
		assertTrue(n.accepts(""));
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("aaa"));
		assertTrue(n.accepts("b"));
		assertTrue(n.accepts("abba"));
		assertFalse(n.accepts("c"));
	}

	private void testLanguageNFA5(NFA n) {
		assertTrue(n.accepts(""));
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("aaa"));
		assertTrue(n.accepts("b"));
		assertTrue(n.accepts("abba"));
		assertTrue(n.accepts("c"));
	}

	private void testLanguageNFA6(NFA n) {
		assertTrue(n.accepts(""));
		assertTrue(n.accepts("a"));
		assertTrue(n.accepts("aa"));
		assertTrue(n.accepts("aaa"));
		assertTrue(n.accepts("b"));
		assertTrue(n.accepts("abba"));
		assertTrue(n.accepts("c"));
	}

	private void testLanguageNFA7(NFA n) {
		assertFalse(n.accepts(""));
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("aaa"));
		assertFalse(n.accepts("b"));
		assertFalse(n.accepts("abba"));
		assertFalse(n.accepts("c"));
	}

	private void testLanguageNFA8(NFA n) {
		assertFalse(n.accepts(""));
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("aaa"));
		assertFalse(n.accepts("b"));
		assertTrue(n.accepts("abba"));
		assertFalse(n.accepts("c"));
	}

	private void testLanguageNFA9(NFA n) {
		assertFalse(n.accepts(""));
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("aaa"));
		assertFalse(n.accepts("b"));
		assertTrue(n.accepts("abba"));
		assertFalse(n.accepts("c"));
	}

	private void testLanguageNFA10(NFA n) {
		assertFalse(n.accepts(""));
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("aaa"));
		assertFalse(n.accepts("b"));
		assertFalse(n.accepts("abba"));
		assertFalse(n.accepts("c"));

	}

	private void testLanguageNFA11(NFA n) {
		assertFalse(n.accepts(""));
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("aaa"));
		assertFalse(n.accepts("b"));
		assertFalse(n.accepts("abba"));
		assertFalse(n.accepts("c"));
	}

	private void testLanguageNFA12(NFA n) {
		assertFalse(n.accepts(""));
		assertFalse(n.accepts("a"));
		assertFalse(n.accepts("aa"));
		assertFalse(n.accepts("aaa"));
		assertFalse(n.accepts("b"));
		assertFalse(n.accepts("abba"));
		assertFalse(n.accepts("c"));
	}

	private void testToDFA(NFA n) {
		DFA d = n.toDFA();
		assertTrue(isValidDFA(d));
		assertTrue(n.equals(d));
		assertTrue(d.equals(n));
	}

	@AfterAll
	public static void printPoints() {
		System.out.println("Gesamtpunkte: " + gesamtPunkte);
	}
}
