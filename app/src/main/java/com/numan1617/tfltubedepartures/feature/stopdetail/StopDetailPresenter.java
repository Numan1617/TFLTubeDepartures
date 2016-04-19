package com.numan1617.tfltubedepartures.feature.stopdetail;

import android.support.annotation.NonNull;
import com.numan1617.tfltubedepartures.base.BasePresenter;
import com.numan1617.tfltubedepartures.base.PresenterView;
import com.numan1617.tfltubedepartures.network.TflService;
import com.numan1617.tfltubedepartures.network.model.Departure;
import com.numan1617.tfltubedepartures.network.model.StopPoint;
import com.numan1617.tfltubedepartures.network.model.StopProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Scheduler;

class StopDetailPresenter extends BasePresenter<StopDetailPresenter.View> {
  private static final int REFRESH_INTERVAL = 30;

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

    final List<String> facilities = getFilteredFacilities(stopPoint);
    view.setFacilities(facilities);

    unsubscribeOnViewDetach(Observable.interval(1, TimeUnit.SECONDS, ioScheduler)
        .observeOn(uiScheduler)
        .doOnNext(refreshNumber -> {
          final int nextRefreshInSeconds =
              (int) (REFRESH_INTERVAL - (refreshNumber % REFRESH_INTERVAL));
          view.setNextRefreshTime(nextRefreshInSeconds);
        })
        .observeOn(ioScheduler)
        .filter(aLong -> aLong % REFRESH_INTERVAL == 0)
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

  @NonNull private List<String> getFilteredFacilities(final StopPoint stopPoint) {
    final List<String> facilities = new ArrayList<>();
    for (final StopProperty stopProperty : stopPoint.additionalProperties()) {
      if (stopProperty.category().equalsIgnoreCase("Facility")
          && !stopProperty.value()
          .equalsIgnoreCase("no")
          && !stopProperty.value().equalsIgnoreCase("0")
          && !stopProperty.key().equalsIgnoreCase("Other Facilities")) {
        facilities.add(stopProperty.key());
      }
    }
    return facilities;
  }

  interface View extends PresenterView {
    void setStopName(@NonNull String stopName);

    void setDepartures(@NonNull List<Departure> departures);

    void setNextRefreshTime(int nextRefreshInSeconds);

    void setFacilities(List<String> facilities);
  }
}
