package uk.ac.ed.inf;

import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * this class is to be used for writing the JSON files
 * for both deliveries (orders) and the drone's flightpath
 */
public class JsonFileWriter
{
    // the JSON object which will store all the data
    // to be written to a given JSON file
    private JSONObject jsonObject;

    // stores a list of all json objects for orders
    private List<JSONObject> allOrderJsonObjects = new ArrayList<>();

    // stores a list of all flight path objects for orders
    private List<JSONObject> allFlightPathJsonObjects = new ArrayList<>();

    /**
     * the constructor for this class
     */
    public JsonFileWriter()
    {
        jsonObject = new JSONObject();
    }

    /**
     * this method writes the data for a given order object
     * to the appropriate JSON file
     * @param orders the list of order objects to be written
     */
    public void writeOrderToJSON(List<Order> orders)
    {
        String filename = "";
        for (Order order: orders)
        {
            // add all the necessary information to the JSON object
            jsonObject.put("orderNo", order.getOrderNumber());
            jsonObject.put("outcome", order.getOutcome().name());
            jsonObject.put("costInPence", order.getOrderDeliveryCost());
            // add the object to the list of order objects, and reset it
            allOrderJsonObjects.add(jsonObject);
            jsonObject = new JSONObject();
            // set up the file name in appropriate format and write file
            filename = "deliveries-" + order.getOrderDate() + ".json";
        }
        writeJSONFile(filename, allOrderJsonObjects);
    }

    /**
     * this method writes the data for a given flight path object
     * to the appropriate JSON file
     * @param flightPaths the list of flight path objects to be written
     */
    public void writeFlightPathToJSON(List<FlightPath> flightPaths)
    {
        String filename = "";
        for (FlightPath flightPath: flightPaths)
        {
            // add all the necessary information to the JSON object
            jsonObject.put("orderNo", flightPath.orderNo());
            jsonObject.put("orderDate", flightPath.orderDate());
            jsonObject.put("fromLongitude", flightPath.fromLongitude());
            jsonObject.put("fromLatitude", flightPath.fromLatitude());
            // check if the angle is null
            if (flightPath.angle() == null)
            {
                jsonObject.put("angle", JSONObject.NULL);
            }
            else
            {
                jsonObject.put("angle", flightPath.angle());
            }
            jsonObject.put("toLongitude", flightPath.toLongitude());
            jsonObject.put("toLatitude", flightPath.toLatitude());
            jsonObject.put("ticksSinceStartOfCalculation", flightPath.ticksSinceStartOfCalculation());
            // add the object to the list of flight path objects, and reset it
            allFlightPathJsonObjects.add(jsonObject);
            jsonObject = new JSONObject();
            // set up the file name in appropriate format and write file
            filename = "flightpath-" + flightPath.orderDate() + ".json";
        }
        writeJSONFile(filename, allFlightPathJsonObjects);
    }

    // this method handles the actual file writing,
    // once all the JSON data is set up
    // @param filename the name of the file to be written
    // @param allJsonObjects
    private void writeJSONFile(String filename, List<JSONObject> allJsonObjects)
    {
        // the directory path to the resultfiles folder
        String path = System.getProperty("user.dir") + "/resultfiles";
        try
        {
            // write the JSON object (and all data it stores) to given file
            FileWriter file = new FileWriter(path + "/" + filename);
            for (JSONObject object: allJsonObjects)
            {
                file.write(object.toString());
            }
            file.close();
        }
        catch (IOException e)
        {
            // Auto-generated catch block
            e.printStackTrace();
        }
    }

}
