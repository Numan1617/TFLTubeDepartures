package com.numan1617.tfltubedepartures.network.model;

import com.google.auto.value.AutoValue;
import java.util.List;

@AutoValue
public abstract class StopPoint {
  public abstract String naptanId();
  public abstract double lat();
  public abstract double lon();
  public abstract String commonName();
  public abstract double distance();
  public abstract List<StopProperty> additionalProperties();
}
