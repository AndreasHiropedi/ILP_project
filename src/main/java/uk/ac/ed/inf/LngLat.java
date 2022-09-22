package uk.ac.ed.inf;

public record LngLat(double lng, double lat)
{

    public boolean inCentralArea()
    {
        // ToDo
        return true;
    }

    public double distanceTo(LngLat location)
    {
        return Math.sqrt( Math.pow( (this.lng - location.lng) , 2) - Math.pow( (this.lat - location.lat) , 2) );
    }

    public boolean closeTo(LngLat location)
    {
        return distanceTo(location) <= 0.00015;
    }

    public LngLat nextPosition(CompassLocation direction)
    {
        // ToDo
        return null;
    }

}
