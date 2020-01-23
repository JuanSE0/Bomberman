package bomberman;

import edu.uc3m.game.GameBoardGUI; 		
										
public class MainProgram {
	
	public static final int SIZE = 17;
	public static final int NUMBEROFLEVELS = 15;
	public static GameBoardGUI board = new GameBoardGUI(SIZE, SIZE);
	public static Cell[][][] myBoard = new Cell[NUMBEROFLEVELS][SIZE][SIZE]; // 15 NUMBER OF LEVELS
	//two dimension array, rows for enemy type, columns for number of them
	public static Enemy enemies[][] = new Enemy [3][];
	public static Player player = new Player();
	public static int level;
	public static boolean newGame=true;
	
	public static void main(String[] args) throws InterruptedException {
		boolean exitGame=false;
		while(!exitGame) {
			//in case we have already play clear sprites
			board.gb_clearSprites();
			level=0;
			board.gb_setTextPointsUp("Score");
			board.gb_setTextPointsDown("Bombs");
			board.gb_setTextAbility1("Speed");
			board.gb_setTextAbility2("Bomb range");
			board.gb_setPortraitPlayer("White_Bomberman_R.png");
			board.gb_setValuePointsUp(player.getScore());
			board.gb_setValuePointsDown(player.getBomb().length);
			board.gb_setValueAbility1(player.getSpeed());
			board.gb_setValueAbility1(player.getBomb()[0].getRatio());
			board.gb_setValueHealthCurrent(player.getHealth());
			board.gb_setValueHealthMax(100);
			
			boolean alive = player.getHealth()>0;
			for (level=0; alive && level<NUMBEROFLEVELS; level++) {
				if(newGame==true) {level=0;}
				newGame=false;
				board.gb_setValueLevel(level+1);
				player.resetTimer();		//setting player timer to 0
				for (int ii = 0; ii < SIZE; ii++) { // Placement of the boardgame itself
					for (int cc = 0; cc < SIZE; cc++) {
						// setting the walls
						if (ii == 0 || cc == 0 || ii == (SIZE - 1) || cc == (SIZE - 1) || (ii % 2 == 0 && cc % 2 == 0)) {
							myBoard[level][ii][cc] = new Cell((byte) 0);
							board.gb_setSquareImage(cc, ii, "wall.gif");
						} //Setting the floor
						else {
							myBoard[level][ii][cc] = new Cell((byte) 1);
							board.gb_setSquareImage(cc, ii, null);
							board.gb_setSquareColor(cc, ii, 173, 230, 150);
							
						}
					}
				} // Placement of the bricks per level
				byte bricks = (byte) (Math.random() * 7 + 40);
				for (int ii = 0; ii < bricks; ii++) {
					boolean placed = false;
					while (!placed) {
						byte brickX = (byte) (Math.random() * SIZE);
						byte brickY = (byte) (Math.random() * SIZE);
						if (myBoard[level][brickX][brickY].getCellID() == 1 && (brickX > 2 || brickY > 2)) {
							myBoard[level][brickX][brickY].setCellID((byte) 2);
							board.gb_setSquareImage(brickX, brickY, "bricks.gif");
							placed = true;
						}
					}
				}//bonuses
				setBonuses(myBoard[level]);
				enemies();
				board.setVisible(true); // "syso" board
				board.gb_addSprite(0, "bomberman111.png", true); // adding player and its initial position
				board.gb_moveSpriteCoord(0, 10, 10);
				player.setX((short)15);
				player.setY((short)19);
				board.gb_setSpriteVisible(0, true);
				boolean doorClosed=true;
				while (alive && doorClosed) {
					alive = player.getHealth()>0;
					player.setTimer(); 	//adding one to the timer
					scoreboard();
					//BONUSES
					for (int ii=0; ii<myBoard[level].length; ii++) {
						for (int cc=0; cc<myBoard[level].length; cc++) {
							if(myBoard[level][ii][cc].isItem() && myBoard[level][ii][cc].getCellID()==1) {
								board.gb_setSquareImage(ii, cc, myBoard[level][ii][cc].getItemImage());
							}
							//if in the position of the player there's a boost
							if(myBoard[level][player.getX()/10][player.getY()/10].isItem()) {
								//boost will return true if the player is not in the opened door
								doorClosed=boost();
							}
						}
					}
					if(!player.isGodMode()) {damage();}		//if godmode is disable check for damage
					for (int ii=0; ii<player.getBomb().length; ii++) {
						if(!player.getBomb()[ii].isAvailable()) {		//If we have bomb placed set timer
							bomb(ii);
						}
					}
					moveEnemies();
					String key = board.gb_getLastAction().trim();
					switch (key) {
					case "right":
					case "left":
					case "up":
					case "down":
						player.setMovement(key, myBoard[level]);
						board.gb_setSpriteImage(0, player.getImage());
						board.gb_moveSpriteCoord(0, player.getX()-5, player.getY()-9);
						break;
					case "space":
						board.gb_clearCommandBar();
						boolean placed = false;	//So it stops once the bomb has been placed and check for others bombs if necessary
						for (int ii=0; ii<player.getBomb().length && !placed; ii++) {
							if (player.getBomb()[ii].isAvailable() && 
									myBoard[level][(player.getX()/10)][(player.getY()/10)].getCellID()!=3) {
								player.getBomb()[ii].setAvailable(false);
								player.getBomb()[ii].setX((short) (player.getX()/10));		//setting bomb x and y
								player.getBomb()[ii].setY((short) (player.getY()/10));
								placed=true;
								//changing cell id to 3 so we know a bomb has been placed here
								myBoard[level][player.getBomb()[ii].getX()][player.getBomb()[ii].getY()].setCellID((byte)3);
								board.gb_println("A bomb has been placed at "+player.getBomb()[ii].getX()+" "+player.getBomb()[ii].getY());
							}
						} 
						break;
					case "tab":
						if(player.isRemoteControl()) {
							for(int ii=0; ii<player.getBomb().length; ii++) {
								if(!player.getBomb()[ii].isAvailable()) {
									player.getBomb()[ii].setTimer(70);
								}
							}
						} 
					
					default: 
						if (key.contains("command")){command(key);}
						else if (key.contains("new game")) {
							doorClosed=newGame(key);}
						else if (key.contains("exit game")) {
							alive=false;
							exitGame=true;
						}
					} 
					Thread.sleep(100);
				}
				if (!alive && !exitGame) {
					//as we have used the animation counter for the movement we reset it to 0
					player.resetAnimationCounter();
					while(player.getAnimationCounter()<25) {
						board.gb_setSpriteImage(0, player.getDieAnimation());
						Thread.sleep(50);
					}
					board.gb_setSpriteVisible(0, false);
					board.gb_println("Game over");
				}
				//going to the next level
				else if (alive){
					board.gb_clearSprites();
					if(player.getTimer()<1800) {
						player.setScore((byte)5);
						board.gb_println("+500 points");
					}
				}
				if(level==14) {	//if the user arrives to the last level
					board.gb_println("Congratulations, you win! Your final score was "+player.getScore());
				}
			}
			boolean leave=false;
			if(exitGame==true) {leave=true;}
			while(!leave) {
				String key = board.gb_getLastAction().trim();
				if (key.contains("new game")) {
					newGame(key);
					leave=true;
					}
				else if (key.contains("exit game")) {
					leave=true;
					exitGame=true;
				}
			}
			
		}
		System.exit(0);
	}
	
