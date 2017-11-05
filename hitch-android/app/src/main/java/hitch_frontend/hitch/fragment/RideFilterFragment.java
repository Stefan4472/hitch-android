package hitch_frontend.hitch.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import hitch_frontend.hitch.R;

/**
 * Fragment that lets user filter rides
 */

public class RideFilterFragment extends Fragment {

    private Button searchButton;
    private EditText precisionInput;
    private AutoCompleteTextView pickUpInput, dropOffInput;
    // bounds of locations within range of search
    private LatLngBounds searchBounds;
    // listener to this fragment
    private OnFilterUpdatedListener mListener;
    // used to get Geocoding data
    private GeoDataClient geoDataClient;
    // container activity must implement this interface
    public interface OnFilterUpdatedListener {
        // sends reference to the fragment and values selected by
        void onFilterUpdated(RideFilterFragment rideFilterFragment, String sourceAddress,
                             String destAddress, float precisionMi);
    }

    private static final String CUR_LAT_KEY = "CUR_LAT", CUR_LONG_KEY = "CUR_LONG",
        BOUNDING_SIZE_KEY = "BOUNDING_SIZE";

    // returns a new instance of the fragment with bundle that has the values given by toEdit.
    // Will populate the dialog with data from toEdit.
    public static RideFilterFragment newInstance(LatLng currentLocation, double boundingSize) {
        RideFilterFragment dialog = new RideFilterFragment();
        // populate the bundle with the given fields
        Bundle bundle = new Bundle();
        bundle.putDouble(CUR_LAT_KEY, currentLocation.latitude);
        bundle.putDouble(CUR_LONG_KEY, currentLocation.longitude);
        bundle.putDouble(BOUNDING_SIZE_KEY, boundingSize);
        // set args and return dialog
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override // ensures activity implements OnFilterUpdatedListener
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFilterUpdatedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFilterUpdatedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ridefilter_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // retrieve arguments from bundle (can't use savedInstanceState)
        Bundle bundle = getArguments();

        // get handle to GeoData database
        geoDataClient = Places.getGeoDataClient(this.getActivity(), null);

        // set search bounds for autocomplete
//        double cur_lat = bundle.getDouble(CUR_LAT_KEY), cur_long = bundle.getDouble(CUR_LONG_KEY);
//        double search_bounds = bundle.getDouble(BOUNDING_SIZE_KEY);
        double cur_lat = 42.391180, cur_long = -72.525717, search_bounds = 0.5;
        searchBounds = new LatLngBounds(new LatLng(cur_lat - search_bounds, cur_long - search_bounds),
                new LatLng(cur_lat + search_bounds, cur_long + search_bounds));

        View view = getView();

        pickUpInput = (AutoCompleteTextView) view.findViewById(R.id.pick_up_entry);
        dropOffInput = (AutoCompleteTextView) view.findViewById(R.id.drop_off_entry);
        precisionInput = (EditText) view.findViewById(R.id.precision_entry);

        searchButton = (Button) view.findViewById(R.id.update_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterUpdated();
            }
        });

        // text changed listener for auto-complete
        pickUpInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 4) { // only start auto-complete after 4 chars
                    Log.d("RideFilterFragment", "Looking Up " + s.toString());
                    // get autocomplete results from GeoData
                    geoDataClient.getAutocompletePredictions(s.toString(), searchBounds,
                                    AutocompleteFilter.TYPE_FILTER_NONE).addOnCompleteListener(new OnCompleteListener<AutocompletePredictionBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<AutocompletePredictionBufferResponse> task) {
                            Log.d("RideFilterFragment", "" + task.isSuccessful());
                            // create adapter from autocomplete results
                            List<String> result_strings = new ArrayList<String>();
                            for (int i = 0; i < task.getResult().getCount(); i++) {
                                result_strings.add(task.getResult().get(i).getPrimaryText(null).toString());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RideFilterFragment.this.getActivity(),
                                    android.R.layout.simple_dropdown_item_1line, result_strings);
                            pickUpInput.setAdapter(adapter);
                        }
                    });
                }
            }
        });
    }

    // handles user clicking button to update the filter. Takes data from UI fields and uses it
    // to create a QueryBuilder
    public void onFilterUpdated() {
        String source = pickUpInput.getText().toString();
        String dest = dropOffInput.getText().toString();
        float precision = Float.parseFloat(precisionInput.getText().toString());
        mListener.onFilterUpdated(this, source, dest, precision);
    }
}
