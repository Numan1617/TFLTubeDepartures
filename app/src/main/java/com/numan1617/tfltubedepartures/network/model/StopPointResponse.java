package com.numan1617.tfltubedepartures.network.model;

import com.google.auto.value.AutoValue;
import java.util.List;

@AutoValue
public abstract class StopPointResponse {
  public abstract List<StopPoint> stopPoints();
}
