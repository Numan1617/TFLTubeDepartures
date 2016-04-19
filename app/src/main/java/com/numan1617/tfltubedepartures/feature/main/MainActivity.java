package com.numan1617.tfltubedepartures.feature.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Bind;
import com.numan1617.tfltubedepartures.R;
import com.numan1617.tfltubedepartures.base.BaseActivity;
import com.numan1617.tfltubedepartures.base.BasePresenter;
import com.numan1617.tfltubedepartures.feature.stopdetail.StopDetailActivity;
import com.numan1617.tfltubedepartures.network.model.StopPoint;
import java.util.List;
import rx.Observable;

import static com.numan1617.tfltubedepartures.feature.main.MainModule.mainPresenter;

public class MainActivity extends BaseActivity<MainPresenter.View> implements MainPresenter.View {
  private static final int REQUEST_CODE_REQUEST_LOCATION_PERMISSION = 1;

  private final MainPresenter presenter;

  private StopListAdapter stopListAdapter;

  @Bind(R.id.recycler_view_stops_list) RecyclerView stopsList;
  @Bind(R.id.progress_bar_loading) View loadingIndicator;
  @Bind(R.id.text_view_no_stops) View noStops;

  @SuppressWarnings("unused") public MainActivity() {
    this(mainPresenter());
  }

  public MainActivity(@NonNull final MainPresenter mainPresenter) {
    this.presenter = mainPresenter;
  }

  @Override protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setupStopList();

    getPresenter().onViewAttached(this);
  }

  private void setupStopList() {
    stopsList.setLayoutManager(new LinearLayoutManager(this));
    stopListAdapter = new StopListAdapter();
    stopsList.setAdapter(stopListAdapter);
  }

  @Override protected int getLayoutId() {
    return R.layout.activity_main;
  }

  @NonNull @Override protected BasePresenter<MainPresenter.View> getPresenter() {
    return presenter;
  }

  @NonNull @Override protected MainPresenter.View getPresenterView() {
    return this;
  }

  @Override
  public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions,
      @NonNull final int[] grantResults) {
    switch (requestCode) {
      case REQUEST_CODE_REQUEST_LOCATION_PERMISSION:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          presenter.locationPermissionGranted();
        } else {
          presenter.locationPermissionDenied();
        }
    }
  }

  @NonNull @Override public Observable<StopPoint> stopPointSelected() {
    return stopListAdapter.stopPointSelected();
  }

  @Override public boolean hasLocationPermission() {
    return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        == PackageManager.PERMISSION_GRANTED;
  }

  @Override public void requestLocationPermission() {
    ActivityCompat.requestPermissions(this,
        new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
        REQUEST_CODE_REQUEST_LOCATION_PERMISSION);
  }

  @Override public void setStopPoints(@NonNull final List<StopPoint> stopPoints) {
    stopListAdapter.setData(stopPoints);
  }

  @Override public void showLoadingView(final boolean visible) {
    loadingIndicator.setVisibility(visible ? View.VISIBLE : View.GONE);
  }

  @Override public void showNoStopsView(final boolean visible) {
    noStops.setVisibility(visible ? View.VISIBLE : View.GONE);
  }

  @Override public void displayStopDetails(final StopPoint stopPoint) {
    final Intent stopDetailActivity = StopDetailActivity.getStartIntent(this, stopPoint);
    startActivity(stopDetailActivity);
  }
}
