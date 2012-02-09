package firefly;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Firefly Interaction Class which handles most of the GUI aspects of the 
 * automation as well as bringing all the fireflies together
 *
 * This code was created as part of the University of Birmingham Intro to
 * Natural Computation module and open sourced because it shows something
 * interesting and cool
 *
 * Licensed under part of the Creative Commons Attribution-ShareAlike 3.0 Unported 
 * (CC BY-SA 3.0)
 * 
 * @author Suhail
 */
public class FireflyInteraction extends JPanel implements ActionListener {
    
    // Declare our parameter constants
    public static final int FIREFLY_ROWS    = 10;
    public static final int FIREFLY_COLUMNS = 10;
    
    // What type of Neighbourhood do we have
    public static enum Neighbourhood { VON_NEUMANN, MOORE, RANDOM };
    public static final Neighbourhood FIREFLY_NEIGHBOURHOOD = Neighbourhood.MOORE;
    
    // Declare our 2D Array of Firefly Objects
    private Firefly [][] fireflies;
    
    // How many timesteps have elapsed and have we synced
    private int timesteps = 0;
    private boolean synced = false;
    
    // Do we want to quit once we've synced or hit x number of steps?
    // 0 means run forever, even if synced
    private int timestepLimit = 0;
    
    /**
     * Ensure we can't construct a new object of Firefly Interaction with no
     * parameters
     */
    private FireflyInteraction() { }
    
    /**
     * Constructor Method which initializes our array based on a given width
     * and height
     * 
     * @param width Number of Fireflies in width
     * @param height Number of Fireflies in height
     */
    public FireflyInteraction(int width, int height) {
        // Instantiate the actual array
        this.fireflies = new Firefly[FIREFLY_ROWS][FIREFLY_COLUMNS];
        
        // Calculate the cell height and width
        int cellHeight = height / FIREFLY_COLUMNS;
        int cellWidth  = width / FIREFLY_ROWS;
                
        // Create and store all the Firefly Objects into our Array
        for (int x = 0; x < FIREFLY_ROWS; x++) {
            for (int y = 0; y < FIREFLY_COLUMNS; y++) {
                this.fireflies[x][y] = new Firefly(x, y, cellWidth, cellHeight);
                
                // Set the state randomly to start
                double step = (Math.random() * Firefly.FIREFLY_FINAL_STEP);
                this.fireflies[x][y].setCurrentStep((int)Math.round(step));
            }
        }
        
        // Pass on to another method to set the neighbours
        this.setNeighbours();
    }
    
    /**
     * Constructor Method which initializes our array based on a given width
     * and height
     * 
     * @param width Number of Fireflies in width
     * @param height Number of Fireflies in height
     * @param timestepLimit Limit of time steps to run
     */
    public FireflyInteraction(int width, int height, int timestepLimit) {
        // Just run the constructor above
        this(width, height);
        
        // We now have a timestep limit!
        this.timestepLimit = timestepLimit;
    }
    
    /**
     * Set neighbors method which loops through our array and sets the relevant
     * neighbors for each firefly. 
     */
    private void setNeighbours() {
        for (int x = 0; x < FIREFLY_ROWS; x++) {
            for (int y = 0; y < FIREFLY_COLUMNS; y++) {
                // Declare an array to hold 8 neighbours
                Firefly [] neighbours = new Firefly[8];
                
                // Now we just need to attempt to set each one using a bunch
                // of try catch statements so Array Out of Bounds results in
                // a null neighbour being set
                
                // Get Neighbour at North
                try { neighbours[0] = fireflies[x][y - 1]; }
                catch (ArrayIndexOutOfBoundsException e) { neighbours[0] = null; }
                
                // Get Neighbour at North East
                try { neighbours[1] = fireflies[x + 1][y - 1]; }
                catch (ArrayIndexOutOfBoundsException e) { neighbours[1] = null; }
                
                // Get Neighbour at East
                try { neighbours[2] = fireflies[x + 1][y]; }
                catch (ArrayIndexOutOfBoundsException e) { neighbours[2] = null; }
                
                // Get Neighbour at South East
                try { neighbours[3] = fireflies[x + 1][y + 1]; }
                catch (ArrayIndexOutOfBoundsException e) { neighbours[3] = null; }
                
                // Get Neighbour at South
                try { neighbours[4] = fireflies[x][y + 1]; }
                catch (ArrayIndexOutOfBoundsException e) { neighbours[4] = null; }
                
                // Get Neighbour at South West
                try { neighbours[5] = fireflies[x - 1][y + 1]; }
                catch (ArrayIndexOutOfBoundsException e) { neighbours[5] = null; }
                
                // Get Neighbour at West
                try { neighbours[6] = fireflies[x - 1][y]; }
                catch (ArrayIndexOutOfBoundsException e) { neighbours[6] = null; }
                
                // Get Neighbour at North West
                try { neighbours[7] = fireflies[x - 1][y - 1]; }
                catch (ArrayIndexOutOfBoundsException e) { neighbours[7] = null; }
                
                // Now attempt to set the neighbours based on the neighbourhood
                if (FIREFLY_NEIGHBOURHOOD == Neighbourhood.MOORE)
                    fireflies[x][y].setNeighboursMoore(neighbours);
                else if (FIREFLY_NEIGHBOURHOOD == Neighbourhood.VON_NEUMANN) {
                    fireflies[x][y].setNeighboursNeumann(neighbours);
                } else if (FIREFLY_NEIGHBOURHOOD == Neighbourhood.RANDOM ) {
                    // Generate a random integer between 0 and 1
                    int random = (int)Math.round(Math.random());
                    
                    // If it's 0, we go for Moore, if it's 1 we go for Neumann
                    if (random == 0) {
                        fireflies[x][y].setNeighboursMoore(neighbours);
                    } else if (random == 1) {
                        fireflies[x][y].setNeighboursNeumann(neighbours);
                    }
                }
            }
        }
    }
   
