import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.event.*;

class GameUI extends JFrame {
	
 //   List<Player> players;
 //   List<Route> routes;
 //   int turn;
    JLabel turnLabel;
    JLabel trainCounterLabel, stationCounterLabel;
    JTextArea logAndPromptMsg, tipsMsg;
    
    JButton drawTrainButton, drawDestButton, claimRouteButton, buildStationButton, backgroundImageButton, nextPlayerButton;
    JButton ticketDeckButton, trainDeckButton, discardTrainDeckButton;
    ArrayList<JButton> faceUpCardButtons; 
    
    GameBoard gameBoard;
    JTable playerStatusBoard; 
    DefaultTableModel statusModel;
    GameState TTREGameState; 
    JPanel handPanel; // field as we need access/change as the game goes
    JPanel drawnTicketsPanel;
    JPanel houseDeckPanel;
    JPanel topPanel, trainStationPanel;
    JPanel bottomPanel;
    JPanel sidePanel;
    JPanel playerStatusPanel;
    boolean bcgImage; //whether to show background gameboard image
    //int currentPlayer;
    boolean currrentTurnIsComplete; //to track if player has completed currrent turn, and disable buttons
//    int recentlyUpdatedPlayerIndex;  // this is to track which player has recent scored points
 // Vintage-inspired colors
    Color parchment = new Color(240, 228, 215);      // #f0e4d7
    Color softCream = new Color(253, 246, 227);      // #fdf6e3
    Color lightTan = new Color(243, 220, 184);       // #f3dcb8
    Color darkText = new Color(44, 62, 80);          // #2c3e50
    Color softYellow = new Color(255, 229, 153);     // #ffe599
    Color mutedGray = new Color(205, 200, 190);      // #cdc8be
    
    
   //this is the full GUI for the TTRE game.  
    public GameUI(GameState passThruGameState) {
    	faceUpCardButtons = new ArrayList<JButton>();
    	TTREGameState = passThruGameState; 
    	bcgImage = true; //default is show background image
 //   	recentlyUpdatedPlayerIndex = -1;  
        //set size, title of Frame
        setTitle("Ticket to Ride: Europe");
        setSize(1800,1100);
        setMinimumSize(new Dimension(1600, 1100));
//        setSize(1500, 1280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 228, 215));
        
        topPanel = new JPanel();
        bottomPanel = new JPanel();
       
        
        //Step1: set up bottom panel display
        drawTrainButton = new JButton("Draw Train Card");
        drawDestButton = new JButton("Draw Destination Ticket");
        claimRouteButton = new JButton("Claim Route");
        buildStationButton = new JButton ("Build Train Station");
        backgroundImageButton = new JButton ("Turn On/Off bcg Image");
        nextPlayerButton = new JButton ("Move to Next Player's Turn");
        

        

        bottomPanelSetUp(); 
        

        
        //Step2: set up left side panel, JTextArea for log and  prompt msg
        logAndPromptMsg = new JTextArea();
        logAndPromptMsg.setEditable(false);
        logAndPromptMsg.setWrapStyleWord(true);
        logAndPromptMsg.setLineWrap(true);
//        logAndPromptMsg.setFont(new Font("Monospaced", Font.PLAIN, 12));
        //Color trayBackground = new Color(102, 81, 60); // Dark wood tone
        //logAndPromptMsg.setBackground(trayBackground);
        logAndPromptMsg.append("Welcome to Ticket to Ride Europe Game!\n");
        logAndPromptMsg.append("Player Name: " + TTREGameState.currentPlayer.name + "\n");
        
        logAndPromptMsg.setBackground(handPanel.getBackground()); // or softCream
        logAndPromptMsg.setFont(new Font("Georgia", Font.PLAIN, 13));
        logAndPromptMsg.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        logAndPromptMsg.setForeground(Color.WHITE);
        
        
 
        sidePanelSetUp();
      
        //Step3: set up top panel display area: 
        tipsMsg = new JTextArea();
        tipsMsg.setBackground(handPanel.getBackground()); // or softCream
        tipsMsg.setForeground(Color.WHITE);
        tipsMsg.setFont(new Font("Georgia", Font.PLAIN, 13));
        //tipsMsg.setForeground(Color.WHITE);
        tipsMsg.setEditable(false);
        tipsMsg.setWrapStyleWord(true);
        tipsMsg.setLineWrap(true);
        tipsMsg.append("Guidance on what to do next:\n");
        tipsMsg.append("(1) Hover mouse on a ticket will highlight the start/end cities on map.\n");
        tipsMsg.append("(2) To draw train cards, click on the hidden deck or face up cards at the page top.\n");
        tipsMsg.append("(3) To claim route, mouse click on the route.\n");
        tipsMsg.append("(4) To build station, mouse click at the city.\n");
        tipsMsg.append("(5) Claimed route, and built station, will be shown with the player's color. \n");
        tipsMsg.append("(6) First round, each player only selects the ticket and move to next player. \n");
        
        ImageIcon ticketIcon = new ImageIcon(getClass().getResource("/images/TICKETBACK.jpg"));
        Image originalTicket = ticketIcon.getImage();
        // Scale it
        Image scaledTicket = originalTicket.getScaledInstance(60, 100, Image.SCALE_SMOOTH);
        ticketDeckButton = new JButton(new ImageIcon(scaledTicket));
//        ticketDeckButton.setToolTipText("Tickets Deck, Click to Draw One");
        ticketDeckButton.setText("Tickets left: " + TTREGameState.ticketsDeck.size());
        ticketDeckButton.setHorizontalTextPosition(SwingConstants.CENTER);
        ticketDeckButton.setVerticalTextPosition(SwingConstants.BOTTOM);  
        ticketDeckButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ticketDeckButton.setBorderPainted(false);
        ticketDeckButton.setContentAreaFilled(false);
        ticketDeckButton.setFocusPainted(false);
        ticketDeckButton.setFont(new Font("Georgia", Font.BOLD, 12));
        ticketDeckButton.setForeground(Color.DARK_GRAY); // or Color.BLACK
        ticketDeckButton.setVerticalTextPosition(SwingConstants.TOP);
        ticketDeckButton.setIconTextGap(0);
        ticketDeckButton.setDisabledIcon(new ImageIcon(scaledTicket));
        
        
        ImageIcon houseTrainCardsIcon = new ImageIcon(getClass().getResource("/images/TRAINBACK.jpg"));
        Image trainBack = houseTrainCardsIcon.getImage();
        // Scale it
        Image scaledTrainBack = trainBack.getScaledInstance(60, 100, Image.SCALE_SMOOTH);
        trainDeckButton = new JButton(new ImageIcon(scaledTrainBack));
        trainDeckButton.setToolTipText("Train Cards Deck, Click to Draw One");
        trainDeckButton.setText("Cards left: " + TTREGameState.allTrainCards.size());
        trainDeckButton.setHorizontalTextPosition(SwingConstants.CENTER);
        trainDeckButton.setVerticalTextPosition(SwingConstants.BOTTOM); 
        trainDeckButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        trainDeckButton.setBorderPainted(false);
        trainDeckButton.setContentAreaFilled(false);
        trainDeckButton.setFocusPainted(false);
        trainDeckButton.setFont(new Font("Georgia", Font.BOLD, 12));
        trainDeckButton.setForeground(Color.DARK_GRAY); // or Color.BLACK
        trainDeckButton.setVerticalTextPosition(SwingConstants.TOP);
        trainDeckButton.setIconTextGap(0);
        trainDeckButton.setDisabledIcon(new ImageIcon(scaledTrainBack));

        //for discard train cards
        discardTrainDeckButton = new JButton(new ImageIcon(scaledTrainBack));
        discardTrainDeckButton.setToolTipText("Discard Cards Deck");
        discardTrainDeckButton.setText("Discard Cards: " + TTREGameState.discardTrainCards.size());
        discardTrainDeckButton.setHorizontalTextPosition(SwingConstants.CENTER);
        discardTrainDeckButton.setVerticalTextPosition(SwingConstants.BOTTOM); 
        discardTrainDeckButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        discardTrainDeckButton.setBorderPainted(false);
        discardTrainDeckButton.setContentAreaFilled(false);
        discardTrainDeckButton.setFocusPainted(false);
        discardTrainDeckButton.setFont(new Font("Georgia", Font.BOLD, 12));
        discardTrainDeckButton.setForeground(Color.DARK_GRAY); // or Color.BLACK
        discardTrainDeckButton.setVerticalTextPosition(SwingConstants.TOP);
        discardTrainDeckButton.setIconTextGap(0);
        discardTrainDeckButton.setDisabledIcon(new ImageIcon(scaledTrainBack));
        
        
        
        houseDeckPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
        houseDeckPanel.setPreferredSize(new Dimension(600, 150));
        
        setupPlayerStatusPanel();
        
        topPanelSetUp();
        
        

        
        
        //enable all buttons at start of game
        enableAllButtons();
    	//disable face up cards buttons
        for (int i = 0; i < faceUpCardButtons.size(); i++) {
        	faceUpCardButtons.get(i).setEnabled(false);
        }
        ticketDeckButton.setEnabled(false);
        trainDeckButton.setEnabled(false);
        nextPlayerButton.setEnabled(false);
        

        
        //Step4: set up main display, draw game board at center of screen
 //       JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        gameBoard = new GameBoard(TTREGameState, bcgImage, this);  //pass in gameUI
        gameBoard.setPreferredSize(new Dimension(1300, 800)); // it was 1400, 900
        gameBoard.setMinimumSize(new Dimension(1300, 800));
        gameBoard.setMaximumSize(new Dimension(1300, 800));
        
        JScrollPane gameBoardsScrollPane = new JScrollPane(gameBoard);
        this.add(gameBoardsScrollPane, BorderLayout.CENTER);
        
        
        //if >=3 LOCO , replaces that face-up card
        if (TTREGameState.hasThreeOrMoreLocosFaceUp()) {
        	setupHouseDeckPanel();
        	//System.out.println ("has 3 loco cards in face up deck. need to refresh");
        	JOptionPane.showMessageDialog(this, "has 3 loco cards in face up deck. need to refresh.");
        	TTREGameState.refreshFaceUpCards();
        	discardTrainDeckButton.setText("Discard Train Cards: " + TTREGameState.discardTrainCards.size());
        	setupHouseDeckPanel();
        	JOptionPane.showMessageDialog(this, "refreshed");
        	//System.out.println ("refreshed");
            //setupHouseDeckPanel(); // update the GUI
        }
       	trainDeckButton.setText("Cards left: " + TTREGameState.allTrainCards.size());
       	
       	
        
       //Step5: if current player has not selected tickets
        SwingUtilities.invokeLater(() -> {
            if (TTREGameState.currentPlayer.playerDrawnTickets != null) {
                selectTickets(2);
                //first round player only select min 2 tickets out of the 4 tickets and do nothing
                disableAllButtons(); 
                nextPlayerButton.setEnabled(true);
            }
            
        });
        
        //this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true); 
        
        
    }  // end of gameUI constructor
    
 
    public void updatePlayerStatusBoard() {
        DefaultTableModel model = (DefaultTableModel) playerStatusBoard.getModel();
        model.setRowCount(0); // clear all rows

        for (Player p : TTREGameState.gamePlayers) {
            model.addRow(new Object[] {
                p.name,
                p.score,
                p.numTrains,
                p.numStations,
                p.longestRoute
            });
        }
        //highlightCurrentPlayerRow(TTREGameState.currentPlayerIndex);
        //playerStatusBoard.getTableHeader().setDefaultRenderer(new MultiLineHeaderRenderer());
        //playerStatusBoard.getColumnModel().getColumn(4).setHeaderValue("Longest\nRoute");
        
        // Make last column wider
        int lastColumnIndex = playerStatusBoard.getColumnCount() - 1;
        if (lastColumnIndex >= 0) {
            playerStatusBoard.getColumnModel().getColumn(lastColumnIndex).setPreferredWidth(150);
        }
        
        playerStatusBoard.repaint(); 
    }
    
