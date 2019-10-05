package crapsMonte;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class player extends dealer {
	
	private static int bankRoll;							// Players starting bankroll (keep track of performance, should not adjust betting style, gambling is a lifelong event)
	public static HashMap<Integer, Integer> activeBets;	// Map of players active bets (key is bet index, value is quantity on bet)
	private static double expectedValue;					// Double representing current expected value for bets (assuming fair dice)
	
	private static int passBet;						// Initialize to table minimum inherited from dealer
	private static int passOdds;					// Initialize to 0, meaning no odds at the time
	
	private static int comeBet;						// Quantity of come bet at this point in time
	private static Set<Integer> comeList;			// List of all placed come bets
	
	public player() {
		bankRoll = 500;
		activeBets = new HashMap<Integer, Integer>();
		expectedValue = 0.0;
	}
	
	/*
	 * Player makes a move and updates all values accordingly
	 */
	private static void placeBets(String strategy) {
		if (pointInt == 0) {
			passBet = tableMin;
			bankRoll -= tableMin;
		} else {
			if (strategy.equals("3 Point Molly")) {
				if (comeList.size() < 3) {
					comeBet = tableMin;
					bankRoll -= tableMin;
				}
			}
		}
	}
	
	/**
	 * @return random dice roll between 1 and 6
	 */
	public static int diceRoll() {
		Random rn = new Random();
		return rn.nextInt(6) + 1;
	}
	
	/**
	 * Method to evaluate what happens for dice roll when the point is off
	 * @param d1 is the first dice
	 * @param d2 is the second dice
	 */
	private static void pointOff(int d1, int d2) {
		int total = d1 + d2;
		System.out.println("You rolled a: " + total + " (" + d1 + " + " + d2 + ").");
		if (total == 7 || total == 11) {
			bankRoll += (passBet * 2);
			passBet = 0;
			System.out.println("7 or 11 hit! Pass line won!");
			return;
		}
		if (total == 2 || total == 3 || total == 12) {
			passBet = 0;
			System.out.println("Crap out :( Lost pass line bet");
			return;
		} else {
			pointInt = total;
			System.out.println("Point updated to: " + total);
		}
	}
	
	private static void pointOn(int d1, int d2) {
		int total = d1 + d2;
		System.out.println("You rolled a: " + total + "(" + d1 + "+" + d2 + ").");
		if (total == 7 || total == 11) {
			crapOut();
			System.out.println("7 or 11 :( You lose");
			return;
		} else {
			if (total == pointInt) {
				System.out.println("Point hit!");
				pointHit();
			}
			if (comeList.contains(total)) {
				System.out.println("Come hit!");
				comeHit(total);
			}
		}
	}
	
	// TODO Pay out hit come bet
	private static void comeHit(int total) {
		
		
	}

	// TODO - Pay out point, clear point bets
	private static void pointHit() {
		
	}
	
	private static void crapOut() {
		comeBet = 0;
		comeList.clear();
		passBet = 0;
		passOdds = 0;
		pointInt = 0;
	}

	/**
	 * Method that scans plays the game, updating global variabls as appropriate
	 * @param strategy dictates how you will place bets
	 * @param turns is a base case, limiting factor on how many turns you will play (testing)
	 */
	private static void letsPlay(String strategy, int turns) {
		for (int i = 0; i < turns; i ++) {
			placeBets(strategy);
			int d1 = diceRoll();
			int d2 = diceRoll();
			if (pointInt == 0)
				pointOff(d1, d2);
			else
				pointOn(d1, d2);
		}
	}
	
	public static void main (String[] args) {
		comeList = new HashSet<Integer>();
		String strategy = "3 Point Molly";
		int turns = 10;
		letsPlay(strategy, turns);
	}
	
}
