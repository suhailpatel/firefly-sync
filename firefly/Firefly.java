package firefly;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Firefly Class which represents an individual Firefly's life cycle by altering
 * it's states and checking for sync amongst neighbours 
 * 
 * This code was created as part of the University of Birmingham Intro to
 * Natural Computation module and open sourced because it shows something
 * interesting and cool
 *
 * Licensed under part of the Creative Commons Attribution-ShareAlike 3.0 Unported 
 * (CC BY-SA 3.0)
 * 
 * @author Suhail Patel
 */
public class Firefly {
    
    // Define the states a firefly can have
    public static enum FireflyState { 
        FIREFLY_CHARGING, FIREFLY_NONSENS, FIREFLY_FLASH 
    };
    
    // Timesteps
    public static final int FIREFLY_START_STEP     = 0;
    public static final int FIREFLY_CHARGING_START = 0;
    public static final int FIREFLY_CHARGING_FINAL = 5;
    public static final int FIREFLY_NONSENS_START  = 6;
    public static final int FIREFLY_NONSENS_FINAL  = 8;
    public static final int FIREFLY_FLASH_STEP     = 9;
    public static final int FIREFLY_FINAL_STEP     = 9;
    
    // Colors
    private final Color FIREFLY_CHARGING_COLOR;
    private final Color FIREFLY_NONSENS_COLOR;
    private final Color FIREFLY_FLASH_COLOR;
    
    // Positioning and Size
    private int positionX;
    private int positionY;
    private int cellWidth;
    private int cellHeight;
    
    // Current Time Step and Current Time State
    private int currentStep;
    private FireflyState currentState;
    
    // Fields for the next time step
    private int nextStep;
    
    // Fields for the various neighbours
    private Firefly north;
    private Firefly northEast;
    private Firefly east;
    private Firefly southEast;
    private Firefly south;
    private Firefly southWest;
    private Firefly west;
    private Firefly northWest;

    /**
     * Constructor Method which takes in a few parameters and sets up the
     * location of this individual firefly
     * 
     * @param positionX X Coorindate Position in our Grid
     * @param positionY Y Coorindate Position in our Grid
     * @param cellWidth How wide is each firefly in the grid
     * @param cellHeight How high is each firefly in the grid
     */
    public Firefly(int positionX, int positionY, int cellWidth, int cellHeight) {
        // Set some default colours
        FIREFLY_CHARGING_COLOR  = new Color(21, 47, 74);
        FIREFLY_NONSENS_COLOR   = new Color(42, 78, 121);
        FIREFLY_FLASH_COLOR     = new Color(128, 208, 249);
        
        // Set our Positioning
        this.positionX = positionX;
        this.positionY = positionY;
        
        // Set the size of each cell
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        
        // Set the starting step to 0
        currentStep = FIREFLY_START_STEP;
        nextStep    = 0;
    }
    
    /**
     * Set neighbors based on a Moore neighborhood approach where each firefly
     * has eight neighbors (North, North East, East, South East, South, South
     * West, West and North West
     * 
     * @param fireflies Firefly array with the neighbors
     */
    public void setNeighboursMoore(Firefly [] fireflies) {
        // If we don't have 8 members then just return and don't do anything
        if (fireflies.length != 8) {
            System.out.println("Invalid amount of neighbours");
            return;
        }
        
        // Set the appropriate fireflies
        this.north      = fireflies[0];
        this.northEast  = fireflies[1];
        this.east       = fireflies[2];
        this.southEast  = fireflies[3];
        this.south      = fireflies[4];
        this.southWest  = fireflies[5];
        this.west       = fireflies[6];
        this.northWest  = fireflies[7];
    }
    
    
    /**
     * Set neighbors based on a Von-Neumann neighborhood approach where each 
     * firefly has eight four (North, East, South, West
     * 
     * @param fireflies Firefly array with the neighbors
     */
    public void setNeighboursNeumann(Firefly [] fireflies) {
        // If we don't have 8 members then just return and don't do anything
        if (fireflies.length != 8) {
            System.out.println("Invalid amount of neighbours");
            return;
        }
        
        // Set the appropriate fireflies
        this.north  = fireflies[0];
        this.east   = fireflies[2];
        this.south  = fireflies[4];
        this.west   = fireflies[6];
        
        // Set the others to null verbosely
        this.northEast = null;
        this.northWest = null;
        this.southEast = null;
        this.southWest = null;
    }
    
