package cluedo.main;
import java.io.IOException;
import java.util.*;

import cluedo.arguments.Accusation;
import cluedo.arguments.Suggestion;
import cluedo.assets.*;
import cluedo.assets.Character;
import cluedo.cards.Card;
import cluedo.cards.CharacterCard;
import cluedo.cards.RoomCard;
import cluedo.cards.WeaponCard;
/**
 * Creates a new instance of a Board, and runs the textClient.
 * @author Casey & Linus
 *
 */
public class CluedoGame {
	public static Initializer initializer; //initializes all of the data
	public static Board board; //an instance of the board.

	private int numPlayers = 0; //stores the amount of players.
	private static List<Player> currentPlayers; //a list of the current players.

	public TextClient textClient; //an instance of the textClient.
	public static boolean askSuccess; //TODO? WHAT IS THIS??
	private static boolean hasAsked = false; //if a player has asked or not.

	private List<Integer> diceList = new ArrayList<Integer>(Arrays.asList(2,3,4,5,6,7,8,9,10,11,12));
	private int currentRoll = diceList.get(0);
	private Player currentPlayer; //the current player of the round.

	/** If player has made a move*/
	private boolean moveMade = false;

	/**
	 * Stores the cards of players who have been kicked out of the game because they made an inccorect accusation.
	 */
	private List<List<Card>> showCards = new ArrayList<>();

	/** This helps generate a random shuffle for the lists */
	private long seed = System.nanoTime();

	/**Stores player's previous option*/
	private String prevOption = "";
	private Accusation accusation = null;

	public CluedoGame(){
		currentPlayers = new ArrayList<Player>();
		initializer = new Initializer();
		board = new Board();
	}

	/**
	 * Gets the list of current Players
	 * @return
	 */
	public static List<Player> currentPlayers(){
		return currentPlayers;
	}

	public int numPlayers(){
		return this.numPlayers;
	}


	/**
	 * Add new player to current players.
	 * @param name
	 */
	public static void addPlayer(String name){
		currentPlayers.add(new Player(name));
	}

	/**
	 * Returns currentPlayers list.
	 * @param name
	 */
	public static List<Player> getCurrentPlayers(){
		return currentPlayers;
	}

	/**
	 * Returns a random value between 2-12.
	 * @return
	 */
	public int diceRoll(){
		Collections.shuffle(diceList);
		currentRoll = diceList.get(0);
		return currentRoll;
	}

	/**
	 * Sets up the board and an instance of the textClient.
	 */
	public void initialSetup(){
		drawAsciiArt();
		TextClient.askPlayers();
		initializer.distributeCharacters();
		initializer.distributeCards();

		board.setPlayerPosition(currentPlayers);
		initializer.setCharacters();
		board.drawBoard();
	}

	/**
	 * Test class for the initial setup.
	 */
	public void testInitialSetup(){
		initializer.distributeCharacters();
		initializer.distributeCards();
		board.setPlayerPosition(currentPlayers);
		board.drawBoard();
	}

	/**
	 * Fun little thing I tried doing. It works!
	 * This method draws "CLUEDO GAME" in ascii representative form.
	 * @author Linus Go
	 */
	public void drawAsciiArt(){
		String art = "";
		art+="+===========================================================================+\n";
		art+="|| #####          by CASEY & LINUS              #####                       ||\n";
		art+="||#     # #      #    # ###### #####   ####    #     #   ##   #    # ###### ||\n";
		art+="||#       #      #    # #      #    # #    #   #        #  #  ##  ## #      ||\n";
		art+="||#       #      #    # #####  #    # #    #   #  #### #    # # ## # #####  ||\n";
		art+="||#       #      #    # #      #    # #    #   #     # ###### #    # #      ||\n";
		art+="||#     # #      #    # #      #    # #    #   #     # #    # #    # #      ||\n";
		art+="|| #####  ######  ####  ###### #####   ####     #####  #    # #    # ###### ||\n";
		art+="+===========================================================================+\n";
		System.out.println(art);
		System.out.println("Welcome to the Cluedo Game.");
	}

	public void setPlayerPosition(){
		Collections.shuffle(currentPlayers, new Random(seed)); 
		board.setPlayerPosition(currentPlayers);
	}

	/**
	 * Runs the game - only if asking players was successful.
	 * @throws InvalidMove 
	 */
	public void runGame() throws InvalidMove{
		if(askSuccess){
			while(!isGameOver()){
				for(int i = 0; i < currentPlayers.size(); i++){
					moveMade = false;
					currentPlayer = currentPlayers.get(i);
					if(!currentPlayer.out()){
						System.out.println("Player " + currentPlayer.getName() + " starts.");
						System.out.println(currentPlayer.getName() + "'s character piece is " + currentPlayer.getCharacterName() + ".");
						loop: while(!moveMade){
							String option;
							if(prevOption.equals("s")){
								System.out.println("Do you want to end your turn or make an accusation? (Press Y for ending your turn or N for making an accusation)");
								option = TextClient.inputString();
								switch(option){
								case "y":
									System.out.println("You have ended your turn.");
									moveMade = true;
									prevOption = "";
									break loop;
								case "n":
									option = "a";
									break;
								}
							}else{
								option = TextClient.askOption();
							}
							doOption(option, currentPlayer);
						}
					}
					if(isGameOver()){
						System.out.println("Game is over.");
						printEnvelope();
						return;
					}
				}
			}
		}
	}

