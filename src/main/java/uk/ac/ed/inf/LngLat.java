package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.geom.Line2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * this class is used to represent locations on the map
 * as a set of co-ordinates of the form (longitude, latitude)
 */
@JsonIgnoreProperties("name")
public class LngLat
{

    // all the information from the REST server
    private double lng;
    private double lat;

    // this constant stores the maximum acceptable distance
    // for a location to be considered close to another location
    private static final double ACCEPTABLE_DISTANCE = 0.00015;

    // this constant store the length of a drone move (in degrees)
    private static final double DRONE_MOVE_LENGTH = 0.00015;

    // this field is used to store the validated URL from command line
    // (used when creating a CentralArea singleton instance, integrates the
    // command line input with that class)
    private static URL BASE_URL;

    // =========================================================================
    // ============================= CONSTRUCTOR ===============================
    // =========================================================================

    /**
     * constructor method for the LngLat objects
     * @param lng the longitude
     * @param lat the latitude
     */
    public LngLat(
            @JsonProperty("longitude")
            double lng,
            @JsonProperty("latitude")
            double lat)
    {
        this.lng = lng;
        this.lat = lat;
    }

    // =========================================================================
    // ================================ GETTERS ================================
    // =========================================================================

    /**
     * getter method for the longitude of a
     * LngLat object
     * @return the longitude as a Double object
     */
    public Double getLng()
    {
        return lng;
    }

    /**
     * getter method for the latitude of a
     * LngLat object
     * @return the latitude as a Double object
     */
    public Double getLat()
    {
        return lat;
    }

    // =========================================================================
    // ================================ SETTERS ================================
    // =========================================================================

    /**
     * setter method for the baseURL field
     * (used to read the co-ordinates of the central area
     * from the validated URL inputted in the command line)
     * @param baseUrl the validated URL inputted in the command line
     */
    public static void setBaseUrl(URL baseUrl)
    {
        BASE_URL = baseUrl;
    }

    // =========================================================================
    // ========================== OTHER CLASS METHODS ==========================
    // =========================================================================

    // checks if the current location is within a given polygon area
    // Note: the implementation below is partly based on the following post
    // <a href = "https://stackoverflow.com/questions/8721406/how-to-determine-if-a-point-is-inside-a-2d-convex-polygon">link</a>
    // @param corners a list of the vertices of the polygon
    // @return true if the current location is inside the polygon or on the edges, false otherwise
    private boolean inArea(List<LngLat> corners)
    {
        // this part handles exact corners
        // (treats them as being inside the central area)
        for (LngLat corner: corners)
        {
            if ( (this.lng == corner.lng) && (this.lat == corner.lat) )
            {
                return true;
            }
        }
        // this part handles points on the edges
        // (treats them as being inside the central area)
        int i = 0;
        int j = 1;
        while (i < corners.size())
        {
            LngLat corner1 = corners.get(i);
            LngLat corner2 = corners.get(j);
            // by definition of a straight line, if the distance from each end of the line
            // to the point adds up to the total length of the line, then the point is on the line
            if (this.distanceTo(corner1) + this.distanceTo(corner2) == corner1.distanceTo(corner2))
            {
                return true;
            }
            i++;
            j++;
            // to check the last edge, we reset j (edge between last vertex and first vertex)
            if (j == corners.size())
            {
                j = 0;
            }
        }
        // this part handles checking if a given point is inside the central area
        // (excluding edges, treats the central area like a polygon shape of any kind)
        int a, b;
        boolean isInsideArea = false;
        for (a = 0, b = corners.size() - 1; a < corners.size(); b = a++)
        {
            if ((corners.get(a).lat > this.lat) != (corners.get(b).lat > this.lat)
                    && (this.lng < (corners.get(b).lng - corners.get(a).lng) * (this.lat - corners.get(a).lat) /
                    (corners.get(b).lat - corners.get(a).lat) + corners.get(a).lng))
            {
                isInsideArea = !isInsideArea;
            }
        }
        return isInsideArea;
    }

