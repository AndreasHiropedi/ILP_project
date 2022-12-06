package uk.ac.ed.inf;

import java.util.List;

/**
 * this class is used to monitor the movement of the drone
 * when delivering orders and computing flight paths
 */
public class Drone
{
    // field used to store the maximum number of moves a drone
    // can make before it runs out of battery
    private static final int MAX_NUMBER_OF_MOVES_ALLOWED = 2000;

    // the address of Appleton Tower
    private static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);

    // this field stores the remaining number of moves
    // that the drone has before it runs out of battery
    private int availableMovesLeft;

    // current position of the drone
    private LngLat currentPosition;

    // constantly store the next position of the drone
    // (where the drone will be after a move is made)
    private LngLat nextPosition;

    // all valid orders for the given date (from command line)
    private List<Order> ordersForGivenDate = Order.getValidOrders();

    /**
     * constructor method for the drone object
     */
    public Drone()
    {
        this.availableMovesLeft = MAX_NUMBER_OF_MOVES_ALLOWED;
        this.currentPosition = APPLETON_TOWER;
    }



}
