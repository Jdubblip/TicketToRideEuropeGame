import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

// this is the runner

public class TicketToRideEurope {
	
	static int rulePageNum;
	
    public static void main(String[] args) {
    	    SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Ticket to Ride: Europe");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            createOpeningWindow(frame);
        });
    }

    // 1. Create the Main Menu
    public static void createOpeningWindow(JFrame frame) {
        frame.getContentPane().removeAll();
        frame.repaint();
        

        // Load the background image
        ImageIcon backgroundIcon = new ImageIcon(TicketToRideEurope.class.getResource("/images/TTRELandingPage.jpeg"));
        Image backgroundImage = backgroundIcon.getImage();

        /* Custom background panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }; */
        
        // Custom background panel with soft cream and image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                //g.setColor(new Color(224, 211, 182)); // soft cream underlay
                g.setColor(new Color(224, 211, 182)); // soft cream background
                g.fillRect(0, 0, getWidth(), getHeight());
                
                // Fixed image size
                int imgWidth = 1024;
                int imgHeight = 620;

                // Center image
                int x = (getWidth() - imgWidth) / 2;
                int y = (getHeight() - imgHeight) / 2;

                g.drawImage(backgroundImage, x, y, imgWidth, imgHeight, this);
                
               // g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setBackground(new Color(224, 211, 182));

        // Top-right button panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBackground(new Color(240, 228, 215)); //parchment color
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
        	    BorderFactory.createMatteBorder(2, 2, 2, 2, Color.LIGHT_GRAY),
        	    BorderFactory.createEmptyBorder(20, 20, 20, 20) // padding inside
        	));

        
     // Unified button styling
        Font buttonFont = new Font("Georgia", Font.BOLD, 18);
        Color buttonTextColor = Color.WHITE;
        Color buttonBackgroundColor = new Color(160, 130, 90); // soft brown
        
        JButton playButton = new JButton("Play Game");
        JButton rulesButton = new JButton("Show Rules");
        JButton exitButton = new JButton("Exit");

        /*Font buttonFont = new Font("Serif", Font.BOLD, 20);
        playButton.setFont(buttonFont);
        rulesButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);
        */
        
        playButton.setFont(buttonFont);
        playButton.setForeground(buttonTextColor);
        playButton.setBackground(buttonBackgroundColor);
        playButton.setFocusPainted(false);
        playButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT); // center in Y-axis box layout
        playButton.setOpaque(true);
        
        rulesButton.setFont(buttonFont);
        rulesButton.setForeground(buttonTextColor);
        rulesButton.setBackground(buttonBackgroundColor);
        rulesButton.setFocusPainted(false);
        rulesButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        rulesButton.setAlignmentX(Component.CENTER_ALIGNMENT); // center in Y-axis box layout
        rulesButton.setOpaque(true);
        
        exitButton.setFont(buttonFont);
        exitButton.setForeground(buttonTextColor);
        exitButton.setBackground(buttonBackgroundColor);
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT); // center in Y-axis box layout
        exitButton.setOpaque(true);

        buttonPanel.add(playButton);
        buttonPanel.add(rulesButton);
        buttonPanel.add(exitButton);

        topPanel.add(buttonPanel, BorderLayout.EAST);
        backgroundPanel.add(topPanel, BorderLayout.NORTH);

        frame.add(backgroundPanel);
        frame.revalidate();
        frame.repaint();

        // Button Actions
        //playButton.addActionListener(e -> playGame());
        
        playButton.addActionListener(e -> {
            frame.dispose();
            playGame();
        });
        
        rulesButton.addActionListener(e -> showRules(frame, 1));
        exitButton.addActionListener(e -> System.exit(0));
    }
    
    public static void playGame() {
    	GameState TTREGameState = new GameState();
    	TTREGameState.startGame();
    	GameUI TTREGameUI = new GameUI(TTREGameState);
    	//TTREGameState.checkTicketFileName();
    	TTREGameUI.setVisible(true); // make the new game window show up
    	
    }
    
    public static void showRules(JFrame frame, int rulePageNum) {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        frame.repaint();
        
        frame.getContentPane().setBackground(new Color(224, 211, 182)); 

        // Load background image
        String ruleImageFileName = "/images/MANUAL P" + rulePageNum + ".jpg";
        ImageIcon backgroundIcon = new ImageIcon(TicketToRideEurope.class.getResource(ruleImageFileName));
        Image backgroundImage = backgroundIcon.getImage();

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(224, 211, 182)); // soft cream underlay
                g.fillRect(0, 0, getWidth(), getHeight());
                
                // Padding (in pixels)
                int padding = 50;
                
                int panelWidth = getWidth() - 2 * padding;
                int panelHeight = getHeight() - 2 * padding;

                int imgWidth = backgroundImage.getWidth(this);
                int imgHeight = backgroundImage.getHeight(this);

                double imgAspect = (double) imgWidth / imgHeight;
                double panelAspect = (double) panelWidth / panelHeight;

                int drawWidth, drawHeight;
                if (panelAspect > imgAspect) {
                    drawHeight = panelHeight;
                    drawWidth = (int) (imgAspect * panelHeight);
                } else {
                    drawWidth = panelWidth;
                    drawHeight = (int) (panelWidth / imgAspect);
                }
                int x = (panelWidth - drawWidth) / 2;
                int y = (panelHeight - drawHeight) / 2;
                y = Math.max(y, padding); // force y to be at least 'padding' away from top

                g.drawImage(backgroundImage, x, y, drawWidth, drawHeight, this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        frame.add(backgroundPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanelInRuleFrame = new JPanel();
        buttonPanelInRuleFrame.setOpaque(false);
        buttonPanelInRuleFrame.setLayout(new BoxLayout(buttonPanelInRuleFrame, BoxLayout.Y_AXIS));
        buttonPanelInRuleFrame.setBackground(new Color(240, 228, 215)); // parchment tone
        buttonPanelInRuleFrame.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 2, 2, 2, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
            ));
        
        JButton startGameButton = new JButton("Start Game");
        JButton moreRulesButton = new JButton("Next Page");
        JButton previousRulesButton = new JButton("Previous Page");
        
        Font buttonFont = new Font("Georgia", Font.BOLD, 18);
        Color buttonTextColor = Color.WHITE;
        Color buttonBackgroundColor = new Color(160, 130, 90); // soft brown
        
        for (JButton button : Arrays.asList(moreRulesButton, previousRulesButton, startGameButton)) {
            button.setFont(buttonFont);
            button.setForeground(buttonTextColor);
            button.setBackground(buttonBackgroundColor);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            button.setMaximumSize(new Dimension(200, 40));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setOpaque(true);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            buttonPanelInRuleFrame.add(button);
            buttonPanelInRuleFrame.add(Box.createRigidArea(new Dimension(0, 15))); // spacing
        }

       
