package hitch_frontend.hitch.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import hitch_frontend.hitch.R;

/**
 * Shows info about a ride and lets user choose.
 */

public class ChooseRideDialogFragment extends DialogFragment {

    // keys used for storing data in bundle
    private static final String SOURCE_ADDRESS_KEY = "SOURCE_ADDRESS",
            DEST_ADDRESS_KEY = "DEST_ADDRESS", TIME_KEY = "TIME", DRIVER_NAME_KEY = "DRIVER_NAME";

    private TextView sourceInfo, destInfo, otherInfo;

    // interface for receiving dialog callbacks
    public interface ChooseRideDialogListener {
        void onConfirm(ChooseRideDialogFragment dialogFragment);
    }

    // listener that receives callbacks
    private ChooseRideDialogListener mListener;

    // returns a new instance of the fragment with bundle that has the values given by toEdit.
    // Will populate the dialog with data from toEdit.
    public static ChooseRideDialogFragment newInstance(String srcAddress, String destAddress,
                                                       String time, String driverName) {
        ChooseRideDialogFragment dialog = new ChooseRideDialogFragment();
        // populate the bundle with the given fields
        Bundle bundle = new Bundle();
        bundle.putString(SOURCE_ADDRESS_KEY, srcAddress);
        bundle.putString(DEST_ADDRESS_KEY, destAddress);
        bundle.putString(TIME_KEY, time);
        bundle.putString(DRIVER_NAME_KEY, driverName);
        // set args and return dialog
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override // ensures activity implements LogDialogListener
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ChooseRideDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ChooseRideDialogListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_ride_layout, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // retrieve arguments from bundle (can't use savedInstanceState)
        Bundle bundle = getArguments();
        String src_address = bundle.getString(SOURCE_ADDRESS_KEY);
        String dest_address = bundle.getString(DEST_ADDRESS_KEY);
        String time = bundle.getString(TIME_KEY);
        String driver = bundle.getString(DRIVER_NAME_KEY);

        sourceInfo = (TextView) view.findViewById(R.id.source_address_text);
        sourceInfo.setText("From " + src_address);

        destInfo = (TextView) view.findViewById(R.id.dest_address_text);
        destInfo.setText("To " + dest_address);

        otherInfo = (TextView) view.findViewById(R.id.details_text);
        otherInfo.setText("With " + driver + " at " + time);

        Button select_button = (Button) view.findViewById(R.id.choose_ride_btn);
        select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onConfirm(ChooseRideDialogFragment.this);
            }
        });
    }
}