    /**
     * Prepare Step method which checks if any of our neighbors have flashed
     * and sets the next step of our firefly cycle based on this information
     */
    public void prepareStep() { 
        // Declare some booleans to make our if statement more readable
        boolean isCharging = currentState == FireflyState.FIREFLY_CHARGING;
        boolean neighbours = (north != null && north.didFlash()) || 
                             (northEast != null && northEast.didFlash()) || 
                             (northWest != null && northWest.didFlash()) ||
                             (south != null && south.didFlash()) || 
                             (southEast != null && southEast.didFlash()) || 
                             (southWest != null && southWest.didFlash()) ||
                             (east != null && east.didFlash()) || 
                             (west != null && west.didFlash());


        // If any are true, reset back to the charging state
        if (isCharging && neighbours) {
            nextStep = FIREFLY_CHARGING_START;
        } else {
            // Increment our Timestep
            nextStep = currentStep + 1;
            
            // If we've gone beyond our timescale we need to go back to the
            // beginning otherwise everything muddles up
            if (nextStep > FIREFLY_FINAL_STEP) nextStep = FIREFLY_START_STEP;
        }
    }
    
    /**
     * Perform step method which sets the next step as the current step
     */
    public void performStep() {
        this.setCurrentStep(nextStep);
    }
    
    /**
     * Getter method to return the current step of our firefly
     * 
     * @return Current Step of our Firefly
     */
    public int getCurrentStep() {
        return this.currentStep;
    }
    
    /**
     * Setter method to set our current step of our firefly. This also updates
     * the current state of the firefly (Charging, Non-Sensitive or Flashing)
     * 
     * @param step New step of the Firefly
     */
    public void setCurrentStep(int step) {
        // Set our step variable
        this.currentStep = step;
        
        // Just ensure our new step is set too!
        this.nextStep = step;
        
        // Find out where we lie in our states
        if (step == FIREFLY_FLASH_STEP) {
            this.currentState = FireflyState.FIREFLY_FLASH;
        } else if (step >= FIREFLY_NONSENS_START && step <= FIREFLY_NONSENS_FINAL) {
            this.currentState = FireflyState.FIREFLY_NONSENS;
        } else if (step >= FIREFLY_CHARGING_START && step <= FIREFLY_CHARGING_FINAL) {
            this.currentState = FireflyState.FIREFLY_CHARGING;
        } else {
            this.currentState = null;
        }
    }
    
    /**
     * Did Flash method which checks if this current firefly is in the Flashing
     * State
     * 
     * @return True if this firefly is flashing
     */
    public boolean didFlash() {
        return this.currentState == FireflyState.FIREFLY_FLASH;
    }
    
    /**
     * Method which returns a boolean based on if this particular firefly is
     * in sync with it's neighbors.
     * 
     * @return Are we in sync with the neighbors 
     */
    public boolean inSyncWithNeighbours() {
        // Declare some booleans to make our if statement more readable
        boolean n = (north != null && this.currentStep != north.getCurrentStep()) &&
                    (northEast != null && this.currentStep != northEast.getCurrentStep()) && 
                    (northWest != null && this.currentStep != northWest.getCurrentStep());
        
        boolean s = (south != null && this.currentStep != south.getCurrentStep()) && 
                    (southEast != null && this.currentStep != southEast.getCurrentStep()) && 
                    (southWest != null && this.currentStep != southWest.getCurrentStep());
        
        boolean rem = (east != null && this.currentStep != east.getCurrentStep()) && 
                      (west != null && this.currentStep != west.getCurrentStep());
                
        if (!n && !s && !rem) { return true; }
      
        return false;
    }
    
    /**
     * Repaint Method which handles the painting of the individual Firefly onto
     * our Graphics Context
     * 
     * @param g Graphics Context
     */
    public void repaint(Graphics2D g) {
        // Clear the rectangle to get rid of any debris is left behind
        g.clearRect(cellWidth * positionX, cellHeight * positionY, cellWidth, cellHeight);
        
        // Get the colour which was previously set in our context
        Color previous = g.getColor();
        
        if (this.currentState == FireflyState.FIREFLY_CHARGING) {
            g.setColor(FIREFLY_CHARGING_COLOR);
        } else if (this.currentState == FireflyState.FIREFLY_NONSENS) {
            g.setColor(FIREFLY_NONSENS_COLOR);
        } else if (this.currentState == FireflyState.FIREFLY_FLASH) {
            g.setColor(FIREFLY_FLASH_COLOR);
        } else {
            g.setColor(Color.RED);      // This is a warning colour!
        }
        
        // Fill our Rectangle with the specific size and width
        g.fillRect(cellWidth * positionX, cellHeight * positionY, cellWidth, cellHeight);
        g.setColor(Color.WHITE);
                
        // Set our colour back to what is was previously
        g.setColor(previous);
    }
}
