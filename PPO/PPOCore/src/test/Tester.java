package test;

import java.util.HashSet;

public class Tester {
	
	public static void main(String[] args) {
		HashSet<Pair<Integer, Integer>> x = new HashSet<>();
		x.add(new Pair<Integer, Integer>(1, 2));
		x.add(new Pair<Integer, Integer>(1, 2));
		x.add(new Pair<Integer, Integer>(1, 2));
		System.out.println(x.size());
	}
}