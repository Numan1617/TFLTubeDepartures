package com.numan1617.tfltubedepartures.feature.main;

import android.support.annotation.NonNull;
import com.numan1617.tfltubedepartures.base.BasePresenter;
import com.numan1617.tfltubedepartures.base.PresenterView;
import com.numan1617.tfltubedepartures.network.TflService;
import com.numan1617.tfltubedepartures.network.model.StopPoint;
import java.util.List;
import rx.Scheduler;

public class MainPresenter extends BasePresenter<MainPresenter.View> {
  private final TflService tflService;
  private final Scheduler uiScheduler;
  private final Scheduler ioScheduler;

  public MainPresenter(@NonNull final Scheduler uiScheduler, @NonNull final Scheduler ioScheduler,
      @NonNull final TflService tflService) {
    this.uiScheduler = uiScheduler;
    this.ioScheduler = ioScheduler;
    this.tflService = tflService;
  }

  @Override public void onViewAttached(@NonNull final View view) {
    super.onViewAttached(view);

    view.showLoadingView(true);

    unsubscribeOnViewDetach(tflService.stopPoint()
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
  }

  interface View extends PresenterView {
    void setStopPoints(@NonNull List<StopPoint> stopPoints);

    void showLoadingView(boolean visible);

    void showNoStopsView(boolean visible);
  }
}
