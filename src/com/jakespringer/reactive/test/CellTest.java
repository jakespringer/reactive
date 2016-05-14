package com.jakespringer.reactive.test;

import com.jakespringer.reactive.engine.Cell;
import com.jakespringer.reactive.engine.Stream;

public class CellTest {
	public static void main(String[] args) {
		Cell<Integer> fizzBuzz = new Cell<>();
		fizzBuzz.filter(x -> x%3 == 0 && x%5 != 0).subscribe(() -> System.out.println("Fizz"));
		fizzBuzz.filter(x -> x%5 == 0 && x%3 != 0).subscribe(() -> System.out.println("Buzz"));
		fizzBuzz.filter(x -> x%5 == 0 && x%3 == 0).subscribe(() -> System.out.print("Fizz"))
				.then(fizzBuzz.filter(x -> x%5 == 0 && x%3 == 0).subscribe(() -> System.out.println("Buzz")));
		fizzBuzz.filter(x -> x%5 != 0 && x%3 != 0).send(x -> System.out.println(x));
		Stream<Integer> stream = fizzBuzz.filter(x -> true);
		stream.subscribe(() -> System.out.println("Failed"));
		stream.kill();
		for (int i=0; i<100; ++i) {
			fizzBuzz.set(i);
		}
	}
}
