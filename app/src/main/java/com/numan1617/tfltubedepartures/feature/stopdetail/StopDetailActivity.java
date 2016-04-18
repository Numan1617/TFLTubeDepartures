package com.numan1617.tfltubedepartures.feature.stopdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;
import butterknife.Bind;
import com.numan1617.tfltubedepartures.R;
import com.numan1617.tfltubedepartures.base.BaseActivity;
import com.numan1617.tfltubedepartures.base.BasePresenter;
import com.numan1617.tfltubedepartures.network.model.StopPoint;

import static com.numan1617.tfltubedepartures.feature.stopdetail.StopDetailModule.stopDetailPresenter;

public class StopDetailActivity extends BaseActivity<StopDetailPresenter.View>
    implements StopDetailPresenter.View {
  private static final String EXTRA_STOP_POINT = "EXTRA_STOP_POINT";
  private final StopDetailPresenter presenter;

  @Bind(R.id.text_view_stop_name) TextView stopNameText;

  public static Intent getStartIntent(@NonNull final Context context,
      @NonNull final StopPoint stopPoint) {
    final Intent intent = new Intent(context, StopDetailActivity.class);
    intent.putExtra(EXTRA_STOP_POINT, stopPoint);
    return intent;
  }

  @SuppressWarnings("unused") public StopDetailActivity() {
    this(stopDetailPresenter());
  }

  public StopDetailActivity(@NonNull final StopDetailPresenter stopDetailPresenter) {
    this.presenter = stopDetailPresenter;
  }

  @Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getPresenter().onViewAttached(this);

    setupViewsWithLoadedIntent();
  }

  @Override protected int getLayoutId() {
    return R.layout.activity_stop_detail;
  }

  @NonNull @Override protected BasePresenter<StopDetailPresenter.View> getPresenter() {
    return presenter;
  }

  @NonNull @Override protected StopDetailPresenter.View getPresenterView() {
    return this;
  }

  private void setupViewsWithLoadedIntent() {
    final Intent loadedIntent = getIntent();
    final StopPoint stopPoint = loadedIntent.getParcelableExtra(EXTRA_STOP_POINT);
    System.out.println(stopPoint);
    presenter.setStopPoint(stopPoint);
  }

  @Override public void setStopName(@NonNull final String stopName) {
    stopNameText.setText(stopName);
  }
}
