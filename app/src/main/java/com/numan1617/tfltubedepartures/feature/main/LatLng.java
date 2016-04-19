package com.numan1617.tfltubedepartures.feature.main;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class LatLng {
  public abstract double latitude();

  public abstract double longitude();

  static LatLng create(final double latitude, final double longitude) {
    return new AutoValue_LatLng(latitude, longitude);
  }
}