    //top JPanel for all house cards and tickets   
    void topPanelSetUp() {
    	/* previous 3 buttons codes
    	topPanel.add(ticketDeckButton);
        topPanel.add(trainDeckButton);
        topPanel.add(discardTrainDeckButton);
        */
    	
    	JPanel deckButtonPanel = new JPanel();
    	deckButtonPanel.setBackground(parchment); // soft parchment
    	deckButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 5)); // spacing between buttons
    	deckButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // inner padding

    	deckButtonPanel.add(ticketDeckButton);
    	deckButtonPanel.add(trainDeckButton);
    	deckButtonPanel.add(discardTrainDeckButton);
    	
    	topPanel.add(deckButtonPanel, BorderLayout.WEST);
    	
        topPanel.setBackground(new Color(102, 77, 51));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        //topPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
        
        setupHouseDeckPanel();
        
    	//disable face up cards buttons so player has to select what to do first, before selecting cards
        for (int i = 0; i < faceUpCardButtons.size(); i++) {
        	faceUpCardButtons.get(i).setEnabled(false);
        }
        
        topPanel.add(houseDeckPanel);  
        updatePlayerStatusBoard();
        topPanel.add(playerStatusPanel);
        
      
        JScrollPane tipsScrollPane = new JScrollPane(tipsMsg);
        tipsScrollPane.setPreferredSize(new Dimension(350, 150));
        //tipsScrollPane.setBorder(BorderFactory.createTitledBorder("Game Instructions:\n"));
        
        //
        Color trayBackground = new Color(102, 81, 60); // Dark wood tone
        Color trayBorder = new Color(80, 60, 40);      // Darker brown edge
        tipsScrollPane.setBackground (trayBackground);
     // Rounded border with padding and shadow effect
        TitledBorder titled = BorderFactory.createTitledBorder(
        	    String.format("Game Instructions:\n")
        	);
    	titled.setTitleColor(Color.WHITE); // optional: make the title visible on dark backgrounds
    	titled.setTitleFont(new Font("Georgia", Font.BOLD, 14)); 
    	tipsScrollPane.setBorder(BorderFactory.createCompoundBorder(
    		    BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(80, 60, 40, 180)),  // shadow
    		    BorderFactory.createCompoundBorder(
    		        titled,
    		        BorderFactory.createEmptyBorder(5, 5, 5, 5)
    		    )
    		));
    	//
        
        
        
        topPanel.add(tipsScrollPane);
        
        
        add(topPanel, BorderLayout.NORTH);
    }
    
    
    void setupPlayerStatusPanel() {
        // Column headers
        String[] columnNames = {"Player", "Score", "Trains", "Stations", "Longest Route"};

        // Create table model and disable editing
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        playerStatusBoard = new JTable(model);
        
        //Table style update
        playerStatusBoard.setDefaultEditor(Object.class, null); // make table read-only
        playerStatusBoard.setRowHeight(25);
        playerStatusBoard.getTableHeader().setReorderingAllowed(false);
        playerStatusBoard.setFont(new Font("Georgia", Font.PLAIN, 12));
        playerStatusBoard.getTableHeader().setFont(new Font("Georgia", Font.BOLD, 12));
        
        
        // Fill in initial player data
        for (Player p : TTREGameState.gamePlayers) {
            model.addRow(new Object[]{
                p.name, p.score, p.numTrains,p.numStations, p.longestRoute
            });
        }
        
        // Make last column wider
        int lastColumnIndex = playerStatusBoard.getColumnCount() - 1;
        if (lastColumnIndex >= 0) {
            playerStatusBoard.getColumnModel().getColumn(lastColumnIndex).setPreferredWidth(150);
        }
        //playerStatusBoard.getColumnModel().getColumn(4).setHeaderValue("Longest\nRoute");
        //playerStatusBoard.getTableHeader().setDefaultRenderer(new MultiLineHeaderRenderer());
        
        DefaultTableCellRenderer playerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (row < TTREGameState.gamePlayers.size()) {
                    Color playerColor = TTREGameState.gamePlayers.get(row).playerColor;

                    // Create a softer, lighter version of the player color
                    Color lighterColor = new Color(
                        (playerColor.getRed() + 255) / 2,
                        (playerColor.getGreen() + 255) / 2,
                        (playerColor.getBlue() + 255) / 2
                    );

                    c.setBackground(lighterColor);

                    // Adjust text color based on brightness
                    int brightness = (lighterColor.getRed() + lighterColor.getGreen() + lighterColor.getBlue()) / 3;
                    c.setForeground(brightness < 130 ? Color.WHITE : Color.BLACK);
                }

                if (row == TTREGameState.currentPlayerIndex) {
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                    c.setBackground(c.getBackground().brighter());
                    ((JComponent) c).setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
                } else {
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                    ((JComponent) c).setBorder(null);
                }

                return c;
            }
        };

        // Apply renderer to all columns
        for (int i = 0; i < playerStatusBoard.getColumnCount(); i++) {
            playerStatusBoard.getColumnModel().getColumn(i).setCellRenderer(playerRenderer);
        }
        
        // Scroll pane for table
        JScrollPane scrollPane = new JScrollPane(playerStatusBoard);
        scrollPane.setPreferredSize(new Dimension(300, 130));

        // Panel to wrap the scroll pane
        playerStatusPanel = new JPanel(new BorderLayout());
        playerStatusPanel.setBackground(parchment);
        playerStatusPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
       // playerStatusPanel.setBorder(BorderFactory.createTitledBorder("Player Status"));
 //       playerStatusPanel.setPreferredSize(new Dimension(500, 120));  
        playerStatusPanel.add(scrollPane, BorderLayout.CENTER);
        playerStatusBoard.repaint();
    }
    
 
    
    
    //bottom JPanel for buttons and current player hands
    void bottomPanelSetUp() {
        
        bottomPanel.setLayout(new BorderLayout()); 
        JPanel buttonPanel = new JPanel(new GridLayout(1, 6, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        Font buttonFont = new Font("Georgia", Font.BOLD, 14);
        for (JButton button : Arrays.asList(drawTrainButton, drawDestButton, claimRouteButton,
                                            buildStationButton, backgroundImageButton, nextPlayerButton)) {
            button.setPreferredSize(new Dimension(150, 30));
            button.setFont(buttonFont);
            button.setFocusPainted(false);
            button.setBackground(mutedGray);
            button.setForeground(darkText); 
            //button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            button.setBorder(BorderFactory.createCompoundBorder(
            	    BorderFactory.createLineBorder(Color.DARK_GRAY),
            	    BorderFactory.createEmptyBorder(6, 12, 6, 12)
            	));
            button.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            buttonPanel.add(button);
        }
       
        //bottomPanel.setPreferredSize(new Dimension(400,250));
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        setupHandPanel(); //initialize hand panel 
        bottomPanel.add(handPanel, BorderLayout.CENTER);  
        //bottomPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN)); // Show border
        add(bottomPanel, BorderLayout.SOUTH);
        
        nextPlayerButton.setEnabled(false);  // not allowing next player before current player does anything
        //listen to next player turn button
        nextPlayerButton.addActionListener(e -> {
        	nextPlayerButtonClicked();
        	updatePlayerStatusBoard();
        	
        });
       
        
        
        drawTrainButton.addActionListener(e -> {
        	drawTrainButtonClicked();
        	updatePlayerStatusBoard();
        });
        
        drawDestButton.addActionListener(e -> {
        	drawTicketButtonClicked();
        	updatePlayerStatusBoard();
        });
        
      
        buildStationButton.addActionListener(e -> {
            claimStationClicked();
            updatePlayerStatusBoard();
        });
        
        claimRouteButton.addActionListener(e -> {
            claimRouteClicked();
            updatePlayerStatusBoard();
        });
        
        // Handle background image on/off click
        backgroundImageButton.addActionListener(e -> {
        	bcgImage = ! bcgImage; //reverse the bcgImage setting
        	gameBoard.setBackgroundVisible(bcgImage);
        	//System.out.println ("bcgImage = " + bcgImage);
        	gameBoard.repaint();
            //JOptionPane.showMessageDialog(frame, "You clicked the button!");
        });
        
    }
    
    //if player clicks the button to draw more train cards
    void drawTrainButtonClicked() {
    	logAndPromptMsg.append("Player " + (TTREGameState.currentPlayerIndex+1) + "selected to draw train cards. \n");
    	tipsMsg.append("Player to draw 2 cards, from face up deck, or hidden deck.\n");
    	
    	//check if there is train cards to be drawn
    	boolean noHiddenCards = TTREGameState.allTrainCards.isEmpty();
    	boolean noFaceUpCards = TTREGameState.houseDeckFaceUp.isEmpty();
    	boolean noDiscard = TTREGameState.discardTrainCards.isEmpty();

    	if (noHiddenCards && noFaceUpCards && noDiscard) {
    	    JOptionPane.showMessageDialog(this, "No train cards left to draw! Select other play options");
    	    return;
    	}
    	
  //      drawTrainButton.setFont(new Font("Arial", Font.BOLD, 16));
    	//disable other buttons, so player can't select other play option once he selects to draw trains
    	
    	drawTrainButton.setEnabled(false);
        drawDestButton.setEnabled(false);
        claimRouteButton.setEnabled(false);
        buildStationButton.setEnabled(false);
        ticketDeckButton.setEnabled(false);
        nextPlayerButton.setEnabled(false);
        
        //check if house deck is running low and add in discard pile if needed
        TTREGameState.refillDeckIfNeeded();
        
        
        //start to draw. this would set counter to zero
        TTREGameState.startTrainCardDrawPhase();
 //       boolean firstCardWasFaceUpLoco = false;
        
        //Remove existing listeners from trainDeckButton (prevents stacking)
        for (ActionListener al : trainDeckButton.getActionListeners()) {
            trainDeckButton.removeActionListener(al);
        }
        
        //add listener to the button for house hidden train deck
        trainDeckButton.addActionListener(e -> {
            if (TTREGameState.cardsDrawnThisTurn < 2) {
                Card drawn = TTREGameState.drawCardFromHiddenDeck();
                if (drawn != null) {
                	//update number of cards
                	trainDeckButton.setText("Cards left: " + TTREGameState.allTrainCards.size());
                    TTREGameState.currentPlayer.playerDeck.add(drawn); //add drawn card to player
                    updateHandPanel();
                    TTREGameState.cardsDrawnThisTurn++;
                    logAndPromptMsg.append("Player drew a hidden card:"+ TTREGameState.cardsDrawnThisTurn + "\n");
                    //logAndPromptMsg.append("Cards drawn this turn: " );
                    if (TTREGameState.isDrawComplete()) endTrainDrawPhase();  //what is endTranDrawPhase
                }
            }
        });
     
        //Ensure face-up buttons are enabled
        for (JButton b : faceUpCardButtons) {
            b.setEnabled(true);
        }

        // Just in case trainDeckButton was disabled earlier
        trainDeckButton.setEnabled(true);

        updatePlayerStatusBoard();
    	
    }//end of drawTrainButtonClicked method
    

    
    
    private void endTrainDrawPhase() {
//        drawTrainButton.setEnabled(false); //not allowing to draw, should be false already
//        for (JButton b : faceUpCardButtons) b.setEnabled(false); //disable the 5 face up card buttons
        //show confirmation pop up message box
        JOptionPane.showMessageDialog(this,
        	    "You drew 2 train cards or 1 LOCO in first run. Move to next player. ", // message
        	    "Draw Train Card(s) Completed",                // title
                JOptionPane.INFORMATION_MESSAGE);
        nextPlayerButton.setEnabled(true); //ready to go next player
        //disable traindeck button and housefaceup card buttons
        trainDeckButton.setEnabled(false);
        for (int i = 0; i < faceUpCardButtons.size(); i++) {
        	faceUpCardButtons.get(i).setEnabled(false);
        }
        
        //logAndPromptMsg.append("End of draw phase. Move to next player.\n");
    }
    
    
    void drawTicketButtonClicked() {
    	//check ticket deck size
    	if (TTREGameState.ticketsDeck.isEmpty()) {
    	    JOptionPane.showMessageDialog(this, "No more destination tickets left in the deck.");
    	    return;
    	}
    	
    	logAndPromptMsg.append("Player " + (TTREGameState.currentPlayerIndex+1) + "selected to draw more tickets. \n");
    	//disable other buttons, so player can't select other play option once he selects to draw ticket
        drawTrainButton.setEnabled(false);
        drawDestButton.setEnabled(false);
        claimRouteButton.setEnabled(false);
        buildStationButton.setEnabled(false);
        nextPlayerButton.setEnabled(false);
        for (int i = 0; i < faceUpCardButtons.size(); i++) {
        	faceUpCardButtons.get(i).setEnabled(false);
        }
        trainDeckButton.setEnabled(false);
    	
        //step 1: draw from tickets deck, update ticket deck counter
    	//ArrayList<Ticket> drawnTickets = TTREGameState.drawTickets();
        int ticketsLeft = TTREGameState.ticketsDeck.size();
        int drawCount = Math.min(3, ticketsLeft);
        ArrayList<Ticket> drawnTickets = TTREGameState.drawTickets(drawCount); 
    	logAndPromptMsg.append(TTREGameState.currentPlayer.name + "drawn tickets " + drawnTickets.size());
        ticketDeckButton.setText("Tickets left: " + TTREGameState.ticketsDeck.size());
        
        //step 2: let player to select min 1 ticket among the 3 drawn tickets
        selectTickets(1); //keep min 1 when player draws during game
        
        //logAndPromptMsg.append("Player " + (TTREGameState.currentPlayerIndex + 1) + 
        //        " kept " + selectedTickets.size() + " ticket(s).\n");
        //logAndPromptMsg.append(unselectedTickets.size() + " unselected ticket is put back to bottom of tickets deck. \n");
        //Step3: refresh UI: refresh player's hand to include the newly selected ticket.  the ticketPanel will disappear, as the ticket selection is done. 
        updateHandPanel();
        ticketDeckButton.setText("Tickets left: " + TTREGameState.ticketsDeck.size());
        //(4) no score change when player draws ticket(s)
        //(5) enable next player button
        TTREGameState.currentPlayer.hasTakenAction = true; 
        drawDestButton.setEnabled(false); //not allowing to draw more tickets
        nextPlayerButton.setEnabled(true);  
    	


    }  //end of draw ticket method
    
    
    //to allow player to select ticket at start or after drawing tickets from house deck
    //drawnTickets are already in player's fields
    void selectTickets(int minNumTicketsToKeep) {
    	
        ArrayList<Ticket> drawnTickets = TTREGameState.currentPlayer.playerDrawnTickets;

        JDialog dialog = new JDialog(this, "Select Tickets to Keep", true); // modal
        dialog.setLayout(new BorderLayout());
        
        //make sure player does not close the dialog without selecting tickets
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(dialog, 
                    "You must select at least " + minNumTicketsToKeep + " ticket(s) to continue.",
                    "Selection Required", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel selectingPanel = new JPanel();
        selectingPanel.setLayout(new BoxLayout(selectingPanel, BoxLayout.Y_AXIS));
        selectingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel instruction = new JLabel("Select at least " + minNumTicketsToKeep + " ticket(s) to keep:");
        instruction.setAlignmentX(Component.CENTER_ALIGNMENT);
        instruction.setFont(new Font("Georgia", Font.BOLD, 14));
        selectingPanel.add(instruction);
        selectingPanel.add(Box.createVerticalStrut(10));

        ArrayList<JCheckBox> checkBoxes = new ArrayList<>();

        for (Ticket ticket : drawnTickets) {
            JPanel ticketPanel = new JPanel(new BorderLayout());
            ticketPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JCheckBox checkBox = new JCheckBox();
            checkBox.setSelected(true); // default to selected
            checkBox.setOpaque(false);
            checkBoxes.add(checkBox);

            JLabel imageLabel = new JLabel();
            String imgFile = ticket.getImageFilename();
            //System.out.println ("image file name in ticket up is" + imgFile);
            ImageIcon icon = new ImageIcon(getClass().getResource(imgFile));
            Image scaled = icon.getImage().getScaledInstance(115, 70, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));

            JLabel textLabel = new JLabel(ticket.getStartCity() + " to " + ticket.getEndCity() + " (" + ticket.getPoints() + ")");
            textLabel.setFont(new Font("Georgia", Font.PLAIN, 12));

            Box ticketInfo = Box.createVerticalBox();
            ticketInfo.add(imageLabel);
            ticketInfo.add(textLabel);

            ticketPanel.add(checkBox, BorderLayout.WEST);
            ticketPanel.add(ticketInfo, BorderLayout.CENTER);

            selectingPanel.add(ticketPanel);
        }

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirmButton.addActionListener(e -> {
            ArrayList<Ticket> selected = new ArrayList<>();
            ArrayList<Ticket> unselected = new ArrayList<>();

            for (int i = 0; i < checkBoxes.size(); i++) {
                if (checkBoxes.get(i).isSelected()) {
                    selected.add(drawnTickets.get(i));
                } else {
                    unselected.add(drawnTickets.get(i));
                }
            }

            if (selected.size() < minNumTicketsToKeep) {
                errorLabel.setText("You must keep at least " + minNumTicketsToKeep + " ticket(s).");
            } else {
                TTREGameState.keepSelectedTickets(selected, unselected);
                //TTREGameState.currentPlayer.numRun++; // mark the player has taken initial turn
                dialog.dispose();
                updateHandPanel();
                logAndPromptMsg.append("Player kept " + selected.size() + " ticket(s).\n");
                //logAndPromptMsg.append("Returned " + unselected.size() + " to bottom of deck.\n");
            }
        });

        selectingPanel.add(Box.createVerticalStrut(10));
        selectingPanel.add(confirmButton);
        selectingPanel.add(Box.createVerticalStrut(5));
        selectingPanel.add(errorLabel);

        dialog.add(selectingPanel, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true); // modal — blocks until player clicks Confirm
        

        
    }  
     // end of selectTicket method
    
    
    void nextPlayerButtonClicked () {
    	int oldCurrentPlayerIndex = TTREGameState.currentPlayerIndex;
    	TTREGameState.nextPlayersTurn(); // call bround ground game logic to update player Index
//    	int newCurrentPlayerIndex = TTREGameState.currentPlayerIndex;
    	//logAndPromptMsg.append("next player button clicked. \n");
    	logAndPromptMsg.append("Player Name: " +TTREGameState.currentPlayer.name + " \n");  //add player name
    	updateHandPanel();  //update the player's handPanel for the updated player
    	
    	if (TTREGameState.isGameOver) {
    		//TTREGameState.endGame(); //this woudl calc the final scores. this should have already been called in GameState when "checkGameOver"
    		gameOverPopup(); 
    		disableAllButtons();
    	}
    	
    	
    	//check if last round is triggered, display msg box if so.  only show this the first time
    	if (TTREGameState.lastRoundTriggered && TTREGameState.lastPlayerIndex == oldCurrentPlayerIndex && (!TTREGameState.isGameOver)) {
    	    JOptionPane.showMessageDialog(this, "Last round triggered. Each player gets one final turn.");
    	}
    	
    	//if this is end of the game

    	
        //if first run, allow players to select which tickets to keep.  and then move to next player
        if (TTREGameState.currentPlayer.numRun == 0) {
        	selectTickets(2); // have to keep min 2 tickets at beginning of game
        	// for each player's first run, they only pick 2 tickets, do nothing else and move to next player's turn
            disableAllButtons(); 
            nextPlayerButton.setEnabled(true);
        	//System.out.println ("player's numRun is 0, selected 2 tickets");
        }
        
    	
    	//if it's not first round, enable the play option buttons
        if (TTREGameState.currentPlayer.numRun !=0) {
        	enableAllButtons();
        	//disable face up cards buttons
        	for (int i = 0; i < faceUpCardButtons.size(); i++) {
        		faceUpCardButtons.get(i).setEnabled(false);
        	}
        	//disable ticket and train deck buttons
        	ticketDeckButton.setEnabled(false);
        	trainDeckButton.setEnabled(false);
        	//disable next player button
        	nextPlayerButton.setEnabled(false); //player has to do something before passing to next player
        }
        
        
    	//if last round triggered, allow the player to skip the turn
    	if (TTREGameState.lastRoundTriggered) nextPlayerButton.setEnabled(true);
    	
    	updateHandPanel();  //update the player's handPanel for the updated player
    	updatePlayerStatusBoard();
    	//repaint();  // repaint all in JFrame
        //JOptionPane.showMessageDialog(frame, "You clicked the button!");
    	
    }
    
    
    public void gameOverPopup() {


        // Sort players by score (optional)
        ArrayList<Player> sortedPlayers = new ArrayList<>(TTREGameState.gamePlayers);
        sortedPlayers.sort((p1, p2) -> Integer.compare(p2.score, p1.score));


        ArrayList<Player> longestPlayers = TTREGameState.getPlayersWithLongestRoute();  // updated method
        TTREGameState.results.append("\nLongest Route: ");
        for (int i = 0; i < longestPlayers.size(); i++) {
            TTREGameState.results.append(longestPlayers.get(i).name);
            if (i < longestPlayers.size() - 1) {
                TTREGameState.results.append(", ");
            }
        }
        TTREGameState.results.append(" (+10 points)");
        
        
        //Player winner = sortedPlayers.get(0);
       // TTREGameState.results.append("\nWinner: ").append(winner.name);
        //show results
       // JOptionPane.showMessageDialog(this, TTREGameState.results.toString(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
        
        
        //consider tie breakers
        int highestScore = sortedPlayers.get(0).score;
     // Step 1: All players with highest score
        ArrayList<Player> contenders = new ArrayList<>();
        for (Player p : sortedPlayers) {
            if (p.score == highestScore) {
                contenders.add(p);
            } else {
                break;
            }
        }

        // Step 2: Filter by most completed tickets
        int maxCompletedTickets = contenders.stream()
            .mapToInt(p -> p.numCompletedTickets)
            .max()
            .orElse(0);
        contenders.removeIf(p -> p.numCompletedTickets < maxCompletedTickets);

        // Step 3: Filter by fewest stations used
        int minStationsUsed = contenders.stream()
            .mapToInt(p -> p.numStations)
            .min()
            .orElse(0);
        contenders.removeIf(p -> p.numStations > minStationsUsed);

        // Step 4: Prefer player with European Express Bonus
        ArrayList<Player> expressWinners = new ArrayList<>();
        for (Player p : contenders) {
            if (p.hasExpressBonus) expressWinners.add(p);
        }
        if (!expressWinners.isEmpty()) {
            contenders = expressWinners;
        }

        // Final result
        TTREGameState.results.append("\nWinner: ");
        for (int i = 0; i < contenders.size(); i++) {
            TTREGameState.results.append(contenders.get(i).name);
            if (i < contenders.size() - 1) TTREGameState.results.append(", ");
        }
        TTREGameState.results.append(" (").append(String.valueOf(highestScore)).append(" points)");
        //
        
        // Show option dialog
        String[] options = { "New Game", "Exit" };
        int choice = JOptionPane.showOptionDialog(
            this,
            TTREGameState.results.toString(),
            "Game Over",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );
    
        
        if (choice == 0) {
            // Restart the game
            this.dispose(); // close current window
            TicketToRideEurope.main(new String[0]);
           // GameState newState = new GameState();
           // newState.startGame();
           // GameUI newGameUI = new GameUI(newState);
  //          newState.setGameUI(newGameUI);
          //  newGameUI.setVisible(true);
        } else {
            // Exit
            System.exit(0);
        }
        
    }
    
    void setupHandPanel() {
        handPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        handPanel.setPreferredSize(new Dimension(400, 160));
        int temp = TTREGameState.currentPlayerIndex;
    	Player curPlayer = TTREGameState.gamePlayers.get(temp);
    	String playerName = curPlayer.name;   
    	String playerColor = curPlayer.getColorName(curPlayer.playerColor);
        //handPanel.setBorder(BorderFactory.createTitledBorder(String.format("Current Turn: %s.  %s's Hand | Color: %s",playerName, playerName, playerColor)));
        
        //handPanel.setBackground(new Color(220, 220, 220)); 
        Color trayBackground = new Color(102, 81, 60); // Dark wood tone
        Color trayBorder = new Color(80, 60, 40);      // Darker brown edge
        handPanel.setBackground (trayBackground);
     // Rounded border with padding and shadow effect
        TitledBorder titled = BorderFactory.createTitledBorder(
        	    String.format("Current Turn: %s.  %s's Hand | Color: %s", playerName, playerName, playerColor)
        	);
    	titled.setTitleColor(Color.WHITE); // optional: make the title visible on dark backgrounds
    	titled.setTitleFont(new Font("Georgia", Font.BOLD, 14)); 
    	handPanel.setBorder(BorderFactory.createCompoundBorder(
    		    BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(80, 60, 40, 180)),  // shadow
    		    BorderFactory.createCompoundBorder(
    		        titled,
    		        BorderFactory.createEmptyBorder(5, 5, 5, 5)
    		    )
    		));
 
        handPanel.setOpaque(true);
        handPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5)); // hgap=10, vgap=5
        //handPanel.setLayout(new BoxLayout(handPanel, BoxLayout.X_AXIS));
       /* handPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(trayBorder, 3, true), // rounded true
            BorderFactory.createEmptyBorder(10, 15, 10, 15)       // padding inside
        )); v */
        
        JPanel trainStationPanel = new JPanel();
        trainStationPanel.setLayout(new BoxLayout(trainStationPanel, BoxLayout.Y_AXIS));
        
        //add trainCounter JLabel
        trainCounterLabel = new JLabel();  // initialize
        trainCounterLabel.setFont(new Font("Georgia", Font.BOLD, 14));
        trainCounterLabel.setForeground(Color.BLACK); // Text color
        
        Color playerColor2 = TTREGameState.currentPlayer.playerColor;

        // Create a softer, lighter version of the player color
        Color lighterColor = new Color(
            (playerColor2.getRed() + 255) / 2,
            (playerColor2.getGreen() + 255) / 2,
            (playerColor2.getBlue() + 255) / 2
        );
        trainCounterLabel.setBackground(lighterColor);                   // Background color
        trainCounterLabel.setOpaque(true);        
        trainCounterLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        trainCounterLabel.setText("  Trains left: " + TTREGameState.currentPlayer.numTrains);
        //handPanel.add(trainCounterLabel);
        
        //add stationCounter JLabel
        stationCounterLabel = new JLabel();  // initialize
        stationCounterLabel.setFont(new Font("Georgia", Font.BOLD, 14));
        stationCounterLabel.setForeground(Color.BLACK);                  // Text color
        stationCounterLabel.setBackground(lighterColor);                   // Background color
        stationCounterLabel.setOpaque(true);        
        stationCounterLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        stationCounterLabel.setText("Stations left: " + TTREGameState.currentPlayer.numStations);
        //handPanel.add(stationCounterLabel);
        
        trainStationPanel.add(trainCounterLabel);
        trainStationPanel.add(Box.createVerticalStrut(8));
        trainStationPanel.add(stationCounterLabel);
        trainStationPanel.setBackground(trayBackground);
        
        handPanel.add(trainStationPanel);
 
        //Add player's hand
        Map<Color, Integer> colorCounts = TTREGameState.currentPlayer.getCardColorCounts();
        for (Color color : colorCounts.keySet()) {
            int count = colorCounts.get(color);

            // Create image icon for that color
            String imagePath = getImagePathForColor(color); // you define this
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            Image scaled = icon.getImage().getScaledInstance(60, 100, Image.SCALE_SMOOTH);
            JLabel iconLabel = new JLabel(new ImageIcon(scaled));

            String colorName = getColorNameinGameUI(color);  
            // Create a label showing the count
            JLabel countLabel = new JLabel(" x" + count +" "+ colorName);
            countLabel.setFont(new Font("Georgia", Font.BOLD, 14));

            // Group icon + count together
            JPanel cardSummary = new JPanel();
            cardSummary.setLayout(new BorderLayout());
            countLabel.setForeground(color);
            if (color == Color.YELLOW || color == Color.WHITE) {
                countLabel.setForeground(Color.DARK_GRAY); // Use dark gray for light backgrounds
            }
           
            // Optional: outline or shadow effect (faux 3D)
//            countLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));

            
            cardSummary.add(iconLabel, BorderLayout.CENTER);
            cardSummary.add(countLabel, BorderLayout.NORTH);
            cardSummary.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            cardSummary.setAlignmentY(Component.TOP_ALIGNMENT);
            cardSummary.setBackground(trayBackground);
            //cardSummary.setPreferredSize(new Dimension(80, 130));
            handPanel.add(cardSummary);
        }
        
        for (Ticket playerTicket : TTREGameState.gamePlayers.get(temp).playerTickets) {
            String ticketImgFileName = playerTicket.getImageFilename();
            ImageIcon icon = new ImageIcon(getClass().getResource(ticketImgFileName));  
            Image ticketScaled = icon.getImage().getScaledInstance(115, 70, Image.SCALE_SMOOTH);
            
            JButton ticketButton = new JButton(new ImageIcon(ticketScaled));
            ticketButton.setBorder(BorderFactory.createEmptyBorder());
            ticketButton.setContentAreaFilled(true);
            ticketButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            ticketButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            ticketButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    TTREGameState.highlightCities(playerTicket.getStartCity(), playerTicket.getEndCity());
               //     System.out.println("in ticket mouseEntered mode");
                    gameBoard.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    TTREGameState.clearHighlightedCities();
             //       System.out.println("in ticket mouseExited mode");
                    gameBoard.repaint();
                }
            });
            
            String startCityName = playerTicket.getStartCity();
            String endCityName = playerTicket.getEndCity();


            //Create the label: "Paris to Budapest (10)"
            boolean isThisTicketCompleted = TTREGameState.checkTicketCompleted(TTREGameState.currentPlayer, playerTicket);
            String labelText = "<html>" + startCityName + " to " + endCityName + "<br>(" + playerTicket.getPoints() + 
                  ") " + (isThisTicketCompleted ? "(completed)" : "(incomplete)") + "</html>";
            JLabel ticketLabel = new JLabel(labelText);
            ticketLabel.setFont(new Font("Georgia", Font.BOLD, 12));
            ticketLabel.setHorizontalAlignment(SwingConstants.CENTER);
            ticketLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            ticketLabel.setForeground(isThisTicketCompleted ? Color.GREEN.darker() : Color.BLACK);

            // Combine button + label into a panel
            JPanel ticketPanel = new JPanel();
            ticketPanel.setLayout(new BoxLayout(ticketPanel, BoxLayout.Y_AXIS));
            ticketPanel.setOpaque(true);
            ticketPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            ticketPanel.add(ticketButton);
            ticketPanel.add(ticketLabel);
            ticketPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            ticketPanel.setAlignmentY(Component.TOP_ALIGNMENT);
            ticketPanel.setBackground(trayBackground);
            
            //add mouse hovering effect to highlight start/end cities of the ticket
            ticketPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    TTREGameState.highlightCities(playerTicket.getStartCity(), playerTicket.getEndCity());
                    //System.out.println("in ticket mouseEntered mode");
                    gameBoard.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    TTREGameState.clearHighlightedCities();
                    //System.out.println("in ticket mouseExited mode");
                    gameBoard.repaint();
                }
            });

            //Add to the hand panel (or wherever you display tickets)
            handPanel.add(ticketPanel);
        }  // end of for loop
      
        
    }  // end of setuphandpanel
    
    //update player hand panel
    void updateHandPanel() {
    	
    	handPanel.removeAll();
    	int temp = TTREGameState.currentPlayerIndex;
    	Player curPlayer = TTREGameState.gamePlayers.get(temp);
    	String playerName = curPlayer.name;   
    	String playerColor = curPlayer.getColorName(curPlayer.playerColor);
        //handPanel.setBorder(BorderFactory.createTitledBorder(String.format("Current Turn: %s.  %s's Hand | Color: %s",playerName, playerName, playerColor)));
  
        handPanel.setBackground(new Color(220, 220, 220)); 
        
        Color trayBackground = new Color(102, 81, 60); // Dark wood tone
        Color trayBorder = new Color(80, 60, 40);      // Darker brown edge
        handPanel.setBackground (trayBackground);
     // Rounded border with padding and shadow effect
        TitledBorder titled = BorderFactory.createTitledBorder(
        	    String.format("Current Turn: %s.  %s's Hand | Color: %s", playerName, playerName, playerColor)
        	);

        	titled.setTitleColor(Color.WHITE); // optional: make the title visible on dark backgrounds
        	titled.setTitleFont(new Font("Georgia", Font.BOLD, 14)); 
        handPanel.setBorder(BorderFactory.createCompoundBorder(
        		    BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(80, 60, 40, 180)),  // shadow
        		    BorderFactory.createCompoundBorder(
        		        titled,
        		        BorderFactory.createEmptyBorder(10, 15, 10, 15)
        		    )
        		));
        handPanel.setOpaque(true);
        handPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5)); // hgap=10, vgap=5
        //handPanel.setLayout(new BoxLayout(handPanel, BoxLayout.X_AXIS));
        
        JPanel trainStationPanel = new JPanel();
        trainStationPanel.setLayout(new BoxLayout(trainStationPanel, BoxLayout.Y_AXIS));
        
        Color playerColor2 = TTREGameState.currentPlayer.playerColor;

        // Create a softer, lighter version of the player color
        Color lighterColor = new Color(
            (playerColor2.getRed() + 255) / 2,
            (playerColor2.getGreen() + 255) / 2,
            (playerColor2.getBlue() + 255) / 2
        );
        
        //add trainCounter JLabel
        trainCounterLabel = new JLabel();  // initialize
        trainCounterLabel.setFont(new Font("Georgia", Font.BOLD, 14));
        trainCounterLabel.setForeground(Color.BLACK);                  // Text color
        trainCounterLabel.setBackground(lighterColor);                   // Background color
        trainCounterLabel.setOpaque(true);        
        trainCounterLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        trainCounterLabel.setText("  Trains left: " + TTREGameState.currentPlayer.numTrains);
        //handPanel.add(trainCounterLabel);
        
        //add stationCounter JLabel
        stationCounterLabel = new JLabel();  // initialize
        stationCounterLabel.setFont(new Font("Georgia", Font.BOLD, 14));
        stationCounterLabel.setForeground(Color.BLACK);                  // Text color
        stationCounterLabel.setBackground(lighterColor);                   // Background color
        stationCounterLabel.setOpaque(true);        
        stationCounterLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        stationCounterLabel.setText("Stations left: " + TTREGameState.currentPlayer.numStations);
        //handPanel.add(stationCounterLabel);
        
        trainStationPanel.add(trainCounterLabel);
        trainStationPanel.add(Box.createVerticalStrut(8));
        trainStationPanel.add(stationCounterLabel);
        trainStationPanel.setBackground(trayBackground);
        
        handPanel.add(trainStationPanel);
        
       //add Player's tickets
        
        Map<Color, Integer> colorCounts = TTREGameState.currentPlayer.getCardColorCounts();

        for (Color color : colorCounts.keySet()) {
            int count = colorCounts.get(color);

            // Create image icon for that color
            String imagePath = getImagePathForColor(color); // you define this
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            Image scaled = icon.getImage().getScaledInstance(60, 100, Image.SCALE_SMOOTH);
            JLabel iconLabel = new JLabel(new ImageIcon(scaled));

            // Create a label showing the count
           String colorName = getColorNameinGameUI(color);  
            // Create a label showing the count
            JLabel countLabel = new JLabel(" x" + count + " " +colorName);
            countLabel.setFont(new Font("Georgia", Font.BOLD, 14));

            // Group icon + count together
            JPanel cardSummary = new JPanel();
            cardSummary.setLayout(new BorderLayout());
            countLabel.setForeground(color);
            if (color == Color.YELLOW || color == Color.WHITE) {
                countLabel.setForeground(Color.DARK_GRAY); // Use dark gray for light backgrounds
            }
            countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            // Optional: outline or shadow effect (faux 3D)
//            countLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));



            cardSummary.add(iconLabel, BorderLayout.CENTER);
            cardSummary.add(countLabel, BorderLayout.NORTH);
            cardSummary.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            cardSummary.setAlignmentY(Component.TOP_ALIGNMENT);
            cardSummary.setBackground(trayBackground);
            //cardSummary.setPreferredSize(new Dimension(80, 130));
            handPanel.add(cardSummary);
        }
        
 
        for (Ticket playerTicket : TTREGameState.gamePlayers.get(temp).playerTickets) {
            String ticketImgFileName = playerTicket.getImageFilename();
            ImageIcon icon = new ImageIcon(getClass().getResource(ticketImgFileName));  
            Image ticketScaled = icon.getImage().getScaledInstance(115, 70, Image.SCALE_SMOOTH);
            String startCityName = playerTicket.getStartCity();
            String endCityName = playerTicket.getEndCity();
            
            JButton ticketButton = new JButton(new ImageIcon(ticketScaled));
            ticketButton.setBorder(BorderFactory.createEmptyBorder());
            ticketButton.setContentAreaFilled(true);
            ticketButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            ticketButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


            //Create the label: "Paris to Budapest (10)"
            boolean isThisTicketCompleted = TTREGameState.checkTicketCompleted(TTREGameState.currentPlayer, playerTicket);
            String labelText = "<html>" + startCityName + " to " + endCityName + "<br>(" + playerTicket.getPoints() + 
                    ") " + (isThisTicketCompleted ? "(completed)" : "(incomplete)") + "</html>";
            JLabel ticketLabel = new JLabel(labelText);
            ticketLabel.setFont(new Font("Georgia", Font.BOLD, 12));
            ticketLabel.setHorizontalAlignment(SwingConstants.CENTER);
            ticketLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            ticketLabel.setForeground(isThisTicketCompleted ? Color.GREEN.darker() : Color.BLACK);
            

            // Combine button + label into a panel
            JPanel ticketPanel = new JPanel();
            ticketPanel.setLayout(new BoxLayout(ticketPanel, BoxLayout.Y_AXIS));
            ticketPanel.setOpaque(true);
            ticketPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            ticketPanel.add(ticketButton);
            ticketPanel.add(ticketLabel);
            ticketPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            ticketPanel.setAlignmentY(Component.TOP_ALIGNMENT);
            ticketPanel.setBackground(trayBackground);
            
            //add mouse hovering effect to highlight start/end cities of the ticket
            ticketButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    TTREGameState.highlightCities(playerTicket.getStartCity(), playerTicket.getEndCity());
                //    System.out.println("in ticket mouseEntered mode");
                    gameBoard.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    TTREGameState.clearHighlightedCities();
               //     System.out.println("in ticket mouseExited mode");
                    gameBoard.repaint();
                }
            });
            
            //add mouse hovering effect to highlight start/end cities of the ticket
            ticketPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    TTREGameState.highlightCities(playerTicket.getStartCity(), playerTicket.getEndCity());
                   // System.out.println("in ticket mouseEntered mode");
                    gameBoard.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    TTREGameState.clearHighlightedCities();
               //     System.out.println("in ticket mouseExited mode");
                    gameBoard.repaint();
                }
            });

            //Add to the hand panel (or wherever you display tickets)
            handPanel.add(ticketPanel);
        }
        handPanel.revalidate();
        handPanel.repaint();
        
    }
    
    
    public String getImagePathForColor(Color color) {
        if (Color.RED.equals(color)) return "/images/RED.png";
        if (Color.BLUE.equals(color)) return "/images/BLUE.png";
        if (Color.YELLOW.equals(color)) return "/images/YELLOW.png";
        if (Color.GREEN.equals(color)) return "/images/GREEN.png";
        if (Color.DARK_GRAY.equals(color)) return "/images/BLACK.png"; //used darkgray for black
        if (Color.WHITE.equals(color)) return "/images/WHITE.png";
        if (color.equals(new Color(255, 140, 0))) return "/images/ORANGE.png";  //self defined orange
        if (Color.MAGENTA.equals(color)) return "/images/MAGENTA.png"; // or MAGENTA.jpg
        if (Color.PINK.equals(color)) return "/images/LOCO.png"; // I used Pink for wildcards

        return "/images/UNKNOWN.jpg"; // fallback image
    }
    
    void setupHouseDeckPanel() {
    	houseDeckPanel.removeAll(); //added
    	
        faceUpCardButtons = new ArrayList<>();  // this would reset the faceUpButtons
        
        //make houseDeckPanel better looking
        houseDeckPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 2)); // center-aligned with spacing
