package com.numan1617.tfltubedepartures.network;

import com.numan1617.tfltubedepartures.network.model.Departure;
import com.numan1617.tfltubedepartures.network.model.StopPoint;
import java.util.List;
import rx.Observable;

public interface TflService {
  Observable<List<StopPoint>> stopPoint();
  Observable<List<Departure>> departures(String stopPointId);
}
