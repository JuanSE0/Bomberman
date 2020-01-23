package bomberman;

public class BluePacMan extends Enemy{

	public BluePacMan() {
		setAnimation(super.animation);
	}
	public void setAnimation(String [][] animation) {
		String[][] copy = {
				{"enemy301.png","enemy302.png","enemy303.png"}, 
				{"enemy311.png","enemy312.png","enemy313.png"},
				{"enemy321.png","enemy322.png","enemy323.png"},
				{"enemy331.png","enemy332.png","enemy333.png"}};
		super.animation=copy; 
	}
	public void movement(Cell[][] myBoard) {

		if (super.counter==-1) {super.movement = (byte) (Math.random()*4+1);}
		if (super.counter==3||super.counter==-1) {super.counter=0;}
		if (super.movement == 1 && 
				myBoard[((super.x + 5) / 10)][(super.y / 10)].isWalkable()) {
			super.x++;
			super.image=super.animation[3][super.counter];
			super.counter++;
		} else if (super.movement == 2 
				&& myBoard[((super.x) / 10)][((super.y+2) / 10)].isWalkable()) {
			super.y++;
			super.image=super.animation[1][super.counter];
			super.counter++;
		} else if (super.movement == 3 && 
				myBoard[((super.x - 5) / 10)][(super.y / 10)].isWalkable()) {
			super.x--;
			super.image=super.animation[2][super.counter];
			super.counter++;
		} else if (super.movement == 4 
				&& myBoard[((super.x) / 10)][((super.y - 10) / 10)].isWalkable()) {
			super.y--;
			super.image=super.animation[0][super.counter];
			super.counter++;
		}
		else {super.counter=-1;}
	}
}