//        TitledBorder houseDeckBorder = BorderFactory.createTitledBorder("Face-Up Train Cards");
//        houseDeckBorder.setTitleFont(new Font("Serif", Font.BOLD, 16));
//        houseDeckPanel.setBorder(houseDeckBorder);
        TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Face-Up Train Cards");
        border.setTitleFont(new Font("Georgia", Font.BOLD, 14));
        houseDeckPanel.setBorder(border);
        //houseDeckPanel.setBackground(new Color(235, 235, 235)); // cornsilk
        houseDeckPanel.setBackground(parchment );
        houseDeckPanel.setOpaque(true);
        
        //        houseDeckPanel.setBorder(BorderFactory.createTitledBorder("FaceUp Deck ")); 
        for (int i = 0; i<TTREGameState.houseDeckFaceUp.size(); i++) {
          Card faceUpCard = TTREGameState.houseDeckFaceUp.get(i);
          ImageIcon icon = new ImageIcon(getClass().getResource(faceUpCard.getCardImageFilename()));
          Image scaled = icon.getImage().getScaledInstance(70, 115, Image.SCALE_SMOOTH);
          ImageIcon scaledIcon = new ImageIcon(scaled);
          JButton faceUpButton = new JButton (scaledIcon);
       // Prevent image from graying out when disabled
          faceUpButton.setDisabledIcon(scaledIcon);
          faceUpButton.setToolTipText("Click to Select Train Card");
          faceUpButton.setBorderPainted(false);
          faceUpButton.setContentAreaFilled(false);
          faceUpButton.setFocusPainted(false);
          faceUpButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
          
          int index = i;  // capture for lambda
          faceUpButton.addActionListener(e -> {
              handleFaceUpCardClick(index);  // card draw logic
          });  
          
          faceUpCardButtons.add(faceUpButton); 
          houseDeckPanel.add(faceUpButton);
      }  
        
