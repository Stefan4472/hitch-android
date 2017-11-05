package hitch_frontend.hitch.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import hitch_frontend.hitch.R;
import hitch_frontend.hitch.fragment.RideFilterFragment;
import hitch_frontend.hitch.helper.RequestUtil;
import hitch_frontend.hitch.helper.Ride;

/**
 * Main App
 */

public class SchedulerActivity extends FragmentActivity implements OnMapReadyCallback,
        RideFilterFragment.OnFilterUpdatedListener {

    private MapView mapView;
    private GoogleMap googleMap;
    private RecyclerView availableRides;
    private LinearLayoutManager displayManager;
    private ViewSwitcher viewSwitcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passenger_layout);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewswitcher);

        availableRides = (RecyclerView) findViewById(R.id.ride_display);
        displayManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        availableRides.setLayoutManager(displayManager);
    }

    @Override  // called when filter is updated
    public void onFilterUpdated(RideFilterFragment rideFilterFragment, String sourceAddress,
                                String destAddress, float precisionMi) {
        Log.d("SchedulerActivity", "Received Filter Params " + sourceAddress + ", " + destAddress +
                ", " + precisionMi);
        List<Ride> filtered_rides = RequestUtil.queryRides(sourceAddress, destAddress, precisionMi);
        // switch to showing the rides
        viewSwitcher.showNext();
        
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        Log.d("SchedulerActivity", "Map is ready");
        googleMap = map;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
