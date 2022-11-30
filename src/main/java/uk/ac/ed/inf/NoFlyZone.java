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
    private ArrayList<ArrayList<Double>> noFlyZoneEdgesAsDoubles;

    // a list that stores the edges of each no-fly-zone
    // as LngLat objects
    private ArrayList<LngLat> noFlyZoneEdges;

    // a list of all the no-fly-zones from the REST server
    public static ArrayList<NoFlyZone> allNoFlyZones;

    /**
     * constructor for the no-fly-zone values
     * @param noFlyZoneEdgesAsDoubles the list of edges for a no-fly-zone object
     *                       from the REST server
     */
    public NoFlyZone(ArrayList<ArrayList<Double>> noFlyZoneEdgesAsDoubles)
    {
        this.noFlyZoneEdgesAsDoubles = noFlyZoneEdgesAsDoubles;
        this.noFlyZoneEdges = generateNoFlyZoneEdges();
        allNoFlyZones.add(this);
    }

    /**
     * getter method for the edges of the no-fly-zone
     * (edges stored as LngLat objects)
     * @return the class field storing the edges of the
     * no-fly-zone as LngLat objects
     */
    public ArrayList<LngLat> getNoFlyZoneEdges()
    {
        return noFlyZoneEdges;
    }

    // method used to populate the no-fly-zone edges list,
    // which stores the edges as LngLat objects
    // @return the newly populated LngLat list
    private ArrayList<LngLat> generateNoFlyZoneEdges()
    {
        for (ArrayList<Double> edge: noFlyZoneEdgesAsDoubles)
        {
            // create a new LngLat object for each pair of co-ordinates
            LngLat point = new LngLat(edge.get(0), edge.get(1));
            noFlyZoneEdges.add(point);
        }
        return noFlyZoneEdges;
    }

}