//        houseDeckPanel.add(trainDeckButton);
        houseDeckPanel.revalidate();
        houseDeckPanel.repaint(); 
    } //end of setupHouseDeckPanel
    
    
    public void handleFaceUpCardClick(int index) {
    	// check if there are cards available for drawing
    	boolean noHidden = TTREGameState.allTrainCards.isEmpty();
    	boolean noFaceUp = TTREGameState.houseDeckFaceUp.stream().allMatch(Objects::isNull);
    	boolean noDiscard = TTREGameState.discardTrainCards.isEmpty();

    	if (noHidden && noFaceUp && noDiscard) {
    	    JOptionPane.showMessageDialog(this, "No train cards available to draw.");
    	    return;
    	}
    	
        if (TTREGameState.cardsDrawnThisTurn >= 2) return;

        Card drawn = TTREGameState.houseDeckFaceUp.get(index);
        // First draw is a face-up Locomotive
        if (drawn.isLOC() && TTREGameState.cardsDrawnThisTurn == 0) {  //first draw is LOCO
            TTREGameState.currentPlayer.playerDeck.add(drawn);
            updateHandPanel(); //update player's hand at bottom of screen
            
            // draw from face up deck, and replace the face up deck from hidden pile
            TTREGameState.drawCardFromFaceUp(index);  
            
            //System.out.println("Face-up cards:");
            int locoCount = 0;
            for (Card c : TTREGameState.houseDeckFaceUp) {
            //    System.out.println(" - " + c.getCardImageFilename() + " isLOC=" + c.isLOC());
                if (c.isLOC()) locoCount++;
            }
            //System.out.println("Loco count: " + locoCount);
            
         // replaces that face-up card
            //the code above would update the 5 face up cards, so need to check if 3 loco
            if (TTREGameState.hasThreeOrMoreLocosFaceUp()) {
            	setupHouseDeckPanel();
            	//System.out.println ("has 3 loco cards in face up deck. need to refresh");
            	JOptionPane.showMessageDialog(this, "has 3 loco cards in face up deck. need to refresh.");
            	TTREGameState.refreshFaceUpCards();
            	discardTrainDeckButton.setText("Discard Train Cards: " + TTREGameState.discardTrainCards.size());
            	setupHouseDeckPanel();
            	JOptionPane.showMessageDialog(this, "refreshed");
            	//System.out.println ("refreshed");
                //setupHouseDeckPanel(); // update the GUI
            }
            
           	trainDeckButton.setText("Cards left: " + TTREGameState.allTrainCards.size());
         
            TTREGameState.cardsDrawnThisTurn = 2;  //end the draw car as first draw is LOCO

            logAndPromptMsg.append(TTREGameState.currentPlayer.name + " drew a face-up locomotive. Turn ends.\n");
            logAndPromptMsg.append("Cards drawn this turn: " + TTREGameState.cardsDrawnThisTurn + "\n");

            setupHouseDeckPanel();  // refresh face-up cards
            endTrainDrawPhase();
            updateHandPanel();
            return;
        }

        // Can't draw face-up Locomotive as 2nd card
        if (drawn.isLOC()) {
            JOptionPane.showMessageDialog(null, "You can't draw a face-up locomotive as your second card.");
            return;
        }

        // Regular card draw
        TTREGameState.currentPlayer.playerDeck.add(drawn);
        TTREGameState.cardsDrawnThisTurn++;
        TTREGameState.drawCardFromFaceUp(index);
        
       // System.out.println("Face-up cards:");
        int locoCount = 0;
        for (Card c : TTREGameState.houseDeckFaceUp) {
       //     System.out.println(" - " + c.getCardImageFilename() + " isLOC=" + c.isLOC());
            if (c.isLOC()) locoCount++;
        }
        //System.out.println("Loco count: " + locoCount);
        
     // replaces that face-up card
        //the code above would update the 5 face up cards, so need to check if 3 loco
        if (TTREGameState.hasThreeOrMoreLocosFaceUp()) {
        	if (!TTREGameState.allTrainCards.isEmpty()) {
        		setupHouseDeckPanel();
        		//System.out.println ("has 3 loco cards in face up deck. need to refresh");
        		JOptionPane.showMessageDialog(this, "has 3 loco cards in face up deck. need to refresh.");
        		TTREGameState.refreshFaceUpCards();
        		discardTrainDeckButton.setText("Discard Train Cards: " + TTREGameState.discardTrainCards.size());
        		setupHouseDeckPanel();
        		JOptionPane.showMessageDialog(this, "refreshed");
        		//System.out.println ("refreshed");
        		//setupHouseDeckPanel(); // update the GUI
        	} else {
        		//System.out.println("Cannot refresh face-up cards — hidden deck is empty.");
        		JOptionPane.showMessageDialog(this, "Face-up cards have 3 or more locomotives, but the hidden deck is empty. Cannot refresh.");
        	}
        }
        
        
        
       	trainDeckButton.setText("Cards left: " + TTREGameState.allTrainCards.size());
        updateHandPanel();
        logAndPromptMsg.append(TTREGameState.currentPlayer.name + " drew face-up " + getColorNameinGameUI(drawn.cardColor) + "\n");
        //logAndPromptMsg.append("Cards drawn this turn: " + TTREGameState.cardsDrawnThisTurn + "\n");
        setupHouseDeckPanel();

        if (TTREGameState.isDrawComplete()) {
            endTrainDrawPhase();
            //logAndPromptMsg.append("calling endTrainDrawPhase() \n");
        //    logAndPromptMsg.append("Cards drawn this turn: " + TTREGameState.cardsDrawnThisTurn + "\n");
        }
        
    }
 
    
    void sidePanelSetUp() {
        //put game log in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(logAndPromptMsg);
  //      scrollPane.setMaximumSize(new Dimension(250,200));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Game Log"));
        scrollPane.setPreferredSize(new Dimension(200, 100)); // Fixed width, flexible height

        
        //scrollPane.setBackground(softCream);
        scrollPane.setFont(new Font("Georgia", Font.PLAIN, 13));
        //scrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
        TitledBorder titleBorder = BorderFactory.createTitledBorder("Game Log");
        titleBorder.setTitleColor(Color.BLACK);  // or a soft cream/beige
        scrollPane.setBorder(titleBorder);
        
        
        Color trayBackground = new Color(102, 81, 60); // Dark wood tone
        Color trayBorder = new Color(80, 60, 40);      // Darker brown edge
        scrollPane.setBackground (trayBackground);
     // Rounded border with padding and shadow effect
        TitledBorder titled = BorderFactory.createTitledBorder(
        	    String.format("Game Log")
        	);

        	titled.setTitleColor(Color.WHITE); // optional: make the title visible on dark backgrounds
        	titled.setTitleFont(new Font("Georgia", Font.BOLD, 14)); 
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
        		    BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(80, 60, 40, 180)),  // shadow
        		    BorderFactory.createCompoundBorder(
        		        titled,
        		        BorderFactory.createEmptyBorder(5, 5, 5, 5)
        		    )
        		));
        scrollPane.setOpaque(true);
        //handPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5)); // hgap=10, vgap=5
        
        
        
        
        // Directly add the scroll pane to WEST
        add(scrollPane, BorderLayout.WEST);
        
    }
    
    //the method to call when player clicks on "build train station" button
    void claimStationClicked() {
    	//disable other buttons, so player can't select other play option once he selects to draw ticket
        drawTrainButton.setEnabled(false);  
        drawDestButton.setEnabled(false); 
        claimRouteButton.setEnabled(false);
//        buildStationButton.setEnabled(false);
        for (int i = 0; i < faceUpCardButtons.size(); i++) {
        	faceUpCardButtons.get(i).setEnabled(false);
        }
        trainDeckButton.setEnabled(false);
        ticketDeckButton.setEnabled(false);
        nextPlayerButton.setEnabled(false);
        
        Player player = TTREGameState.currentPlayer;
        

        //player has no stations
        if (player.numStations <= 0) {  
            JOptionPane.showMessageDialog(this, "You have no stations left to build.");
            //allow player to choose other moves
            drawTrainButton.setEnabled(true);  
            drawDestButton.setEnabled(true); 
            claimRouteButton.setEnabled(true);
            buildStationButton.setEnabled(false);
            for (int i = 0; i < faceUpCardButtons.size(); i++) {
            	faceUpCardButtons.get(i).setEnabled(true);
            }
            trainDeckButton.setEnabled(true);
            ticketDeckButton.setEnabled(true);
            
            return;
        }

        // Enable click mode to select city on gameBoard
        TTREGameState.isClaimingStation = true;
        tipsMsg.append("Click a city on the map to build your station.\n");
        
        updatePlayerStatusBoard();
        
    }//end of claimStationClicked method
    
    void claimRouteClicked() {
    	//disable other buttons, so player can't select other play option once he selects to draw ticket
        drawTrainButton.setEnabled(false);  
        drawDestButton.setEnabled(false); 
        buildStationButton.setEnabled(false);
//        buildStationButton.setEnabled(false);
        for (int i = 0; i < faceUpCardButtons.size(); i++) {
        	faceUpCardButtons.get(i).setEnabled(false);
        }
        trainDeckButton.setEnabled(false);
        ticketDeckButton.setEnabled(false);
        
        Player player = TTREGameState.currentPlayer;
        if (player.numTrains <= 0) {
            JOptionPane.showMessageDialog(this, "You have no trains left.");
            return;
        }

        TTREGameState.isClaimingRoute = true; // this is the key code that sets it true, so claiming can start
        tipsMsg.append("Click on a route between two cities to claim it.\n");

        updatePlayerStatusBoard();
    }
    
    //disable all buttons except backgound image
    void disableAllButtons() {
    	drawTrainButton.setEnabled(false);
    	drawDestButton.setEnabled(false);
    	claimRouteButton.setEnabled(false);
    	buildStationButton.setEnabled(false);
    	ticketDeckButton.setEnabled(false);
    	trainDeckButton.setEnabled(false);
    	nextPlayerButton.setEnabled(false);
    	for (int i=0; i<faceUpCardButtons.size(); i++)
    		faceUpCardButtons.get(i).setEnabled(false);
    }
    
    //enable all buttons
    void enableAllButtons() {
        drawTrainButton.setEnabled(true);
    	drawDestButton.setEnabled(true);
    	claimRouteButton.setEnabled(true);
    	buildStationButton.setEnabled(true);
    	ticketDeckButton.setEnabled(true);
    	trainDeckButton.setEnabled(true);
       	nextPlayerButton.setEnabled(true);
    	for (int i=0; i<faceUpCardButtons.size(); i++)
    		faceUpCardButtons.get(i).setEnabled(true);
    	
    }
    
    
    private String getColorNameinGameUI(Color color) {
        if (Color.RED.equals(color)) return "Red";
        if (Color.BLUE.equals(color)) return "Blue";
        if (Color.YELLOW.equals(color)) return "Yellow";
        if (Color.GREEN.equals(color)) return "Green";
        if (Color.DARK_GRAY.equals(color)) return "Black";
        if (Color.WHITE.equals(color)) return "White";
        if (color.equals(new Color(255, 140, 0))) return "Orange";
        if (Color.MAGENTA.equals(color)) return "Magenta";
        if (Color.PINK.equals(color)) return "Loco";
        return "Unknown";
    }
    
    
}  // end of gameUI class
    

//Below:
class MultiLineHeaderRenderer extends JTextArea implements TableCellRenderer {
 public MultiLineHeaderRenderer() {
     setLineWrap(true);
     setWrapStyleWord(true);
     setOpaque(true);
     setFont(new JLabel().getFont());
     setBorder(UIManager.getBorder("TableHeader.cellBorder"));
     setBackground(UIManager.getColor("TableHeader.background"));
     setForeground(UIManager.getColor("TableHeader.foreground"));
 }

 @Override
 public Component getTableCellRendererComponent(JTable table, Object value,
         boolean isSelected, boolean hasFocus, int row, int column) {
     setText(value == null ? "" : value.toString());
     setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
     return this;
 }
 
}
