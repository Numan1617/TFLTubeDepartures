package com.numan1617.tfltubedepartures.feature.main;

import android.support.annotation.NonNull;
import com.numan1617.tfltubedepartures.base.BasePresenter;
import com.numan1617.tfltubedepartures.base.PresenterView;
import com.numan1617.tfltubedepartures.network.TflService;
import com.numan1617.tfltubedepartures.network.model.StopPoint;
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

    unsubscribeOnViewDetach(tflService.stopPoint()
        .observeOn(uiScheduler)
        .subscribeOn(ioScheduler)
        .subscribe(stopPoints -> {
          for (final StopPoint stopPoint : stopPoints) {
            System.out.println(stopPoint);
          }
        }, Throwable::printStackTrace));
  }

  interface View extends PresenterView {
  }
}
