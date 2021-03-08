package app.tests;

import app.control.HalmaControlConstants;
import app.control.PointUtils;
import junit.framework.TestCase;

public class PointUtilsTest extends TestCase {
	
	
	public void testExactDistanceBetweenPoints() {
		
		assertEquals(
				7.0,
				PointUtils.distanceBetweenPoints(
						3.0, 2.0,
						10.0, 2.0
				)
		);
		
		assertEquals(
				5.0,
				PointUtils.distanceBetweenPoints(
						3.0, 5.0,
						6.0, 9.0
				)
		);
		
	}
	
	public void testApproximateDistanceBetweenPoints() {
		
		assertEquals(
				8.6,
				PointUtils.distanceBetweenPoints(
						3.0, 5.0,
						10.0, 10.0
				),
				HalmaControlConstants.MOVE_DISTANCE_TOLERANCE
		);
		
		assertEquals(
				6.4,
				PointUtils.distanceBetweenPoints(
						13.0, 4.0,
						9.0, 9.0
				),
				HalmaControlConstants.MOVE_DISTANCE_TOLERANCE
		);
		
	}
	
	public void testApproximateDistanceBetweenApproximatePoints() {
		
		assertEquals(
				3.639,
				PointUtils.distanceBetweenPoints(
						7.42, 10.54,
						10.46, 8.54
				),
				HalmaControlConstants.MOVE_DISTANCE_TOLERANCE
		);
		
		assertEquals(
				4.181,
				PointUtils.distanceBetweenPoints(
						12.88, 6.46,
						9.3, 4.3
				),
				HalmaControlConstants.MOVE_DISTANCE_TOLERANCE
		);
		
	}
	
	public void testUsualAnglesBetweenPoints() {
		
		assertEquals(
				45,
				PointUtils.angleBetweenPoints(11, 8, 8, 5)
		);
		
		assertEquals(
				90,
				PointUtils.angleBetweenPoints(9, 10, 9, 5)
		);
		
		assertEquals(
				180,
				PointUtils.angleBetweenPoints(7, 11, 12, 11)
		);
		
		assertEquals(
				0,
				PointUtils.angleBetweenPoints(12, 11, 7, 11)
		);
		
	}
	
	public void testUnusualAnglesBetweenPoints() {
		
		assertEquals(
				-117,
				PointUtils.angleBetweenPoints(11, 9, 12, 11)
		);
		
		assertEquals(
				124,
				PointUtils.angleBetweenPoints(7, 10, 9, 7)
		);
		
	}
	
	public void testAnglesBetweenApproximatePoints() {
		
		assertEquals(
				-26,
				PointUtils.angleBetweenPoints(12.5, 7.4, 8.44, 9.42)
		);
		
		assertEquals(
				63,
				PointUtils.angleBetweenPoints(6.26, 7.08, 6.24, 7.04)
		);
		
	}
	

}
