import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Ticket {
	int points; 
	boolean shortTicket; //true if it's short
	String startCity; 
	String endCity; 
	
	//constructor for regular colored cards, not LOCO
	public Ticket(String startCityName, String endCityName, int score) { 
		startCity = startCityName; 
		endCity = endCityName; 
		points = score; 
		shortTicket = points < 20;  //short ticket if points < 20
			
	}
	
	public int getPoints() {
		return points;
	}
	
	public boolean isShort() {
		return shortTicket; 
	}

	public String getStartCity() {
		return startCity; 
	}
	
	public String getEndCity() {
		return endCity; 
	}
	
    public String getImageFilename() {
        // Optional: sanitize for consistent image names
        return "/images/" + startCity.toUpperCase() + "-" + endCity.toUpperCase() + ".png";
    }
	
}
