package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * this class is used to keep track of all the no-fly-zones
 * that the drone is meant to avoid whilst flying to and
 * from restaurants and delivering orders
 */
@JsonIgnoreProperties("name")
public class NoFlyZone
{
    // all the information from the REST server
    @JsonProperty("coordinates")
    private ArrayList<ArrayList<Double>> noFlyZoneEdges;

    // a list that stores the edges of each no-fly-zone
    // as LngLat objects
    private ArrayList<LngLat> noFlyZone;

    // a list of all the no-fly-zones from the REST server
    public static ArrayList<NoFlyZone> allNoFlyZones;

    /**
     * constructor for the no-fly-zone values
     * @param noFlyZoneEdges the list of edges for a no-fly-zone object
     *                       from the REST server
     */
    public NoFlyZone(ArrayList<ArrayList<Double>> noFlyZoneEdges)
    {
        this.noFlyZoneEdges = noFlyZoneEdges;
        this.noFlyZone = generateNoFlyZone();
        allNoFlyZones.add(this);
    }

    /**
     * getter method for the edges of the no-fly-zone
     * (edges stored as LngLat objects)
     * @return the class field storing the edges of the
     * no-fly-zone as LngLat objects
     */
    public ArrayList<LngLat> getNoFlyZone()
    {
        return noFlyZone;
    }

    /**
     * method used to populate the no-fly-zone edges list,
     * which stores the edges as LngLat objects
     * @return the newly populated LngLat list
     */
    public ArrayList<LngLat> generateNoFlyZone()
    {
        for (ArrayList<Double> edge: noFlyZoneEdges)
        {
            LngLat point = new LngLat(edge.get(0), edge.get(1));
            noFlyZone.add(point);
        }
        return noFlyZone;
    }

    /**
     * this method is used to check if a given point is
     * inside any of the no-fly-zones
     * @param point the point to be checked (as a LngLat object)
     * @return true if the point is inside a no-fly-zone,
     * false otherwise
     */
    public boolean insideNoFlyZone(LngLat point)
    {
        // TODO: implement this method
        return true;
    }

}
