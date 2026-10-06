import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class EdgeRoute {
	City destination; 
	City start; 
	Color routeColor; //original color in game map
	int length; //how many cars are needed
	boolean isTunnel;
	boolean isFerry; 
	boolean hasDoubleRoute;
	int routeIndex; //to handle double routes, -1 as left, 1 as right.  should be zero if no double route
	Player ownedBy;
	boolean isCurved;
	Point curveControlPoint; 
	int numberOfLOC;
	
	//constructor for straight route
	public EdgeRoute(City destCity, City startCity, Color col, int lngth, boolean isT, boolean isF, int rtIndex, int numLOC) {
		destination = destCity;
		start = startCity;
		routeColor = col; 
		length = lngth;  //this is to tell how many train cars
		isTunnel = isT;
		isFerry = isF;

		routeIndex = rtIndex; 
		ownedBy = null; 
		isCurved = false;	
		numberOfLOC = numLOC; //number of locomotive
		hasDoubleRoute = (rtIndex != 0);
	}
	
	//constructor for curved route
	public EdgeRoute(City destCity, City startCity, Color col, int lngth, boolean isT, boolean isF, int rtIndex, Point ctrlPt, int numLOC) {
		destination = destCity;
		start = startCity;
		routeColor = col; 
		length = lngth;  //this is to tell how many train cars
		isTunnel = isT;
		isFerry = isF;
	
		ownedBy = null; 
		routeIndex = rtIndex;
		isCurved = true; 
		curveControlPoint = ctrlPt; 
		numberOfLOC = numLOC; //number of locomotive
		hasDoubleRoute = (rtIndex != 0);
				
	}
	
	public boolean setOwner(Player playerName) {
		if (this.ownedBy == null) { // if the route has no owner
			this.ownedBy = playerName; 
			return true; 
		}
		else {
			System.out.println ("the route is already owned by others" + this.ownedBy.name);
			return false; 				
		}
	}
	
	public boolean isOwned() {
	    return ownedBy != null;
	}
	
	public City getDestination() {
		return destination; 
	}

	
	public City getStart() {
		return start; 
	}
	
	public int getLength() {
		return length;
	}
	
	public Color getColor() {
		return routeColor; 
	}
	
	public String getRouteName() {
		return start.getCityName() + " to " + destination.getCityName(); 
	}
	
	//return number of LOCO cards along the route
	public int getNumRequiredLocomotives() {
		return numberOfLOC;
	}
	
	public String getRouteNameWithColor() {
	    String start = getStart().name;
	    String end = getDestination().name;
	    int length = getLength();
	    String colorName = GameState.getCardColorName(this.routeColor);
	    if (colorName=="DARK_GRAY") colorName = "BLACK"; //I have been using dark gray for GUI, but for output here, I want to stay with black
	    if (colorName=="LIGHT_GRAY") colorName = "GRAY"; //I have been using dark gray for GUI, but for output here, I want to stay with black
	    

	    return String.format("%s to %s — %d cars (%s)", start, end, length, colorName);

		
	}
	
	//this is to return the other city at the route
	public City getOtherCity(City city) {
	    return city.equals(start) ? destination : start;
	}


}
