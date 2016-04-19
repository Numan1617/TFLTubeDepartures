package com.numan1617.tfltubedepartures;

import android.app.Application;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.numan1617.tfltubedepartures.network.TflService;
import com.numan1617.tfltubedepartures.network.retrofit.RetrofitTflService;

public class AppModule {
  private static Application application;
  private static TflService tflService;

  @NonNull
  public static TflService tflService() {
    if (tflService == null) {
      tflService = new RetrofitTflService(BuildConfig.TFL_API_APP_ID, BuildConfig.TFL_API_KEY);
    }
    return tflService;
  }

  @NonNull
  public static GoogleApiClient.Builder googleApiClientBuilder() {
    return new GoogleApiClient.Builder(application).addApi(LocationServices.API);
  }

  public static void setApplication(@NonNull final Application application) {
    AppModule.application = application;
  }
}
