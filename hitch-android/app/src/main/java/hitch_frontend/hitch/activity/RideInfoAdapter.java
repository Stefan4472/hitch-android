package hitch_frontend.hitch.activity;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import hitch_frontend.hitch.R;

/**
 * Adapter to visually display a list of RideInfo objects in a RecyclerView. Communicates via the
 * LogEntryListener interface, which fires events for relevant actions. Background coloring (i.e.
 * different color for selected and deselected) should be managed by an outside class and use
 * LogEntryViewHolder.setClicked(). Each LogEntry instance has a trash-icon button which fires
 * the onDeleteLogEntry callback. The actual delete action must also be handled by an outside class.
 */

public class RideInfoAdapter extends RecyclerView.Adapter<RideInfoAdapter.RideInfoViewHolder> {

    // list of RideInfo objects to be displayed in the RecyclerView
    private List<RideInfo> displayedRides;
    // used to inflate views for LogEntries
    private LayoutInflater inflater;
    private RideInfoListener mListener;

    // callback interface. Each callback passes the index of the selected LogEntry in the Adapter's
    // internal list, as well as the actual LogEntry instance.
    public interface RideInfoListener {
        // fired when user chooses a ride, returns index+RideInfo object
        void onChooseRide(int index, RideInfo chosen);
    }

    // todo: could this be dangerous? make copy of logs?
    public RideInfoAdapter(Context context, List<RideInfo> ridesToDisplay, RideInfoListener listener) {
        inflater = LayoutInflater.from(context);
        this.displayedRides = ridesToDisplay;
        mListener = listener;
    }

    @Override // inflates a ViewHolder so it can be populated in onBind
    public RideInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ride_entry_view = inflater.inflate(R.layout.rideinfo_layout, parent, false);
        return new RideInfoViewHolder(ride_entry_view);
    }

    @Override // binds the data from the RideInfo at given position to the holder that contains the view itself
    public void onBindViewHolder(final RideInfoViewHolder holder, final int position) {
        RideInfo bound_entry = displayedRides.get(position);
        holder.sourceInfoText.setText("From " + bound_entry.getSourceAddress());
        holder.destInfoText.setText("To " + bound_entry.getDestAddress());
        holder.otherInfoText.setText("With " + bound_entry.getDriverName() + " at " + bound_entry.getFormattedDepartTime());
        holder.chooseRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onChooseRide(position, displayedRides.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return displayedRides.size();
    }

    public RideInfo get(int index) {
        return displayedRides.get(index);
    }

    class RideInfoViewHolder extends RecyclerView.ViewHolder {

        private TextView sourceInfoText, destInfoText, otherInfoText;
        private Button chooseRideBtn;

        RideInfoViewHolder(View itemView) {
            super(itemView);
            sourceInfoText = (TextView) itemView.findViewById(R.id.source_address_text);
            destInfoText = (TextView) itemView.findViewById(R.id.dest_address_text);
            otherInfoText = (TextView) itemView.findViewById(R.id.details_text);
            chooseRideBtn = (Button) itemView.findViewById(R.id.choose_ride_btn);
        }
    }
}