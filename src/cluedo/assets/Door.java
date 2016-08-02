package cluedo.assets;

/**
 * Door object for determining which way a door is facing.
 * 
 * @author oznaprazzi
 *
 */
public class Door {
	private boolean isHorizontal;
	private Position position;
	
	/**
	 * Stores where the front of the door is.
	 */
	private Position inFront;
	
	public Door(boolean direction, int x, int y){
		this.isHorizontal = direction;
		this.position = new Position(x, y);
	}
	
	/**
	 * Returns which direction the door is facing.
	 * @return
	 */
	public boolean isHorizontal(){
		return this.isHorizontal;
	}
	
	/**
	 * Sets the door's front.
	 * @param p
	 */
	public void setInFront(Position p){
		this.inFront = p;
	}
	
	/**
	 * Returns the door's front.
	 * @return
	 */
	public Position getInFront(){
		return this.inFront;
	}
	
	/**
	 * Returns the position of this door.
	 * @return
	 */
	public Position getPosition(){
		return this.position;
	}
}
