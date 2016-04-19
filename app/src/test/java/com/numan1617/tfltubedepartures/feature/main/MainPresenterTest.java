package com.numan1617.tfltubedepartures.feature.main;

import com.numan1617.tfltubedepartures.GoogleApiClientWrapper;
import com.numan1617.tfltubedepartures.base.BasePresenterTest;
import com.numan1617.tfltubedepartures.network.TflService;
import com.numan1617.tfltubedepartures.network.model.StopPoint;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.PublishSubject;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest extends BasePresenterTest<MainPresenter, MainPresenter.View> {
  private final TestScheduler ioTestScheduler = new TestScheduler();
  private final PublishSubject<List<StopPoint>> stopPointSubject = PublishSubject.create();
  private final PublishSubject<StopPoint> stopSelectedSubject = PublishSubject.create();

  @Mock TflService tflService;
  @Mock GoogleApiClientWrapper googleApiClientWrapper;
  @Mock LocationRequester locationRequester;

  @Before public void before() {
    super.before();
    when(tflService.stopPoint(anyDouble(), anyDouble())).thenReturn(stopPointSubject);
    doAnswer(invocation -> {
      ((GoogleApiClientWrapper.ConnectionCallback) invocation.getArguments()[0]).onConnected();
      return null;
    }).when(googleApiClientWrapper)
        .registerConnectionCallbacks(any(GoogleApiClientWrapper.ConnectionCallback.class));
  }

  @Override protected MainPresenter createPresenter() {
    return new MainPresenter(Schedulers.immediate(), ioTestScheduler, tflService,
        googleApiClientWrapper, locationRequester);
  }

  @Override protected MainPresenter.View createView() {
    final MainPresenter.View view = mock(MainPresenter.View.class);
    when(view.stopPointSelected()).thenReturn(stopSelectedSubject);
    return view;
  }

  @Test public void onRequestLocation_connectClient() {
    when(view.hasLocationPermission()).thenReturn(true);
    presenterOnViewAttached();

    verify(googleApiClientWrapper).connect();
  }

  @Test public void onViewDetached_disconnectClient() {
    presenterOnViewDetached();

    verify(googleApiClientWrapper).disconnect();
  }

  @Test public void onStopsRetrieved_setStopsOnView() {
    when(view.hasLocationPermission()).thenReturn(true);
    when(locationRequester.lastLocation(any())).thenReturn(mock(LatLng.class));
    presenterOnViewAttached();

    ioTestScheduler.triggerActions();
    final List<StopPoint> stops = Arrays.asList(null, null);
    stopPointSubject.onNext(stops);

    verify(view).setStopPoints(stops);
  }

  @Test public void afterRetrieveStops_hideLoadingView() {
    when(view.hasLocationPermission()).thenReturn(true);
    when(locationRequester.lastLocation(any())).thenReturn(mock(LatLng.class));
    presenterOnViewAttached();

    ioTestScheduler.triggerActions();
    final List<StopPoint> stops = Arrays.asList(null, null);
    stopPointSubject.onNext(stops);

    verify(view).showLoadingView(false);
  }

  @Test public void onZeroStopsReturned_showNoStopsView() {
    when(view.hasLocationPermission()).thenReturn(true);
    when(locationRequester.lastLocation(any())).thenReturn(mock(LatLng.class));
    presenterOnViewAttached();

    ioTestScheduler.triggerActions();
    stopPointSubject.onNext(Collections.emptyList());

    verify(view).showNoStopsView(true);
  }

  @Test public void onStopSelected_displayStopDetails() {
    presenterOnViewAttached();

    ioTestScheduler.triggerActions();
    stopSelectedSubject.onNext(null);

    verify(view).displayStopDetails(null);
  }

  @Test public void noLocationPermission_requestLocationPermission() {
    when(view.hasLocationPermission()).thenReturn(false);
    when(locationRequester.lastLocation(any())).thenReturn(mock(LatLng.class));
    presenterOnViewAttached();

    verify(view).requestLocationPermission();
  }

  @Test public void hasLocationPermission_getLocation() {
    when(view.hasLocationPermission()).thenReturn(true);
    when(locationRequester.lastLocation(any())).thenReturn(mock(LatLng.class));

    presenterOnViewAttached();

    verify(locationRequester).lastLocation(googleApiClientWrapper);
  }

  @Test public void beforeRetrieveStops_showLoadingView() {
    when(view.hasLocationPermission()).thenReturn(true);
    when(locationRequester.lastLocation(any())).thenReturn(mock(LatLng.class));
    presenterOnViewAttached();

    verify(view).showLoadingView(true);
  }

  @Test public void onViewAttached_retrieveNearbyStops() {
    final LatLng latLng = mock(LatLng.class);
    when(latLng.latitude()).thenReturn(1.);
    when(latLng.longitude()).thenReturn(1.);
    when(view.hasLocationPermission()).thenReturn(true);
    when(locationRequester.lastLocation(any())).thenReturn(latLng);
    presenterOnViewAttached();

    verify(tflService).stopPoint(1., 1.);
  }

  @Test public void permissionGranted_requestLocation() {
    when(view.hasLocationPermission()).thenReturn(false);
    presenterOnViewAttached();

    final LatLng latLng = mock(LatLng.class);
    when(latLng.latitude()).thenReturn(1.);
    when(latLng.longitude()).thenReturn(1.);
    when(locationRequester.lastLocation(any())).thenReturn(latLng);

    presenter.locationPermissionGranted();

    verify(tflService).stopPoint(1., 1.);
  }
}