/*
        Dimension buttonSize = new Dimension(250, 50);
        Font buttonFont = new Font("Serif", Font.BOLD, 20);
        for (JButton button : Arrays.asList(startGameButton, moreRulesButton, previousRulesButton)) {
            button.setPreferredSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.setFont(buttonFont);
        }
*/
        // Button actions
        startGameButton.addActionListener(e -> {
            frame.dispose();
            playGame();
        });
        moreRulesButton.addActionListener(e -> {
            int nextPage = (rulePageNum % 3) + 1;
            showRules(frame, nextPage);
        });
        previousRulesButton.addActionListener(e -> {
            int previousPage = (rulePageNum == 1) ? 3 : (rulePageNum - 1);
            showRules(frame, previousPage);
        });

        buttonPanelInRuleFrame.add(Box.createVerticalGlue());
        buttonPanelInRuleFrame.add(moreRulesButton);
        buttonPanelInRuleFrame.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanelInRuleFrame.add(previousRulesButton);
        buttonPanelInRuleFrame.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanelInRuleFrame.add(startGameButton);
        buttonPanelInRuleFrame.add(Box.createVerticalGlue());

        JPanel eastWrapper = new JPanel(new BorderLayout());
        eastWrapper.setOpaque(false);
        eastWrapper.setBackground(new Color(224, 211, 182));
        eastWrapper.add(buttonPanelInRuleFrame, BorderLayout.CENTER);

        frame.add(eastWrapper, BorderLayout.EAST);

        frame.revalidate();
        frame.repaint();
    }


}
	


