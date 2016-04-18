package com.numan1617.tfltubedepartures.feature.main;

import com.numan1617.tfltubedepartures.base.BasePresenterTest;
import com.numan1617.tfltubedepartures.network.TflService;
import com.numan1617.tfltubedepartures.network.model.StopPoint;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.mockito.Mock;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.PublishSubject;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest extends BasePresenterTest<MainPresenter, MainPresenter.View> {
  private final TestScheduler ioTestScheduler = new TestScheduler();
  private final PublishSubject<List<StopPoint>> stopPointSubject = PublishSubject.create();

  @Mock TflService tflService;

  @Override protected MainPresenter createPresenter() {
    return new MainPresenter(Schedulers.immediate(), ioTestScheduler, tflService);
  }

  @Override protected MainPresenter.View createView() {
    return mock(MainPresenter.View.class);
  }

  @Test public void beforeRetrieveStops_showLoadingView() {
    when(tflService.stopPoint()).thenReturn(Observable.empty());
    presenterOnViewAttached();

    verify(view).showLoadingView(true);
  }

  @Test public void onViewAttached_retrieveNearbyStops() {
    when(tflService.stopPoint()).thenReturn(Observable.empty());
    presenterOnViewAttached();

    verify(tflService).stopPoint();
  }

  @Test public void onStopsRetrieved_setStopsOnView() {
    when(tflService.stopPoint()).thenReturn(stopPointSubject);
    presenterOnViewAttached();

    ioTestScheduler.triggerActions();
    final List<StopPoint> stops = Arrays.asList(null, null);
    stopPointSubject.onNext(stops);

    verify(view).setStopPoints(stops);
  }

  @Test public void afterRetrieveStops_hideLoadingView() {
    when(tflService.stopPoint()).thenReturn(stopPointSubject);
    presenterOnViewAttached();

    ioTestScheduler.triggerActions();
    final List<StopPoint> stops = Arrays.asList(null, null);
    stopPointSubject.onNext(stops);

    verify(view).showLoadingView(false);
  }

  @Test public void onZeroStopsReturned_showNoStopsView() {
    when(tflService.stopPoint()).thenReturn(this.stopPointSubject);
    presenterOnViewAttached();

    ioTestScheduler.triggerActions();
    stopPointSubject.onNext(Collections.emptyList());

    verify(view).showNoStopsView(true);
  }
}