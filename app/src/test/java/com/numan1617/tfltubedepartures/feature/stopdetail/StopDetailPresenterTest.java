package com.numan1617.tfltubedepartures.feature.stopdetail;

import com.numan1617.tfltubedepartures.base.BasePresenterTest;
import com.numan1617.tfltubedepartures.network.model.StopPoint;
import org.junit.Test;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StopDetailPresenterTest extends BasePresenterTest<StopDetailPresenter, StopDetailPresenter.View> {

  @Override protected StopDetailPresenter createPresenter() {
    return new StopDetailPresenter(Schedulers.immediate(), Schedulers.immediate());
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
}