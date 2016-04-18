package com.numan1617.tfltubedepartures.network.model;

import com.google.auto.value.AutoValue;
import com.google.gson.TypeAdapterFactory;
import java.util.List;

@AutoValue
public abstract class StopPointResponse {
  public abstract List<StopPoint> stopPoints();

  public static TypeAdapterFactory typeAdapterFactory() {
    return AutoValue_StopPointResponse.typeAdapterFactory();
  }
}
