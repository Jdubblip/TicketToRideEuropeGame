import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

//this is the brain of the game, the key piece of the background UML. 
public class GameState {

	ArrayList<Player> gamePlayers; 
	ArrayList<Card> allTrainCards; //this is the active house hidden deck during game
//	ArrayList<Card> houseDeckHidden;//may not need this one any more. as allTrainCards keep remaining
	public ArrayList<Card> discardTrainCards;  // this is the discard deck for all train cards
	ArrayList<Card> houseDeckFaceUp;// 5 cards, set up when start game  
	//ArrayList<Card> discardDeck;  //did I used it??
	ArrayList<Ticket> allTickets;  //this has all the tickets, only used during initial set up
	ArrayList<Ticket> longTickets;
	ArrayList<Ticket> shortTickets;
	ArrayList<Ticket> ticketsDeck; //house tickets deck at any time of game. discard is added to bottom
	Player currentPlayer;
	int currentPlayerIndex; 
	int lastPlayerIndex; 
	GameMap TTREMap; 
	int numPlayers; 
	boolean isGameOver; //to flag if game is over or not
	int cardsDrawnThisTurn; //to track that 2 cards are to be drawn
	boolean isClaimingStation;
	boolean isClaimingRoute;
	boolean lastRoundTriggered; 
	Set<String> highlightedCityNames;
	int recentlyUpdatedPlayerIndex; //this was not actually used
	StringBuilder results; 
	
	//start a new game
	public GameState() {
		gamePlayers = new ArrayList<Player>(); 
		allTrainCards =  new ArrayList<Card>();  // this is for all the train cards, initialized when start game
//		houseDeckHidden = new ArrayList<Card>();
		discardTrainCards = new ArrayList<Card>();
		houseDeckFaceUp = new ArrayList<Card>();
		//discardDeck = new ArrayList<Card>();
		allTickets = new ArrayList<Ticket>();
		longTickets = new ArrayList<Ticket>();
		shortTickets = new ArrayList<Ticket>();
		ticketsDeck = new ArrayList<Ticket>();
		TTREMap = new GameMap(); 
		TTREMap.initializeGameMap(); //here is to add all cities
		//results = new StringBuilder();
//		System.out.println("city array size in code GameState  " + TTREMap.getAllCities().size());
		currentPlayerIndex = 0; 
		isClaimingStation = false; 
		isClaimingRoute = false; 
		recentlyUpdatedPlayerIndex = -1; 
	//	TTREMap.initializeGameMap(); // add all cities names.
		initializePlayers(); //initialize with 4 players
		initializeAllTrainCards(); // initialize all the 120 train cards
		initializeTickets(); 
		numPlayers = 4; //default is a 4 players game
		isGameOver = false; //always false to start
		lastRoundTriggered = false; 
		cardsDrawnThisTurn = 0;
		highlightedCityNames = new HashSet<>();		
	}
	
	public void initializePlayers() {
		int numTrainsForPlayer = 45; 
		Player player1 = new Player ("Player1", Color.RED, numTrainsForPlayer, 3, 0);
		Player player2 = new Player ("Player2", Color.GREEN, numTrainsForPlayer, 3, 0);
		Player player3 = new Player ("Player3", Color.YELLOW, numTrainsForPlayer, 3, 0);
		Player player4 = new Player ("Player4", Color.BLUE, numTrainsForPlayer, 3, 0);
		gamePlayers.add(player1);
		gamePlayers.add(player2);
		gamePlayers.add(player3);
		gamePlayers.add(player4);
		
	}
	
	
	public void nextPlayersTurn() {
		// check game over or not whenver switching to next player
		checkGameOver();  
		currentPlayer.numRun ++; //increase current player's run counter
		System.out.println ("curent player" + currentPlayer.name + " numRun" + currentPlayer.numRun);
		currentPlayerIndex = (currentPlayerIndex + 1 ) % gamePlayers.size();
		currentPlayer = gamePlayers.get(currentPlayerIndex);
		cardsDrawnThisTurn =0; //reset cardsDrawnThisTurn to zero
		currentPlayer.hasTakenAction = false; // just starting next player
		//disable face up cards button
		
	}

	
	public void checkGameOver() {
	    // Step 1: Trigger the final round if not already triggered
	    if (!lastRoundTriggered) {
	        for (Player player : gamePlayers) {
	            if (player.numTrains <= 2) {
	                lastRoundTriggered = true;	                
	                lastPlayerIndex = currentPlayerIndex; // this player starts the last round
	                break;
	            }
	        }
	    }
	    else {
	    // Step 2: End the game only after all players had one final turn
	    // This happens when the current player is the one RIGHT AFTER lastPlayerIndex
	    		//int nextIndex = (lastPlayerIndex + 1) % gamePlayers.size();
	    		if (currentPlayerIndex == lastPlayerIndex) {
	    			isGameOver = true;
	    			endGame();
	        }
	    
	}
	    
	} // end of checkGameOver method
	

	
	//game is over, update score
	public void endGame() {
    	calcFinalScores();
	    //StringBuilder results = new StringBuilder("Final Scores and Ticket Results:\n\n");
    	results = new StringBuilder(); //reset the results text
    	results.append("Final Scores and Ticket Results\n\n");
	    for (Player player : gamePlayers) {
	        results.append(player.name).append(" — Score: ").append(player.score).append("\n");
	        results.append("Tickets:\n");
	        player.numCompletedTickets=0;
	        for (Ticket ticket : player.playerTickets) {
	            boolean completed = checkTicketCompleted(player, ticket);
	            if (completed) player.numCompletedTickets++; 
	            results.append(" - ")
	                   .append(ticket.getStartCity()).append(" to ").append(ticket.getEndCity())
	                   .append(" (").append(ticket.getPoints()).append(" points): ")
	                   .append(completed ? " Completed" : " Not Completed")
	                   .append("\n");
	        } // end of ticket for loop
	        
	    } //end of player loop
		
	} // end of endGame() method
	

	
	
