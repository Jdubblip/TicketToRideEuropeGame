import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Player {
    String name;  
    Color playerColor; 
    int numTrains; //number of plastic trains left, start from 45
    int numStations; //number of plastic stations, start from 3
    int score;
    ArrayList<Card> playerDeck; 
    ArrayList<Ticket> playerTickets, playerDrawnTickets; //playerDrawnTickets
    ArrayList<EdgeRoute> ownedRoutes; 
    ArrayList<City> ownedStation; 
    int currentMove; //1/2/3/4 about player move type.  0 as inactive
    boolean hasTakenAction; //if player has made a move during his turn
    int numRun; //to track player's number of run, to allow tickets selection
    int longestRoute;
    int numCompletedTickets;
    boolean hasExpressBonus; 
  //  int pointsFromTicket; 
    
    
    //initialize a player with name
    public Player(String playerName, Color passthruColor, int numberTrains, int numberStations, int playerScore) {
        this.name = playerName;
        playerColor = passthruColor; 
        this.numTrains = numberTrains; 
        this.numStations = numberStations; 
        this.score = playerScore;
        this.playerDeck = new ArrayList<>();
        this.playerTickets = new ArrayList<>(); 
        this.playerDrawnTickets = new ArrayList<>();  // this is to hold the tickets drawn 
        this.ownedRoutes = new ArrayList<>();
        this.ownedStation = new ArrayList<>();
        this.currentMove = 0; //set to 0 as inative, not player's turn. 
        hasTakenAction = false; 
        numRun = 0; 
        longestRoute = 0; 
        numCompletedTickets = 0; 
        hasExpressBonus = false;
        
    }
    
    //return true if player has station at the city
    public boolean hasStationAt(City city) {
        return ownedStation.contains(city);
    }

    //add new points to player's score and return the updated score
    public int updateScore(int newPoints) {
    	this.score = this.score + newPoints; 
        return this.score; 

    }
    
    public int getScore () {
    	return this.score; 
    }
    
    //add one new train car card to player's hand
    public void addOneCard(Card newCard) {
    	this.playerDeck.add(newCard); //add to end of existing list
    }
    
    //add two new train car cards to player's hand
    public void addTwoCards(Card newCard1, Card newCard2) {
    	this.playerDeck.add(newCard1);
    	this.playerDeck.add(newCard2);
     }
    
    //add ticket to player's hand
    public void addTicket (Ticket newTicket) {
    	this.playerTickets.add(newTicket); 
    }
    
    //draw ticket, pending player to select
    public void drawTicket (Ticket newTicket) {
    	this.playerDrawnTickets.add(newTicket); 
    }
    
    
    //called by GameState to print player cards color
    public void printCards() {
    	for (int i=0; i<playerDeck.size(); i++) {
    		System.out.println(getColorName(playerDeck.get(i).cardColor));   
    	}
    }
    
    public void printTicket() {
    	for (int i=0; i<playerTickets.size(); i++) {
    		System.out.println("ticket  start city end city" + playerTickets.get(i).startCity + playerTickets.get(i).endCity);
    	}
    }
    

    public static String getColorName(Color color) {
        if (Color.RED.equals(color)) return "RED";
        if (Color.BLUE.equals(color)) return "BLUE";
        if (Color.YELLOW.equals(color)) return "YELLOW";
        if (Color.GREEN.equals(color)) return "GREEN";
        if (Color.DARK_GRAY.equals(color)) return "DARK_GRAY";
        if (Color.WHITE.equals(color)) return "WHITE";
        if (Color.ORANGE.equals(color)) return "ORANGE";  // not the orange used in program
        if (Color.MAGENTA.equals(color)) return "MAGENTA";
        if (Color.LIGHT_GRAY.equals(color)) return "LIGHT_GRAY";
        return "Unknown";
    }
    
    //need to handle LIGHT_GRAY routes which can be claimed by any color
    //count sum(LOCO+specific color) if that's enough
    public boolean hasEnoughCards (Color routeColor, int routeLength) {
  
        int colorCount = 0;
        int locoCount = 0;

        for (Card card : playerDeck) {
            if (card.LOCO) {
                locoCount++;
            } else if (card.cardColor.equals(routeColor)) {
                colorCount++;
            }
        }
        return (colorCount + locoCount) >= routeLength;
    	
    } //end of hasEnoughCards method
    
    
    //return the removed cards so they can be put to discard pile
    public ArrayList<Card> deductCards(Color routeColor, int cost) {
        int needed = cost;
        ArrayList<Card> cardsToRemove = new ArrayList<>();

        // First, use as many matching color cards as possible
        for (Card card : playerDeck) {
            if (needed == 0) break;
            if (!card.LOCO && card.cardColor.equals(routeColor)) {
                cardsToRemove.add(card);
                needed--;
            }
        }

        // Then use locomotives to fill the rest
        for (Card card : playerDeck) {
            if (needed == 0) break;
            if (card.LOCO) {
                cardsToRemove.add(card);
                needed--;
            }
        }

        // Remove the selected cards from the player's hand, add to discard pile
        //TTREGameState.discardTrainCards.addAll(cardsToRemove);
        playerDeck.removeAll(cardsToRemove);
        return cardsToRemove; 
        
    }
    
    public Map<Color, Integer> getCardColorCounts() {
        Map<Color, Integer> colorCounts = new HashMap<>();

        for (Card card : playerDeck) {
            Color color = card.getColor(); // assumes your Card class has getCardColor()
            colorCounts.put(color, colorCounts.getOrDefault(color, 0) + 1); //I used PINK for LOCO
        }

        return colorCounts;
    }
    
} // end of Player class

