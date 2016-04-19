package com.numan1617.tfltubedepartures.feature.stopdetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.numan1617.tfltubedepartures.R;
import com.numan1617.tfltubedepartures.network.model.Departure;

class DepartureViewHolder extends RecyclerView.ViewHolder {
  private static final long ONE_MINUTE = 60;

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

    final String arrivalText = getArrivalText(departure.timeToStation());

    arrivalTime.setText(arrivalText);
  }

  @NonNull private String getArrivalText(final long arrivalTime) {
    final String arrivalText;
    if (arrivalTime < 30) {
      arrivalText = this.arrivalTime.getContext().getString(R.string.departure_due_now);
    } else if (arrivalTime <= ONE_MINUTE) {
      arrivalText =
          this.arrivalTime.getContext().getString(R.string.departure_due_less_than_a_minute);
    } else {
      arrivalText = this.arrivalTime.getContext()
          .getString(R.string.departure_due_in_minutes,
              Math.round(arrivalTime / (float) ONE_MINUTE));
    }
    return arrivalText;
  }
}