    public void startTrainCardDrawPhase() {
        cardsDrawnThisTurn = 0;
    }

	// draw one train car card from hidden deck
    public Card drawCardFromHiddenDeck() {
        if (allTrainCards.isEmpty()) {
            System.out.println("No cards left in hidden deck.");
            return null;
        }
        Card drawn = allTrainCards.remove(0);  // draw from top of deck.  0 is top. 
        //System.out.println(currentPlayer.name + " drew a hidden card: " + drawn.getCardImageFilename());
        return drawn; 
    }
	
   // draw one train car card from face up cards.  index is among 0 to 4, which face up card. 
    public Card drawCardFromFaceUp(int index) {
        if (index < 0 || index >= houseDeckFaceUp.size()) return null;

        Card drawn = houseDeckFaceUp.get(index);
        houseDeckFaceUp.set(index, drawReplacementCard()); //replace from hidden deck
        return drawn;
    }//end of Method
    
    
    public Card drawReplacementCard() {
        if (!allTrainCards.isEmpty()) {
            return allTrainCards.remove(0);  //remove top of cards
        }
        return null;
    }
    
    public boolean isDrawComplete() {
        return cardsDrawnThisTurn >= 2;
    }
    
    
    
	//initialize the deck of train cards
	public void initializeAllTrainCards() {
		
		//int numTrainCardsPerColor = 12;
		int numTrainCardsPerColor = 12;
		int numLOCOCards = 14; 
		
		// new Color(255, 140, 0) for Orange
		for (int i = 0; i<numTrainCardsPerColor; i++) {
			Card trainCard = new Card(Color.BLUE);
			allTrainCards.add(trainCard);
		} //end of for loop
		
		for (int i = 0; i<numTrainCardsPerColor; i++) {
			Card trainCard = new Card(Color.RED);
			allTrainCards.add(trainCard);
		} //end of for loop
		
		for (int i = 0; i<numTrainCardsPerColor; i++) {
			Card trainCard = new Card(Color.GREEN);
			allTrainCards.add(trainCard);
		} //end of for loop
		
		for (int i = 0; i<numTrainCardsPerColor; i++) {
			Card trainCard = new Card(Color.YELLOW);
			allTrainCards.add(trainCard);
		} //end of for loop
		
		for (int i = 0; i<numTrainCardsPerColor; i++) {
			Card trainCard = new Card(Color.DARK_GRAY);
			allTrainCards.add(trainCard);
		} //end of for loop
		
		
		for (int i = 0; i<numTrainCardsPerColor; i++) {
			Card trainCard = new Card(Color.WHITE);
			allTrainCards.add(trainCard);
		} //end of for loop
		
		for (int i = 0; i<numTrainCardsPerColor; i++) {
			Card trainCard = new Card(new Color(255, 140, 0));
			allTrainCards.add(trainCard);
		} //end of for loop
		
		for (int i = 0; i<numTrainCardsPerColor; i++) {
			Card trainCard = new Card(Color.MAGENTA);
			allTrainCards.add(trainCard);
		} //end of for loop

		for (int i = 0; i<numLOCOCards; i++) {
			Card trainCard = new Card(true, Color.PINK); 
			allTrainCards.add(trainCard);  //LOCO cards are set to pink in Card class
		} //end of for loop

	
	} //end of initialize cards method
	
	
	public void initializeTickets() {
		allTickets.add(new Ticket("Angora", "Kharkov", 10)); //yes 1  YES
		allTickets.add(new Ticket("Amsterdam", "Pamplona", 7)); //yes2  YES
		allTickets.add(new Ticket("Amsterdam", "Wilno", 12)); //yes3  YES
		allTickets.add(new Ticket("Athina", "Wilno", 11)); //yes4  YES
		allTickets.add(new Ticket("Athina", "Angora", 5)); //yes5  YES
		allTickets.add(new Ticket("Barcelona", "Bruxelles", 8));//yes6  YES
		allTickets.add(new Ticket("Barcelona", "Munchen", 8)); //yes7  YES
		allTickets.add(new Ticket("Berlin", "Bucuresti", 8)); //yes8  YES
		allTickets.add(new Ticket("Berlin", "Roma", 9)); //yes9  YES
		allTickets.add(new Ticket("Berlin", "Moskva", 12)); //yes10  YES
		allTickets.add(new Ticket("Brest", "Petrograd", 20)); //yes11   YES
		allTickets.add(new Ticket("Brest", "Marseille", 7)); //yes12  YES
		allTickets.add(new Ticket("Brest", "Venezia", 8)); //yes13  YES
		allTickets.add(new Ticket("Bruxelles", "Danzig", 9)); //yes14  YES
		allTickets.add(new Ticket("Budapest", "Sofia", 5)); //yes15  YES
		allTickets.add(new Ticket("Cadiz", "Stockholm", 21)); //yes16   YES
		allTickets.add(new Ticket("Edinburgh", "Athina", 21)); //yes17  YES  
		allTickets.add(new Ticket("Edinburgh", "Paris", 7)); //yes17  YES +1
		allTickets.add(new Ticket("Essen", "Kyiv", 10)); //yes18  YES
		allTickets.add(new Ticket("Erzurum", "Rostov", 5)); //yes19  YES
		allTickets.add(new Ticket("Frankfurt", "Smolensk", 13));//yes20  YES
		allTickets.add(new Ticket("Frankfurt", "Kobenhavn", 5));//yes21  YES
		allTickets.add(new Ticket("Kobenhavn", "Erzurum", 21)); //yes22  YES
		allTickets.add(new Ticket("Kyiv", "Sochi", 8));//yes23  YES
		allTickets.add(new Ticket("Kyiv", "Petrograd", 6)); //yes24  YES
		allTickets.add(new Ticket("Lisboa", "Danzig", 20)); //yes25  YES
		allTickets.add(new Ticket("London", "Wien", 10)); //yes26  YES
		allTickets.add(new Ticket("London", "Berlin", 7)); //yes27  YES
		allTickets.add(new Ticket("Madrid", "Dieppe", 8));//yes28  YES
		allTickets.add(new Ticket("Madrid", "Zurich", 8));//yes29  YES
		allTickets.add(new Ticket("Marseille", "Essen", 8));//yes30  YES
		allTickets.add(new Ticket("Palermo", "Moskva", 20)); //yes31  YES
        allTickets.add(new Ticket("Palermo", "Constantinople", 8)); //yes32  YES
        allTickets.add(new Ticket("Paris", "Wien", 8));//yes34  YES  -1
        allTickets.add(new Ticket("Paris", "Zagrab", 7));//yes35  YES
        allTickets.add(new Ticket("Riga", "Bucuresti", 10));  //YES  +1
        allTickets.add(new Ticket("Roma", "Smyrna", 8)); //yes36  YES
        allTickets.add(new Ticket("Sarajevo", "Sevastopol", 8));//yes37  YES
        allTickets.add(new Ticket("Smolensk", "Rostov", 8));//yes38  YES
        allTickets.add(new Ticket("Sofia", "Smyrna", 5));//yes39  YES
        allTickets.add(new Ticket("Stockholm", "Wien", 11)); //yes40  YES
        allTickets.add(new Ticket("Venezia", "Constantinople", 10)); //yes41  YES
        allTickets.add(new Ticket("Zagrab", "Brindisi", 6)); //yes42   YES
        allTickets.add(new Ticket("Zurich", "Budapest", 6)); //yes43  YES
        allTickets.add(new Ticket("Zurich", "Brindisi", 6)); //yes44  YES
        allTickets.add(new Ticket("Warszawa", "Smolensk", 6)); //yes45  YES
        
     
   //     System.out.println(allTickets.size());
        //separate into longTickets and shortTickets
        for (int i=0; i<allTickets.size(); i++) {
        	if (!allTickets.get(i).shortTicket) 
        		longTickets.add(allTickets.get(i));
  //     		System.out.println("long ticket, point = " + allTickets.get(i).getPoints() + "start city  " + allTickets.get(i).startCity + "  end city " + allTickets.get(i).endCity);
        	else shortTickets.add(allTickets.get(i));
        }
    //    System.out.println("shortTickets size: "+shortTickets.size());
    //    System.out.println("longTickets size: "+longTickets.size());
	}
	
