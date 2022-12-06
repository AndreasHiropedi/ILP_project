package uk.ac.ed.inf;

import java.util.ArrayList;
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

    // a list of all the moves the drone makes
    // (all points covered during the flight)
    private List<LngLat> allMoves = new ArrayList<>();

    // all valid orders for the given date (from command line)
    private List<Order> ordersForGivenDate = Order.getValidOrders();

    // field used to store the date inputted from the command line
    // (to be used for GeoJSON file writing)
    private String dateOfFlightPlan;

    // =========================================================================
    // ============================= CONSTRUCTOR ===============================
    // =========================================================================

    /**
     * constructor method for the drone object
     */
    public Drone()
    {
        this.availableMovesLeft = MAX_NUMBER_OF_MOVES_ALLOWED;
        this.currentPosition = APPLETON_TOWER;
    }

    // =========================================================================
    // ================================ GETTERS ================================
    // =========================================================================

    /**
     * getter method for the list of all the moves the drone makes
     * @return a list of LngLat objects representing all the points
     * covered during the flight of the drone
     */
    public List<LngLat> getAllMoves()
    {
        return allMoves;
    }

    /**
     * getter method for the date of flight path field
     * @return the date of the flight path (to be used
     * in the GeoJSON file writing)
     */
    public String getDateOfFlightPlan()
    {
        return dateOfFlightPlan;
    }

    // =========================================================================
    // ================================ SETTERS ================================
    // =========================================================================

    /**
     * setter method for the date of flight path field
     * @param dateOfFlightPlan the date inputted in the command line
     */
    public void setDateOfFlightPlan(String dateOfFlightPlan)
    {
        this.dateOfFlightPlan = dateOfFlightPlan;
    }

    // =========================================================================
    // ========================== OTHER CLASS METHODS ==========================
    // =========================================================================




    /**
     * this method is used to write the flight plan for delivering
     * orders for a specific date (inputted in the command line)
     * to a GeoJSON file
     */
    public void writePlanToGeoJSON()
    {
        GeoJSONWriter geoJSONWriter = new GeoJSONWriter();
        geoJSONWriter.writePlanToGeoJSON(this);
    }

}
