package hitch_frontend.hitch.activity;

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

    private Spinner departArriveSpinner, am_pmSpinner;
    private Button searchButton;
    private EditText pickUpInput, dropOffInput, precisionInput;
    private long time;

    // listener to this fragment
    private OnFilterUpdatedListener mListener;

    // container activity must implement this interface
    public interface OnFilterUpdatedListener {
        // sends reference to the fragment and values selected by
        void onFilterUpdated(RideFilterFragment rideFilterFragment, String sourceAddress,
                             String destAddress, float precisionMi, long time, boolean useDepart);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        time = System.currentTimeMillis() + 1000 * 6 * 10; // default: leaving in 10 min
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

        final View view = getView();

        departArriveSpinner = (Spinner) view.findViewById(R.id.depart_arrive_spinner);
        // populate spinner
        List<String> depart_arrive_choices = new ArrayList<>();
        depart_arrive_choices.add("Depart At");
        depart_arrive_choices.add("Arrive By");
        departArriveSpinner.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, depart_arrive_choices));

        am_pmSpinner = (Spinner) view.findViewById(R.id.am_pm_spinner);
        // populate spinner
        List<String> am_pm_choices = new ArrayList<>();
        depart_arrive_choices.add("a.m.");
        depart_arrive_choices.add("p.m.");
        departArriveSpinner.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, am_pm_choices));

        pickUpInput = (EditText) view.findViewById(R.id.pick_up_entry);
        dropOffInput = (EditText) view.findViewById(R.id.drop_off_entry);
        precisionInput = (EditText) view.findViewById(R.id.precision_entry);

        searchButton = (Button) view.findViewById(R.id.update_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterUpdated(view);
            }
        });
    }

    /*/ handles user clicking a button to select a date. Determines which date we are setting
    // (first or second) and pops up a DatePickerFragment
    public void handleDatePickerClicked(View v) throws IllegalArgumentException {
        switch (v.getId()) {
            case R.id.choose_date_1: // pop up dialog to select date1
                DatePickerFragment date_picker = DatePickerFragment.newInstance(date1);
                date_picker.setListener(
                        new DatePickerFragment.DatePickerListener() {
                            @Override
                            public void onDateSelected(DatePickerFragment dialogFragment, Date selectedDate) {
                                date1 = selectedDate;
                                datePicker1.setText(DateUtil.format(date1));
                            }
                        });
                date_picker.show(getFragmentManager(), "DatePicker");
                break;
            case R.id.choose_date_2: // pop up dialog to select date2
                DatePickerFragment date2_picker = DatePickerFragment.newInstance(date2);
                date2_picker.setListener(
                        new DatePickerFragment.DatePickerListener() {
                            @Override
                            public void onDateSelected(DatePickerFragment dialogFragment, Date selectedDate) {
                                date2 = selectedDate;
                                datePicker2.setText(DateUtil.format(date2));
                            }
                        });
                date2_picker.show(getFragmentManager(), "DatePicker");
                break;
            default:
                Log.d("LogfilterFragment", "Unrecognized View");
                throw new IllegalArgumentException();
        }
    }*/

    // handles user clicking button to update the filter. Takes data from UI fields and uses it
    // to create a QueryBuilder
    public void onFilterUpdated(View view) {
        String source = pickUpInput.getText().toString();
        String dest = dropOffInput.getText().toString();
        String depart_arrive_config = departArriveSpinner.getSelectedItem().toString();
        boolean use_depart = depart_arrive_config.equals(getString(R.string.depart_at));
        String am_pm_config = am_pmSpinner.getSelectedItem().toString();
        long time = System.currentTimeMillis() + 1000 * 60 * 10; // todo: let configure time--24 hour??
        float precision = Float.parseFloat(precisionInput.getText().toString());
        mListener.onFilterUpdated(this, source, dest, precision, time, use_depart);
    }
}