	public void startGame() {
		//(1) shuffle the train cards
		Collections.shuffle(allTrainCards);
		//(2) deal 4 cards to each player
		for (int run = 0; run<4; run++) {
    		for (int i = 0; i<numPlayers; i++) {
	      		Card currentCard = allTrainCards.remove(0);
	     		gamePlayers.get(i).addOneCard(currentCard);
	     	}
		}
		//print each player's hand
//		for (int i = 0; i < numPlayers; i++) {
//		    System.out.println("Player " + (i + 1) + "'s train cards: ");
//		    gamePlayers.get(i).printCards();
//		}
		//(3) turn top 5 train cards face up
		for (int i=0; i<5; i++) {
		    if (!allTrainCards.isEmpty()) {
		        houseDeckFaceUp.add(allTrainCards.remove(0)); // remove top card from deck, add to face-up
		    }
		}
//		for (int i =0; i<houseDeckFaceUp.size(); i++) {
//			System.out.println ("face up deck " + i + getCardColorName(houseDeckFaceUp.get(i).cardColor));
//		}
		
		//(4) shuffle the 6 long tickets
		Collections.shuffle(longTickets);
		//(5) deal 1 long ticket to each player
		for (int i = 0; i<numPlayers; i++) {
			Ticket currentLongTicket = longTickets.remove(0);
	     	gamePlayers.get(i).drawTicket(currentLongTicket);
//	     	System.out.println ("Player " + (i + 1) + "ticket  start city end city" + currentLongTicket.startCity + currentLongTicket.endCity);
	    }
//		System.out.println("size of longTickets: " + longTickets.size());
		
		//(6) shuffle the 40 short tickets
		Collections.shuffle(shortTickets);
		//(7) deal 3 short ticket to each player
		for (int run = 0; run<3; run++) {
			for (int i = 0; i<numPlayers; i++) {
				Ticket currentShortTicket = shortTickets.remove(0);
				gamePlayers.get(i).drawTicket(currentShortTicket);
	//			System.out.println ("Player " + (i + 1) + "ticket  start city end city" + currentShortTicket.startCity + currentShortTicket.endCity);
	    	}
		}
//		System.out.println("size of shortTickets: " + shortTickets.size());
		
		//(8) combine remaining longTickets and shortTickets into ticketsDeck and shuffle (the remaining set that used for rest of the game)
		for (int i=0; i< shortTickets.size(); i++) {
			ticketsDeck.add(shortTickets.get(i));
		}
		//for (int i=0; i<longTickets.size(); i++) {
		//	ticketsDeck.add(longTickets.get(i));
		//}
		Collections.shuffle(ticketsDeck);
	
//		System.out.println("size of ticketsDeck: " + ticketsDeck.size());
		/*print each player's tickets
		for (int i = 0; i< numPlayers; i++) {
			System.out.println("Player " + (i + 1));
			gamePlayers.get(i).printTicket();
		}
		*/
		
		//(9) set current Player at 1
		currentPlayerIndex =0;
		currentPlayer = gamePlayers.get(currentPlayerIndex);
		
		//this is the game loop
	//    while (!gameOver) {
	        //doTurn(); // or some game logic
	        // After game state changes:
	//        gameUI.repaint(); // trigger GUI update
	 //   }
		
	} // end of startGame
	
	
    public static String getCardColorName(Color cardColor) {
        if (Color.RED.equals(cardColor)) return "RED";
        if (Color.BLUE.equals(cardColor)) return "BLUE";
        if (Color.YELLOW.equals(cardColor)) return "YELLOW";
        if (Color.GREEN.equals(cardColor)) return "GREEN";
        if (Color.DARK_GRAY.equals(cardColor)) return "DARK_GRAY";
        if (Color.WHITE.equals(cardColor)) return "WHITE";
        if (cardColor.equals(new Color(255, 140, 0))) return "ORANGE";
        if (Color.MAGENTA.equals(cardColor)) return "MAGENTA";
        if (Color.PINK.equals(cardColor)) return "LOCO";
        if (Color.LIGHT_GRAY.equals(cardColor)) return "LIGHT_GRAY";
        return "Unknown";
      }
    
