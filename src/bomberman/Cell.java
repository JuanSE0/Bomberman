package bomberman;

public class Cell {

	private byte cellID;	//ID 0 for wall, 1 for green, 2 for brick, 3 , 4 enemy SPAWN, 5 fire, 6 player position
	private boolean walkable = true;
	private boolean item;
	private byte itemID=-1;		//-1 NONE
	
	public Cell(byte cellID) {
		setCellID(cellID);
	}
	
	public void setCellID(byte cellID) {
		this.cellID = cellID;
		if (cellID==0) {					//0 WALL
			this.walkable = false;
		}
		else if(cellID==1) {				//1 FLOOR
			this.walkable = true;
		}
		else if(cellID==2) {				//2 BRICK
			this.walkable=false;
		}
		else if (cellID==3) {
			this.walkable=true;			//3 Placed bomb
		}								//4 spawn of enemies							
		else if (cellID==5) {			//5 fire
			this.walkable=true;			//6 player position, only used by blue stalker
		}
	}
	public byte getItemID() {
		return itemID;
	}

	public void setItemID(byte itemID) {
		this.itemID = itemID;
		this.item=true;
	}

	public boolean isItem() {
		return item;
	}
	// setItem will only be used once an item has been picked, so the player can't pick a boost twice
	// for door closed and open it won't be called
	public void setItem(boolean item){		
		this.item = item; 
	}
	
	public boolean isWalkable() {
		return this.walkable;
	}
	public boolean isEnemyWalkable() {		//for enemies, bombs are not walkable
		if(this.walkable && this.cellID!=3) {return true;}
		else {return false;}
	}
	public byte getCellID(){
		return this.cellID;
	}

	public String getItemImage() {
		if(this.itemID==0) {return "DoorClosed.png";}
		else if(this.itemID==1) {return "Bombupsprite.png";}
		else if(this.itemID==2) {return "Fireupsprite.png";}
		else if(this.itemID==3) {return "Fullfiresprite.png";}
		else if(this.itemID==4) {return "Remote_Control_2.png";}
		else if(this.itemID==5) {return "Skatesprite.png";}
		else if(this.itemID==6) {return "Getasprite.png";}
		else if (this.itemID==7) {return "DoorClosed.png";}		//door open, not necessary (as image is the same) but gives a better understanding
		else {return "-1";}
	}
}
