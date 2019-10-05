package crapsMonte;

import java.awt.List;
import java.util.HashMap;
import java.util.Random;

public class player extends dealer{
	
	private int bankRoll;							// Players starting bankroll
	public HashMap<Integer, Integer> activeBets;	// Map of players active bets (key is bet index, value is quantity on bet)
	private double expectedValue;					// Double representing current expected value for bets (assuming fair dice)
	
	public player() {
		bankRoll = 500;
		activeBets = new HashMap<Integer, Integer>();
		expectedValue = 0.0;
	}
	
	private void makeTurn() {
		HashMap<Integer, Integer> newBets = new HashMap<Integer, Integer>();
	}
	
	/*
	 * Player makes a move and updates all values accordingly
	 */
	private void placeBets() {
		
	}
	
	/*
	 * Using variables of active bets, calculate current expected value
	 */
	private void calcEV() {
		expectedValue = 0;
	}
	
	
	private void updateBets(List newBet) {
		
	}
	
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