	public static void setBonuses(Cell[][] myBoard) {
		//I believe more bonuses mean more fun, so I incremented chances of bonuses on each level
			byte[] bonuses = new byte[7];
			bonuses[0]=1;							//closed door
			bonuses[1]=(byte)(Math.random()*2+1);	//Extrabombs, id of the item 1
			bonuses[2]=(byte)(Math.random()+1);							//ExtraExplosionRatio itemID 2 (fire)
			if((level+1)%5==0) {bonuses[3]=1;}			//ExtraSuperExplosion itemID 3 (superfire)
			if((level+1)%4==0) {bonuses[4]=1;}	//Remote control ID 4
			bonuses[5]=(byte)(Math.random()*3+1);						//Speed ID 5
			if(((int)(Math.random()*10))<2) {		//20% chance
				bonuses[6]=1;					//Speed to 1 ID6
			}
			for (int cc=0; cc<bonuses.length; cc++)
			for(int tt=0; tt<bonuses[cc]; tt++) {
				boolean placed = false;
				while (!placed) {
					byte x = (byte) (Math.random() * myBoard.length);
					byte y = (byte) (Math.random() * myBoard.length);
					if (myBoard[x][y].getCellID() == 2 && !myBoard[x][y].isItem()) {
						myBoard[x][y].setItemID((byte) cc);
						bonuses[cc]-=1;
						placed = true;
					}
				}
			}
	}
		