   //draw tickets from deck and put to player's playerDrawnTickets arrayList 
   public ArrayList<Ticket> drawTickets(int num) {
 //       ArrayList<Ticket> drawn = new ArrayList<>();
        currentPlayer.playerDrawnTickets.clear(); //clear the playerDrawnTickets set
        for (int i = 0; i < num && !ticketsDeck.isEmpty(); i++) {
        	Ticket tempTicket = ticketsDeck.remove(0);
//            drawn.add(tempTicket); // draw from top of deck
            currentPlayer.drawTicket(tempTicket); //add to curren player's drawnTickets array
        }
        return currentPlayer.playerDrawnTickets; // show the drawn tickets to player to choose
   }
   
 
   public void keepSelectedTickets(ArrayList<Ticket> chosenTickets, ArrayList<Ticket> discardTickets) {
	    if (chosenTickets.size() < 1) {
	        throw new IllegalArgumentException("Must keep at least one destination ticket");
	    }
	    //add chosen tickets to player's ticket collection
	    for (Ticket ticket : chosenTickets) {
	        currentPlayer.addTicket(ticket);
	    }

	    currentPlayer.playerDrawnTickets.clear();  // empty drawnTickets list
	    
	    //Return unchosen tickets to bottom of the deck
	    //per teacher's score matrix, "all discarded destination tickets are removed from the game". 
	    // therefore the 3 line codes below are commented out
	    //for (Ticket ticket : discardTickets) {
	    //	ticketsDeck.add(ticket); // optionally shuffle bottom later
	     //   }
	    
	} //end of keepSelectedTickets method
   
   
   //the city which is passed into this code, been checked and is good to be claimed
   //make sure all checks are done before coming here
   public void claimStationAtCity(City city) {
  	    city.setHasStation(); // change the hasStationOwner in city to be true
  	    city.setStationOwner(currentPlayer);  // set ownwer
  	    //player station number deduct
  	    currentPlayer.numStations--;
  	    currentPlayer.ownedStation.add(city);
  	    isClaimingStation = false; // disable claim mode
  	    // Optionally log
  	    System.out.println("Station claimed at " + city.getCityName() + " by " + currentPlayer.name);
  	}
   
   
   public ArrayList<Player> getPlayersWithLongestRoute() {
	    ArrayList<Player> longestPlayers = new ArrayList<>();
	    int longest = 0;

	    for (Player p : gamePlayers) {
	        if (p.longestRoute > longest) {
	            longest = p.longestRoute;
	            longestPlayers.clear();
	            longestPlayers.add(p);
	        } else if (p.longestRoute == longest) {
	            longestPlayers.add(p);
	        }
	    }

	    for (Player p: longestPlayers) p.hasExpressBonus = true;  //those are express bonus winner
	    
	    return longestPlayers;
	}
   
