package crapsMonte;

import java.util.Random;

public class casino {
	
	// Global vars to keep track of point status and value
	private static int pointInt;
	private static boolean pointBool;
	
	/**
	 * @return random dice roll between 1 and 6
	 */
	public static int diceRoll() {
		Random rn = new Random();
		return rn.nextInt(6) + 1;
	}
	
	/**
	 * Simulate various dice rolls to verify random works correctly
	 */
	public static void simRolls() {
		System.out.println("Simulating dice rolls for 5 turns: ");
		int r1; int r2; int turn;
		for (int i = 0; i < 5; i ++) {
			r1 = diceRoll(); r2 = diceRoll();
			turn = r1 + r2;
			System.out.println("Roll 1: " + r1 + " Roll 2: " + r2 + "\nTotal: " + turn);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main (String[] args) {
		simRolls();
	}
}
