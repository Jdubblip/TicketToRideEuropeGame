import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.Timer;

//this is the graphic part that handles the map of cities and routes. 
public class GameBoard extends JPanel { //this is the GUI for game map part
   
	GameState TTREGameState; 
	GameUI TTREGameUI; 
	//List<Route> routes;
    //Map<String, Point> cityPositions;  //list of cities with their X/Y locations
	boolean backgroundImage; 
	boolean playerCanNotClaim;  //when player doesn't have enough station or cards to claim
	private int glowRadius = 14;
	private boolean glowIncreasing = true;
	private Timer glowTimer;
	private BufferedImage boardImage;
	int recentlyUpdatedPlayerIndex;
	
    public GameBoard(GameState gameState, boolean bcgImage, GameUI passThruGameUI) {
        this.TTREGameState = gameState;
        this.backgroundImage = bcgImage;
        this.TTREGameUI = passThruGameUI;
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setBackground(new Color(224, 211, 182));
        this.setOpaque(true);
        
        recentlyUpdatedPlayerIndex = -1; 
//        this.setresetResizable(false);
        
        //to allow glowing effect
        glowTimer = new Timer(40, e -> {
            if (glowIncreasing) {
                glowRadius++;
                if (glowRadius >= 22) glowIncreasing = false;
            } else {
                glowRadius--;
                if (glowRadius <= 14) glowIncreasing = true;
            }
            repaint();
        });
        glowTimer.start();
        
       
        try {
            boardImage = ImageIO.read(getClass().getResource("/images/MAP.jpg"));
         //   System.out.println("boardImage x = " + boardImage.getWidth());
         //   System.out.println("boardImage y = " + boardImage.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	if (TTREGameState.isClaimingStation) {
            		handleMapClickCity(e.getX(), e.getY());
            		// if player doesn't have enough stations or cards to claim, let him choose other options
            			if (playerCanNotClaim) {
            				TTREGameState.isClaimingStation = false;
            				TTREGameUI.enableAllButtons();
            				TTREGameUI.ticketDeckButton.setEnabled(false);
            				TTREGameUI.trainDeckButton.setEnabled(false);
            				TTREGameUI.nextPlayerButton.setEnabled(false);
            				for (int i=0; i<TTREGameUI.faceUpCardButtons.size(); i++)
            					TTREGameUI.faceUpCardButtons.get(i).setEnabled(false);
            			
            			}
            		}  // if claiming station
            	
            	if (TTREGameState.isClaimingRoute) {
            		handleMapClickRoute(e.getX(), e.getY());
            	}
            }
        }); 
        
        
    }//end of GameBoard constructor
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
          
     Graphics2D g2d = (Graphics2D) g;
     
     
     
//   BufferedImage boardImage; 
     if (backgroundImage && boardImage != null) {
         //g2d.drawImage(boardImage, 0, 0, getWidth(), getHeight(), null);
         Composite originalComposite = g2d.getComposite();

         // Set transparency to 60% (1.0 = fully opaque, 0.0 = fully transparent)
         g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

         // Draw the background image with transparency
         
         //int scaledWidth = boardImage.getWidth() / 2;
         //int scaledHeight = boardImage.getHeight() / 2;
         
         if (boardImage != null && backgroundImage) {
             double xScale = 0.9;  // shrink horizontally
             double yScale = 0.75;
             int imgWidth = boardImage.getWidth();
             int imgHeight = boardImage.getHeight();

             int scaledWidth = (int)(imgWidth * 0.5 * xScale);
             int scaledHeight = (int) (imgHeight * 0.5 * yScale);  // no vertical scaling

             int x = (getWidth() - scaledWidth) / 2;
             int y = (getHeight() - scaledHeight) / 2;

             g2d.drawImage(boardImage, 0, 0, scaledWidth, scaledHeight, null);
         }
         /*
         int panelWidth = getWidth();  //the gameboard size
         int panelHeight = getHeight();
         int imgWidth = boardImage.getWidth();
         int imgHeight = boardImage.getHeight();

         double scale = Math.min(panelWidth / (double) imgWidth, panelHeight / (double) imgHeight);

         int scaledWidth = (int) (imgWidth * scale);
         int scaledHeight = (int) (imgHeight * scale);

         double xScale = 2;
         int scaleWidth = (int) (scaledWidth * xScale);
 //        int x =(panelWidth - scaledWidth) / 2 ;
 //        int y = (panelHeight - scaledHeight) / 2;

         g2d.drawImage(boardImage, 0, 0, scaledWidth, scaledHeight, null); */
         
         

         //g2d.drawImage(boardImage, 0, 0, scaledWidth, scaledHeight, null);
      //   g2d.drawImage(boardImage, 0, 0, getWidth(), getHeight(), null);

         // Restore the original composite
         g2d.setComposite(originalComposite);
         
         
     }

     drawGridline(g);
 
     //draw all routes
     ArrayList<EdgeRoute> allRoutes = TTREGameState.TTREMap.getAllRoutes();
     for (EdgeRoute route:allRoutes) {
    	 if (route.isCurved)
    	 {
    		 drawTrainCarsCurved(g2d,route);
    	 }
    	 else drawTrainCarsStraight(g2d, route);  // if not curved
     } 

     //draw all cities
     ArrayList<City> allCities = TTREGameState.TTREMap.getAllCities();
     for (City city : allCities) {
    	    if (city.isOwned()) {
    	        drawStationIcon(g2d, city, city.ownedBy().playerColor);
    	    } else {
    	        drawCity(g2d, city);  // draw a regular city dot
    	    }
    	}
     
        
    } // end of paintComponent
    
    //draw each city when they don't have a station built
    private void drawCity (Graphics2D g, City city) {
    	
    	int x = city.getX();
    	int y = city.getY();
    	
    	
    	//g.setColor(routeColor);  // original route color
    	//g.fill(routeShape);      // or draw the line/curve/etc
    	
    	
        // Enable anti-aliasing for smooth edges
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Check if this city is highlighted
        boolean isHighlighted = TTREGameState.getHighlightedCities().contains(city.getCityName());

        // Glow effect
        if (isHighlighted) {
            //g.setColor(new Color(0, 255, 0, 120)); // green glow for highlight
            //g.fillOval(x - 14, y - 14, 28, 28);
//            System.out.println (city.getCityName() + " is highlighted in drawCity method");
            // BRIGHT green glow with more transparency and size
           // g.setColor(new Color(0, 255, 0, 120)); // stronger glow
        	g.setColor(Color.CYAN);
            g.fillOval(x - glowRadius, y - glowRadius, glowRadius * 2, glowRadius * 2);
          //  g.fillOval(x - 14, y - 14, 28, 28); // larger glow

            // Inner circle (more vivid)
            g.setColor(new Color(0, 255, 100)); // vivid green
            g.fillOval(x - 8, y - 8, 16, 16); // slightly larger

            // White bold outer ring
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(3)); // thicker border
            g.drawOval(x - 8, y - 8, 16, 16);
            
        } else {
            // regular city glow
//        	g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f)); // 40% opacity
            g.setColor(new Color(255, 165, 0, 80)); // orange with transparency
            g.fillOval(x - 10, y - 10, 20, 20);

            g.setColor(new Color(255, 140, 0)); // bright orange
            g.fillOval(x - 7, y - 7, 15, 15);

            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(2));
            g.drawOval(x - 7, y - 7, 15, 15);
            //g.setColor(new Color(255, 165, 0, 80)); // normal orange glow
            //g.fillOval(x - 10, y - 10, 20, 20);
