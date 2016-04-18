package com.numan1617.tfltubedepartures.feature.main;

import com.numan1617.tfltubedepartures.base.BasePresenterTest;
import com.numan1617.tfltubedepartures.network.TflService;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.mockito.Mock;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest extends BasePresenterTest<MainPresenter, MainPresenter.View> {
  private final TestScheduler ioTestScheduler = new TestScheduler();

  @Mock TflService tflService;

  @Override protected MainPresenter createPresenter() {
    return new MainPresenter(Schedulers.immediate(), ioTestScheduler, tflService);
  }

  @Override protected MainPresenter.View createView() {
    return mock(MainPresenter.View.class);
  }

  @Test public void onViewAttached_retrieveNearbyStops() {
    when(tflService.stopPoint()).thenReturn(Observable.empty());
    presenterOnViewAttached();

    ioTestScheduler.advanceTimeBy(1, TimeUnit.MILLISECONDS);

    verify(tflService).stopPoint();
  }
}