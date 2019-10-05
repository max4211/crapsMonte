package crapsMonte;

import java.util.HashMap;

public class dealer {
	
	public static int pointInt = 0; 									// Keep track of point number (0 means no point)
	public static int tableMin = 5;									// Table minimum that you can bet
	
	public static HashMap<Integer, Double> pointPayouts; // HashMap that tracks pay outs for all point odds
	public static HashMap<Integer, Double> hardwayPayouts; // HashMap that tracks pay outs for all point odds
	
	public dealer() {
		pointInt = 0;
		tableMin = 5;
		declarePoints();
	}
	
	/*
	 * Method to declare the payouts for various odds schemes
	 */
	private static void declarePoints() {
		pointPayouts = new HashMap<Integer, Double>();
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
			pointPayouts.put(point, payout);
		}
	}
	
	public static void main (String[] args) {
		declarePoints();
	}
	
}