//            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // reset
        }



        // City name
        g.setColor(Color.BLACK);
        Font font = new Font("Serif", Font.BOLD, 12);
        g.setFont(font);
        g.drawString(city.getCityName(), x + 12, y - 10);
    	

            	
    }
    
    //draw station when a play builds it at a city (x,y)
    public void drawStationIcon(Graphics2D g2d, City city, Color playerColor) {
    	int x = city.getX();
    	int y = city.getY();
    	
        boolean isHighlighted = TTREGameState.getHighlightedCities().contains(city.getCityName());
        
        if (isHighlighted) {
            // Animated pulsing glow
            g2d.setColor(new Color(0, 255, 100, 100)); // glowing green
            g2d.fillOval(x - glowRadius, y - glowRadius, glowRadius * 2, glowRadius * 2);
        } else {
            // Static glow in player color
            g2d.setColor(new Color(playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(), 100));
            g2d.fillOval(x - 14, y - 14, 28, 28);
        }

        // Outer circle (black border)
        g2d.setColor(Color.BLACK);
        g2d.fillOval(x - 10, y - 10, 20, 20);

        // Inner circle (player color)
        g2d.setColor(playerColor);
        g2d.fillOval(x - 8, y - 8, 16, 16);

        // Write city name
        g2d.setColor(Color.BLACK);
        Font font = new Font("Serif", Font.BOLD, 12);
        g2d.setFont(font);
        g2d.drawString(city.getCityName(), x + 12, y - 10);

    }
    
    //draw route of train cars between 2 cities
    private void drawTrainCarsCurved (Graphics2D g2d, EdgeRoute route) {
    	
    	Color baseColor = route.getColor();
    	Color paintColor;

    	//for route not owned by anyone
    	if (route.ownedBy == null) {
    	    // 80 = semi-transparent alpha
    	    paintColor = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 100);
    	} else {
    	    paintColor = route.ownedBy.playerColor;; // claimed: draw solid
    	}
    	
        boolean isClaimed = (route.ownedBy != null);
 

    	
        // Offset magnitude between the two routes
        int offsetAmount = 8;
        int routeIndex = route.routeIndex;
        int numCars = route.getLength();
        double x1 = route.getStart().getX(), y1 = route.getStart().getY();
        double x2 = route.getDestination().getX(), y2 = route.getDestination().getY();
        double ctrlX = route.curveControlPoint.getX(), ctrlY = route.curveControlPoint.getY();

        // Calculate curve direction vector at t=0.5
        double dx = 2 * (1 - 0.5) * (ctrlX - x1) + 2 * 0.5 * (x2 - ctrlX);
        double dy = 2 * (1 - 0.5) * (ctrlY - y1) + 2 * 0.5 * (y2 - ctrlY);

        // Compute perpendicular vector
        double length = Math.sqrt(dx * dx + dy * dy);
        double perpX = -dy / length;
        double perpY = dx / length;

        // Offset control point for routeIndex 0 or 1
        double offsetDir = routeIndex;  //-1 or 1 for double routes, for offset
  //      ctrlX += perpX * offsetAmount * offsetDir;
   //     ctrlY += perpY * offsetAmount * offsetDir;
        
     // Offset all 3: start, control, and end
        x1 += perpX * offsetAmount * offsetDir;
        y1 += perpY * offsetAmount * offsetDir;

        x2 += perpX * offsetAmount * offsetDir;
        y2 += perpY * offsetAmount * offsetDir;

        ctrlX += perpX * offsetAmount * offsetDir;
        ctrlY += perpY * offsetAmount * offsetDir;
        

        // Draw train cars along the offset curve
        for (int i = 0; i < numCars; i++) {
            double t = (i + 0.5) / numCars;

            // Position on the Bézier curve
            double x = Math.pow(1 - t, 2) * x1 + 2 * (1 - t) * t * ctrlX + Math.pow(t, 2) * x2;
            double y = Math.pow(1 - t, 2) * y1 + 2 * (1 - t) * t * ctrlY + Math.pow(t, 2) * y2;

            // Tangent vector for rotation
            double tx = 2 * (1 - t) * (ctrlX - x1) + 2 * t * (x2 - ctrlX);
            double ty = 2 * (1 - t) * (ctrlY - y1) + 2 * t * (y2 - ctrlY);
            double angle = Math.atan2(ty, tx);

            // Save state, transform, draw
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.rotate(angle);
            
            int width = 35, height = 15;
            // Shadow layer (only if claimed)
            if (isClaimed) {
                g2d.setColor(new Color(0, 0, 0, 100));
                int shadowOffset = 4;
                g2d.fillRoundRect(-width / 2 + shadowOffset, -height / 2 + shadowOffset, width, height, 6, 6);
                //g2d.fillRoundRect(-width / 2 + 2, -height / 2 + 2, width, height, 6, 6);
            }


            // Draw rounded train car
           // int width = 35, height = 15;
            g2d.setColor(paintColor);
            g2d.fillRoundRect(-width / 2, -height / 2, width, height, 6, 6);
            
 
            //if tunnel, set outline as black color and thicker line
            if (route.isTunnel) {
            	g2d.setColor(Color.BLACK); 
            	 g2d.setStroke(new BasicStroke(2));
            }
            else {
                g2d.setColor(Color.GRAY);
                g2d.setStroke(new BasicStroke(1));
            }
           
            g2d.drawRoundRect(-width / 2, -height / 2, width, height, 6, 6);
            
            // Step2b. Vertical Hatch for claimed routes ===
            if (isClaimed) {
            	g2d.setStroke(new BasicStroke(1));  // reset stroke for hatch lines
                g2d.setColor(Color.BLACK); // hatch color
                int hatchSpacing = 4;
                for (int xx = -width / 2; xx <= width / 2; xx += hatchSpacing) {
                    g2d.drawLine(xx, -height / 2, xx, height / 2);
                }
            }
            
            //Draw locomotive symbol on first car
            if ((i == 0) && (route.isFerry)){
                g2d.setColor(Color.BLACK);
                Font font = new Font("Serif", Font.BOLD, 8);
                g2d.setFont(font);
                g2d.drawString("LOC", -6,2);  // adjust position slightly
            }
            
            //draw LOCO symbol on 2nd car if there is 2nd LOCO
            if ((i == 1) && (route.isFerry) && (route.numberOfLOC ==2)){
                g2d.setColor(Color.BLACK);
                Font font = new Font("Serif", Font.BOLD, 8);
                g2d.setFont(font);
                g2d.drawString("LOC", -6,2);  // adjust position slightly
            }

            
            
            g2d.setTransform(old);
        }
    	
            
    }
    
    
    // to return the "nearby" route when mouse click at x/y location
    private ArrayList<EdgeRoute> getRouteNearPoint(int x, int y) {
    	
    	ArrayList<EdgeRoute> nearbyRoutes = new ArrayList<>();
    	
        for (EdgeRoute route : TTREGameState.TTREMap.TTRERoutes) {
            int numCars = route.getLength();
            int carLength = 35;
            int carWidth = 15;
            int spacing = 4;
            int offsetAmount = 8;
            int routeIndex = route.routeIndex;

            int x1 = route.getStart().getX();
            int y1 = route.getStart().getY();
            int x2 = route.getDestination().getX();
            int y2 = route.getDestination().getY();

            if (route.curveControlPoint != null) {
                // CURVED ROUTE DETECTION
                double cx = route.curveControlPoint.getX();
                double cy = route.curveControlPoint.getY();

                // Offset curve
                double dx = 2 * (1 - 0.5) * (cx - x1) + 2 * 0.5 * (x2 - cx);
                double dy = 2 * (1 - 0.5) * (cy - y1) + 2 * 0.5 * (y2 - cy);
                double length = Math.sqrt(dx * dx + dy * dy);
                double perpX = -dy / length;
                double perpY = dx / length;

                x1 += perpX * offsetAmount * routeIndex;
                y1 += perpY * offsetAmount * routeIndex;
                x2 += perpX * offsetAmount * routeIndex;
                y2 += perpY * offsetAmount * routeIndex;
                cx += perpX * offsetAmount * routeIndex;
                cy += perpY * offsetAmount * routeIndex;

                for (int i = 0; i < numCars; i++) {
                    double t = (i + 0.5) / numCars;
                    double bx = Math.pow(1 - t, 2) * x1 + 2 * (1 - t) * t * cx + Math.pow(t, 2) * x2;
                    double by = Math.pow(1 - t, 2) * y1 + 2 * (1 - t) * t * cy + Math.pow(t, 2) * y2;

                    //if (Point.distance(x, y, bx, by) < 20) return route;
                    if (Point.distance(x, y, bx, by) < 20) {
                    	nearbyRoutes.add(route);
                    	break;
                   }
                    
                }

            } else {
                //STRAIGHT ROUTE DETECTION

                double dx = x2 - x1;
                double dy = y2 - y1;
                double routeLength = Math.sqrt(dx * dx + dy * dy);

                // Step between train cars
                double stepX = (dx / routeLength) * (carLength + spacing);
                double stepY = (dy / routeLength) * (carLength + spacing);

                // Perpendicular offset for double routes
                double perpX = -dy / routeLength;
                double perpY = dx / routeLength;

                // Offset start to center first car
                int spacingToCity = ((int)routeLength - numCars * carLength - (numCars - 1) * spacing) / 2;
                double baseX = x1 + (dx / routeLength) * (spacingToCity + carLength / 2.0) + perpX * offsetAmount * routeIndex;
                double baseY = y1 + (dy / routeLength) * (spacingToCity + carLength / 2.0) + perpY * offsetAmount * routeIndex;

                for (int i = 0; i < numCars; i++) {
                    double cx = baseX + i * stepX;
                    double cy = baseY + i * stepY;

                    if (Point.distance(x, y, cx, cy) < 20) {
                    	nearbyRoutes.add(route);
                    	break;
                   }
                }
            }
        }
        return nearbyRoutes;
    }
    
    
  private void drawTrainCarsStraight(Graphics2D g2d, EdgeRoute route) {	 
	  
  	Color baseColor = route.getColor();
  	Color paintColor;

  	if (route.ownedBy == null) {
  	    // 50 = semi-transparent alpha
  	    paintColor = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 100);
  	} else { //owned by someone
  	    paintColor = route.ownedBy.playerColor; // claimed: draw solid using Player Color
  	}
 
      int numCars = route.getLength();
      int carWidth = 15;
      int spacing = 4; // space between cars
      int offset = 8;  // for double route side-by-side
  
      int p1x = route.getStart().getX();
      int p1y = route.getStart().getY();
      int p2x = route.getDestination().getX();
      int p2y = route.getDestination().getY();

      double dx = (p2x - p1x);
      double dy = (p2y - p1y);
      double routeLength = Math.sqrt(dx * dx + dy * dy);
      int carLength = 35; 
      int spacingToCity = ((int)routeLength - numCars * carLength - (numCars-1) * spacing)/2;
      //int carLength = ((int)routeLength - spacing*(numCars-1) - spacingToCity *2)/numCars;
      
      double stepX = (dx / routeLength) * (carLength + spacing);
      double stepY = (dy / routeLength) * (carLength + spacing);
      
      double dirX = dx / routeLength;
      double dirY = dy / routeLength;

      // Perpendicular unit vector
      double perpX = -(dy / routeLength);
      double perpY = dx / routeLength;

      //int routesToDraw = route.hasDoubleRoute ? 2 : 1;

      //for (int r = 0; r < routesToDraw; r++) {
          double offsetDir = route.routeIndex;  // this should be -1, 0, or 1
