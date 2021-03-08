package app.game;

import java.util.ArrayList;

import app.ui.PawnHole;
import javafx.scene.paint.Color;

public class Player extends PlayerIdentity {

	
	private int totalTurns = 0;

	private Player opponent;
	
	private ArrayList<PawnHole> baseHoles = new ArrayList<>();

	private ArrayList<PawnHole> securedPawns = new ArrayList<>();

	
	public static final Player NO_PLAYER = null;
	
	
	public Player duplicate() {
		
		return new Player(this.name, this.color);
		
	}

	public Player(final PlayerIdentity identity) {
		
		this(identity.name, identity.color);
		
	}
	
	public Player(final String name, final Color color) {

		super(name, color);

	}
	
	public void setTotalTurns(int totalTurns) {
		
		this.totalTurns = totalTurns;
		
	}
	
	public void incrementTotalTurns() {
		
		this.totalTurns++;
		
	}
	
	public int getTotalTurns() {
		
		return this.totalTurns;
		
	}

	public void addToSecuredPawns(PawnHole pawnHole) {
		this.securedPawns.add(pawnHole);
	}

	public void removeFromSecuredPawns(PawnHole pawnHole) {
		this.securedPawns.remove(pawnHole);
	}

	public void setOpponent(Player opponent) {
		this.opponent = opponent;
	}
	
	
	public boolean hasSecuredPawn(PawnHole pawnHole) {
		return this.securedPawns.contains(pawnHole);
	}
	
	public boolean hasSecuredAllPawns() {
		return !baseHoles.isEmpty() && securedPawns.size() == baseHoles.size();
	}
	
	public ArrayList<PawnHole> getSecuredPawns(){
		return securedPawns;
	}
	
	public void addBaseHole(PawnHole hole){
		
		if(baseHoles.contains(hole)) {
			return;
		}
		
		this.baseHoles.add(hole);
		
	}
	
	public ArrayList<PawnHole> getBaseHoles(){
		
		return this.baseHoles;
		
	}

	public String getName() {
		return this.name;
	}

	public Color getColor() {
		return this.color;
	}

	public Player getOpponent() {
		return this.opponent;
	}
	

}