	public static void bomb(int ii) {
		if (!player.isRemoteControl()) {				//if remote control is active don't add anything to the timer, so it will always remain without blowing
			player.getBomb()[ii].setTimer();			//Adding one to the timer
		}
		int x = player.getBomb()[ii].getX();		//So the code is easier to read
		int y = player.getBomb()[ii].getY();
		myBoard[level][x][y].setCellID((byte)3);
		if(player.getBomb()[ii].getTimer()<70) {		//Time till it blows
			board.gb_setSquareImage(x, y, player.getBomb()[ii].getPlacementAnimation());		
		}
		else if (player.getBomb()[ii].getTimer()==70) {
			if (player.isRemoteControl()) {
				player.getBomb()[ii].setTimer();			
			}
			player.getBomb()[ii].setAnimationsCounter((byte)0);
			}
		else if (player.getBomb()[ii].getTimer()>70 && player.getBomb()[ii].getTimer()<=90){
			if (player.isRemoteControl()) {
				player.getBomb()[ii].setTimer();			//Adding one to the timer
			}
			for (int cc=-player.getBomb()[ii].getRatio(); 
					cc<=player.getBomb()[ii].getRatio();cc++) {
				for (int tt=-player.getBomb()[ii].getRatio(); 
						tt<=player.getBomb()[ii].getRatio(); tt++) {
					//Setting the fire
					if ((tt==0 || cc==0) && player.getBomb()[ii].getTimer()<90 &&
							(x+cc)<myBoard[level].length && (y+tt)<myBoard[level].length &&		//In case explosion ratio>1 and it leaves the board
							(x+cc)>=0 && (y+tt)>=0 &&
							myBoard[level][x+cc][y+tt].getCellID()!=0) {	
						myBoard[level][x+cc][y+tt].setCellID((byte)5);	
						board.gb_setSquareImage(x+cc, y+tt, player.getBomb()[ii].getBlowingAnimation(cc, tt));
					}
					//destroying the fire
					else if ((tt==0 || cc==0) && (x+cc)<myBoard[level].length && (y+tt)<myBoard[level].length 
							&& (x+cc)>=0 && (y+tt)>=0
							&& myBoard[level][x+cc][y+tt].getCellID()!=0 ) {		//In case explosion ratio>1 and it leaves the board
						board.gb_setSquareImage(x+cc, y+tt, null);
						myBoard[level][x+cc][y+tt].setCellID((byte)1);	//setting again normal floor
					}
				}
			}
			player.getBomb()[ii].setAnimationsCounter();
		}//Allowing to reuse the bomb, setting its timer to 0
		else if (player.getBomb()[ii].getTimer()>90){
			player.getBomb()[ii].setAvailable(true);
			myBoard[level][x][y].setCellID((byte)1);	//setting cell id of the place of the bomb back to one
			board.gb_setSquareImage(x, y, null);
			player.getBomb()[ii].setTimer(0);
			player.getBomb()[ii].setAnimationsCounter((byte)0);
		}
	}
	
	
	public static void enemies() {
		int id=1; //We start by 1 as 0 is player
		enemies[0] = new Balloon[(int) (Math.random()*9+1)];	//Number of balloons [1-10]
		for(int ii=0; ii<enemies[0].length; ii++) {
			enemies[0][ii] = new Balloon();
			//id ii+1 as 0 is the player
			board.gb_addSprite((ii+1), "enemy100.png", true);
			board.gb_setSpriteVisible((ii+1), true);
		}
		id+=enemies[0].length;
		if ((level+1)/2>=1) {										//Number of blueStalkers
			if(level<6) {enemies[1] =  new BlueStalker[(level+1/2)];}
			else {enemies[1] =  new BlueStalker[5];}				//otherwise there are too many enemies
			for(int ii=0; ii<enemies[1].length; ii++) {
				enemies[1][ii] = new BlueStalker();
				board.gb_addSprite((ii+id), "enemy200.png", true);
				board.gb_setSpriteVisible((ii+id), true);
			}
			id+=enemies[1].length;
		}
		if((level+1)>2) {
			if(level<7) {enemies[2] =  new BluePacMan[(level+1/2)];}
			else {enemies[2] =  new BluePacMan[5];}		//otherwise too many enemies
			for(int ii=0; ii<enemies[2].length; ii++) {
				enemies[2][ii] = new BluePacMan();
				board.gb_addSprite((ii+id), "enemy300.png", true);
				board.gb_setSpriteVisible((ii+id), true);
			}
		}
		
		id=1; //We start by 1 as 0 is player
		for (int ii=0; ii<enemies.length; ii++) {
			for (int cc=0; enemies[ii]!=null && cc<enemies[ii].length; cc++) {
				boolean placed=false;
				while(!placed) {		
					byte x = (byte) (Math.random()*SIZE);		
					byte y = (byte) (Math.random()*SIZE);
					//x>4 || y>4 so we don't have enemies spawned too close to the player
					if(myBoard[level][x][y].getCellID()==1 && (x>4 || y>4)) {
						board.gb_moveSprite(cc+id, x, y);
						myBoard[level][x][y].setCellID((byte)4);
						enemies[ii][cc].x=(short) (x*10+5);
						enemies[ii][cc].y=(short) (y*10+9);
						placed = true;
					}
				}
			}
			if(enemies[ii]!=null) {id+=enemies[ii].length;}
		}
	}
	public static void moveEnemies() {
		boolean enemyAlive=false;
		int id=1;		//starting with id 1 as id 0 is the player (sprites)
		for(int ii=0;enemies!=null && ii<enemies.length; ii++) {			//enemies[ii]!=null if there are all enemy types, there will be levels with only balloons
			for (int cc=0; enemies[ii]!=null && cc<enemies[ii].length; cc++) {
				 if(enemies[ii][cc]!=null) {
					 // if there are blue Stalkers store player position into the board by changing a cell id so we know which cell is player
					if (ii==1) {
						//if I didn't check if the player is over a bomb I would change the id of bomb place, allowing bluestalker to follow me
						//even through the bomb and blue pacman wouldn't be able to eat the bomb 
						boolean playerOverBomb=false;
						for(int tt=0;!playerOverBomb && tt<player.getBomb().length; tt++) {
							if (player.getBomb()[tt].getX()==(player.getX()/10) && 
									player.getBomb()[tt].getY()== (player.getY()/10)) {
								playerOverBomb=true;
							}
						}
						if(!playerOverBomb) {myBoard[level][(player.getX()/10)][(player.getY()/10)].setCellID((byte)6);}
					}
					enemies[ii][cc].movement(myBoard[level]);
					//if there are blue pacmans and they are on a bomb
					if(ii==2) {
						int x = (enemies[ii][cc].x/10);
						int y = (enemies[ii][cc].y/10);
						if(myBoard[level][x][y].getCellID()==3) {
							for (int tt=0; tt<player.getBomb().length; tt++) {
								if(!player.getBomb()[tt].isAvailable() && player.getBomb()[tt].getX()==x &&
										player.getBomb()[tt].getY()==y) {
									player.getBomb()[tt].setTimer(91);
								}
							}
						}
					}
					board.gb_moveSpriteCoord(cc+id, enemies[ii][cc].x-5, enemies[ii][cc].y-9);
					board.gb_setSpriteImage(cc+id, enemies[ii][cc].image);
					if (myBoard[level][((enemies[ii][cc].x)/10)][((enemies[ii][cc].y)/10)].getCellID()==5) {
						player.setScore((byte)(ii+1));
						board.gb_println("You have killed an enemy score has increased");
						enemies[ii][cc]=null;
						board.gb_setSpriteVisible(cc+id, false);
					}
				}
			}
			for(int cc=0; !enemyAlive && enemies[ii]!=null && cc<enemies[ii].length; cc++) {
				if(enemies[ii][cc]!=null) {enemyAlive=true;}
			}
			if(enemies[ii]!=null) {id+=enemies[ii].length;}
		}
		if(!enemyAlive) {
			boolean doorfound=false;
			for (int tt=0; !doorfound && tt<myBoard[level].length;tt++) {
				for (int vv=0; vv<myBoard[level][tt].length; vv++) {
					if(myBoard[level][tt][vv].getItemID()==0) {
						myBoard[level][tt][vv].setItemID((byte)7);
						board.gb_println("Door is now open, find it and you will go to the next level");
						doorfound=true;
					}
				}
			}
		}
	}
	public static void scoreboard() {
		board.gb_setValueHealthCurrent(player.getHealth());
		board.gb_setValuePointsUp(player.getScore());
		board.gb_setValueAbility1(player.getSpeed());
		board.gb_setValueAbility2(player.getBomb()[0].getRatio());
		byte availableBombs=0;
		for (int ii=0; ii<player.getBomb().length; ii++) {
			if(player.getBomb()[ii].isAvailable()) {availableBombs++;}
		}
		board.gb_setValuePointsDown(availableBombs);
	}
	public static void damage() {
		for (int ii=0; enemies!=null && ii<enemies.length; ii++) {
			if(enemies[ii]!=null) {
				for (int cc=0; cc<enemies[ii].length; cc++) {
					if(enemies[ii][cc]!=null) {
						if((player.getX()/10)==(enemies[ii][cc].x/10) && 
								(player.getY()/10)==(enemies[ii][cc].y/10)) {
							player.setHealth((byte)(ii+1));
						}
					}
				}
			}
		}
		if(myBoard[level][player.getX()/10][(player.getY()/10)].getCellID()==5) {
			player.setHealth((byte)0);
		}
	}
	