   /*return the player with the longest route
   public Player getPlayerWithLongestRoute() {
	    if (gamePlayers == null || gamePlayers.isEmpty()) return null;

	    Player longestPlayer = gamePlayers.get(0);
	    for (Player p : gamePlayers) {
	        if (p.longestRoute > longestPlayer.longestRoute) {
	            longestPlayer = p;
	        }
	    }
	    return longestPlayer;
	}
   */
   
   //calc each player's points from completed tickets.  didnt' use this one. used the calcFinalScore() below
   public void calcTicketScores() {
	   
	    for (Player player : gamePlayers) {
	    	//step 1: + or - score from tickets
	        int ticketPoints = 0;
	        for (Ticket ticket : player.playerTickets) {
	            boolean completed = checkTicketCompleted(player, ticket);  //check if player has completed the ticket or not
	            if (completed) {
	                ticketPoints += ticket.getPoints(); // add ticket value
	            } else {
	                ticketPoints -= ticket.getPoints(); // subtract ticket value if failed
	            }
	        }  //end for loop for all tickets of the player
	        player.updateScore(ticketPoints);  
	        //System.out.println(player.name + " ticket points: " + ticketPoints);
	        // Optional: log final score
	        //System.out.println(player.name + " final score: " + player.score);
	        //step 2: score from number of stations
	        int scoreFromStation = player.numStations * 4; 
	        player.updateScore(scoreFromStation);
	        
	    }  // end for loop for all players

	    //step 3: award longest route bonus
	    //Player longestRoutePlayer = getPlayerWithLongestRoute(); 
	    ArrayList <Player> longestRoutePlayers = getPlayersWithLongestRoute();
	    if (longestRoutePlayers != null) {
	    	for (Player p : longestRoutePlayers) {
	    	    p.score += 10; // or whatever the bonus is
	    	}
	        //longestRoutePlayer.updateScore(10); // Europe bonus 10ppt for longest continuous route
	        //System.out.println(longestRoutePlayer.name + " received +10 for longest route.");
	    } 
	    
	    
	} //end of method
   
   
   //to calc each player's score
   public void calcFinalScores() {
	   
	    for (Player player : gamePlayers) {
	    	//step 1: + or - score from tickets
	        int ticketPoints = 0;
	        for (Ticket ticket : player.playerTickets) {
	            boolean completed = checkTicketCompleted(player, ticket);  //check if player has completed the ticket or not
	            if (completed) {
	                ticketPoints += ticket.getPoints(); // add ticket value
	            } else {
	                ticketPoints -= ticket.getPoints(); // subtract ticket value if failed
	            }
	        }  //end for loop for all tickets of the player
	        player.updateScore(ticketPoints);  
	        System.out.println(player.name + " ticket points: " + ticketPoints);
	        // Optional: log final score
	        System.out.println(player.name + " final score: " + player.score);
	        //step 2: score from number of stations
	        int scoreFromStation = player.numStations * 4; 
	        player.updateScore(scoreFromStation);
	        
	    }  // end for loop for all players

	    //step 3: award longest route bonus.  there could be multpile players with same longest route
	    ArrayList <Player> longestRoutePlayers = getPlayersWithLongestRoute(); 
	    //Player longestRoutePlayer = getPlayerWithLongestRoute(); 
	    if (longestRoutePlayers != null) {
	    	// give 10 points to all players with the same longest route (handling tie of longest route)
	    	for (Player p : longestRoutePlayers) {
	    	    p.score += 10; // or whatever the bonus is
	    	}
	        //longestRoutePlayer.updateScore(10); // Europe bonus 10ppt for longest continuous route
	        //System.out.println(longestRoutePlayer.name + " received +10 for longest route.");
	    } 
	    
	    
	} //end of calcFinalScore method
   
   
   public boolean checkTicketCompleted(Player player, Ticket ticket) {
	    String startCity = ticket.getStartCity();
	    String endCity = ticket.getEndCity();

	    City start = TTREMap.findCityByName(startCity);
	    City destination = TTREMap.findCityByName(endCity);


	    if (start == null || destination == null) {
	        System.err.println("Invalid city in ticket: " + startCity + " or " + endCity);
	        return false;
	    }

	    if (start.equals(destination)) return true;
	    if (player.ownedRoutes.isEmpty()) return false;

	    // Build player's own graph
	    Map<City, Set<City>> playerGraph = new HashMap<>();
	    for (EdgeRoute route : player.ownedRoutes) {
	        City a = route.getStart();
	        City b = route.getDestination();
	        playerGraph.computeIfAbsent(a, k -> new HashSet<>()).add(b);
	        playerGraph.computeIfAbsent(b, k -> new HashSet<>()).add(a);
	    }

	    // BFS setup
	    Set<City> visited = new HashSet<>();
	    Queue<City> queue = new LinkedList<>();
	    queue.add(start);
	    visited.add(start);

	    while (!queue.isEmpty()) {
	        City current = queue.poll();
	        if (current.equals(destination)) {
	            return true;
	        }

	        
	        // Traverse player's owned routes
	        Set<City> neighbors = playerGraph.get(current);
	        if (neighbors != null) {
	            for (City neighbor : neighbors) {
	                if (visited.add(neighbor)) {
	                    queue.add(neighbor);
	                }
	            }
	        }

	        // Traverse opponent routes ONLY IF the other end has a station owned by this player
	        for (EdgeRoute route : TTREMap.TTRERoutes) {
	            if (route.isOwned() && !player.ownedRoutes.contains(route)) {
	                City a = route.getStart();
	                City b = route.getDestination();

	                // If current city is connected to a city where player has a station, simulate route
	                if (a.equals(current) && player.hasStationAt(b)) {
	                    if (visited.add(b)) queue.add(b);
	                } else if (b.equals(current) && player.hasStationAt(a)) {
	                    if (visited.add(a)) queue.add(a);
	                }
	            }
	        }
	    }  //end of while loop
 
	        
	        /* checking the player's routes
	        for (City neighbor : playerGraph.getOrDefault(current, new HashSet<>())) {
	            if (visited.add(neighbor)) {
	                queue.add(neighbor);
	            }
	        }
	        */


	        /* If player has a station at this city, also consider one borrowed opponent route into this city
	        if (player.hasStationAt(current)) {
	            for (EdgeRoute route : TTREMap.TTRERoutes) {
	                if (route.isOwned() && !player.ownedRoutes.contains(route)) {
	                    if (route.getStart().equals(current)) {  //this is to add neighbors from borrowed route
	                        City neighbor = route.getDestination();
	                        if (visited.add(neighbor)) {
	                            queue.add(neighbor);
	                        }
	                    } else if (route.getDestination().equals(current)) {
	                        City neighbor = route.getStart();
	                        if (visited.add(neighbor)) {
	                            queue.add(neighbor);
	                        }
	                    }
	                }
	            }
	        }  */ //end of if player hasStationAt
	        
	        
	  

	    return false; // No path found
	} 
   
