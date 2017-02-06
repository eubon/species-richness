package sk.sav.ibot.speciesrichness.geo;

/**
 * Class representing spatial point.
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
public class LatLon {

    private double latitude;
    private double longitude;

    public LatLon(double latitude, double longitude) {
        if (latitude < -90 && latitude > 90) {
            throw new IllegalArgumentException("latitude must be within range [-90, 90]");
        }
        if (longitude < -180 && longitude > 180) {
            throw new IllegalArgumentException("longitude must be within range [-180, 180]");
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Geographical latitude.
     *
     * @return
     */
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Geographical longitude.
     *
     * @return
     */
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "(" + latitude + ", " + longitude + ")";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.latitude) ^ (Double.doubleToLongBits(this.latitude) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.longitude) ^ (Double.doubleToLongBits(this.longitude) >>> 32));
        return hash;
    }

    /**
     * Two points are equal if their latitudes and longitudes are equal.
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LatLon other = (LatLon) obj;
        if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(other.latitude)) {
            return false;
        }
        return Double.doubleToLongBits(this.longitude) == Double.doubleToLongBits(other.longitude);
    }

}
