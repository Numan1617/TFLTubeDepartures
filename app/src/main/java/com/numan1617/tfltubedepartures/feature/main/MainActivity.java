package com.numan1617.tfltubedepartures.feature.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import butterknife.Bind;
import com.numan1617.tfltubedepartures.R;
import com.numan1617.tfltubedepartures.base.BaseActivity;
import com.numan1617.tfltubedepartures.base.BasePresenter;
import com.numan1617.tfltubedepartures.network.model.StopPoint;
import java.util.List;
import rx.Observable;

import static com.numan1617.tfltubedepartures.feature.main.MainModule.mainPresenter;

public class MainActivity extends BaseActivity<MainPresenter.View> implements MainPresenter.View {

  private final MainPresenter presenter;
  @Bind(R.id.recycler_view_stops_list) RecyclerView stopsList;
  @Bind(R.id.progress_bar_loading) View loadingIndicator;
  @Bind(R.id.text_view_no_stops) View noStops;
  private StopListAdapter stopListAdapter;

  @SuppressWarnings("unused") public MainActivity() {
    this(mainPresenter());
  }

  public MainActivity(@NonNull final MainPresenter mainPresenter) {
    this.presenter = mainPresenter;
  }

  @Override protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setupStopList();

    getPresenter().onViewAttached(this);
  }

  private void setupStopList() {
    stopsList.setLayoutManager(new LinearLayoutManager(this));
    stopListAdapter = new StopListAdapter();
    stopsList.setAdapter(stopListAdapter);
  }

  @Override protected int getLayoutId() {
    return R.layout.activity_main;
  }

  @NonNull @Override protected BasePresenter<MainPresenter.View> getPresenter() {
    return presenter;
  }

  @NonNull @Override protected MainPresenter.View getPresenterView() {
    return this;
  }

  @NonNull @Override public Observable<StopPoint> stopPointSelected() {
    return stopListAdapter.stopPointSelected();
  }

  @Override public void setStopPoints(@NonNull final List<StopPoint> stopPoints) {
    stopListAdapter.setData(stopPoints);
  }

  @Override public void showLoadingView(final boolean visible) {
    loadingIndicator.setVisibility(visible ? View.VISIBLE : View.GONE);
  }

  @Override public void showNoStopsView(final boolean visible) {
    noStops.setVisibility(visible ? View.VISIBLE : View.GONE);
  }

  @Override public void displayStopDetails(final StopPoint stopPoint) {
    Toast.makeText(MainActivity.this, stopPoint.commonName(), Toast.LENGTH_SHORT).show();
  }
}