   // old version without considering station claimed for ticket completion
   public boolean checkTicketCompletedNoStation(Player player, Ticket ticket) {
	    String startCity = ticket.getStartCity();
	    String endCity = ticket.getEndCity();
	    
	    City start = TTREMap.findCityByName(startCity);
	    City destination = TTREMap.findCityByName(endCity);
	    
	    if (player.ownedRoutes.isEmpty()) return false;

	    //Null safety check
	    if (start == null || destination == null) {
	        System.err.println("Invalid city in ticket: " + startCity + " or " + endCity);
	        return false;
	    }	    
	    
	    // Build player's personal adjacency list from their owned routes
	    Map<City, ArrayList<City>> playerGraph = new HashMap<>();
	    for (EdgeRoute route : player.ownedRoutes) {
	        City a = route.getStart();
	        City b = route.getDestination();
	        playerGraph.computeIfAbsent(a, k -> new ArrayList<>()).add(b);
	        playerGraph.computeIfAbsent(b, k -> new ArrayList<>()).add(a);
	    }

	    // Run BFS from ticket.start to find ticket.destination
	    Set<City> visited = new HashSet<>();
	    Queue<City> queue = new LinkedList<>();
	    queue.add(start);
	    visited.add(start);

	    while (!queue.isEmpty()) {
	        City current = queue.poll();
	        if (current.equals(destination)) {
	            return true;
	        }

	        ArrayList<City> neighbors = playerGraph.getOrDefault(current, new ArrayList<>());
	        for (City neighbor : neighbors) {
	            if (!visited.contains(neighbor)) {
	                visited.add(neighbor);
	                queue.add(neighbor);
	            }
	        }
	    }

	    return false; // No path found
	}  //end of checkTicketCompletedNoStation method
   
   
   public void highlightCities(String city1, String city2) {
	    highlightedCityNames.clear(); // optional: only allow one pair highlighted at a time
	    highlightedCityNames.add(city1);
	    highlightedCityNames.add(city2);
//	    System.out.println ("in highlightCities method in GameState");
//	    System.out.println ("city 1 " + city1 + " city 2 " + city2);
	}

