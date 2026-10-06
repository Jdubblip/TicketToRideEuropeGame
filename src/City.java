import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class City {
	String name; 
	boolean hasStationOwner; 
	Player stationOwner; 
	int cityX; //to put in x, y coordinates of each city
	int cityY;
	
	public City(String cityName, int x, int y) {
		this.name = cityName;
		this.hasStationOwner = false;  //no station owner to begin with
		this.stationOwner = null; 
		this.cityX = x;
		this.cityY = y;
		//this.cityLocation = new Point (x, y); 
	}
	
	public String getCityName() {
		return this.name; 
	}

	public boolean isOwned() {
		return this.hasStationOwner; 
	}
	
	public Player ownedBy() {
		return this.stationOwner; 
	}
	
	public int getX() {
		return this.cityX; 
	}
	
	public int getY() {
		return this.cityY; 
	}
	
	public void setHasStation() {
		hasStationOwner = true;
	}
	
	public void setStationOwner(Player newOwner) {
		stationOwner = newOwner;
	}
	

}
