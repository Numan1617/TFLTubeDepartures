package com.numan1617.tfltubedepartures.feature.main;

import android.support.annotation.NonNull;
import com.numan1617.tfltubedepartures.GoogleApiClientWrapper;
import com.numan1617.tfltubedepartures.base.BasePresenter;
import com.numan1617.tfltubedepartures.base.PresenterView;
import com.numan1617.tfltubedepartures.network.TflService;
import com.numan1617.tfltubedepartures.network.model.StopPoint;
import java.util.List;
import rx.Observable;
import rx.Scheduler;

public class MainPresenter extends BasePresenter<MainPresenter.View> {
  static final double DEFAULT_LATITUDE = 51.5033;
  static final double DEFAULT_LONGITUDE = 0.1195;

  private final TflService tflService;
  private final Scheduler uiScheduler;
  private final Scheduler ioScheduler;
  private final GoogleApiClientWrapper googleApiClientWrapper;
  private final LocationRequester locationRequester;

  private View view;

  public MainPresenter(@NonNull final Scheduler uiScheduler, @NonNull final Scheduler ioScheduler,
      @NonNull final TflService tflService,
      @NonNull final GoogleApiClientWrapper googleApiClientWrapper,
      @NonNull final LocationRequester locationRequester) {
    this.uiScheduler = uiScheduler;
    this.ioScheduler = ioScheduler;
    this.tflService = tflService;
    this.googleApiClientWrapper = googleApiClientWrapper;
    this.locationRequester = locationRequester;
  }

  @Override public void onViewAttached(@NonNull final View view) {
    super.onViewAttached(view);
    this.view = view;

    if (view.hasLocationPermission()) {
      getLocationAndUpdateStops();
    } else {
      view.requestLocationPermission();
    }

    unsubscribeOnViewDetach(view.stopPointSelected()
        .observeOn(uiScheduler)
        .subscribeOn(ioScheduler)
        .subscribe(view::displayStopDetails));
  }

  @Override public void onViewDetached() {
    super.onViewDetached();
    googleApiClientWrapper.disconnect();
  }

  public void locationPermissionGranted() {
    getLocationAndUpdateStops();
  }

  public void locationPermissionDenied() {

  }

  private void getLocationAndUpdateStops() {
    googleApiClientWrapper.registerConnectionCallbacks(() -> {
      LatLng latLng = locationRequester.lastLocation(googleApiClientWrapper);

      if (latLng == null || !isInLondon(latLng)) {
        latLng = LatLng.create(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
      }
      view.showLoadingView(true);

      unsubscribeOnViewDetach(tflService.stopPoint(latLng.latitude(), latLng.longitude())
          .observeOn(uiScheduler)
          .subscribeOn(ioScheduler)
          .subscribe(stopPoints -> {
            view.showLoadingView(false);
            if (stopPoints.size() == 0) {
              view.showNoStopsView(true);
            } else {
              view.setStopPoints(stopPoints);
            }
          }, Throwable::printStackTrace));
    });
    googleApiClientWrapper.connect();
  }

  private boolean isInLondon(@NonNull final LatLng latLng) {
    return (latLng.latitude() > 51.288923
        && latLng.latitude() < 51.706130
        && latLng.longitude() > -0.524597
        && latLng.longitude() < 0.280151);
  }

  interface View extends PresenterView {
    @NonNull Observable<StopPoint> stopPointSelected();

    boolean hasLocationPermission();

    void requestLocationPermission();

    void setStopPoints(@NonNull List<StopPoint> stopPoints);

    void showLoadingView(boolean visible);

    void showNoStopsView(boolean visible);

    void displayStopDetails(StopPoint stopPoint);
  }
}
