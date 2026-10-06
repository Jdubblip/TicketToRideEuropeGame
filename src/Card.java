import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Card {
	Color cardColor; 
	boolean LOCO; //locomotive or not

	//constructor for regular colored cards, not LOCO
	public Card(Color passthruColor) { 
		cardColor = passthruColor;
		LOCO = false; 
	}

	//construtor for LOCO cards
	public Card(boolean passthruLOCO, Color cardClr) {
		LOCO = passthruLOCO;
		cardColor = cardClr; //use PINK to show loco cards
	}
	
	public Color getColor( ) {
		return cardColor; 
	}

	public boolean isLOC() {
		return LOCO; 
	}
	
    @Override
    public String toString() {
        return cardColor.toString();
    }
    
    public String getCardImageFilename() {
        // Optional: sanitize for consistent image names
    	
        if (Color.RED.equals(cardColor)) return "/images/RED.png";
        if (Color.BLUE.equals(cardColor)) return "/images/BLUE.png";
        if (Color.YELLOW.equals(cardColor)) return "/images/YELLOW.png";
        if (Color.GREEN.equals(cardColor)) return "/images/GREEN.png";
        if (Color.DARK_GRAY.equals(cardColor)) return "/images/BLACK.png";
        if (Color.WHITE.equals(cardColor)) return "/images/WHITE.png";
        if (cardColor.equals(new Color(255, 140, 0))) return "/images/ORANGE.png";
        if (Color.MAGENTA.equals(cardColor)) return "/images/MAGENTA.png";
        //if (Color.LIGHT_GRAY.equals(cardColor)) return "/images/LIGHT_GRAY.jpg";
        if (Color.PINK.equals(cardColor)) return "/images/LOCO.png";
        return "/images/UNKNOWN.jpg"; // You can replace with null or a default card back
        
    }
    
}
