package bomberman;

public class Player {
	private int timer;
	private byte speed = 2;
	private Bomb[] bomb = new Bomb[1];
	private int score;
	private byte health = 100;	//initial health
	private short x;	// See graphically, 20 seems more from next block and 0 more from the block, 15 start 
	private short y;	//Same reason, 20 bottom seems more from next block and 0 more from the block itself 19 start
	private String image;
	private byte animationCounter;	
	private boolean remoteControl;
	private boolean godMode;
	private String[][] animation = {
			{"bomberman101.png","bomberman102.png","bomberman103.png","bomberman104.png","bomberman105.png"}, //0 decreasingY 
			{"bomberman111.png","bomberman112.png","bomberman113.png","bomberman114.png","bomberman115.png"},	//1 increasingY
			{"bomberman121.png","bomberman122.png","bomberman123.png","bomberman124.png","bomberman125.png"}, //2 decreasingX
			{"bomberman131.png","bomberman132.png","bomberman133.png","bomberman134.png","bomberman135.png"}};	//3 increasingX	
	public Player() {
		bomb[0] = new Bomb();
	}
	public void setTimer() {
		this.timer++;
	}
	public void resetTimer() {
		this.timer=0;
	}
	public void addBombNum() {
		byte ratio = this.bomb[0].getRatio(); //coping ratio, only important information of the bomb
		Bomb [] copy = new Bomb[this.bomb.length+1];
		for(int ii=0; ii<copy.length; ii++) {
			copy[ii] = new Bomb();
			copy[ii].setRatio(ratio);
		}
		this.bomb=null;
		this.bomb=copy;
	}
	public void fireUp() {
		//if the ratio is not the maximum
		if (this.bomb[0].getRatio()<5) {
			for (int ii=0; ii<this.bomb.length; ii++) {
				this.bomb[ii].setRatio((byte)(this.bomb[ii].getRatio()+1));
			}
		}
	}
	public void fireMax() {
		for (int ii=0; ii<this.bomb.length; ii++) {
			this.bomb[ii].setRatio((byte)5);
		}
	}
	public void speedUp() {
		if(this.speed<10) {
			this.speed++;
		}
	}
	public void speedMin() {
		this.speed=1;
	}
	public void setScore(byte id) {
		if (id==1) {score+=100;}		//KILLING RED BALOON
		else if (id==2) {score+=250;}	//KILLING BLUE STALKER
		else if (id==3) {score+=350;}	//Killing blue pacman
		else if (id==4) {score+=25;}	//getting a bonus
		else if (id==5) {score+=500;}	//going to the next level
		
	}
	public void setHealth(byte enemyID) {
		if (enemyID==0) {this.health=0;}		//fire
		else if (enemyID==1) {this.health-=20;}	//balloon
		else if (enemyID==2) {this.health-=40;}	//blue stalker
		else if (enemyID==3) {this.health-=45;}	//blue pac-man
		if (this.health<0) {this.health=0;}
	}
	
	public void setX(short x) {
		this.x = x;
	}

	public void setY(short y) {
		this.y = y;
	}
	public void setRemoteControl(boolean remoteControl) {	//for power up, always introducing true
		this.remoteControl=remoteControl;
	}
	public void setRemoteControl() {		//for command
		if(this.remoteControl==false) {this.remoteControl=true;}
		else {this.remoteControl=false;}
	}
	public void setSpeed(byte speed) {	
		if(speed<=10) {
			this.speed=speed;
		}
		else {this.speed=(byte)10;}
	}
	public void setGodMode() {
		if(this.godMode==false) {this.godMode=true;}
		else {this.godMode=false;}
	}
	public void resetAnimationCounter() {		//resets animation counter to 0
		this.animationCounter = 0;
	}
	public void resetPlayer() {
		this.health=100;						//resets for new game
		this.bomb=null;
		this.bomb = new Bomb[1];
		bomb[0] = new Bomb();
		this.score=0;
		this.speed=2;
		this.godMode=false;
		this.remoteControl=false;
		this.timer=5000;
	}
	public void setMovement(String key, Cell[][] myBoard) {
		switch (key) {
		case "right":
			if(myBoard[((this.x+1*this.speed)/10)][((this.y)/10)].isWalkable()) {
				this.animationCounter+=1;
				if((this.animationCounter+1)>4) {
					this.animationCounter=0;
				}
				this.image = animation[3][animationCounter];
				this.x+=(short) (1*this.speed);}
			break;
		case "left":
			if(myBoard[((this.x-1*this.speed)/10)][((this.y)/10)].isWalkable()) {
				 this.animationCounter+=1;
				if((this.animationCounter+1)>4) {
					this.animationCounter=0;
				}
				this.image = animation[2][animationCounter];
				this.x-=(short) (1*this.speed);}
			break;
		case "up":
			if(myBoard[((this.x)/10)][((this.y-1*this.speed)/10)].isWalkable()) {
				 this.animationCounter+=1;
				if((this.animationCounter+1)>4) {
					this.animationCounter=0;
				}
				this.image = animation[0][animationCounter];
				this.y-=(short) (1*this.speed);}
			break;
		case "down":
			if(myBoard[((this.x)/10)][((this.y+1*this.speed)/10)].isWalkable()) {
				 this.animationCounter+=1;
				if((this.animationCounter+1)>4) {
					this.animationCounter=0;
				}
				this.image = animation[1][animationCounter];
				this.y+=(short) (1*this.speed);}
			break;
		}
	}
	
	public String getDieAnimation() {
		this.animationCounter++;
		if(this.animationCounter<5) {return "bomberman141.png";}
		else if(this.animationCounter<10) {return"bomberman142.png";}
		else if(this.animationCounter<15) {return"bomberman143.png";}
		else if(this.animationCounter<20) {return"bomberman144.png";}
		else {return"bomberman145.png";}
		
	}
	public byte getAnimationCounter() {
		return animationCounter;
	}

	public Bomb[] getBomb() {
		return bomb;
	}

	public boolean isRemoteControl() {
		return remoteControl;
	}

	public byte getHealth() {
		return this.health;
	}
	public byte getSpeed() {
		return this.speed;
	}
	public int getScore() {
		return this.score;
	}
	public short getX() {
		return this.x;
	}
	public short getY() {
		return this.y;
	}
	public String getImage() {
		return this.image;
	}
	public boolean isGodMode() {
		return godMode;
	}
	public int getTimer() {
		return this.timer;
	}
}
