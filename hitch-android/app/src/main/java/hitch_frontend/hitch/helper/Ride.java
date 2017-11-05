package hitch_frontend.hitch.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Defines a Ride, can be read from a JSON object.
 */

public class Ride {

    private String sourceAddress, destAddress, driverName;
    private long departTime;
    public double srcLat, srcLong, destLat, destLong;

    public Ride(String sourceAddress, String destAddress, String driverName, long departTime) {
        this.sourceAddress = sourceAddress;
        this.destAddress = destAddress;
        this.driverName = driverName;
        this.departTime = departTime;
    }

    public double getSrcLat() {
        return srcLat;
    }

    public double getSrcLong() {
        return srcLong;
    }

    public double getDestLat() {
        return destLat;
    }

    public double getDestLong() {
        return destLong;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public long getDepartTime() {
        return departTime;
    }

    public void setDepartTime(long departTime) {
        this.departTime = departTime;
    }

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    public String getFormattedDepartTime() {
        return timeFormat.format(new Date(departTime));
    }

    public static Ride readFromJSON(String JSON) {
        return new Ride("", "", "", 11221);
    }

    @Override
    public String toString() {
        return "Ride(" + sourceAddress + ", " + destAddress + ", " + driverName + ", " + getFormattedDepartTime() + ")";
    }
}
