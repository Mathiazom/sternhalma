package app.control;

import java.util.Iterator;

import app.ui.PawnHole;

public class PointUtils {
	
	public static double distanceBetweenHoles(PawnHole aHole, PawnHole bHole) {
		
		return distanceBetweenPoints(aHole.getCenterX(), aHole.getCenterY(), bHole.getCenterX(), bHole.getCenterY());
		
	}
	
	public static double distanceBetweenPoints(double x1, double y1, double x2, double y2) {

		final double xDiff = Math.abs(x1 - x2);
		final double yDiff = Math.abs(y1 - y2);

		return Math.hypot(xDiff, yDiff);

	}

	public static int angleBetweenPoints(double x1, double y1, double x2, double y2) {

		return (int) Math.round(Math.toDegrees(Math.atan2(y1 - y2, x1 - x2)));

	}


}
