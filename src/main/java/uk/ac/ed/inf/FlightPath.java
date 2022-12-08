package uk.ac.ed.inf;

import java.util.List;

/**
 * this class is used to store information about
 * each individual move made by the drone
 * (also stores information regarding the current order
 * being processed by the drone)
 */
public record FlightPath (
        String orderNo,
        String orderDate,
        double fromLongitude,
        double fromLatitude,
        Double angle,
        double toLongitude,
        double toLatitude,
        int  ticksSinceStartOfCalculation
)
{

    /**
     * this method is used to write the flight path objects
     * for a specific date (inputted in the command line)
     * to a JSON file
     */
    public static void writeFlightPathsToJson(List<FlightPath> allFlightPaths)
    {
        JsonFileWriter fileWriter = new JsonFileWriter();
        fileWriter.writeFlightPathToJSON(allFlightPaths);
    }

}
