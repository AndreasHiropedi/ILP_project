package uk.ac.ed.inf;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

/**
 * this class is used to represent locations on the map
 * as a set of co-ordinates of the form (longitude, latitude)
 * @param lng the longitude
 * @param lat the latitude
 */
@JsonIgnoreProperties("name")
public record LngLat(
        @JsonProperty("longitude")
        double lng,
        @JsonProperty("latitude")
        double lat)
{

    /**
     * this constant stores the maximum acceptable distance
     * for a location to be considered close to another location
     */
    private static final double ACCEPTABLE_DISTANCE = 0.00015;

    /**
     * checks if the current location is within the Central Campus area,
     * as read from the REST server
     * @return true if the current location is in the Central Campus area, false otherwise
     */
    public boolean inCentralArea() {
        ArrayList<LngLat> corners = RetrieveData.getInstance().retrieveCentralArea();

        // this part handles exact corners
        // (treats them as being inside the central area)
        for (LngLat corner: corners)
        {
            if ( (this.lng == corner.lng) && (this.lat == corner.lat) )
            {
                return true;
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
                Math.pow( (this.lng - location.lng) , 2) + Math.pow( (this.lat - location.lat) , 2)
        );
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
    public LngLat nextPosition(CompassLocation position)
    {
        // if the drone is hovering, then the position stays the same
        if (position == null)
        {
            return this;
        }
        // converts the compass direction to an angle (in degrees)
        double angle = position.ordinal() * 22.5;
        double lng = this.lng + ACCEPTABLE_DISTANCE * Math.cos(Math.toRadians(angle));
        double lat = this.lat + ACCEPTABLE_DISTANCE * Math.sin(Math.toRadians(angle));
        return new LngLat(lng, lat);
    }

}