	public void doOption(String option, Player p) throws InvalidMove{
		switch(option){
		case "m":
			doMove(p);
			moveMade = true;
			break;
		case "c":
			System.out.println("Your current cards.");
			for(Card c : p.getCards()){
				System.out.println(c.toString());
			}
			break;
		case "d":
			System.out.println("Show all previous players' cards");
			for(List<Card> lc : showCards){
				for(Card c : lc){
					System.out.println(c.toString());
				}
			}
			break;
		case "a":
			System.out.println("Player " + currentPlayer.getName() + " wishes to make an accusation.");
			accusation = makeAccusation(currentPlayer);
			if(accusation == null){
				System.out.println("The accusation pieces did not match."); 
				System.out.println("You can no longer make a move.");
				showCards.add(p.getCards());
				p.setOut(true);
				board.getBoard()[p.position().getX()][p.position().getY()] = p.getLookBack();
			}
			moveMade = true;
			break;
		case "s":
			//TODO: ALSO NEED TO FINISH THIS PART.
			System.out.println("Player " + currentPlayer.getName() + " wishes to make an suggestion.");
			Suggestion sugg = makeSuggestion(currentPlayer);

			if(sugg == null){
				break;
			}

			if(sugg.checkSuggestion(currentPlayers)){
				System.out.println("At least one extra card was found");
			}else{
				System.out.println("no extra cards were found");
			}

			prevOption = "s";
			break;
		}
	}

	/**
	 * This makes an accusation.
	 * @param current Player
	 */
	public Suggestion makeSuggestion(Player p){
		if(p.getRoom() == null){
			System.out.println("ERROR: Sorry, you must be in a room to make a suggestion.");
			return null;
		}
		System.out.println("-----------SUGGESTION!-------------");
		System.out.println("What cards do you want to nominate?");
		System.out.println("----------------------------------");
		System.out.println("AVAILABLE CARDS:");
		//the objects for creating a suggestion.
		//TODO: needs to be based on room he's in..
		WeaponCard weapon = null;
		CharacterCard character = null;
		RoomCard room = new RoomCard(p.getRoom());
		int indexChoice;

		List<WeaponCard> weapons = Initializer.getWeaponCards();
		List<CharacterCard> suspects = Initializer.getCharacterCards();

		System.out.println("Instructions: Enter index of the item you want to nominate.");
		for(int i = 0; i < 2;){
			if(i == 0){
				System.out.println("Step 1) Choose from available weapons: ");
				int index = 0;
				for(WeaponCard ww : weapons){
					System.out.println(index + " "  + ww.toString());				
					index++;
				}
				indexChoice = TextClient.askIndex(weapons);
				weapon = (WeaponCard) weapons.get(indexChoice);
				i++;
			}
			else if(i == 1){
				System.out.println("Step 2) Choose from available Suspects: ");
				int index = 0;
				for(CharacterCard cc : suspects){
					System.out.println(index + " "  + cc.toString());
					index++;
				}
				indexChoice = TextClient.askIndex(weapons);
				character = (CharacterCard) suspects.get(indexChoice);
				i++;
			}
		}
		System.out.println("----------------------------------");
		System.out.println(" CONFIRMED Suggestion Pieces:     ");
		System.out.println(" weapon: " + weapon);
		System.out.println(" character: " + character);
		System.out.println(" room: " + room);
		System.out.println("----------------------------------");
		return new Suggestion(weapon, room, character, p);
		//return TextClient.askSuggestion(p);
	}