	public void clearHighlightedCities() {
	    highlightedCityNames.clear();
//	    System.out.println ("in clearHighlightCities method in GameState");
	}

	public Set<String> getHighlightedCities() {
	    return highlightedCityNames;
	}
  
	public boolean hasThreeOrMoreLocosFaceUp() {
	    int locoCount = 0;

	    for (Card card : houseDeckFaceUp) {
	        if (card.isLOC()) {
	            locoCount++;
	        }
	    }
	    if (locoCount >= 3) {
	        System.out.println("3+ locos detected — refreshing face-up cards.");
	    }
	    return locoCount >= 3;
	}
	
	public void refreshFaceUpCards() {
	    // Step 0: Don't refresh if hidden deck is empty
	    if (allTrainCards.isEmpty()) {
	        System.out.println("Cannot refresh face-up cards — hidden deck is empty.");
	        return;
	    }

		
	    // Step 1: Return current face-up cards to the bottom of the draw pile
	    discardTrainCards.addAll(houseDeckFaceUp);

	    // Step 2: Clear current face-up cards
	    houseDeckFaceUp.clear();

	    // Step 3: Draw 5 new cards
	    for (int i = 0; i < 5 && !allTrainCards.isEmpty(); i++) {
	        houseDeckFaceUp.add(allTrainCards.remove(0));
	    }

	    // Step 4: Check again for 3 or more locomotives — repeat if needed
	    int locoCount = 0;
	    for (Card c : houseDeckFaceUp) {
	        if (c != null && c.isLOC()) locoCount++;
	    }
	    
	    // Step 5: Repeat refresh if 3+ locos — but only if hidden deck still has cards
	    if (locoCount >= 3 && !allTrainCards.isEmpty()) {
	        System.out.println("3+ locos detected again — refreshing face-up cards.");
	        refreshFaceUpCards();  // recursive call is safe now
	    }

	   // if (locoCount >= 3) {
	  //      System.out.println("3+ locos detected again — refreshing face-up cards.");
	   //     refreshFaceUpCards(); // repeat the process
	   // }
	} // end of method
	
	
	public void refillDeckIfNeeded() {
	    if (allTrainCards.size() < 2 && !discardTrainCards.isEmpty()) {
	        Collections.shuffle(discardTrainCards);
	        allTrainCards.addAll(discardTrainCards);
	        discardTrainCards.clear();
	    }
	} // end of refillDeckIfNeeded method
	
	
	//to return the longest route for all players
	public Map<Player, Integer> getLongestRouteLengths() {
	    Map<Player, Integer> longestRoutes = new HashMap<>();

	    for (Player player : gamePlayers) {
	        // Step 1: Build the graph of the player's claimed routes
	        Map<City, ArrayList<EdgeRoute>> graph = buildPlayerGraph(player.ownedRoutes);

	        // Step 2: Try DFS from every city
	        int playerMaxLength = 0;
	        for (City city : graph.keySet()) {
	            Set<EdgeRoute> visitedEdges = new HashSet<>();
	            int lengthFromCity = dfsLongestPath(city, graph, visitedEdges);
	            playerMaxLength = Math.max(playerMaxLength, lengthFromCity);
	        }

	        longestRoutes.put(player, playerMaxLength);
	    }

	    return longestRoutes;
	}

