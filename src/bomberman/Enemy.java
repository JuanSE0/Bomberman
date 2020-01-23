package bomberman;

public abstract class Enemy {		//an abstract class is a class where I implement the method				
	protected short x, y;
	protected byte counter;	
	protected byte movement;		
	protected String image;
	protected String[][] animation;
	
	public abstract void movement (Cell[][] myBoard);
		
}
