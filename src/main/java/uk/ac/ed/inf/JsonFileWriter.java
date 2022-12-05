package uk.ac.ed.inf;

import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

/**
 * this class is to be used for writing the JSON files
 * for both deliveries (orders) and the drone's flightpath
 */
public class JsonFileWriter
{
    // the JSON object which will store all the data
    // to be written to a given JSON file
    private JSONObject jsonObject;

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
     * @param order the order object to be written
     */
    public void writeOrderToJSON(Order order)
    {
        // add all the necessary information to the JSON object
        jsonObject.put("orderNo", order.getOrderNumber());
        jsonObject.put("outcome", order.getOutcome().name());
        jsonObject.put("costInPence", order.getOrderDeliveryCost());
        // set up the file name in appropriate format and write file
        String filename = "deliveries-" + order.getOrderDate() + ".json";
        writeJSONFile(filename);
    }

    /**
     * this method writes the data for a given flight path object
     * to the appropriate JSON file
     * @param flightPath the flight path object to be written
     */
    public void writeFlightPathToJSON(FlightPath flightPath)
    {
        // add all the necessary information to the JSON object
        jsonObject.put("orderNo", flightPath.orderNo());
        jsonObject.put("fromLongitude", flightPath.fromLongitude());
        jsonObject.put("fromLatitude", flightPath.fromLatitude());
        jsonObject.put("angle", flightPath.angle());
        jsonObject.put("toLongitude", flightPath.toLongitude());
        jsonObject.put("toLatitude", flightPath.toLatitude());
        jsonObject.put("ticksSinceStartOfCalculation", flightPath.ticksSinceStartOfCalculation());
        // set up the file name in appropriate format and write file
        String filename = "flightpath-" + flightPath.orderDate() + ".json";
        writeJSONFile(filename);
    }

    // this method handles the actual file writing,
    // once all the JSON data is set up
    // @param filename the name of the file to be written
    private void writeJSONFile(String filename)
    {
        // the directory path to the resultfiles folder
        String path = System.getProperty("user.dir") + "/resultfiles";
        try
        {
            // write the JSON object (and all data it stores) to given file
            FileWriter file = new FileWriter(path + "/" + filename);
            file.write(jsonObject.toString());
            file.close();
        }
        catch (IOException e)
        {
            // Auto-generated catch block
            e.printStackTrace();
        }
    }

}
