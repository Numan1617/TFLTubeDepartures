package com.numan1617.tfltubedepartures;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.api.GoogleApiClient;

public class GoogleApiClientWrapper {
  private GoogleApiClient apiClient;

  public GoogleApiClientWrapper(@NonNull final GoogleApiClient.Builder builder) {
    apiClient = builder.build();
  }

  public void connect() {
    apiClient.connect();
  }

  public void disconnect() {
    if (apiClient != null) {
      apiClient.disconnect();
    }
    apiClient = null;
  }

  public GoogleApiClient getApiClient() {
    return apiClient;
  }

  public void registerConnectionCallbacks(@NonNull final ConnectionCallback connectionCallback) {
    apiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
      @Override public void onConnected(@Nullable final Bundle bundle) {
        connectionCallback.onConnected();
      }

      @Override public void onConnectionSuspended(int i) {

      }
    });
  }

  public interface ConnectionCallback {
    void onConnected();
  }
}
