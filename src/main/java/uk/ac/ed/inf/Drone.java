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

    // keeps track of the time when the algorithm is started
    private long startTime;

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
        startTime = System.nanoTime();
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
        // initialise the counter for number of moves taken
        movesTakenForOneWayFlightPath = 0;
        // used to keep the drone from getting stuck in an infinite loop
        CompassDirection getsStuck = null;
        // if we're not close to the destination
        while (!currentPositionClone.closeTo(destination))
        {
            // create a list of all angles that are not taking the drone in a no-fly-zone
            ArrayList<CompassDirection> anglesNotInNoFlyZones = new ArrayList<>();
            for (CompassDirection direction : CompassDirection.values())
            {
                // for each direction, compute a possible next position
                LngLat nextPosition = currentPositionClone.nextPosition(direction);
                // and check if that lands the drone in a no-fly-zone
                if (!nextPosition.inNoFlyZone() && !currentPositionClone.lineCutsThroughNoFlyZones(nextPosition))
                {
                    // if it doesn't, add it to the list
                    anglesNotInNoFlyZones.add(direction);
                }
            }
            // if the list already contains a direction that could get the drone
            // stuck in an infinite loop, remove it
            if (anglesNotInNoFlyZones.contains(getsStuck))
            {
                anglesNotInNoFlyZones.remove(getsStuck);
            }
            // set the closest angle to the first element in the list, and compute the closest distance
            CompassDirection bestAngle = anglesNotInNoFlyZones.get(0);
            double bestDistance = destination.distanceTo(currentPositionClone.nextPosition(bestAngle));
            // check if there is a better direction to take
            for (CompassDirection direction : anglesNotInNoFlyZones)
            {
                LngLat nextPosition = currentPositionClone.nextPosition(direction);
                double distance = destination.distanceTo(nextPosition);
                // if there is, update the variables accordingly
                if (distance < bestDistance)
                {
                    bestAngle = direction;
                    bestDistance = distance;
                }
            }
            // create a new FlightPath object
            LngLat newPosition = currentPositionClone.nextPosition(bestAngle);
            angle = bestAngle.ordinal() * 22.5;
            ticksSinceStartOfCalculation = (int) (System.nanoTime() - startTime);
            createFlightPathObject(orderNo, newPosition);
            // and take that best direction
            makeMove(newPosition);
            // get all compass directions
            List<CompassDirection> allDirections = Arrays.stream(CompassDirection.values()).toList();
            // remove the reverse of the angle that was taken
            int reverseOfAngleTaken = allDirections.indexOf(bestAngle) - 8;
            if (reverseOfAngleTaken < 0)
            {
                reverseOfAngleTaken += 16;
            }
            // and set the stuck direction to that reverse to avoid getting stuck
            getsStuck = allDirections.get(reverseOfAngleTaken);
        }
        // if we are close to the destination, make a hover move
        // create a new FlightPath object for the hover move
        angle = null;
        ticksSinceStartOfCalculation = (int) (System.nanoTime() - startTime);
        createFlightPathObject(orderNo, currentPosition);
        // and make the hover move
        makeMove(currentPositionClone);
        return movesTakenForOneWayFlightPath;
    }

    // this method moves the drone from its current position
    // to a new give position
    // @param updatedPosition the new position to which the drone
    //                        should be moved
    private void makeMove(LngLat updatedPosition)
    {
        // update the potential path to take
        potentialPathToTake.add(updatedPosition);
        // and the current position clone
        currentPositionClone = updatedPosition;
        // and the number of moves taken
        movesTakenForOneWayFlightPath++;
    }

    // this method creates a FlightPath object for every move made
    // by the drone when delivering a certain order
    // @param orderNo     the corresponding order number
    // @param newPosition the new position to which the drone
    //                    should be moved (kept constant if the
    //                    move is a hover)
    private void createFlightPathObject(String orderNo, LngLat newPosition)
    {
        FlightPath currentFlightPath = new FlightPath(orderNo, dateOfFlightPlan,
                currentPositionClone.getLng(), currentPositionClone.getLat(),
                angle, newPosition.getLng(), newPosition.getLat(),
                ticksSinceStartOfCalculation);
        allFlightPathsForGivenDate.add(currentFlightPath);
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
        FlightPath.writeFlightPathsToJson(allFlightPathsForGivenDate);
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
