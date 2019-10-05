package crapsMonte;

import java.awt.List;
import java.util.HashMap;

public class player {
	
	private static int bankRoll;							// Players starting bankroll
	public static HashMap<Integer, Integer> activeBets;		// Map of players active bets (key is bet index, value is quantity on bet)
	private static double expectedValue;					// Double representing current expected value for bets (assuming fair dice)
	
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
	
}