    /**
     * computes the Pythagorean distance between the current location and a given location
     * @param location a given location (using the (longitude, latitude) format)
     * @return the Pythagorean distance between the two locations, as a double
     */
    public double distanceTo(LngLat location)
    {
        return Math.sqrt(
                Math.pow( (this.lng - location.lng) , 2) +
                        Math.pow( (this.lat - location.lat) , 2));
    }

    /**
     * checks if a given location is close to the current location of the drone
     * @param location the location to be checked (using (longitude, latitude) format)
     * @return true if the given location is within the maximum acceptable distance
     * from the current location, false otherwise
     */
    public boolean closeTo(LngLat location)
    {
        return this.distanceTo(location) < ACCEPTABLE_DISTANCE;
    }

    /**
     * updates the location of the drone, once it makes a move in the given
     * compass direction (if the drone is just hovering, then the position of
     * the drone remains unchanged)
     * @param position the compass direction in which the drone is headed
     * @return the new location of the drone after the move is made
     * (using the (longitude, latitude) format)
     */
    public LngLat nextPosition(CompassDirection position)
    {
        // if the drone is hovering, then the position stays the same
        if (position == null)
        {
            return this;
        }
        // converts the compass direction to an angle (in degrees)
        double angle = position.ordinal() * 22.5;
        double lng = this.lng + DRONE_MOVE_LENGTH * Math.cos(Math.toRadians(angle));
        double lat = this.lat + DRONE_MOVE_LENGTH * Math.sin(Math.toRadians(angle));
        return new LngLat(lng, lat);
    }

    /**
     * checks if the current location is within the central campus area
     * based on information from the REST server
     * @return true if the point is inside the central campus area,
     * false otherwise
     */
    public boolean inCentralArea()
    {
        // get the central area from the singleton class
        List<LngLat> corners = CentralArea.getInstance(BASE_URL).getCentralArea();
        return inArea(corners);
    }

    /**
     * checks if the current location is within any of the no-fly-zones
     * based on information from the REST server
     * @return true if the current location is in a no-fly-zone, false otherwise
     */
    public boolean inNoFlyZone()
    {
        // get all the existing no-fly-zones
        List<NoFlyZone> existingNoFlyZones = NoFlyZone.allNoFlyZones;
        for (NoFlyZone noFlyZone: existingNoFlyZones)
        {
            // for each one, check to see if the current location is inside
            // that no-fly-zone
            List<LngLat> noFlyZoneCorners = noFlyZone.getNoFlyZoneEdges();
            if (inArea(noFlyZoneCorners))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * check if a line between two points cuts through any of the
     * no-fly-zones obtained from the inputted URL
     * @param newPosition the new point being considered as a future move
     * @return true if the line cuts through a no-fly-zone, false otherwise
     */
    public boolean lineCutsThroughNoFlyZones(LngLat newPosition) {
        // create a line between the current point and the new point
        Line2D line = new Line2D.Double(lng, lat, newPosition.getLng(), newPosition.getLat());
        for (NoFlyZone noFlyZone: NoFlyZone.allNoFlyZones)
        {
            // for each no-fly-zone, get the co-ordinates of the edges
            ArrayList<ArrayList<Double>> noFlyZoneCoords = noFlyZone.getNoFlyZoneEdgesAsDoubles();
            for (int i = 0, j = noFlyZoneCoords.size()-1; i < noFlyZoneCoords.size(); j = i++)
            {
                // for each pair of co-ordinates, generate a line
                Line2D noFlyZoneLine = new Line2D.Double(noFlyZoneCoords.get(i).get(0),noFlyZoneCoords.get(i).get(1),
                        noFlyZoneCoords.get(j).get(0), noFlyZoneCoords.get(j).get(1));
                // and check if the two lines intersect
                if (line.intersectsLine(noFlyZoneLine))
                {
                    return true;
                }
            }
        }
        return false;
    }

}
