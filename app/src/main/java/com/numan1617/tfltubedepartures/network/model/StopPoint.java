package com.numan1617.tfltubedepartures.network.model;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.google.gson.TypeAdapterFactory;
import java.util.List;

@AutoValue
public abstract class StopPoint implements Parcelable {
  public abstract String id();
  public abstract String naptanId();
  public abstract double lat();
  public abstract double lon();
  public abstract String commonName();
  public abstract double distance();
  public abstract List<StopProperty> additionalProperties();

  public static TypeAdapterFactory typeAdapterFactory() {
    return AutoValue_StopPoint.typeAdapterFactory();
  }
}
