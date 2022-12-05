package uk.ac.ed.inf;

/**
 * this class is used to monitor the movement of the drone
 * when delivering orders and computing flight paths
 */
public class Drone
{
    // field used to store the maximum number of moves a drone
    // can make before it runs out of battery
    private static final int MAX_NUMBER_OF_MOVES_ALLOWED = 2000;

    // this field stores the remaining number of moves
    // that the drone has before it runs out of battery
    private int availableMovesLeft;

    /**
     * constructor method for the drone object
     */
    public Drone()
    {
        this.availableMovesLeft = MAX_NUMBER_OF_MOVES_ALLOWED;
    }



}
