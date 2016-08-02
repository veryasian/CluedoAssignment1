package cluedo.assets;

import java.util.*;

import cluedo.cards.Card;

/**
 * Class that represents a player in Cluedo.
 * This consists of a hand, and a players name, and if they're out of the game or not.
 * @author linus
 *
 */
public class Player {

	private List<Card> hand; //Holds the hand 
	private boolean isOut = false;
	private String name;
	private String characterName;
	private Position position;
	private int numberofMoves;
	private boolean isInRoom = false;
	private String lookback = "/|";
	private Room room = null;

	private List<Position> coordinates = new ArrayList<Position>();
	private Position[] possibleCoords = new Position[4];

	/**
	 * Create a Player with a given name, a hand, a current character, and their x and y position.
	 * 
	 * @param name
	 * @param c
	 * @param h
	 * @param x
	 * @param y
	 */
	public Player(String name){
		this.name = name;
		hand = new ArrayList<>();
	}

	/**
	 * Sets the amount of moves a player may have.
	 */
	public void setNumberofMoves(int amount){
		if(amount > 12 || amount < 2) throw new IllegalArgumentException("player can have only 2-12 moves.");
		this.numberofMoves = amount;
	}

	/**
	 * This allows the player to move a step.
	 */

	public void moveAStep(){
		this.numberofMoves--;
	}
	/**
	 * Returns the amount of moves a player currently has.
	 * 
	 */
	public int numberofMoves(){
		return this.numberofMoves;
	}

	/**
	 * Assigns a character to a player.
	 * E.g. Linus gets Professor Plum.
	 * @param c
	 */
	public void setCharacter(Character c){
		this.characterName = c.getName();
	}

	/**
	 * Adds a card to the a players hand.
	 * @param c
	 */
	public void addCard(Card c){
		this.hand.add(c);
	}

	/**
	 * Set player's X and Y position.
	 * @param x
	 * @param y
	 */
	public void setPos(int x, int y){
		this.position = new Position(x, y);
		coordinates.add(this.position);
		if(x == 0 && y == 17){
			this.lookback = "|/|";
		}
		possibleCoords[0] = new Position(x + 1, y);
		possibleCoords[1] = new Position(x - 1, y);
		possibleCoords[2] = new Position(x, y + 1);
		possibleCoords[3] = new Position(x, y - 1);
	}

	public Position position(){
		return this.position;
	}

	/**
	 * Return name of player.
	 * @return
	 */
	public String getName(){
		return this.name;
	}

	/**
	 * Return name of character name.
	 * @return
	 */
	public String getCharacterName(){
		return this.characterName;
	}

	/**
	 * Return list of cards of player.
	 * @return
	 */
	public List<Card> getCards(){
		return this.hand;
	}

	/**
	 * Displays a list of available cards, printed to the console.
	 */
	public void displayHand(){
		System.out.println("Current Cards in Hand: ");
		for(int i = 0; i < hand.size(); i++){
			System.out.println(String.valueOf(i) + " : " + hand.get(i).toString());
		}
	}

	/**
	 * This sets the current player to be out.
	 */
	public void setOut(){
		this.isOut = true;
	}

	/**
	 * Sets if the player has entered a room or not.
	 * @param rm
	 */
	public void setIsInRoom(boolean rm){
		this.isInRoom = rm;
	}

	/**
	 * Returns if the player is in a room or not.
	 * @return
	 */
	public boolean isInRoom() {
		System.out.println("Currently in room: " + this.isInRoom);
		return this.isInRoom;
	}

	/**
	 * Set look back string, in order to remove player's position when moving to another square.
	 */
	public void setLookBack(String lb){
		this.lookback = lb;
	}

	/**
	 * Returns the lookback string.
	 * @return
	 */
	public String getLookBack(){
		return this.lookback;
	}

	/**
	 * Set room that player is in.
	 */
	public void setRoom(Room rm){
		this.room = rm;
	}
	/**
	 * Get room that player is in.
	 */
	public Room getRoom(){
		return this.room;
	}

	/**
	 * Returns the list of coordinates that the player has been in within a single move.
	 * @return
	 */
	public List<Position> coordinatesTaken(){
		return coordinates;
	}

	public Position[] getPossibleCoords(){
		return this.possibleCoords;
	}

	public String toString(){
		return "Name: " + this.name + ", Character Piece: " + this.characterName;
	}
}
