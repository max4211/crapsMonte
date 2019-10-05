package crapsMonte;

import java.util.HashMap;

public class dealer {
	
	public int pointInt; 									// Keep track of point number (0 means no point)
	public int tableMin;									// Table minimum that you can bet
	
	public static HashMap<Integer, Double> pointOddsPayouts; // HashMap that tracks pay outs for all point odds
	
	public dealer() {
		pointInt = 0;
		tableMin = 5;
		declarePayouts();
	}
	
	/*
	 * Method to declare the payouts for various odds schemes
	 */
	private static void declarePayouts() {
		pointOddsPayouts = new HashMap<Integer, Double>();
		int[] points = {4, 5, 6, 8, 9, 10};
		double payout = 0.0;
		for (int point: points) {
			if (point == 4 || point == 10)
				payout = 2.0/1.0;
			if (point == 5 || point == 9)
				payout = 3.0/2.0;
			if (point == 6 || point == 8)
				payout = 6.0/5.0;
			System.out.println("Adding to payout point: " + point + ", payout: " + payout);
			pointOddsPayouts.put(point, payout);
		}
	}
	
	public static void main (String[] args) {
		declarePayouts();
	}
	
}
