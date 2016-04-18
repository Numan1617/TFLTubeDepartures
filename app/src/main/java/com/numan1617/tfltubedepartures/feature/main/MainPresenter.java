package com.numan1617.tfltubedepartures.feature.main;

import android.support.annotation.NonNull;
import com.numan1617.tfltubedepartures.base.BasePresenter;
import com.numan1617.tfltubedepartures.base.PresenterView;
import com.numan1617.tfltubedepartures.network.TflService;
import rx.Scheduler;

public class MainPresenter extends BasePresenter<MainPresenter.View> {
  public MainPresenter(@NonNull final Scheduler uiScheduler, @NonNull final Scheduler ioScheduler,
      @NonNull final TflService tflService) {
  }

  interface View extends PresenterView {
  }
}