//          if (routesToDraw == 1) offsetDir = 0; //if only single route, no need to offset. 
          double baseX = p1x + (dirX * (spacingToCity + carLength/2.0)) + perpX * offset * offsetDir;
          double baseY = p1y + (dirY * (spacingToCity + carLength/2.0)) + perpY * offset * offsetDir;

          for (int i = 0; i < numCars; i++) {
              double cx = baseX + i * stepX;
              double cy = baseY + i * stepY;

              AffineTransform old = g2d.getTransform();
              g2d.translate(cx, cy);
              g2d.rotate(Math.atan2(dy, dx));
              
              // Check if claimed, use either player color or route color
              boolean isClaimed = (route.ownedBy != null);
              Color playerColor = isClaimed ? route.ownedBy.playerColor : paintColor;

              int width = carLength;
              int height = carWidth;
              
              // Step 1: Draw shadow if claimed
              if (isClaimed) {
                  g2d.setColor(new Color(0, 0, 0, 100)); // translucent black
                  int shadowOffset = 4;
                  g2d.fillRoundRect(-width / 2 + shadowOffset, -height / 2 + shadowOffset, width, height, 6, 6);
                  //g2d.fillRoundRect(-width / 2 + 2, -height / 2 + 2, width, height, 6, 6);
                  GradientPaint gp = new GradientPaint(
                		    -width / 2, 0, playerColor.brighter(),
                		    width / 2, 0, playerColor.darker()
                		);
                  g2d.setPaint(gp);
                  g2d.fillRoundRect(-width / 2, -height / 2, width, height, 6, 6);
                  g2d.setColor(new Color(255, 255, 255, 60));
                  g2d.fillRoundRect(-width / 2 + 3, -height / 2 + 2, width - 6, 4, 4, 4);
                  g2d.setColor(Color.BLACK);
                  g2d.setStroke(new BasicStroke(2f));
                  g2d.drawRoundRect(-width / 2, -height / 2, width, height, 6, 6);		
              }

              // Step2: Car body
              g2d.setColor(playerColor);
              g2d.fillRoundRect(-carLength / 2, -carWidth / 2, carLength, carWidth, 6, 6);

              // Step2b. Vertical Hatch for claimed routes ===
              if (isClaimed) {
            	  g2d.setStroke(new BasicStroke(1));  // reset stroke for hatch lines
                  g2d.setColor(Color.BLACK); // hatch color
                  int hatchSpacing = 4;
                  for (int x = -carLength / 2; x <= carLength / 2; x += hatchSpacing) {
                      g2d.drawLine(x, -carWidth / 2, x, carWidth / 2);
                  }
              }
              
              
              // Step3: Car border
              //if tunnel, set outline as black color and thicker line
              if (route.isTunnel) {
              	g2d.setColor(Color.BLACK); 
              	 g2d.setStroke(new BasicStroke(2));
              }
              else {
                  g2d.setColor(Color.GRAY);
                  g2d.setStroke(new BasicStroke(1));
              }
              g2d.drawRoundRect(-carLength / 2, -carWidth / 2, carLength, carWidth, 6, 6);
              
              //Draw locomotive symbol on first car
              if ((i == 0) && (route.isFerry)){
                  g2d.setColor(Color.BLACK);
                  Font font = new Font("Serif", Font.BOLD, 8);
                  g2d.setFont(font);
                  g2d.drawString("LOC", -6,2);  // adjust position slightly
              }
              
              //draw LOCO symbol on 2nd car if there is 2nd LOCO
              if ((i == 1) && (route.isFerry) && (route.numberOfLOC ==2)){
                  g2d.setColor(Color.BLACK);
                  Font font = new Font("Serif", Font.BOLD, 8);
                  g2d.setFont(font);
                  g2d.drawString("LOC", -6,2);  // adjust position slightly
              }
              
              
              g2d.setTransform(old); // reset rotation and translation
              g2d.setStroke(new BasicStroke(2)); 
          }
     // }
	  
  } //end of draw straight method
    
 //on/off together with background image  
 void drawGridline(Graphics g) {
	    Graphics2D g2d = (Graphics2D) g;

	    int panelWidth = getWidth();
	    int panelHeight = getHeight();

	    int minorSpacing = 20; // pixels between minor lines
	    int majorSpacing = 100; // pixels between major lines

	    // === Minor gridlines ===
	    g2d.setColor(Color.LIGHT_GRAY); // light gray
	    g2d.setStroke(new BasicStroke(2));
        Font font = new Font("Serif", Font.BOLD, 12);
        g2d.setFont(font);
        if (backgroundImage) {
        	for (int x = 0; x < panelWidth; x += minorSpacing) {
        		g2d.drawLine(x, 0, x, panelHeight);
        		g2d.drawString(String.valueOf(x), x + 2, 12); // label x
        	}
        	for (int y = 0; y < panelHeight; y += minorSpacing) {
        		g2d.drawLine(0, y, panelWidth, y);
        		g2d.drawString(String.valueOf(y), 2, y - 2); // label y
        	}
        }

	    // === Major gridlines ===
        if (backgroundImage) {
        	g2d.setColor(Color.DARK_GRAY); // darker gray
        	g2d.setStroke(new BasicStroke(2));

        	for (int x = 0; x < panelWidth; x += majorSpacing) {
        		g2d.drawLine(x, 0, x, panelHeight);
        		g2d.drawString(String.valueOf(x), x + 2, 12); // label x
        	}
        	for (int y = 0; y < panelHeight; y += majorSpacing) {
        		g2d.drawLine(0, y, panelWidth, y);
        		g2d.drawString(String.valueOf(y), 2, y - 2); // label y
        	}
        }
        
 }
   
 
 public void setBackgroundVisible(boolean showBackground) {
	    this.backgroundImage = showBackground;
	    repaint();
	}
 
 
 public void handleMapClickCity(int x, int y) {
	 	playerCanNotClaim = false; 
	 	if (!TTREGameState.isClaimingStation || TTREGameState.currentPlayer == null ) return; // not in claim station mode

	    City clickedCity = getCityAtPoint(new Point(x, y));
	    
	    if (clickedCity == null) {
	        JOptionPane.showMessageDialog(this, "No valid city found at that location.");
	        return;  // remain at station claiming mode as player can select other location
	    }

	    if (clickedCity.hasStationOwner) {
	        JOptionPane.showMessageDialog(this, clickedCity.getCityName() + " already has a station!");
	        return; // added 4/20 9PM remain at station claiming mode as player can select other location
	    } 
	     
	    Player player = TTREGameState.currentPlayer;
	    
	    if (player.numStations <= 0) {
	        JOptionPane.showMessageDialog(this, "You have no more stations available!");
	        playerCanNotClaim = true; 
	        return;
	    }
	    
	    
	    // Step 1: Calculate station cost (1st = 1, 2nd = 2, etc.)
	    int stationUsed = 3 - player.numStations;
	    int cost = stationUsed + 1;

	    // Step 2: Determine valid colors (with loco support)
	    Map<Color, Integer> cardCounts = player.getCardColorCounts();
	    int locoCount = cardCounts.getOrDefault(Color.PINK, 0);  //loco is set as pink color in the codes
	    ArrayList<Color> validColors = new ArrayList<>();

	    for (Color c : cardCounts.keySet()) {
	        if (c.equals(Color.PINK)) continue;
	        if (cardCounts.get(c) + locoCount >= cost) {
	            validColors.add(c);
	        }
	    }

	    if (validColors.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "You don't have enough cards to build a station.");
	        playerCanNotClaim = true; 
	        return;
	    }

	    // Step 3: Let player choose which color to use
	    Color selectedColor;
	    if (validColors.size() == 1) {
	        selectedColor = validColors.get(0);
	    } else {
	        String[] colorOptions = validColors.stream().map(this::getColorNameinGameBoard).toArray(String[]::new);
	        int choice = JOptionPane.showOptionDialog(this,
	            "Choose a color to use to build the station:",
	            "Select Color",
	            JOptionPane.DEFAULT_OPTION,
	            JOptionPane.QUESTION_MESSAGE,
	            null,
	            colorOptions,
	            colorOptions[0]);

	        if (choice == JOptionPane.CLOSED_OPTION) return;  //player can still continue claiming
	        selectedColor = validColors.get(choice);
	    }

	    // Step 4: Deduct cards
	    try {
	        //player.deductCards(selectedColor, cost);
		    ArrayList<Card> deductedCards = player.deductCards(selectedColor, cost);
		    //add all removed cards to discardTrainCards. 
		    TTREGameState.discardTrainCards.addAll(deductedCards);
		    //update discard pile counter
		    TTREGameUI.discardTrainDeckButton.setText("Discard Train Cards: " + TTREGameState.discardTrainCards.size());
		   // System.out.println("discard pile size is: " + TTREGameState.discardTrainCards.size());
//		    System.out.println("discard pile card 0 color is: " + TTREGameState.discardTrainCards.get(0).cardColor);
//		    System.out.println("discard pile card 1 color is: " + TTREGameState.discardTrainCards.get(1).cardColor);
	    } catch (IllegalStateException e) {
	        JOptionPane.showMessageDialog(this, "Unexpected error: Not enough cards.");
	        playerCanNotClaim = true; 
	        return;
	    }

	    //Step 5: Claim station
	    TTREGameState.claimStationAtCity(clickedCity);
	    this.repaint();
	    TTREGameUI.updateHandPanel();

	    // Update game state and UI
	    TTREGameUI.updatePlayerStatusBoard();
	    JOptionPane.showMessageDialog(this, "You have built a train station at " + clickedCity.getCityName());
	    TTREGameUI.logAndPromptMsg.append(player.name + " built a train station at " + clickedCity.getCityName() + "\n");
	    TTREGameUI.stationCounterLabel.setText("Stations left: " + player.numStations);
	    TTREGameUI.buildStationButton.setEnabled(false);
	    TTREGameUI.nextPlayerButton.setEnabled(true);
	    TTREGameState.isClaimingStation = false;
	    player.hasTakenAction = true;

	    // Optional: check for game over
	    //TTREGameUI.checkGameOver();

	} // end of handleMapClick method
 
   //return city based on mouse click location
 public City getCityAtPoint(Point p) {
	    for (City city : TTREGameState.TTREMap.TTRECities) {
	        int cityX = city.getX();
	        int cityY = city.getY();
	        int radius = 15; // detection radius around city

	        if (p.distance(cityX, cityY) < radius) {
	            return city;
	        }
	    }
	    return null;
	} //end of getCityAtPoint method
 
