package hitch_frontend.hitch.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import hitch_frontend.hitch.R;

/**
 * Fragment that lets user filter rides
 */

public class RideFilterFragment extends Fragment {

    private Button searchButton;
    private EditText pickUpInput, dropOffInput, precisionInput;

    // listener to this fragment
    private OnFilterUpdatedListener mListener;

    // container activity must implement this interface
    public interface OnFilterUpdatedListener {
        // sends reference to the fragment and values selected by
        void onFilterUpdated(RideFilterFragment rideFilterFragment, String sourceAddress,
                             String destAddress, float precisionMi);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        pickUpInput = (EditText) view.findViewById(R.id.pick_up_entry);
        dropOffInput = (EditText) view.findViewById(R.id.drop_off_entry);
        precisionInput = (EditText) view.findViewById(R.id.precision_entry);

        searchButton = (Button) view.findViewById(R.id.update_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterUpdated();
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
