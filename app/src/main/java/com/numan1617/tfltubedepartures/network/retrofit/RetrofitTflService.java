package com.numan1617.tfltubedepartures.network.retrofit;

import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.numan1617.tfltubedepartures.network.TflService;
import com.numan1617.tfltubedepartures.network.model.Departure;
import com.numan1617.tfltubedepartures.network.model.StopPoint;
import com.numan1617.tfltubedepartures.network.model.StopPointResponse;
import com.numan1617.tfltubedepartures.network.model.StopProperty;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import java.util.List;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public class RetrofitTflService implements TflService {
  private final Api api;

  public RetrofitTflService(@NonNull final String appId, @NonNull final String apiKey) {
    final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

    final OkHttpClient okHttpClient = new OkHttpClient();
    okHttpClient.interceptors().add(logging);
    okHttpClient.interceptors().add(chain -> {
      final Request request = chain.request();
      final HttpUrl url = request.httpUrl()
          .newBuilder()
          .addQueryParameter("app_id", appId)
          .addQueryParameter("app_key", apiKey)
          .build();
      final Request modifiedRequest = request.newBuilder().url(url).build();
      return chain.proceed(modifiedRequest);
    });

    final Gson gson =
        new GsonBuilder().registerTypeAdapterFactory(StopPointResponse.typeAdapterFactory())
            .registerTypeAdapterFactory(StopPoint.typeAdapterFactory())
            .registerTypeAdapterFactory(StopProperty.typeAdapterFactory())
            .registerTypeAdapterFactory(Departure.typeAdapterFactory())
            .create();

    final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://api.tfl.gov.uk")
        .setConverter(new GsonConverter(gson))
        .setClient(new OkClient(okHttpClient))
        .build();

    this.api = restAdapter.create(Api.class);
  }

  @Override
  public Observable<List<StopPoint>> stopPoint(final double latitude, final double longitude) {
    return api.stopPoint(latitude, longitude).map(StopPointResponse::stopPoints);
  }

  @Override public Observable<List<Departure>> departures(final String stopPointId) {
    return api.departures(stopPointId);
  }

  private interface Api {
    @GET("/StopPoint?stopTypes=NaptanMetroStation&radius=900&useStopPointHierarchy=false&returnLines=True")
    Observable<StopPointResponse> stopPoint(@Query("lat") double latitude,
        @Query("lon") double longitude);

    @GET("/StopPoint/{stopPointId}/Arrivals") Observable<List<Departure>> departures(
        @Path("stopPointId") String stopPointId);
  }
}