//take care of mouse click on gameBoard when player is claiming route
 public void handleMapClickRoute(int x, int y) {
	    if (TTREGameState.isClaimingRoute) {
	    	EdgeRoute clickedRoute = null; 
	    	
	        ArrayList<EdgeRoute> nearbyRoutes = getRouteNearPoint(x, y);
	        
	        if (nearbyRoutes.isEmpty()) {
	            TTREGameUI.tipsMsg.append("Click closer to a route.\n");
	            return;
	        }
	        //only 1 route
	        else if (nearbyRoutes.size() == 1) {
	            clickedRoute = nearbyRoutes.get(0);
	        } 
	        
	     // Multiple matches: Let player choose
	        else {
	            
	            String[] options = new String[nearbyRoutes.size()];
	            for (int i = 0; i < nearbyRoutes.size(); i++) {
	                EdgeRoute r = nearbyRoutes.get(i);
	                options[i] = r.getRouteNameWithColor();
	            }

	            int choice = JOptionPane.showOptionDialog(
	                this,
	                "Multiple routes found. Which one do you want to claim?",
	                "Choose Route",
	                JOptionPane.DEFAULT_OPTION,
	                JOptionPane.QUESTION_MESSAGE,
	                null,
	                options,
	                options[0]
	            );

	            if (choice >= 0) {
	                clickedRoute = nearbyRoutes.get(choice);
	            }
	        }
	        
	        //print selected route name
	        
	        if (clickedRoute != null) {
//	        	System.out.println ("clicked route is  " + clickedRoute.getRouteName());
       
	        	if (clickedRoute.ownedBy != null) {
	        		JOptionPane.showMessageDialog(this, "This route is already claimed.");
	        		return;
	        	}

	        	//check if the player already owns one of the double route and prevent player from claiming the 2nd one
	        	for (EdgeRoute route : TTREGameState.TTREMap.TTRERoutes) {
	        	    if (route == clickedRoute) continue; // skip the one being claimed
	        	    if (route.getStart().equals(clickedRoute.getStart()) &&
	        	        route.getDestination().equals(clickedRoute.getDestination()) &&
	        	        clickedRoute.ownedBy == null && route.ownedBy == TTREGameState.currentPlayer) {

	        	        JOptionPane.showMessageDialog(this, "You can't claim both routes between the same cities.");
	        	        return;
	        	    }

	        	    // check reversed city order (in case double routes cities are stored flipped)
	        	    if (route.getStart().equals(clickedRoute.getDestination()) &&
	        	        route.getDestination().equals(clickedRoute.getStart()) &&
	        	        clickedRoute.ownedBy == null && route.ownedBy == TTREGameState.currentPlayer) {

	        	        JOptionPane.showMessageDialog(this, "You can't claim both routes between the same cities.");
	        	        return;
	        	    }
	        	}
	        	//end of double route ownership check
	        	
	        	boolean claimed = claimRoute(clickedRoute);  //code in the same GameBoard class.
	        	if (claimed)  {
	        		TTREGameState.isClaimingRoute = false;
	        		this.repaint();  //repaint gameboard. 
	        	} else {
	            // allow player to try something else — don't disable other options
	        		TTREGameUI.tipsMsg.append("Route claim failed. Choose another action.\n");
	        		TTREGameUI.enableAllButtons();
	        		TTREGameUI.nextPlayerButton.setEnabled(false);
	        		TTREGameUI.trainDeckButton.setEnabled(false);
	        		TTREGameUI.ticketDeckButton.setEnabled(false);
	        		for (int i=0; i<TTREGameUI.faceUpCardButtons.size(); i++)
	        			TTREGameUI.faceUpCardButtons.get(i).setEnabled(false);
	        	} //end of else
	        }
	    }
	} // end of handleMapClickRoute method
 
 
 private boolean claimRoute(EdgeRoute route) {
	    Player player = TTREGameState.currentPlayer;
	    int cost = route.getLength(); // or route-specific logic
	    // required wildcards (e.g., 1 or 2)
	    int requiredLocos = route.getNumRequiredLocomotives();  
	    Color routeColor = route.getColor();

	    Color usedColor = routeColor;
	    int locoCount = player.getCardColorCounts().getOrDefault(Color.PINK, 0);
	    
	    Map<Color, Integer> cardCounts = player.getCardColorCounts();  //set of player's cards, numbers and colors

	    // Handle gray route selection  - I used "LIGHT_GRAY" for the gray color in game
	    if (routeColor.equals(Color.LIGHT_GRAY)) {
	        ArrayList<Color> validColors = new ArrayList<>();

	        for (Color color : cardCounts.keySet()) {
	            if (color.equals(Color.PINK)) continue;
	            //int count = cardCounts.get(color);
	            int count = cardCounts.getOrDefault(color, 0);
	            //if (count + locoCount >= cost) {
	            if (count + locoCount >= cost && locoCount >= requiredLocos) {
	                validColors.add(color);
	            }
	        }

	        if (validColors.isEmpty()) {
	            JOptionPane.showMessageDialog(this, "You don't have enough cards to claim this gray route.");
	            return false;
	        }

	        // Pick color, "usedColor"
	        if (validColors.size() == 1) {
	            usedColor = validColors.get(0);
	        } else {
	            String[] options = validColors.stream().map(this::getColorNameinGameBoard).toArray(String[]::new);
	            int selected = JOptionPane.showOptionDialog(
	                this,
	                "Choose a color to use for the gray route:",
	                "Select Route Color",
	                JOptionPane.DEFAULT_OPTION,
	                JOptionPane.QUESTION_MESSAGE,
	                null,
	                options,
	                options[0]
	            );

	            if (selected == JOptionPane.CLOSED_OPTION) return false;
	            usedColor = validColors.get(selected);  // usedColor is the one to be used for claiming route
	        }
	    }  //end of the if GRAY loop
	    
	    
	    int colorCount = cardCounts.getOrDefault(usedColor, 0);
	    int locoCountAvailable = cardCounts.getOrDefault(Color.PINK, 0);

	    // Need at least `requiredLocos` locomotives
	    if (locoCountAvailable < requiredLocos) {
	        JOptionPane.showMessageDialog(this, "This ferry route requires at least " + requiredLocos + " locomotive card(s).");
	        return false;
	    }


	    
	 // After meeting required locomotives, allow remaining cost to be covered by color + more pinks
	    int remainingCost = cost - requiredLocos;
	    int totalAvailable = colorCount + locoCountAvailable - requiredLocos;

	    if (totalAvailable < remainingCost) {
	        JOptionPane.showMessageDialog(this, "You don't have enough " +
	            getColorNameinGameBoard(usedColor) + " cards and locomotives to claim this route.");
	        return false;
	    }

	    int useColorCards = Math.min(colorCount, remainingCost);
	    int useExtraLocos = remainingCost - useColorCards;

	    // Deduct cards
	   // ArrayList<Card> removedCards = new ArrayList<>();
	    //removedCards.addAll(player.deductCards(Color.PINK, requiredLocos + useExtraLocos));
	    //removedCards.addAll(player.deductCards(usedColor, useColorCards));
	    
	   //new part for tunnel 
	    ArrayList<Card> removedCards = new ArrayList<>();

	    if (route.isTunnel) {
	        // Reveal 3 tunnel cards
	        ArrayList<Card> revealed = new ArrayList<>();
	        int extraCost = 0;

	        for (int i = 0; i < 3 && !TTREGameState.allTrainCards.isEmpty(); i++) {
	            Card drawn = TTREGameState.drawCardFromHiddenDeck();
	            revealed.add(drawn);
	            Color c = drawn.getColor();
	            //extraCost is the number of matching color + wild card.  this is what the player needs to pay extra
	            if (c.equals(usedColor) || c.equals(Color.PINK)) {
	                extraCost++;
	            }
	        }

	        // Show revealed cards thru msg window
	        StringBuilder msg = new StringBuilder("This is a tunnel route.\n");
	        msg.append("Top 3 cards drawn from the deck: ");
	        for (Card c : revealed) {
	            msg.append(getColorNameinGameBoard(c.getColor())).append("; ");
	        }
	        msg.append("\nYou must pay ").append(extraCost)
	           .append(" additional train card").append(extraCost == 1 ? "" : "s")
	           .append(" (matching color or locomotives).");
	        
	        int choice = JOptionPane.showConfirmDialog(this, msg.toString(), "Tunnel Reveal", JOptionPane.OK_CANCEL_OPTION);
	        if (choice != JOptionPane.OK_OPTION) return false;

	        //add revealed card to discard pile, regardless if the player ends up claiming the tunnel
	        TTREGameState.discardTrainCards.addAll(revealed);
	        // Now recalculate availability with extra cost
	        int totalCost = cost + extraCost;
	        int totalRequiredLocos = requiredLocos;
	        int colorCountNow = player.getCardColorCounts().getOrDefault(usedColor, 0);
	        int locoCountNow = player.getCardColorCounts().getOrDefault(Color.PINK, 0);

	        if (locoCountNow < totalRequiredLocos) {
	            JOptionPane.showMessageDialog(this, "Not enough locomotives to cover ferry requirement.");
	            return false;
	        }

	        remainingCost = totalCost - totalRequiredLocos;
	        totalAvailable = colorCountNow + locoCountNow - totalRequiredLocos;

	        if (totalAvailable < remainingCost) {
	            JOptionPane.showMessageDialog(this, "You do not have enough cards to pay for this tunnel route.");
	            //return false;
	            // Still mark turn as taken and enable next player button
	            TTREGameUI.nextPlayerButton.setEnabled(true);
	            TTREGameUI.claimRouteButton.setEnabled(false);
	            TTREGameState.currentPlayer.hasTakenAction = true;
	            return true; // return true to indicate a turn was taken
	        }

	        useColorCards = Math.min(colorCountNow, remainingCost);
	        int useLocos = remainingCost - useColorCards;

	        // Deduct total cards
	        removedCards.addAll(player.deductCards(Color.PINK, totalRequiredLocos + useLocos));
	        removedCards.addAll(player.deductCards(usedColor, useColorCards));

	    } else {
	        // Non-tunnel deduction
	        removedCards.addAll(player.deductCards(Color.PINK, requiredLocos + useExtraLocos));
	        removedCards.addAll(player.deductCards(usedColor, useColorCards));
	    }
	    

	    
	    //add all removed cards to discardTrainCards. 
	    TTREGameState.discardTrainCards.addAll(removedCards);
	    //update discard pile counter
	    TTREGameUI.discardTrainDeckButton.setText("Discard Train Cards: " + TTREGameState.discardTrainCards.size());
	    //System.out.println("discard pile size is: " + TTREGameState.discardTrainCards.size());
	    
	    TTREGameUI.claimRouteButton.setEnabled(false); //disable claim route button after it's claimed

	    player.numTrains -= cost;  //minus the trains that are used
	    route.setOwner(player);
	    player.ownedRoutes.add(route);

	    JOptionPane.showMessageDialog(this, "Route claimed between " +
	        route.getStart().getCityName() + " and " + route.getDestination().getCityName());

	    TTREGameUI.logAndPromptMsg.append(player.name + " claimed a route between "
	        + route.getStart().getCityName() + " and " + route.getDestination().getCityName() + "\n");

	    TTREGameUI.trainCounterLabel.setText("Trains left: " + player.numTrains);
	    
	    TTREGameUI.updateHandPanel();
	    
	    TTREGameUI.nextPlayerButton.setEnabled(true);
	    
	    //update player's score
	    int points;
	    switch (cost) {
	        case 1: points = 1; break;
	        case 2: points = 2; break;
	        case 3: points = 4; break;
	        case 4: points = 7; break;
	        case 6: points = 15; break;
	        case 8: points = 21; break;
	        default: points = 0; break; // fallback if cost is invalid
	    }
	    
	    TTREGameState.recentlyUpdatedPlayerIndex = TTREGameState.currentPlayerIndex;
	    TTREGameState.currentPlayer.updateScore(points);
	    
	    //also update player's longest route
	    Map<Player, Integer> longestRoutes = TTREGameState.getLongestRouteLengths();
	    for (Map.Entry<Player, Integer> entry : longestRoutes.entrySet()) {
	        Player p = entry.getKey();
	        p.longestRoute = entry.getValue();
//	        System.out.println(p.name + "'s longest route length: " + length);
	    }
	    
	    TTREGameUI.updatePlayerStatusBoard();
	    TTREGameState.currentPlayer.hasTakenAction = true; 
	    
	    return true; 
	    
	} // end of claimRoute method
 
 

	    
 private String getColorNameinGameBoard (Color color) {
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
	    

 
 
 
} //end of class GameBoard


