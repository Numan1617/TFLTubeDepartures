package com.numan1617.tfltubedepartures.feature.stopdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.numan1617.tfltubedepartures.R;
import com.numan1617.tfltubedepartures.network.model.StopPoint;

public class StopDetailActivity extends AppCompatActivity {
  private static final String EXTRA_STOP_POINT = "EXTRA_STOP_POINT";

  public static Intent getStartIntent(@NonNull final Context context,
      @NonNull final StopPoint stopPoint) {
    final Intent intent = new Intent(context, StopDetailActivity.class);
    intent.putExtra(EXTRA_STOP_POINT, stopPoint);
    return intent;
  }

  @Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stop_detail);
  }
}
