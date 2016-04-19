package com.numan1617.tfltubedepartures.feature.stopdetail;

import com.numan1617.tfltubedepartures.base.BasePresenterTest;
import com.numan1617.tfltubedepartures.network.TflService;
import com.numan1617.tfltubedepartures.network.model.Departure;
import com.numan1617.tfltubedepartures.network.model.StopPoint;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.mockito.Mock;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StopDetailPresenterTest
    extends BasePresenterTest<StopDetailPresenter, StopDetailPresenter.View> {
  private final TestScheduler ioTestScheduler = new TestScheduler();
  @Mock TflService tflService;

  @Override protected StopDetailPresenter createPresenter() {
    return new StopDetailPresenter(Schedulers.immediate(), ioTestScheduler, tflService);
  }

  @Override protected StopDetailPresenter.View createView() {
    return mock(StopDetailPresenter.View.class);
  }

  @Test public void setStopPoint_nameSetOnView() {
    presenterOnViewAttached();
    final StopPoint stopPoint = mock(StopPoint.class);
    final String stopName = "Stop Name";
    when(stopPoint.commonName()).thenReturn(stopName);

    presenter.setStopPoint(stopPoint);
    verify(view).setStopName(stopName);
  }

  @Test public void setStopPoint_loadDepartures() {
    presenterOnViewAttached();
    final StopPoint stopPoint = mock(StopPoint.class);
    final String stopId = "StopId";
    when(stopPoint.id()).thenReturn(stopId);

    presenter.setStopPoint(stopPoint);

    ioTestScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
    verify(tflService).departures(stopId);
  }

  @Test public void setStopPoint_refreshDeparturesEvery30Seconds() {
    presenterOnViewAttached();
    final StopPoint stopPoint = mock(StopPoint.class);
    final String stopId = "StopId";
    when(stopPoint.id()).thenReturn(stopId);

    when(tflService.departures(any())).thenReturn(Observable.just(Arrays.asList(null, null)));

    presenter.setStopPoint(stopPoint);

    ioTestScheduler.advanceTimeBy(31, TimeUnit.SECONDS);
    verify(tflService, times(2)).departures(stopId);
  }

  @Test public void onReceiveDepartures_sortByTimeToStop() {
    presenterOnViewAttached();

    final StopPoint stopPoint = mock(StopPoint.class);
    final String stopId = "StopId";
    when(stopPoint.id()).thenReturn(stopId);

    final Departure departure1 = mock(Departure.class);
    when(departure1.timeToStation()).thenReturn(1000L);

    final Departure departure2 = mock(Departure.class);
    when(departure2.timeToStation()).thenReturn(10000L);

    final Departure departure3 = mock(Departure.class);
    when(departure3.timeToStation()).thenReturn(10L);

    final List<Departure> departures = Arrays.asList(departure1, departure2, departure3);
    when(tflService.departures(any())).thenReturn(Observable.just(departures));


    final List<Departure> sortedDepartures = Arrays.asList(departure3, departure1, departure2);

    presenter.setStopPoint(stopPoint);

    ioTestScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
    verify(view).setDepartures(sortedDepartures);
  }

  @Test public void onReceiveDepartures_limitDeparturesTo3() {
    presenterOnViewAttached();

    final StopPoint stopPoint = mock(StopPoint.class);
    final String stopId = "StopId";
    when(stopPoint.id()).thenReturn(stopId);

    final Departure departure = mock(Departure.class);
    when(departure.timeToStation()).thenReturn(1000L);

    final List<Departure> departures = Arrays.asList(departure, departure, departure, departure);
    when(tflService.departures(any())).thenReturn(Observable.just(departures));


    final List<Departure> sortedDepartures = Arrays.asList(departure, departure, departure);

    presenter.setStopPoint(stopPoint);

    ioTestScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
    verify(view).setDepartures(sortedDepartures);
  }

  @Test public void everySecond_updateRefreshTime() {
    presenterOnViewAttached();

    final StopPoint stopPoint = mock(StopPoint.class);
    final String stopId = "StopId";
    when(stopPoint.id()).thenReturn(stopId);

    final Departure departure = mock(Departure.class);
    when(departure.timeToStation()).thenReturn(1000L);

    final List<Departure> departures = Arrays.asList(departure, departure, departure, departure);
    when(tflService.departures(any())).thenReturn(Observable.just(departures));

    presenter.setStopPoint(stopPoint);

    ioTestScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
    verify(view).setNextRefreshTime(30);
    ioTestScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
    verify(view).setNextRefreshTime(29);
    ioTestScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
    verify(view).setNextRefreshTime(28);
  }
}