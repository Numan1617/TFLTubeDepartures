package com.numan1617.tfltubedepartures.feature.main;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.numan1617.tfltubedepartures.AppModule.tflService;

public class MainModule {
  static MainPresenter mainPresenter() {
    return new MainPresenter(AndroidSchedulers.mainThread(), Schedulers.io(), tflService());
  }
}