	public static boolean boost () {
		if(myBoard[level][player.getX()/10][player.getY()/10].getItemID()!=0 &&	//if id its not the closed door
				myBoard[level][player.getX()/10][player.getY()/10].getItemID()!=7) {// neither the open door
			myBoard[level][player.getX()/10][player.getY()/10].setItem(false);		//saying no item is longer on this cell
			board.gb_setSquareImage(player.getX()/10, player.getY()/10, null);		//erasing image
			byte id = myBoard[level][player.getX()/10][player.getY()/10].getItemID();
			player.setScore((byte)4);
			board.gb_println("+20 points");
			if(id==1) {
				player.addBombNum();
				board.gb_println("You now have an extra bomb");
				board.gb_setValuePointsDown(player.getBomb().length);					//updating number of bombs
				}
			else if (id==2) {
				player.fireUp();
				board.gb_setValueAbility2(player.getBomb()[0].getRatio());
				board.gb_println("Explosion range is now 1 square bigger");
				}
			else if (id==3) {
				player.fireMax();
				board.gb_println("Explosion range is now the maximum");
			}
			else if (id==4) {
				if(!player.isRemoteControl()) {	//if it hasn't already been implemented 
					player.setRemoteControl(true);
					board.gb_println("Remote control enabled");
				}
			} 		
			else if (id==5) {
				player.speedUp();
				board.gb_setValueAbility1(player.getSpeed());
				board.gb_println("Speed has increased by 10%");
				}
			else if (id==6) {
				player.speedMin();
				board.gb_println("Speed has been restored to default");
			}
		}
		else if (myBoard[level][player.getX()/10][player.getY()/10].getItemID()==7) {
			return false;
		}
		return true;
	}
	public static void command(String key) {	
		switch (key) {
		case"command /clear":		//clears level from either blocks and enemies
			for (int ii=0; ii<myBoard[level].length; ii++) {
				for (int cc=0; cc<myBoard[level][ii].length; cc++) {
					if(myBoard[level][ii][cc].getCellID()==2) {
						myBoard[level][ii][cc].setCellID((byte)1);
						board.gb_setSquareImage(ii, cc, null);}
				}
			}
			int id=1;		//as player is id 0 we start by one (sprite id)
			for (int ii=0; enemies!=null && ii<enemies.length; ii++) {
				//outside the for in case there are still blueStalkers but not balloons
				if(enemies[ii]!=null) {
					for (int cc=0; cc<enemies[ii].length; cc++) {
						board.gb_setSpriteVisible(cc+id, false);
					}
				}
				if(enemies[ii]!=null) {
					id+=enemies[ii].length;
					enemies[ii]=null;
				}
			}
			board.gb_println("The level has been cleared");
			break;
		case"command /destroy":	//destroys all blocks
			for (int ii=0; ii<myBoard[level].length; ii++) {
				for (int cc=0; cc<myBoard[level][ii].length; cc++) {
					if(myBoard[level][ii][cc].getCellID()==2) {
						myBoard[level][ii][cc].setCellID((byte)1);
						board.gb_setSquareImage(ii, cc, null);}
				}
			}
			board.gb_println("All blocks have been destroyed");
			break;
		case "command /speed"+"[1-10]":
			String part[] = key.split("ed");
			board.gb_println(part[2]);
			player.setSpeed((byte)Integer.parseInt(part[2]));
			break;
		case"command /kill":		//kills all enemies
			id=1; //id= 1 as player is id 0 (sprites)
			for (int ii=0; enemies!=null && ii<enemies.length; ii++) {
				//outside the for in case there are still blueStalkers but not balloons
				if(enemies[ii]!=null) {
					for (int cc=0; cc<enemies[ii].length; cc++) {
						board.gb_setSpriteVisible(cc+id, false);
					}
				}
				if(enemies[ii]!=null) {id+=enemies[ii].length;}
				enemies[ii]=null;
			}
			board.gb_println("All enemies are now death");
			break;
		case "command /show":
			for (int ii=0; ii<myBoard[level].length; ii++) {
				for (int cc=0; cc<myBoard[level][ii].length; cc++) {
					if (myBoard[level][ii][cc].isItem()) {
						board.gb_setSquareImage(ii, cc, myBoard[level][ii][cc].getItemImage());
					}
				}
			}
			break;
		case "command /godmode":
			player.setGodMode();
			if(player.isGodMode()) {board.gb_println("Godmode has been enabled");}
			else {board.gb_println("Godmode has been disabled");}
			break;
		case "command /remotecontrol":
			player.setRemoteControl();
			if(player.isRemoteControl()) {board.gb_println("Remote detonation is now enabled");}
			else {board.gb_println("Remote detonation is now disabled");}
			break;
		case "command /speedup":
			player.speedUp();
			board.gb_println("Speed has been incrased by 10%");
			board.gb_setValueAbility1(player.getSpeed());
			break;
		case "command /addbomb":
			player.addBombNum();
			board.gb_println("You now have an extra bomb");
			break;
		case "command /fireup":
			player.fireUp();
			board.gb_println("Your explotions are now greater");
			break;
		case "command /resetstats":
			player.resetPlayer();
			player.resetTimer();
			board.gb_println("Stats have been reseted");
			scoreboard();
			break;
		case "command /?":
			board.gb_clearConsole();
			board.gb_println("You can use the following commands");
			board.gb_println("/clear "+"/destroy "+"/kill "+"/show "+"/godmode "+"/remotecontrol"+"/speedup"+"/addbomb"+"/fireup"+"/resetstats");
			break;
		default: 
			board.gb_println("Unknown command please enter /? for discovering available commands");
		}
		
		board.gb_clearCommandBar();
	
	}
	public static boolean newGame(String key){
		String[] parts = key.split(" ");
		player.resetPlayer();
		board.gb_setTextPlayerName(parts[2]);
		newGame = true;
		board.gb_clearConsole();
		return false;
		}
		
}
