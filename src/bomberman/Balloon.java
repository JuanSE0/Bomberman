package bomberman;

public class Balloon extends Enemy{

	public Balloon() {			//I SUPPOSED IMAGES 111 112 113 WERE FOR GOING LEFT AND UP
								//AND 121 122 123 DOWN AND RIGHT
		setAnimation(super.animation);
	}
	public void setAnimation(String [][] animation) {
		String[][] copy={
				{"enemy111.png","enemy112.png","enemy113.png"}, //LEFT AND UP
				{"enemy121.png","enemy122.png","enemy123.png"}};	//DOWN AND RIGHT
		super.animation=copy;
	}
	public void movement(Cell[][] myBoard) {

		if (super.counter==0 || super.counter==8) {
			super.movement = (byte) (Math.random()*4+1);
			super.counter=0;
			}
		if (super.movement == 1 && 
				myBoard[((super.x - 1) / 10)][(super.y / 10)].isEnemyWalkable()){
			super.x--;
			super.counter++;
			super.image=super.animation[0][(super.counter/3)];
		} else if (super.movement == 2 
				&& myBoard[((super.x) / 10)][((super.y - 1) / 10)].isEnemyWalkable()) {
			super.y--;
			super.counter++;
			super.image=super.animation[0][(super.counter/3)];
		} else if (super.movement == 3 && 
				myBoard[((super.x + 1) / 10)][(super.y / 10)].isEnemyWalkable()) {
			super.x++;
			super.counter++;
			super.image=super.animation[1][(super.counter/3)];
		} else if (super.movement == 4 
				&& myBoard[((super.x) / 10)][((super.y + 1) / 10)].isEnemyWalkable()) {
			super.y++;
			super.counter++;
			super.image=super.animation[1][(super.counter/3)];
		} 
		else {super.counter=0;}
	}

}
