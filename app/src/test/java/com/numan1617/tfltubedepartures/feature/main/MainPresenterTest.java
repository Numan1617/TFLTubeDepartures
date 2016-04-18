package com.numan1617.tfltubedepartures.feature.main;

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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest extends BasePresenterTest<MainPresenter, MainPresenter.View> {
  private final TestScheduler ioTestScheduler = new TestScheduler();
  private final PublishSubject<List<StopPoint>> stopPointSubject = PublishSubject.create();
  private final PublishSubject<StopPoint> stopSelectedSubject = PublishSubject.create();

  @Mock TflService tflService;

  @Before public void before() {
    super.before();
    when(tflService.stopPoint()).thenReturn(stopPointSubject);
  }

  @Override protected MainPresenter createPresenter() {
    return new MainPresenter(Schedulers.immediate(), ioTestScheduler, tflService);
  }

  @Override protected MainPresenter.View createView() {
    final MainPresenter.View view = mock(MainPresenter.View.class);
    when(view.stopPointSelected()).thenReturn(stopSelectedSubject);
    return view;
  }

  @Test public void beforeRetrieveStops_showLoadingView() {
    presenterOnViewAttached();

    verify(view).showLoadingView(true);
  }

  @Test public void onViewAttached_retrieveNearbyStops() {
    presenterOnViewAttached();

    verify(tflService).stopPoint();
  }

  @Test public void onStopsRetrieved_setStopsOnView() {
    presenterOnViewAttached();

    ioTestScheduler.triggerActions();
    final List<StopPoint> stops = Arrays.asList(null, null);
    stopPointSubject.onNext(stops);

    verify(view).setStopPoints(stops);
  }

  @Test public void afterRetrieveStops_hideLoadingView() {
    presenterOnViewAttached();

    ioTestScheduler.triggerActions();
    final List<StopPoint> stops = Arrays.asList(null, null);
    stopPointSubject.onNext(stops);

    verify(view).showLoadingView(false);
  }

  @Test public void onZeroStopsReturned_showNoStopsView() {
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
}