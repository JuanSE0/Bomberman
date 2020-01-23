package bomberman;

public class BlueStalker extends Enemy{

	public BlueStalker() {
		setAnimation(super.animation);
	}
	public void setAnimation(String [][] animation) {
		String[][] copy={
				{"enemy211.png","enemy212.png","enemy213.png"}, //LEFT AND UP
				{"enemy221.png","enemy222.png","enemy223.png"}};	//DOWN AND RIGHT
		super.animation=copy;
	}
	public void movement(Cell myBoard[][]) {
		if(myBoard[(super.x/10)][(super.y/10)].getCellID()!=6){
			boolean coordinatesFound=false;
			for (int ii=1;!coordinatesFound && ii<(myBoard.length-1); ii++) {	//starting at 1 as 0 is the wall
				for (int cc=1;!coordinatesFound && cc<(myBoard[ii].length-1); cc++) {	//ending at length-1 for same reason
					if(myBoard[ii][cc].getCellID()==6) {
						coordinatesFound=true;
						if ((super.x/10)>ii) {
							if(myBoard[((super.x-1)/10)][(super.y/10)].isEnemyWalkable()){
								super.x--;
								super.image=super.animation[0][super.counter];
								}
							}
						else if ((super.y/10)>cc) {
							if(myBoard[(super.x/10)][((super.y-1)/10)].isEnemyWalkable()){
								super.y--;
								super.image=super.animation[0][super.counter];
								}
							}
						else if((super.x/10)<ii) {
							if(myBoard[((super.x+1)/10)][(super.y/10)].isEnemyWalkable()){
								super.x++;
								super.image=super.animation[1][super.counter];
								}
							}
						else if ((super.y/10)<cc) {
							if(myBoard[(super.x/10)][((super.y+1)/10)].isEnemyWalkable()){
								super.y++;
								super.image=super.animation[1][super.counter];
								}
							}
						super.counter++;
						//restoring cell id so there are not multiple player positions
						myBoard[ii][cc].setCellID((byte)1);
						if(super.counter==3) {super.counter=0;}
					}
				}
			}
		}
		//restoring cell id in case enemy and player where in the same square and player has survived
		else {myBoard[(super.x/10)][(super.y/10)].setCellID((byte)1);}
	}
}
