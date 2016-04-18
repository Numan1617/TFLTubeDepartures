package com.numan1617.tfltubedepartures.feature.main;

import android.support.annotation.NonNull;
import com.numan1617.tfltubedepartures.R;
import com.numan1617.tfltubedepartures.base.BaseActivity;
import com.numan1617.tfltubedepartures.base.BasePresenter;
import com.numan1617.tfltubedepartures.network.model.StopPoint;
import java.util.List;

import static com.numan1617.tfltubedepartures.feature.main.MainModule.mainPresenter;

public class MainActivity extends BaseActivity<MainPresenter.View> implements MainPresenter.View {

  private final MainPresenter presenter;

  @SuppressWarnings("unused") public MainActivity() {
    this(mainPresenter());
  }

  public MainActivity(@NonNull final MainPresenter mainPresenter) {
    this.presenter = mainPresenter;
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

  @Override public void setStopPoints(@NonNull final List<StopPoint> stopPoints) {

  }

  @Override public void showLoadingView(final boolean visible) {

  }

  @Override public void showNoStopsView(final boolean visible) {

  }
}
