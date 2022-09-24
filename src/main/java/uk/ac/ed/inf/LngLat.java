package uk.ac.ed.inf;

public record LngLat(double lng, double lat)
{

    private static final double ACCEPTABLE_DISTANCE = 0.00015;

    public boolean inCentralArea()
    {
        // TODO
        return false;
    }

    public double distanceTo(LngLat location)
    {
        return Math.sqrt( Math.pow( (this.lng - location.lng) , 2) - Math.pow( (this.lat - location.lat) , 2) );
    }

    public boolean closeTo(LngLat location)
    {
        return this.distanceTo(location) < ACCEPTABLE_DISTANCE;
    }

    public LngLat nextPosition(CompassLocation position)
    {
        // TODO
        return null;
    }

}
