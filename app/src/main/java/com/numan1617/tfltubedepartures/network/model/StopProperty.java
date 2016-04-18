package com.numan1617.tfltubedepartures.network.model;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.google.gson.TypeAdapterFactory;

@AutoValue
public abstract class StopProperty implements Parcelable {
  public abstract String category();
  public abstract String key();
  public abstract String sourceSystemKey();
  public abstract String value();

  public static TypeAdapterFactory typeAdapterFactory() {
    return AutoValue_StopProperty.typeAdapterFactory();
  }
}
