package hitch_frontend.hitch.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a ride.
 */

public class RideInfo {

    private String sourceAddress, destAddress, driverName;
    private long departTime;

    public RideInfo(String sourceAddress, String destAddress, String driverName, long departTime) {
        this.sourceAddress = sourceAddress;
        this.destAddress = destAddress;
        this.driverName = driverName;
        this.departTime = departTime;
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
}