	// Build the adjacency list for a player's claimed routes
	private Map<City, ArrayList<EdgeRoute>> buildPlayerGraph(ArrayList<EdgeRoute> routes) {
	    Map<City, ArrayList<EdgeRoute>> graph = new HashMap<>();

	    for (EdgeRoute route : routes) {
	        graph.computeIfAbsent(route.getStart(), k -> new ArrayList<>()).add(route);
	        graph.computeIfAbsent(route.getDestination(), k -> new ArrayList<>()).add(route);
	    }

	    return graph;
	}

	// Recursive DFS to calculate the longest path from a city
	private int dfsLongestPath(City current, Map<City, ArrayList<EdgeRoute>> graph, Set<EdgeRoute> visitedEdges) {
	    int max = 0;

	    for (EdgeRoute edge : graph.getOrDefault(current, new ArrayList<>())) {
	        if (!visitedEdges.contains(edge)) {
	            visitedEdges.add(edge);

	            City nextCity = edge.getOtherCity(current);
	            int pathLength = edge.getLength() + dfsLongestPath(nextCity, graph, visitedEdges);

	            max = Math.max(max, pathLength);

	            visitedEdges.remove(edge); // backtrack
	        }
	    }

	    return max;
	}
	
	public void checkTicketFileName() {
	
	
        for (int i=0; i<allTickets.size();i++) {
        	Ticket ticket = allTickets.get(i);
            String filename = ticket.getImageFilename();

            // Check if the file exists
            //File file = new File("." + filename); // Assuming you are running from the project root
            if (getClass().getResource(filename) != null) {
                System.out.println("Found: " + filename);
            } else {
                System.out.println("MISSING: " + filename);
            }
        }
	}
	
	
   
} //end of whole GameState class

/* remaining work:

 *  * (6) clean up game log and instruction box
 *  * (9) checking completed routes when there is a station? 
 *  * 
 * (12) when play scores point, show animation of adding points
 *(7) let players select their own names, include player avatar
 * (3) longest router consider station built
 * (4) make sure board image always works, scales okay?
 
 * 5/8/25 modifications:
 * first round player only select from the 4 tickets, no other move
 * hidden ticket pile disabled, to avoid drawing tickets before click "draw ticket" button
 * city and routes updates to match the background map better
 * when click "next player", hand is updated first, before showing the new player's 4 drawn tickets
 *  
 * 
 * 
 */
