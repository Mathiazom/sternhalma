package app.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import app.control.HalmaReferee;
import app.control.HalmaReferee.MoveType;
import app.game.Player;
import app.ui.HalmaDimensions;
import app.ui.HalmaUIConstants;
import app.ui.PawnHole;
import junit.framework.TestCase;

public class HalmaRefereeTest extends TestCase {
	
	
	private ArrayList<Player> players;
	
	private double stepDistanceX;

	private double stepDistanceY;
	
	private HalmaDimensions dimensions;
	
	public void setUp() {
		
		final HalmaDimensions dimensions = new HalmaDimensions(1920.0, 1080.0);
		
		this.stepDistanceX = dimensions.getStepDistanceX();
		this.stepDistanceY = dimensions.getStepDistanceY();
		this.dimensions = dimensions;
		
		this.players = new ArrayList<>(Arrays.asList(new Player("Green", HalmaUIConstants.GREEN_PLAYER_COLOR),
				new Player("Yellow", HalmaUIConstants.YELLOW_PLAYER_COLOR),
				new Player("Orange", HalmaUIConstants.ORANGE_PLAYER_COLOR),
				new Player("Red", HalmaUIConstants.RED_PLAYER_COLOR),
				new Player("Purple", HalmaUIConstants.PURPLE_PLAYER_COLOR),
				new Player("Blue", HalmaUIConstants.BLUE_PLAYER_COLOR)));
		
	}
	
	public void assertEqualMoveType(MoveType expected, ArrayList<PawnHole> holes, PawnHole fromHole, PawnHole toHole) {
		
		final HalmaReferee referee = new HalmaReferee(stepDistanceX, holes);
		
		assertEquals(expected, referee.judgeMove(fromHole, toHole, null));
		
	}
	
	public void testSingleMoveHorizontally() {
		
		final ArrayList<PawnHole> holes = new ArrayList<>();
		
		final PawnHole fromHole = new PawnHole(
				players.get(0),
				dimensions.getScreenCenterX(),
				dimensions.getScreenCenterY(),
				dimensions.getPawnDim(),
				dimensions.getPawnStrokeWidth()
		);
		
		final PawnHole toHole = new PawnHole(
				Player.NO_PLAYER,
				dimensions.getScreenCenterX() + stepDistanceX,
				dimensions.getScreenCenterY(),
				dimensions.getPawnDim(),
				dimensions.getPawnStrokeWidth()
		);
		
		holes.add(fromHole);
		holes.add(toHole);
		
		assertEqualMoveType(MoveType.ONE_STEP, holes, fromHole, toHole);
		
	}
	
	public void testSingleMoveDiagonally() {
		
		final ArrayList<PawnHole> holes = new ArrayList<>();
		
		final PawnHole fromHole = new PawnHole(
				players.get(0),
				dimensions.getScreenCenterX(),
				dimensions.getScreenCenterY(),
				dimensions.getPawnDim(),
				dimensions.getPawnStrokeWidth()
		);
		
		final PawnHole toHole = new PawnHole(
				Player.NO_PLAYER,
				dimensions.getScreenCenterX() + stepDistanceX/2,
				dimensions.getScreenCenterY() + stepDistanceY,
				dimensions.getPawnDim(),
				dimensions.getPawnStrokeWidth()
		);
		
		holes.add(fromHole);
		holes.add(toHole);
		
		assertEqualMoveType(MoveType.ONE_STEP, holes, fromHole, toHole);
		
	}
	
	public void testJumpHorizontally() {
		
		final ArrayList<PawnHole> holes = new ArrayList<>();
		
		final PawnHole fromHole = new PawnHole(
				players.get(0),
				dimensions.getScreenCenterX(),
				dimensions.getScreenCenterY(),
				dimensions.getPawnDim(),
				dimensions.getPawnStrokeWidth()
		);
		
		final PawnHole jumpHole = new PawnHole(
				players.get(new Random().nextInt(players.size())),
				dimensions.getScreenCenterX() + stepDistanceX,
				dimensions.getScreenCenterY(),
				dimensions.getPawnDim(),
				dimensions.getPawnStrokeWidth()
		);
		
		final PawnHole toHole = new PawnHole(
				Player.NO_PLAYER,
				dimensions.getScreenCenterX() + 2*stepDistanceX,
				dimensions.getScreenCenterY(),
				dimensions.getPawnDim(),
				dimensions.getPawnStrokeWidth()
		);
		
		holes.add(fromHole);
		holes.add(jumpHole);
		holes.add(toHole);
		
		assertEqualMoveType(MoveType.JUMP, holes, fromHole, toHole);
		
	}
	
	
	public void testJumpDiagonally() {
		
		final ArrayList<PawnHole> holes = new ArrayList<>();
		
		final PawnHole fromHole = new PawnHole(
				players.get(0),
				dimensions.getScreenCenterX(),
				dimensions.getScreenCenterY(),
				dimensions.getPawnDim(),
				dimensions.getPawnStrokeWidth()
		);
		
		final PawnHole jumpHole = new PawnHole(
				players.get(new Random().nextInt(players.size())),
				dimensions.getScreenCenterX() + stepDistanceX/2,
				dimensions.getScreenCenterY() + stepDistanceY,
				dimensions.getPawnDim(),
				dimensions.getPawnStrokeWidth()
		);
		
		final PawnHole toHole = new PawnHole(
				Player.NO_PLAYER,
				dimensions.getScreenCenterX() + stepDistanceX,
				dimensions.getScreenCenterY() + 2*stepDistanceY,
				dimensions.getPawnDim(),
				dimensions.getPawnStrokeWidth()
		);
		
		holes.add(fromHole);
		holes.add(jumpHole);
		holes.add(toHole);
		
		assertEqualMoveType(MoveType.JUMP, holes, fromHole, toHole);
		
	}
	
	
	

}
