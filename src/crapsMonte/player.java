package crapsMonte;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class player extends dealer {
	
	private static int bankRoll = 500;					// Players starting bankroll (keep track of performance, should not adjust betting style, gambling is a lifelong event)
	public static HashMap<String, Integer> activeBets;	// Map of players active bets (key is bet index, value is quantity on bet)
	public static HashMap<Integer, Integer> hardBetMap;	// Map of players active hard bets
	
	private static int passBet;							// Initialize to table minimum inherited from dealer
	private static int passOdds;						// Initialize to 0, meaning no odds at the time
	private static int oddsMultiplier = 2;				// Multiple of pass bet allowed
	
	private static int comeBet;							// Quantity of come bet at this point in time
	public static HashMap<Integer, Integer> comeBetStraight;	// HashMap of straight come bet number and sizes
	public static HashMap<Integer, Integer> comeBetOdds;	// HashMap of odds come bet number and sizes
	
	private static int currentWager = 0;				// How much money is currently being wagered
	private static int betMultiplier = 1;				// Mulitplier of table min	
	
	private static String passString = "pass line";		// Variuos strings to keep track of turn bets
	private static String comeString = "come line";
	private static String oddsString = " odds";
	
	private static String turnAction = "";
	
	/*
	 * Player makes a move and updates all values accordingly
	 * When point is off, bet the pass line (only current option explored)
	 * When point is on and pass odds have not been placed, bet pass odds
	 * When point is on and come bets are less than 3, place a come bet
	 */
	private static void placeBets(String strategy) {
		if (pointInt == 0) {
			betPassLine();
		} else {
			if (strategy.equals("3 Point Molly")) {
				if (passOdds == 0) {
					betPassOdds();
				}
				if (comeBetStraight.size() < 3) {
					betComeLine();
				}
			}
		}
		printActiveBets();
	}
	
	/**
	 * Iterate through active bets hashmap and display all bets
	 */
	private static void printActiveBets() {
		int betSize;
		for (String s: activeBets.keySet()) {
			betSize = activeBets.get(s);
			System.out.println("Bet: " + s + ", Size: " + betSize);
		}
	}
	
	/**
	 * 
	 * @param Update appropriate indices of bank roll (wager and roll) according to bet size
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
		int bet = oddsMultiplier * passBet;
		activeBets.put(passString + oddsString, bet);
		passOdds = bet;
		turnAction += " bet pass line odds";
		updateBankRoll(bet);
	}

	/**
	 * Place bet on pass line
	 */
	
	private static void betPassLine() {
		int bet = betMultiplier * tableMin;
		activeBets.put(passString, bet);
		passBet = bet;
		turnAction += " bet pass line";
		updateBankRoll(bet);
	}
	
	/**
	 * Place bet on come line
	 */
	private static void betComeLine() {
		int bet = betMultiplier * tableMin;
		activeBets.put(comeString, bet);
		comeBet = bet;
		turnAction += " bet come line";
		updateBankRoll(bet);
	}
	
	/**
	 * Place bet on come line odds for a certain roll
	 * Note you are only shifting the current come bet to the number
	 * Additionally placing out a bet with odds on a certain number
	 */
	private static void betComeOdds(int total) {
		int bet = oddsMultiplier * passBet;
		activeBets.put(comeString + oddsString, bet);
		comeBetOdds.put(total, bet);
		comeBetStraight.put(total, betMultiplier * tableMin);
		turnAction += " bet come line odds on " + total;
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
	
	/**
	 * Process appropriate global vars when the pass line hits
	 */
	private static void hitPassLine() {
		bankRoll += (passBet * 2);
		currentWager -= passBet;
		passBet = 0;
		activeBets.remove(passString);
		System.out.println("7 or 11 hit! Pass line won!");
	}
	
	/**
	 * Set point value appropriately
	 * @param total			the dice roll to set the point to
	 */
	private static void setPoint(int total) {
		pointInt = total;
		System.out.println("Point updated to: " + total);
	}
	
	/**
	 * Logical processes to process dice roll when the point is on
	 * @param d1 the first dice roll
	 * @param d2 the second dice roll
	 */
	private static void pointOn(int d1, int d2) {
		int total = d1 + d2;
		printDice(d1, d2);
		if (total == 7 || total == 11) {
			crapOut();
			return;
		} else {
			if (total == pointInt) {
				pointHit(total);
			}
			if (comeBetStraight.containsKey(total)) {
				comeHit(total);
			}
			if (comeBet != 0) {
				betComeOdds(total);
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
		double payout = straight * 2 + odds + odds * pointPayouts.get(roll);
		bankRoll += payout;
		currentWager -= straight;
		currentWager -= odds;
		System.out.println("Payout: " + payout);
	}
	
	/**
	 * Actions that occur after a come bet hits
	 * @param total represent the dice roll, source of payout on come odds
	 */
	private static void comeHit(int total) {
		System.out.println("Come hit!");
		int odds = 0;
		if (comeBetOdds.containsKey(total)) {
			odds = comeBetOdds.get(total);
			comeBetOdds.remove(total);
		}
		comeBetStraight.remove(total);
		diceHit(total, comeBet, odds);
	}

	// TODO - Pay out point, clear point bets
	private static void pointHit(int total) {
		System.out.println("Point hit!");
		diceHit(total, passBet, passOdds);
		passBet = 0;
		passOdds = 0;
		activeBets.remove(passString);
		activeBets.remove(passString + oddsString);
	}
	
	/**
	 * Crap out, reset all values (come list, active bets, etc.)
	 */
	private static void crapOut() {
		System.out.println("Point on and hit 7 or 11 :( You lose");
		currentWager = 0;
		comeBet = 0;
		passBet = 0;
		passOdds = 0;
		pointInt = 0;
		comeBetStraight.clear();
		comeBetOdds.clear();
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
	private static void printBankRoll() {
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
			printBankRoll();
			placeBets(strategy);
			int d1 = diceRoll();
			int d2 = diceRoll();
			if (pointInt == 0)
				pointOff(d1, d2);
			else
				pointOn(d1, d2);
		}
	}
	
	/**
	 * Main class to test results
	 * @param args
	 */
	public static void main (String[] args) {
		dealer.declarePoints();
		comeBetStraight = new HashMap<Integer, Integer>();
		comeBetOdds = new HashMap<Integer, Integer>();
		activeBets = new HashMap<String, Integer>();
		String strategy = "3 Point Molly";
		int turns = 10;
		letsPlay(strategy, turns);
	}
	
}
