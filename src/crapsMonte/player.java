package crapsMonte;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class player extends dealer {
	
	private static int bankRoll = 500;				// Players starting bankroll (keep track of performance, should not adjust betting style, gambling is a lifelong event)
	public static Set<String> activeBets;			// Map of players active bets (key is bet index, value is quantity on bet)
	
	private static int passBet;						// Initialize to table minimum inherited from dealer
	private static int passOdds;					// Initialize to 0, meaning no odds at the time
	
	private static int comeBet;						// Quantity of come bet at this point in time
	private static int comeOdds;
	private static Set<Integer> comeList;			// List of all placed come bets
		
	private static int currentWager = 0;			// How much money is currently being wagered
	private static int multiplier = 1;				// Mulitplier of table min	
	
	private static String passString = "pass line";	// Variuos strings to keep track of turn bets
	private static String comeString = "come line";
	private static String oddsString = " odds";
	
	private static String turnAction = "";
	
	/*
	 * Player makes a move and updates all values accordingly
	 */
	private static void placeBets(String strategy) {
		if (pointInt == 0) {
			betPassLine();
		} else {
			if (strategy.equals("3 Point Molly")) {
				if (passOdds == 0) {
					betPassOdds();
				}
				if (comeList.size() < 3) {
					betComeLine();
				}
			}
		}
	}
	
	/**
	 * 
	 * @param Update appropriate indices of bankroll (wager and roll) according to bet size
	 */
	private static void updateBankRoll(int bet) {
		bankRoll -= bet;
		currentWager += bet;
		printAction();
	}
	
	/**
	 * Place bet on pass line odds
	 */
	private static void betPassOdds() {
		activeBets.add(passString + oddsString);
		int bet = 2 * passBet;
		passOdds = bet;
		turnAction += " bet pass line odds";
		updateBankRoll(bet);
	}

	/**
	 * Place bet on pass line
	 */
	
	private static void betPassLine() {
		activeBets.add(passString);
		int bet = multiplier * tableMin;
		passBet = bet;
		turnAction += " bet pass line";
		updateBankRoll(bet);
	}
	
	/**
	 * Place bet on come line
	 */
	private static void betComeLine() {
		activeBets.add(comeString);
		int bet = multiplier * tableMin;
		comeBet = bet;
		turnAction += " bet come line";
		updateBankRoll(bet);
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
		printDice(d1, d2);
		if (total == 7 || total == 11) {
			hitPassLine();
			return;
		}
		if (total == 2 || total == 3 || total == 12) {
			crapOut();
			return;
		} else {
			setPoint(total);
			return;
		}
	}
	
	private static void hitPassLine() {
		bankRoll += (passBet * 2);
		currentWager -= passBet;
		passBet = 0;
		activeBets.remove(passString);
		System.out.println("7 or 11 hit! Pass line won!");
	}
	
	private static void setPoint(int total) {
		pointInt = total;
		System.out.println("Point updated to: " + total);
	}
	
	private static void pointOn(int d1, int d2) {
		int total = d1 + d2;
		printDice(d1, d2);
		if (total == 7 || total == 11) {
			crapOut();
			System.out.println("7 or 11 :( You lose");
			return;
		} else {
			if (total == pointInt) {
				System.out.println("Point hit!");
				pointHit(total);
			}
			if (comeList.contains(total)) {
				System.out.println("Come hit!");
				comeHit(total);
			}
			if (comeBet != 0) {
				comeList.add(total);
			}
		}
	}
	/**
	 * Pay out for a dice hit (come and point determine global resets, this just pays you)
	 * @param roll 		is the roll of the dice
	 * @param straight 	is the straight up bet on that roll (pays 1 to 1)
	 * @param odds 		are the odds bet placed on that roll (pays according to map structure from dealer
	 * @param point 	is whether it was a point hit or not
	 */
	private static void diceHit(int roll, int straight, int odds) {
		bankRoll += straight * 2;
		bankRoll += odds + odds * pointPayouts.get(roll);
		currentWager -= straight;
		currentWager -= odds;
	}
	
	// TODO Update this to hash map with value and wager)
	// NOTE For now just assumed to place min bet
	private static void comeHit(int total) {
		comeList.remove(total);
		diceHit(total, comeBet, comeOdds);
		comeBet = 0;
		comeOdds = 0;
	}

	// TODO - Pay out point, clear point bets
	private static void pointHit(int total) {
		diceHit(total, passBet, passOdds);
		passBet = 0;
		passOdds = 0;
		
	}
	
	private static void crapOut() {
		currentWager = 0;
		comeBet = 0;
		comeOdds = 0;
		passBet = 0;
		passOdds = 0;
		pointInt = 0;
		comeList.clear();
		activeBets.clear();
	}
	
	/*
	 * Print turn action
	 */
	private static void printAction() {
		System.out.println("Turn action: " + turnAction);
		turnAction = "";
		printWager();
	}
	
	/**
	 * Summarize status of current bets with prints to console
	 */
	private static void printRoll() {
		System.out.println("Current bankroll: " + bankRoll);
	}
	
	/**
	 * Summarize current wagers
	 */
	private static void printWager() {
		System.out.println("Current wager: " + currentWager);
	}
	
	/**
	 * Print dice roll
	 * @param d1 first die
	 * @param d2 second die
	 */
	private static void printDice(int d1, int d2) {
		System.out.println("You rolled a: " + (d1 + d2) + " (" + d1 + " + " + d2 + ").");
	}

	/**
	 * Method that scans plays the game, updating global variabls as appropriate
	 * @param strategy dictates how you will place bets
	 * @param turns is a base case, limiting factor on how many turns you will play (testing)
	 */
	private static void letsPlay(String strategy, int turns) {
		for (int i = 0; i < turns; i ++) {
			System.out.println("\nRoll #" + (i+1));
			printRoll();
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
		dealer.declarePoints();
		comeList = new HashSet<Integer>();
		activeBets = new HashSet<String>();
		String strategy = "3 Point Molly";
		int turns = 10;
		letsPlay(strategy, turns);
	}
	
}
