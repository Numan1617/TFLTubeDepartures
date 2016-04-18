package com.numan1617.tfltubedepartures.feature.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.numan1617.tfltubedepartures.R;
import com.numan1617.tfltubedepartures.network.model.StopPoint;

class StopPointViewHolder extends RecyclerView.ViewHolder {
  @Bind(R.id.text_view_stop_name) TextView stopName;
  private StopPoint stopPoint;

  public StopPointViewHolder(final View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  public void bindData(final StopPoint data) {
    this.stopPoint = data;

    stopName.setText(data.commonName());
  }

  public StopPoint getStopPoint() {
    return stopPoint;
  }
}
