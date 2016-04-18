package com.numan1617.tfltubedepartures.network.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class StopProperty {
  public abstract String category();
  public abstract String key();
  public abstract String sourceSystemKey();
  public abstract String value();
}
