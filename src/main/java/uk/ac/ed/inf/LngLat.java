package uk.ac.ed.inf;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

@JsonIgnoreProperties("name")
public record LngLat(
        @JsonProperty("longitude") double lng,
        @JsonProperty("latitude") double lat)
{

    /**
     *
     */
    private static final double ACCEPTABLE_DISTANCE = 0.00015;

    /**
     *
     * @return
     */
    public boolean inCentralArea() throws IOException {
        String baseURL = "https://ilp-rest.azurewebsites.net/centralArea";
        ArrayList<LngLat> corners = new ObjectMapper().readValue(new URL(baseURL), new TypeReference<>(){});

        int a, b;
        boolean isInsideArea = false;
        for (a = 0, b = corners.size() - 1; a < corners.size(); b = a++)
        {
            if ((corners.get(a).lat >= this.lat) != (corners.get(b).lat >= this.lat)
                    && (this.lng < (corners.get(b).lng - corners.get(a).lng) * (this.lat - corners.get(a).lat) /
                    (corners.get(b).lat - corners.get(a).lat) + corners.get(a).lng))
            {
                isInsideArea = !isInsideArea;
            }
        }
        return isInsideArea;
    }

    /**
     *
     * @return
     */
    public double distanceTo(LngLat location)
    {
        return Math.sqrt( Math.pow( (this.lng - location.lng) , 2) - Math.pow( (this.lat - location.lat) , 2) );
    }

    /**
     *
     * @return
     */
    public boolean closeTo(LngLat location)
    {
        return this.distanceTo(location) < ACCEPTABLE_DISTANCE;
    }

    /**
     *
     * @return
     */
    public LngLat nextPosition(CompassLocation position)
    {
        if (position == null)
        {
            return this;
        }
        double angle = position.ordinal() * 22.5;
        double lng = this.lng + Math.sin(Math.toRadians(angle));
        double lat = this.lat + Math.cos(Math.toRadians(angle));
        return new LngLat(lng, lat);
    }

}
