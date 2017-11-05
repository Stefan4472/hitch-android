package hitch_frontend.hitch.activity;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import hitch_frontend.hitch.R;
import hitch_frontend.hitch.fragment.ChooseRideDialogFragment;
import hitch_frontend.hitch.fragment.RideFilterFragment;
import hitch_frontend.hitch.helper.RequestUtil;
import hitch_frontend.hitch.helper.Ride;
import hitch_frontend.hitch.view.RideListAdapter;

/**
 * Main App
 */

public class SchedulerActivity extends FragmentActivity implements OnMapReadyCallback,
        RideFilterFragment.OnFilterUpdatedListener, RideListAdapter.RideListAdapterListener,
        GoogleMap.OnMarkerClickListener, ChooseRideDialogFragment.ChooseRideDialogListener {

    private MapView mapView;
    private GoogleMap googleMap;
    private ViewSwitcher viewSwitcher;
    private LocationManager locationManager;
    // current centered lat and long
    private double mapLatitude, mapLongitude;
    // bounding box for rides
    private LatLngBounds searchBounds;
    // rides on display, found by query
    List<Ride> displayedRides = new ArrayList<>();
    // corresponding GoogleMap markers
    List<Marker> displayedMarkers = new ArrayList<>();
    // selection made
    Ride selectedRide;
    // filters the rides
    RideFilterFragment rideFilterFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passenger_layout);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            mapLatitude = location.getLatitude();
            mapLongitude = location.getLongitude();
        } catch (SecurityException e) {
            Log.d("SchedulerActivity", "Don't have permission");
            // set lat, long to ILC
            mapLatitude = 42.391180;
            mapLongitude = -72.525717;
        }

        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewswitcher);
        // set LatLngBounds to +-0.5 degrees on each side
        searchBounds = new LatLngBounds(new LatLng(mapLatitude - 0.5, mapLongitude - 0.5),
                new LatLng(mapLatitude + 0.5, mapLongitude + 0.5));
        displayedRides = RequestUtil.queryRides(mapLatitude, mapLongitude, 0.5f);
    }

    @Override  // called when filter is updated
    public void onFilterUpdated(RideFilterFragment rideFilterFragment, String sourceAddress,
                                String destAddress, float precisionMi) {
        Log.d("SchedulerActivity", "Received Filter Params " + sourceAddress + ", " + destAddress +
                ", " + precisionMi);
        displayedRides = RequestUtil.queryRides(sourceAddress, destAddress, precisionMi);
        // sets map points to filtered rides
        updateMarkers();
    }

    @Override  // user selects ride from RecyclerView of options
    public void onChooseRide(int index, Ride chosen) {
        Log.d("SchedulerActivity", "Ride Chosen " + chosen);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d("SchedulerActivity", "Map is ready");
        googleMap = map;

        updateMarkers();
    }

    // sets googleMap markers to leaving location of each ride
    public void updateMarkers() {
        // remove current locations
        googleMap.clear();
        displayedMarkers.clear();

        // add marker at current location
        LatLng curr_location = new LatLng(mapLatitude, mapLongitude);
        googleMap.addMarker(new MarkerOptions().position(curr_location).title("Current Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curr_location, 14.0f));

        LatLng location;
        Marker marker;
        for (Ride r : displayedRides) {
            location = new LatLng(r.destLat, r.destLong);
            // add marker to map and displayedMarkers
            marker = googleMap.addMarker(new MarkerOptions().position(location).title("")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            displayedMarkers.add(marker);
        }
    }

    @Override  // handles user clicking a marker-- pop up option to take given ride
    public boolean onMarkerClick(Marker marker) {
        // get selected Marker and corresponding ride
        int index = displayedMarkers.indexOf(marker);
        selectedRide = displayedRides.get(index);
        Log.d("SchedulerActivity", "Selected " + selectedRide);
        // pop up dialog displaying info + asking to confirm
        ChooseRideDialogFragment d = ChooseRideDialogFragment.newInstance(selectedRide.getSourceAddress(),
                selectedRide.getDestAddress(), selectedRide.getFormattedDepartTime(), selectedRide.getDriverName());
        return false;
    }

    @Override
    public void onConfirm(ChooseRideDialogFragment dialogFragment) {
        dialogFragment.dismiss();
        // we use the selected ride, which was last clicked
        Log.d("SchedulerActivity", "Selected Ride " + selectedRide);
    }
}
