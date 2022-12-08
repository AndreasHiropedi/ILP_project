package uk.ac.ed.inf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * this class is used to write the GeoJSON files
 * needed as part of the specification
 */
public class GeoJSONWriter
{

    /**
     * constructor method for the GeoJSON file
     * writer
     */
    public GeoJSONWriter()
    {

    }

    /**
     * this method writes the data for a given drone flight plan
     * for delivering orders for a given date (from command line)
     * to the appropriate GeoJSON file
     * @param drone the drone object containing the flight plan
     *              to be written
     */
    public void writePlanToGeoJSON(Drone drone)
    {
        // the directory path to the resultfiles folder
        String path = System.getProperty("user.dir") + "/resultfiles";
        // get all the necessary information from the drone object
        List<LngLat> finalCoordinates = drone.getAllMovesMade();
        String fileDate = drone.getDateOfFlightPlan();
        try
        {
            // set up all the data for GeoJSON format
            ArrayList<Point> pointsList = new ArrayList<>();
            for (LngLat point : finalCoordinates) {
                pointsList.add(Point.fromLngLat(point.getLng(), point.getLat()));
            }
            LineString line = LineString.fromLngLats(pointsList);
            Feature feature = Feature.fromGeometry(line);
            FeatureCollection collection = FeatureCollection.fromFeature(feature);
            String fileName = "drone-" + fileDate + ".geojson";
            // and write it to the appropriate file
            FileWriter file = new FileWriter(path + "/" + fileName);
            file.write(collection.toJson());
            file.close();
        }
        catch (IOException e)
        {
            System.err.println("Exception occurred: \n" + e);
            System.exit(1);
        }
    }

}
