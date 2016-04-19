package com.numan1617.tfltubedepartures.feature.main;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.location.LocationServices;
import com.numan1617.tfltubedepartures.GoogleApiClientWrapper;

class LocationRequester {
  @Nullable LatLng lastLocation(@NonNull final GoogleApiClientWrapper apiClientWrapper) {
    final Location lastLocation =
        LocationServices.FusedLocationApi.getLastLocation(apiClientWrapper.getApiClient());
    return lastLocation == null ? null
        : LatLng.create(lastLocation.getLatitude(), lastLocation.getLongitude());
  }
}
