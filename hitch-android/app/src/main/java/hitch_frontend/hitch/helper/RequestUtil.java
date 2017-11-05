package hitch_frontend.hitch.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for communicating with database.
 */

public class RequestUtil {

    // returns user id if email, password are valid, otherwise -1
    public static int validateUser(String email, String password) {
        return -1;
    }

    // returns list of Rides that match given query
    public static List<Ride> queryRides(String source, String dest, float precision, long time,
                                        boolean use_depart) {
        return new ArrayList<>();
    }

    // returns list of populated RideInfo objects for given user--rides they've signed up for
    public static List<Ride> getUsersRides(String username) {
        return new ArrayList<>();
    }
}
