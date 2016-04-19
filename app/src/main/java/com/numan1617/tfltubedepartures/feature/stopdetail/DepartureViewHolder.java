package com.numan1617.tfltubedepartures.feature.stopdetail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.numan1617.tfltubedepartures.R;
import com.numan1617.tfltubedepartures.network.model.Departure;

class DepartureViewHolder extends RecyclerView.ViewHolder {
  @Bind(R.id.text_view_line_destination) TextView lineDestination;
  @Bind(R.id.text_view_arrival_time) TextView arrivalTime;

  DepartureViewHolder(final View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  public void bindData(final Departure departure) {
    lineDestination.setText(lineDestination.getContext()
        .getString(R.string.departure_line_destination, departure.lineName(),
            departure.destinationName()));
    arrivalTime.setText(String.valueOf(departure.timeToStation()));
  }
}
