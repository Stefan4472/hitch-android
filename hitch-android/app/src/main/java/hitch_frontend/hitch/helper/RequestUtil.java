package hitch_frontend.hitch.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for communicating with database.
 */

public class RequestUtil {

    private static final String HTTP_ADDRESS = "http://34.210.24.196/";

    // returns user id if email, password are valid, otherwise -1
    public static int validateUser(String email, String password) {
        return -1;
    }

    // returns list of Rides that match given query
    public static List<Ride> queryRides(String source, String dest, float precision) {
        return new ArrayList<>();
    }

    // returns list of Rides that match given query
    public static List<Ride> queryRides(double srcLat, double srcLong, float precision) {
        return new ArrayList<>();
    }

    // returns list of Rides that match given query
    public static List<Ride> queryRides(double srcLat, double srcLong, double destLat, double destLong, float precision) {
        return new ArrayList<>();
    }

    // returns list of populated RideInfo objects for given user--rides they've signed up for
    public static List<Ride> getUsersRides(String username) {
        return new ArrayList<>();
    }
}
