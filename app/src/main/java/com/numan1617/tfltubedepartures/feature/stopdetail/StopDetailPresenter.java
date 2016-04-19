package com.numan1617.tfltubedepartures.feature.stopdetail;

import android.support.annotation.NonNull;
import com.numan1617.tfltubedepartures.base.BasePresenter;
import com.numan1617.tfltubedepartures.base.PresenterView;
import com.numan1617.tfltubedepartures.network.TflService;
import com.numan1617.tfltubedepartures.network.model.Departure;
import com.numan1617.tfltubedepartures.network.model.StopPoint;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Scheduler;

class StopDetailPresenter extends BasePresenter<StopDetailPresenter.View> {
  private final Scheduler uiScheduler;
  private final Scheduler ioScheduler;
  private final TflService tflService;
  private View view;

  public StopDetailPresenter(@NonNull final Scheduler uiScheduler,
      @NonNull final Scheduler ioScheduler, @NonNull final TflService tflService) {
    this.uiScheduler = uiScheduler;
    this.ioScheduler = ioScheduler;
    this.tflService = tflService;
  }

  @Override public void onViewAttached(@NonNull final View view) {
    super.onViewAttached(view);
    this.view = view;
  }

  public void setStopPoint(final StopPoint stopPoint) {
    view.setStopName(stopPoint.commonName());
    unsubscribeOnViewDetach(Observable.interval(1, TimeUnit.SECONDS, ioScheduler)
        .filter(aLong -> aLong % 30 == 0)
        .flatMap(ignored -> tflService.departures(stopPoint.id()))
        .repeat()
        .map(departures -> {
          Collections.sort(departures, new DepartureArrivalComparator());
          return departures;
        })
        .map(departures -> {
          if (departures.size() >= 3) {
            return departures.subList(0, 3);
          }
          return departures;
        })
        .observeOn(uiScheduler)
        .subscribe(departures -> {
          view.setDepartures(departures);
        }, Throwable::printStackTrace));
  }

  interface View extends PresenterView {
    void setStopName(@NonNull String stopName);

    void setDepartures(@NonNull List<Departure> departures);
  }
}
