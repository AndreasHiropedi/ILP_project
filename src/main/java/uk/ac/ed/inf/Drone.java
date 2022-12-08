package uk.ac.ed.inf;

import java.util.ArrayList;
import java.util.Arrays;
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

    // current position of the drone
    private LngLat currentPosition;

    // a deep clone object of the current position
    // (to be used when planning a potential path)
    private LngLat currentPositionClone;

    // a list of all the moves the drone makes
    // (all points covered during the flight)
    private ArrayList<LngLat> allMovesMade = new ArrayList<>();

    // a list of all the moves the drone might consider making
    // (all points covered during the flight)
    private ArrayList<LngLat> potentialPathToTake;

    // stores the number of moves taken when going from a
    // given start point to a given end point
    private int movesTakenForOneWayFlightPath;

    // this field stores the remaining number of moves
    // that the drone has before it runs out of battery
    private int availableMovesLeft;

    // stores the angle at which the drone moves
    // (to be used in file writing)
    private Double angle;

    // stores the time taken to compute a given move
    // (to be used in file writing)
    private int ticksSinceStartOfCalculation;

    // field used to store the date inputted from the command line
    // (to be used for GeoJSON file writing)
    private String dateOfFlightPlan;

    // a list of all the flight path objects created for the given date
    // (to be written to a JSON file)
    private List<FlightPath> allFlightPathsForGivenDate = new ArrayList<>();


    // =========================================================================
    // ============================= CONSTRUCTOR ===============================
    // =========================================================================

    /**
     * constructor method for the drone object
     */
    public Drone()
    {
        availableMovesLeft = MAX_NUMBER_OF_MOVES_ALLOWED;
        currentPosition = APPLETON_TOWER;
        allMovesMade.add(currentPosition);
    }

    // =========================================================================
    // ================================ GETTERS ================================
    // =========================================================================

    /**
     * getter method for the list of all the moves the drone makes
     * @return a list of LngLat objects representing all the points
     * covered during the flight of the drone
     */
    public List<LngLat> getAllMovesMade()
    {
        return allMovesMade;
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
     * (to be used when writing files to GeoJSON)
     * @param dateOfFlightPlan the date inputted in the command line
     */
    public void setDateOfFlightPlan(String dateOfFlightPlan)
    {
        this.dateOfFlightPlan = dateOfFlightPlan;
    }

    // =========================================================================
    // ========================== OTHER CLASS METHODS ==========================
    // =========================================================================

    // this method checks if a delivery can be made for a given order
    // by planning a route to and from the restaurant, and checking if
    // the drone has enough battery left to make the delivery
    // @param destination a LngLat object storing the location of the restaurant
    // @param orderNo the order number (to be used when creating FlightPath objects)
    // @return true if the order can be delivered, false otherwise
    private boolean canDeliverOrder(LngLat destination, String orderNo)
    {
        // initialise the deep clone, and the potential path to take
        currentPositionClone = new LngLat(currentPosition.getLng(), currentPosition.getLat());
        potentialPathToTake = new ArrayList<>();
        // compute the number of moves needed to make the delivery
        int movesToMakeDelivery = computeFlightPath(destination, orderNo);
        movesToMakeDelivery += computeFlightPath(APPLETON_TOWER, orderNo);
        // if we have enough moves left to make the delivery
        if (availableMovesLeft - movesToMakeDelivery >= 0)
        {
            // append the created flight path to the list of all moves made
            allMovesMade.addAll(potentialPathToTake);
            // reset the current position to be the drop-off location
            // (where the order was delivered)
            currentPosition = currentPositionClone;
            // and account for the moves taken to deliver the order
            availableMovesLeft -= movesToMakeDelivery;
            // return true, since it was possible to make the delivery
            return true;
        }
        // if it is not possible to make the delivery, return false
        // so that the order outcome is not updated
        return false;
    }

    // this method computes the flight path to a given destination from the current position
    // @param destination: a LngLat object storing the location of the restaurant
    // @param orderNo: the order number (to be used when creating FlightPath objects)
    // @return the number of moves taken to get from the current position to the
    // given destination
    private int computeFlightPath(LngLat destination, String orderNo)
    {
        movesTakenForOneWayFlightPath = 0;
        CompassLocation backwards = null;
        while (!currentPositionClone.closeTo(destination))
        {
            ArrayList<CompassLocation> compassDirectionsNotInNoFlyZone = new ArrayList<>();
            for (CompassLocation direction : CompassLocation.values())
            {
                LngLat nextPosition = currentPositionClone.nextPosition(direction);

                if (!nextPosition.inNoFlyZone() && !currentPositionClone.lineCutsThroughNoFlyZones(nextPosition))
                {
                    compassDirectionsNotInNoFlyZone.add(direction);
                }
            }

            if (compassDirectionsNotInNoFlyZone.contains(backwards))
            {
                compassDirectionsNotInNoFlyZone.remove(backwards);
            }

            CompassLocation closestAngle = compassDirectionsNotInNoFlyZone.get(0);
            double closestDistance = destination.distanceTo(currentPositionClone.nextPosition(closestAngle));

            for (CompassLocation direction : compassDirectionsNotInNoFlyZone)
            {
                LngLat nextPosition = currentPositionClone.nextPosition(direction);
                double distance = destination.distanceTo(nextPosition);
                if (distance < closestDistance)
                {
                    closestAngle = direction;
                    closestDistance = distance;
                }
            }
            LngLat newPosition = currentPositionClone.nextPosition(closestAngle);
            makeMove(newPosition);

            List<CompassLocation> cum = Arrays.stream(CompassLocation.values()).toList();
            int sadjbifghbfsuhojdfkdjiu = cum.indexOf(closestAngle)-8;
            if (sadjbifghbfsuhojdfkdjiu<0)
            {
                sadjbifghbfsuhojdfkdjiu+=16;
            }
            backwards = cum.get(sadjbifghbfsuhojdfkdjiu);
        }
        makeMove(currentPositionClone);
        return movesTakenForOneWayFlightPath;
    }

    // this method moves the drone from its current position
    // to a new give position
    // @param newPosition the new position to which the drone
    //                    should be moved
    private void makeMove(LngLat newPosition)
    {
        // update the potential path to take
        potentialPathToTake.add(newPosition);
        // and the current position clone
        currentPositionClone = newPosition;
        // and the number of moves taken
        movesTakenForOneWayFlightPath++;
    }

    /**
     * generate the flight plan of the drone to deliver
     * as many orders as it can, for a given set of orders
     * (also generates all the required JSON and GeoJSON output files)
     * @param allOrdersForGivenDate all the available orders
     *                              for the inputted date
     *                              (from command line)
     */
    public void planFlightPath(List<Order> allOrdersForGivenDate)
    {
        // get the list of valid orders
        List<Order> validOrders = Order.getValidOrders();
        for (Order order: validOrders)
        {
            // for each order, get the corresponding restaurant location
            Restaurant correspondingRestaurant = order.getCorrespondingRestaurant();
            LngLat destination = correspondingRestaurant.getRestaurantLocation();
            // check if we have enough moves left to deliver the order
            if (canDeliverOrder(destination, order.getOrderNumber()))
            {
                // if so, plan the route and deliver the order
                // and record the order as delivered
                order.setOutcome(OrderOutcome.Delivered);
            }
        }
        // once all valid orders have been looped over, write all output files
        Order.writeOrdersToJson(allOrdersForGivenDate);
        // FlightPath.writeFlightPathsToJson(allFlightPathsForGivenDate);
        writePlanToGeoJSON();
    }

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
