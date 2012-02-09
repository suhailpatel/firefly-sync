package firefly;

import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * Firefly Experiment class which creates an experimental version of
 * FireflyInteraction with a Time step limit
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
public class FireflyExperiment {
    /**
     * Main Class which starts off the Application by creating a new instance
     * of our GUI
     * 
     * @param args Command Line Starting Args
     */
    public static void main(String[] args) {
        // Create a new Instance of our Firely Interaction Panel
        FireflyInteraction panel = new FireflyInteraction(600, 600, 2500);
        
        // Create a new Instance of JFrame and add our Firefly Panel on
        JFrame frame = new JFrame("Firefly Cellular Automata");
        frame.setSize(600, 600);
        frame.setContentPane(panel);
        
        // Ensure our app exits when the frame is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        
        // Create a timer and start ticking!
        Timer t = new Timer(10, panel);
        t.start();
        
        // Set our Frame to be visible
        frame.setVisible(true); 
    }
}
