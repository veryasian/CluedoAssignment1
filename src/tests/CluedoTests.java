package tests;
import org.junit.*;

import cluedo.arguments.Accusation;
import cluedo.arguments.Suggestion;
import cluedo.assets.*;
import cluedo.assets.Character;
import cluedo.cards.Card;
import cluedo.cards.CharacterCard;
import cluedo.cards.Envelope;
import cluedo.cards.RoomCard;
import cluedo.cards.WeaponCard;
import cluedo.main.*;
import cluedo.main.CluedoGame.InvalidMove;

import static org.junit.Assert.*;
import java.util.*;


public class CluedoTests {

	
	
	/**
	 * Tests that it can move one position.
	 */
	@Test
	public void testValidMove_1() {
		 
		CluedoGame game = new CluedoGame();
		try {
			Player player = setupMockPlayer("Ralf", "Miss Scarlett", new Position(23, 13));
			player.setLookBack("_|");
			CluedoGame.board.getBoard()[player.position().getX()][player.position().getY()] = player.getCharacterName() + "|"; 
			CluedoGame.board.move(-1, 0, player);
			assertEquals(new Position(22, 13), player.position());
		} catch (CluedoGame.InvalidMove e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Tests that you can move multiple steps.
	 */
	@Test
	public void testValidMove_2() {
		 
		CluedoGame game = new CluedoGame();
		try {
			Player player = setupMockPlayer("Rodolfo", "Miss Scarlett", new Position(23, 13));
			player.setLookBack("_|");
			CluedoGame.board.getBoard()[player.position().getX()][player.position().getY()] = player.getCharacterName() + "|"; 
			CluedoGame.board.move(-4, 0, player);
			assertEquals(new Position(19, 13), player.position());
		} catch (CluedoGame.InvalidMove e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Ensures that you cannot move backwards from your position.
	 */
	@Test
	public void testInvalidMove_1(){
		 
		CluedoGame game = new CluedoGame();
		try{
			Player player = setupMockPlayer("Samuel", "Professor Plum", new Position(0,17));
			player.setLookBack("|_");
			CluedoGame.board.getBoard()[player.position().getX()][player.position().getY()] = player.getCharacterName() + "|"; 
			CluedoGame.board.move(-1, 0, player);
			assertEquals(new Position(0, 17), player.position());
		}catch(CluedoGame.InvalidMove e){
			fail(e.getMessage());
		}
	}
	
	/**
	 * Ensures that you cannot move into a wall. If you move 10 spaces, and 10th square is a wall, it will move
	 * 9 spaces, 1 before the wall.
	 */
	@Test
	public void testInvalidMove_2(){
		 
		CluedoGame game = new CluedoGame();
		try{
			Player player = setupMockPlayer("Namuel", "Professor Plum", new Position(7,24));
			player.setLookBack("__");
			CluedoGame.board.getBoard()[player.position().getX()][player.position().getY()] = player.getCharacterName() + "|"; 
			for(int i = 0; i < 10; i++){
			CluedoGame.board.move(0, -1, player);	
			}
			assertEquals(new Position(7, 16), player.position()); //assert that it moved it one square behind the wall.
		}catch(CluedoGame.InvalidMove e){
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test that it utilizes the diceRoll correctly.
	 */
	@Test
	public void testValidMove_3(){
		 
		CluedoGame game = new CluedoGame();
		int diceRoll = game.diceRoll();
		try{
			Player player = setupMockPlayer("Xamuel", "Professor Plum", new Position(0,17));
			player.setLookBack("_|");
			CluedoGame.board.getBoard()[player.position().getX()][player.position().getY()] = player.getCharacterName() + "|"; 
			for(int i = 0; i < diceRoll; i++){
				CluedoGame.board.move(1, 0, player);
			}
			assertEquals(new Position(diceRoll, player.position().getY()), player.position());
		}catch(CluedoGame.InvalidMove e){
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test that I can go inside of a room.
	 */
	@Test
	public void testGoingInRoom_1(){
		CluedoGame game = new CluedoGame();
		try{
			Player player = setupMockPlayer("Lamuel", "Professor Plum", new Position(0,17));
			player.setLookBack("|_|");
			CluedoGame.board.getBoard()[player.position().getX()][player.position().getY()] = player.getCharacterName() + "|"; 
			CluedoGame.board.move(6, -2, player);
			System.out.println(player.position().toString());
			assertTrue(player.isInRoom());
		}catch(CluedoGame.InvalidMove e){
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGoingInRoom_2(){
		CluedoGame game = new CluedoGame();
		try {
			Player player1 = setupMockPlayer("Ralf", "Professor Plum", new Position(6,18));
			Player player2 = setupMockPlayer("Homer", "Miss Scarlett", new Position(6,17));
			player1.setLookBack("_|");
			player2.setLookBack("_|");
			CluedoGame.board.move(0, 1, player1);
			CluedoGame.board.move(0, 1, player2);
			CluedoGame.board.move(0, 1, player2);
			Player player3 = setupMockPlayer("Sam", "Colonel Mustard", new Position(6,18));
			Player player4 = setupMockPlayer("Kumar", "Mrs. White", new Position(6,17));
			player3.setLookBack("_|");
			player4.setLookBack("_|");
			CluedoGame.board.move(0, 1, player3);
			CluedoGame.board.move(0, 1, player4);
			CluedoGame.board.move(0, 1, player4);
			Player player5 = setupMockPlayer("Flo", "The Reverend Green", new Position(6,18));
			Player player6 = setupMockPlayer("Peter", "Mrs. Peacock", new Position(6,17));
			player5.setLookBack("_|");
			player6.setLookBack("_|");
			CluedoGame.board.move(0, 1, player5);
			CluedoGame.board.move(0, 1, player6);
			CluedoGame.board.move(0, 1, player6);
			System.out.println(player1.position().toString());
			System.out.println(player2.position().toString());
			System.out.println(player3.position().toString());
			System.out.println(player4.position().toString());
			System.out.println(player5.position().toString());
			System.out.println(player6.position().toString());
			
		} catch (InvalidMove e) {
			fail(e.getMessage());
		}
		
	}
	
	/**
	 * Test that I can leave a room after entering it.
	 */
	@Test
	public void testLeavingRoom(){
		CluedoGame game = new CluedoGame();
		try{
			Player player = setupMockPlayer("Lamuel", "Professor Plum", new Position(0,17));
			player.setLookBack("|_");
			CluedoGame.board.getBoard()[player.position().getX()][player.position().getY()] = player.getCharacterName() + "|"; 
			CluedoGame.board.move(6, -2, player); //moving diagonally but who cares anyway?
			System.out.println(player.position().toString());
			assertTrue(player.isInRoom());
			CluedoGame.board.exitRoom(player);
			assertTrue(!player.isInRoom());
		}catch(CluedoGame.InvalidMove e){
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGoingInStairs(){
		CluedoGame game = new CluedoGame();
		try{
			Player player = setupMockPlayer("Lamuel", "Professor Plum", new Position(0,17));
			player.setLookBack("|_");
			CluedoGame.board.getBoard()[player.position().getX()][player.position().getY()] = player.getCharacterName() + "|"; 
			CluedoGame.board.move(6, 2, player); //moving diagonally but who cares anyway?
			assertTrue(player.isInRoom());
			CluedoGame.board.canMove(player);
			//get the room that the player WILL LEAVE
			Room oldRoom = player.getRoom();
			CluedoGame.board.moveToRoom(player, oldRoom.getOtherRoom());
			assertTrue(player.isInRoom());
			//check that the rooms Other Room equals the old room.
			assertEquals(player.getRoom().getOtherRoom(), oldRoom);
		}catch(CluedoGame.InvalidMove e){
			fail(e.getMessage());
		}	
	}
	
	/**
	 * Test when you try to go use stairs in a room that does not have them.
	 */
	@Test
	public void testInvalidGoingStairs(){
	CluedoGame game = new CluedoGame();
	/**
	 * TODO: NOTE: this throws a Null Pointer Exception when you try to access a room that does not have a room.
	 */
	try{
		Player player = setupMockPlayer("Xamuel", "Professor Plum", new Position(0,17));
		player.setLookBack("|_");
		CluedoGame.board.getBoard()[player.position().getX()][player.position().getY()] = player.getCharacterName() + "|"; 
		CluedoGame.board.move(6, -2, player);
		assertTrue(player.isInRoom());
		Room currentRoom = player.getRoom();
		CluedoGame.board.moveToRoom(player, currentRoom.getOtherRoom()); //THIS SHOULDN'T TELEPORT YOU!
		assertEquals(player.getRoom(), currentRoom);
	}catch(CluedoGame.InvalidMove e){
		fail(e.getMessage());
	}
  }
	
	/**
	 * Test that a player must make a valid name.
	 */
	@Test
	public void testInvalidCharacterName(){
		CluedoGame game = new CluedoGame();
		try{
			Player player = setupMockPlayer("Namuel", "Professor Poo Poo Head", new Position(0,17));
			System.out.println(player.getCharacterName());
			assertTrue(player.getCharacterName() == null);
		}catch(CluedoGame.InvalidMove e){
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test that a player must make a valid name.
	 */
	@Test
	public void testValidCharacterTa(){
		CluedoGame game = new CluedoGame();
		try{
			Player player = setupMockPlayer("Namuel", "Professor Plum", new Position(0,17));
			System.out.println(player.getCharacterName());
			assertTrue(player.getCharacterName() == null);
		}catch(CluedoGame.InvalidMove e){
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test that it generates a correct suggestion object.
	 * NOTE: REQUIRES USER INPUT to pass.
	 */
	@Test
	public void testValidSuggestion_01(){
	CluedoGame game = new CluedoGame();
	try{
		Player player = setupMockPlayer("Xamuel", "Miss Scarlett", new Position(0,17));
		player.setLookBack("|_");
		CluedoGame.board.getBoard()[player.position().getX()][player.position().getY()] = player.getCharacterName() + "|";
		CluedoGame.board.move(6, -2, player);
		assertTrue(player.getRoom() != null);
		Suggestion sug = game.makeSuggestion(player);
		assertTrue(sug != null);
	}catch(CluedoGame.InvalidMove e){
		fail(e.getMessage());
	}
   }
	
	/**
	 * This tests that the user makes a correct suggestion, and it will iterate over the other players hands.
	 * Not Finished
	 * 
	 */
	@Test
	public void testValidSuggestion_02(){
	CluedoGame game = new CluedoGame();
	try{
		Player player = setupMockPlayer("A", "Miss Scarlett", new Position(0,17));
		player.addCard(new CharacterCard(new Character("Miss Scarlett")));
		player.addCard(new WeaponCard(new Weapon("Rope")));
		player.addCard(new RoomCard(new Room("Dining Room")));
		
		Player player2 = setupMockPlayer("B", "Professor Plum", new Position(0,25));
		player2.addCard(new CharacterCard(new Character("Miss Scarlett")));
		player2.addCard(new WeaponCard(new Weapon("Rope")));
		player2.addCard(new RoomCard(new Room("Dining Room")));
		
		
		Player player3 = setupMockPlayer("C", "The Reverend Green", new Position(7,24));
		player3.addCard(new CharacterCard(new Character("Miss Scarlett")));
		player3.addCard(new WeaponCard(new Weapon("Rope")));
		player3.addCard(new RoomCard(new Room("Dining Room")));

		
		
		
		player.setLookBack("|_");
		CluedoGame.board.getBoard()[player.position().getX()][player.position().getY()] = player.getCharacterName() + "|";
		CluedoGame.board.move(6, -2, player);
		assertTrue(player.getRoom() != null);
		Suggestion sug = game.makeSuggestion(player);
		
		game.checkOtherPlayers(sug);
	}catch(CluedoGame.InvalidMove e){
		fail(e.getMessage());
	}
	}
	/**
	 * Test that you cannot make an invalid suggestion.
	 * 
	 */
	@Test
	public void testInvalidSuggestion(){
		CluedoGame game = new CluedoGame();
		try{
			Player player = setupMockPlayer("Xamuel", "Miss Scarlett", new Position(0,17));
			player.setLookBack("|_");
			CluedoGame.board.getBoard()[player.position().getX()][player.position().getY()] = player.getCharacterName() + "|";
			CluedoGame.board.move(+2, 0, player);
			assertTrue(player.getRoom() == null);
			Suggestion sug = game.makeSuggestion(player);
			assertTrue(sug == null);
		}catch(CluedoGame.InvalidMove e){
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test that you can make a valid accusation.
	 */
	@Test
	public void testValidAccusation_1(){
		CluedoGame game = new CluedoGame();
		try{
			Envelope env = new Envelope();
			WeaponCard wep = new WeaponCard(new Weapon("Dagger"));
			RoomCard room = new RoomCard(new Room("Ballroom"));
			CharacterCard character = new CharacterCard(new Character("Miss Scarlett"));
			env.add(wep);
			env.add(room);
			env.add(character);
			Player player = setupMockPlayer("Xamuel", "Miss Scarlett", new Position(0,17));
			game.addPlayer("player");
			player.setLookBack("|_");
			CluedoGame.board.getBoard()[player.position().getX()][player.position().getY()] = player.getCharacterName() + "|";
			Accusation accu = new Accusation(wep,room,character,player, env);
			assertTrue(accu != null);
			//assertTrue(accu.argumentStatus());
		}catch(CluedoGame.InvalidMove e){
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test that you can make a valid accusation - AND the game will be over.
	 */
	@Test
	public void testValidAccusation_2(){
		CluedoGame game = new CluedoGame();
		try{
			Envelope env = new Envelope();
			WeaponCard wep = new WeaponCard(new Weapon("Dagger"));
			RoomCard room = new RoomCard(new Room("Ballroom"));
			CharacterCard character = new CharacterCard(new Character("Miss Scarlett"));
			env.add(wep);
			env.add(room);
			env.add(character);
			Player player = setupMockPlayer("Xamuel", "Miss Scarlett", new Position(0,17));
			game.addPlayer("player");
			player.setLookBack("|_");
			CluedoGame.board.getBoard()[player.position().getX()][player.position().getY()] = player.getCharacterName() + "|";
			Accusation accu = new Accusation(wep,room,character,player,env);
			assertTrue(accu != null);
			assertTrue(accu.accusationStatus());
			//assertTrue(accu.checkAccusation(env, player));
			//assertTrue(game.isGameOver());
		}catch(CluedoGame.InvalidMove e){
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test that you can make an invalid accusation.
	 */
	@Test
	public void testInvalidAccusation_1(){
		CluedoGame game = new CluedoGame();
		try{
			Envelope env = new Envelope();
			WeaponCard wep = new WeaponCard(new Weapon("Dagger"));
			RoomCard room = new RoomCard(new Room("Ballroom"));
			CharacterCard character = new CharacterCard(new Character("Miss Scarlett"));
			env.add(wep);
			env.add(room);
			env.add(character);
			Player player = setupMockPlayer("Xamuel", "Miss Scarlett", new Position(0,17));
			game.addPlayer("player");
			player.setLookBack("|_");
			CluedoGame.board.getBoard()[player.position().getX()][player.position().getY()] = player.getCharacterName() + "|";
			Accusation accu = new Accusation(wep,room,(new CharacterCard(new Character("Professor Plum"))), player, env);
			assertTrue(accu != null);
			assertFalse(accu.accusationStatus());
		}catch(CluedoGame.InvalidMove e){
			fail(e.getMessage());
		}
	}
	
	/**
	 * Setup a mock game of monopoly with a player located at a given location.
	 */
	private Player setupMockPlayer(String name, String charName, Position pos)
			throws CluedoGame.InvalidMove {
		
		Player p = new Player(name);
		p.setCharacter(new Character(charName));
		p.setPos(pos.getX(), pos.getY());
		return p;
	}
}