	/**
	 * This makes an accusation.
	 * @param p
	 */
	public Accusation makeAccusation(Player p){
		System.out.println("-----------ACCUSATION!-------------");
		System.out.println("What cards do you want to nominate?");
		System.out.println("----------------------------------");
		System.out.println("AVAILABLE CARDS:");

		//the objects for creating a suggestion.
		WeaponCard weapon = null;
		CharacterCard character = null;
		RoomCard room = null;
		int indexChoice = -1;

		//sublists containing cards of a certain category.
		List<WeaponCard> weapons = Initializer.getWeaponCards();
		List<RoomCard> rooms = Initializer.getRoomCards();
		List<CharacterCard> suspects = Initializer.getCharacterCards();
		System.out.println("Instructions: Enter index of the item you want to nominate.");
		for(int i = 0; i < 3;){
			if(i == 0){
				System.out.println("Step 1) Choose from available weapons: ");
				int index = 0;
				for(WeaponCard ww : weapons){
					System.out.println(index + " "  + ww.toString());				
					index++;
				}
				indexChoice = TextClient.askIndex(weapons);
				weapon = (WeaponCard) weapons.get(indexChoice);
				i++;
			}else if(i == 1){
				System.out.println("Step 2) Choose from available Rooms: ");
				int index = 0;
				for(RoomCard rr : rooms){
					System.out.println(index + " "  + rr.toString());
					index++;
				}
				indexChoice = TextClient.askIndex(rooms);
				room = (RoomCard) rooms.get(indexChoice);
				i++;
			}else{
				System.out.println("Step 3) Choose from available Suspects: ");
				int index = 0;
				for(CharacterCard cc : suspects){
					System.out.println(index + " "  + cc.toString());
					index++;
				}
				indexChoice = TextClient.askIndex(suspects);
				character = (CharacterCard) suspects.get(indexChoice);
				i++;
			}
		}

		Card[] env = Initializer.getEnvelope().getEnvelope();

		int count = 0;
		for(Card card : env){
			if(card instanceof WeaponCard){
				if(card.equals(weapon)){
					count++;
				}
			}else if(card instanceof CharacterCard){
				if(card.equals(character)){
					count++;
				}
			}else if(card instanceof RoomCard){
				if(card.equals(room)){
					count++;
				}
			}
		}
		System.out.println("----------------------------------");
		System.out.println(" CONFIRMED Accusation Pieces:     ");
		System.out.println(" weapon: " + weapon);
		System.out.println(" character: " + character);
		System.out.println(" room: " + room);
		System.out.println("----------------------------------");
		Accusation ac = new Accusation(weapon, room, character, p,  Initializer.getEnvelope());
		if(ac.accusationStatus()){
			return ac;
		}
		return null;
	}

	public void doMove(Player p) throws InvalidMove{
		currentPlayer.setNumberofMoves(diceRoll());
		System.out.println(currentPlayer.getName() + " rolls a " + currentPlayer.numberofMoves() + ".");
		System.out.println(currentPlayer.getName() + "  has " + currentPlayer.numberofMoves() + " moves.");
		if(currentPlayer.isInRoom() && currentPlayer.getRoom().hasStairs()){
			System.out.println("Do you want to take the stairs or do you want to get out of the room?");
			System.out.println("Press Y for stairs and N for exiting the room");
			String choice = TextClient.inputString();
			switch(choice){
			case "y":
				board.moveToRoom(p, p.getRoom().getOtherRoom());
				break;
			case "n":
				board.exitRoom(p);
				break;
			}
		}else if(currentPlayer.isInRoom()){
			System.out.println("You must exit the room now as the room does not have any stairs for you to take.");
			board.exitRoom(p);
		}else{
			while(currentPlayer.numberofMoves() > 0){
				System.out.println(currentPlayer.getName() + " (" + currentPlayer.getCharacterName() + ")" + " currently has " + currentPlayer.numberofMoves() + " moves left.");
				System.out.println("current location: " + currentPlayer.position().getX() + ", " + currentPlayer.position().getY());
				TextClient.movementListener(currentPlayer);
				if(currentPlayer.isInRoom()){
					System.out.println("You have entered a room. You will need to wait for your next turn to be able to");
					System.out.println("take the stairs or exit the room.");
					break;
				}
				if(!board.canMove(p) && !p.coordinatesTaken().isEmpty()){
					System.out.println("Sorry you do not have anywhere to move now.");
					break;
				}
			}
			if(currentPlayer.numberofMoves() <= 0){
				System.out.println(currentPlayer.getName() + " has run out of moves.");
			}
		}
	}

	public boolean isGameOver(){
		if(accusation != null && accusation.accusationStatus()){
			return true;
		}else{
			int count = currentPlayers.size();
			for(Player p : currentPlayers){
				if(p.out()){
					count--;
				}
			}
			if(count == 1){
				return true;
			}
		}
		return false;
	}

	public void printEnvelope(){
		System.out.println("The envelope consisted of these cards: ");
		for(Card c : initializer.getEnvelope().getEnvelope()){
			System.out.println(c.toString());
		}
	}

	/**
	 * Indicates an attempt to make an invalid move.
	 *
	 */
	public static class InvalidMove extends Exception {
		public InvalidMove(String msg) {
			super(msg);
		}
	}


	/**
	 * TODO: Casey, you need to decide how moving is implemented.
	 * Pathfinding, or basic WSAD movement?
	 * This displays the instructions to move
	 */
	public void displayInstructions(){
		System.out.println("Type W to move UP.");
		System.out.println("Type S to move DOWN.");
		System.out.println("Type A to move LEFT.");
		System.out.println("Type D to move RIGHT.");
	}

	public static void main(String[] args) throws InvalidMove{
		CluedoGame game = new CluedoGame();
		game.initialSetup();
		game.runGame();

	}
}