    /**
     * Paint Component method which handles all the drawing of the GUI as well
     * as the drawing of our Fireflies
     * 
     * @param g Graphics Context
     */
    @Override
    public void paintComponent(Graphics g) {
        // Cast our Graphics Object to a Graphics 2D Object
        Graphics2D g2D = (Graphics2D)g;
        
        // Loop through each of our Fireflies and perform the repainting
        for (int x = 0; x < FIREFLY_ROWS; x++) {
            for (int y = 0; y < FIREFLY_COLUMNS; y++) {
                fireflies[x][y].repaint(g2D);
            }
        }
    }
    
    /**
     * Action Performed method which is called every time our timer ticks and
     * prepares our Fireflies for the next state and also checks for
     * synchronization
     * 
     * @param ae  Action Event
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        // Loop through each of our Fireflies and prepare the next state
        for (int x = 0; x < FIREFLY_ROWS; x++) {
            for (int y = 0; y < FIREFLY_COLUMNS; y++) {
                // Perform timestep...
                fireflies[x][y].prepareStep();
            }
        }
        
        // Loop through each of our Fireflies and perform the action
        for (int x = 0; x < FIREFLY_ROWS; x++) {
            for (int y = 0; y < FIREFLY_COLUMNS; y++) {
                // Perform timestep...
                fireflies[x][y].performStep();
            }
        }
        
        // Repaint our Panel to show the next automata state
        this.repaint();
        
        // Increment our timestep counter
        this.timesteps++;
        
        // Check if we are Syncronised
        if (!synced && this.isSynchronised()) {
            System.out.println("Synchronisation achieved in " + this.timesteps + " timesteps");
            
            // Set our sync variable to true
            this.synced = true;
        }
        
        // Have we hit the timesteps limit?
        if ((this.timestepLimit > 0 && this.timesteps >= this.timestepLimit) || synced) {
            System.exit(0);
        }
    }
    
    /**
     * This is a pretty basic aggressive method which goes through each element
     * in our array and checks if they all match up 
     */
    public boolean isSynchronised() {
        // Print out every 100 timesteps
        if (this.timesteps % 100 == 0)
            System.out.println("Number of timesteps elapsed: " + this.timesteps);
        
        // Loop through all our Fireflies and see if they are synced
        for (int x = 0; x < FIREFLY_ROWS; x++) {
            for (int y = 0; y < FIREFLY_COLUMNS; y++) {
                if (!fireflies[x][y].inSyncWithNeighbours()) {
                    return false;
                }
            }
        }
        
        // If we've got so far we must have sync, therefore return true
        return true;
    }
    
    /**
     * Main Class which starts off the Application by creating a new instance
     * of our GUI
     * 
     * @param args Command Line Starting Args
     */
    public static void main(String[] args) {
        // Create a new Instance of our Firely Interaction Panel
        FireflyInteraction panel = new FireflyInteraction(600, 600);
        
        // Create a new Instance of JFrame and add our Firefly Panel on
        JFrame frame = new JFrame("Firefly Cellular Automata");
        frame.setSize(600, 600);
        frame.setContentPane(panel);
        
        // Ensure our app exits when the frame is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        
        // Create a timer and start ticking!
        Timer t = new Timer(100, panel);
        t.start();
        
        // Set our Frame to be visible
        frame.setVisible(true); 
    }
}
