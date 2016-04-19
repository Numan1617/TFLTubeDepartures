package com.numan1617.tfltubedepartures.feature.stopdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import butterknife.Bind;
import com.numan1617.tfltubedepartures.R;
import com.numan1617.tfltubedepartures.base.BaseActivity;
import com.numan1617.tfltubedepartures.base.BasePresenter;
import com.numan1617.tfltubedepartures.network.model.Departure;
import com.numan1617.tfltubedepartures.network.model.StopPoint;
import java.util.List;

import static com.numan1617.tfltubedepartures.feature.stopdetail.StopDetailModule.stopDetailPresenter;

public class StopDetailActivity extends BaseActivity<StopDetailPresenter.View>
    implements StopDetailPresenter.View {
  private static final String EXTRA_STOP_POINT = "EXTRA_STOP_POINT";
  private final StopDetailPresenter presenter;

  private DepartureAdapter departureAdapter;

  @Bind(R.id.text_view_stop_name) TextView stopNameText;
  @Bind(R.id.recycler_view_departure_list) RecyclerView departureList;
  @Bind(R.id.text_view_refresh_in) TextView refreshInText;

  public static Intent getStartIntent(@NonNull final Context context,
      @NonNull final StopPoint stopPoint) {
    final Intent intent = new Intent(context, StopDetailActivity.class);
    intent.putExtra(EXTRA_STOP_POINT, stopPoint);
    return intent;
  }

  @SuppressWarnings("unused") public StopDetailActivity() {
    this(stopDetailPresenter());
  }

  public StopDetailActivity(@NonNull final StopDetailPresenter stopDetailPresenter) {
    this.presenter = stopDetailPresenter;
  }

  @Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    departureAdapter = new DepartureAdapter();
    departureList.setAdapter(departureAdapter);
    departureList.setLayoutManager(new LinearLayoutManager(this));

    getPresenter().onViewAttached(this);

    setupViewsWithLoadedIntent();
  }

  @Override protected int getLayoutId() {
    return R.layout.activity_stop_detail;
  }

  @NonNull @Override protected BasePresenter<StopDetailPresenter.View> getPresenter() {
    return presenter;
  }

  @NonNull @Override protected StopDetailPresenter.View getPresenterView() {
    return this;
  }

  private void setupViewsWithLoadedIntent() {
    final Intent loadedIntent = getIntent();
    final StopPoint stopPoint = loadedIntent.getParcelableExtra(EXTRA_STOP_POINT);
    System.out.println(stopPoint);
    presenter.setStopPoint(stopPoint);
  }

  @Override public void setStopName(@NonNull final String stopName) {
    stopNameText.setText(stopName);
  }

  @Override public void setDepartures(@NonNull final List<Departure> departures) {
    departureAdapter.setData(departures);
  }

  @Override public void setNextRefreshTime(final int nextRefreshInSeconds) {
    refreshInText.setText(getString(R.string.departure_refresh_in, nextRefreshInSeconds));
  }

  @Override public void setFacilities(@NonNull final List<String> facilities) {

  }
}
