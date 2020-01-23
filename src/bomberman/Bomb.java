package bomberman;

public class Bomb {
	private short x, y;
	private byte ratio = 1;
	private byte animationsCounter;
	private String[] placementAnimation = {"bomb1.gif","bomb2.gif"};
	private String[][][] blowingAnimation =
		{	{{""					,"explosion_N1.gif",				""},		//Top1
			{"explosion_W1.gif","explosion_C1.gif","explosion_E1.gif"},	//Middle1
			{""					,"explosion_S1.gif",				""}},	//Bottom1
				
			{{"",				"explosion_N2.gif",				""},		//Top2
			{"explosion_W2.gif","explosion_C2.gif","explosion_E2.gif"},	//Middle2
			{""					,"explosion_S2.gif",				""}},	//Bottom2
			
			{{""					,"explosion_N3.gif",				""},		//Top3
			{"explosion_W3.gif","explosion_C3.gif","explosion_E3.gif"},	//Middle3
			{""					,"explosion_S3.gif"				,""}},	//Bottom3
			
			{{""					,"explosion_N4.gif",				""},		//Top4
			{"explosion_W4.gif","explosion_C4.gif","explosion_E4.gif"},	//Middle4
			{""					,"explosion_S4.gif",				""}}};	//Bottom4
	private String[][] animationBiggerRatio=
		{	{"explosion_H1.gif","explosion_V1.gif"},		//Horizontal and Vertical 1
			{"explosion_H2.gif","explosion_V2.gif"},		//Horizontal and Vertical 2
			{"explosion_H3.gif","explosion_V3.gif"},		//Horizontal and Vertical 3
			{"explosion_H4.gif","explosion_V4.gif"}};	//Horizontal and Vertical 4
	private boolean available = true;
	private int timer;
	public Bomb() {
	}
	
	public long getTimer() {
		return timer;
	}
	public void setTimer(int time) {
		this.timer=time;
	}

	public void setTimer() {		//for automatically adding one
		this.timer++;
	}
	public void setAnimationsCounter(byte animationsCounter) {
		this.animationsCounter=animationsCounter;
	}
	public void setAnimationsCounter() {
		this.animationsCounter++;
	}
	public byte getRatio() {
		return this.ratio;
	}
	public void setRatio(byte ratio) {
		this.ratio=ratio;
	}
	public void setX(short x) {
		this.x = x;
	}
	public void setY(short y) {
		this.y = y;
	}
	
	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public short getX() {
		return this.x;
	}
	
	public short getY() {
		return this.y;
	}
	
	public String getPlacementAnimation() {
		if(this.animationsCounter==1) {this.animationsCounter=0;}
		else {this.animationsCounter++;}
		return this.placementAnimation[this.animationsCounter];
	}
	public String getBlowingAnimation(int rows, int columns) {
		if(this.animationsCounter==4) {this.animationsCounter=2;}
		if(this.ratio==1) {return blowingAnimation[this.animationsCounter][this.ratio+columns][this.ratio+rows];}
		else {
			if(this.ratio-columns==0 &&this.ratio-rows==0) {		//center
				return blowingAnimation[this.animationsCounter][1][1];
			}
			else if(this.ratio+columns==0) {					//top
				return blowingAnimation[this.animationsCounter][0][1];
			}
			else if(this.ratio==columns) {					//bottom
				return blowingAnimation[this.animationsCounter][2][1];
			}
			else if ((rows!=-this.ratio||rows!=this.ratio) && columns!=0) {	
				return animationBiggerRatio[this.animationsCounter][1];	//rest  vertical explosion
			}
			else if(this.ratio+rows==0) {					//left
				return blowingAnimation[this.animationsCounter][1][0];
			}	
			else if(this.ratio==rows) {						//right
				return blowingAnimation[this.animationsCounter][1][2];
			}
			else if((columns!=-this.ratio||columns!=0) && rows!=0) {		//rest horizontal explosion
				return animationBiggerRatio[this.animationsCounter][0];
			}
			else{return blowingAnimation[this.animationsCounter][1][1];}
		}
	}
}
