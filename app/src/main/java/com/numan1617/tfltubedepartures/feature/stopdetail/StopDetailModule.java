package com.numan1617.tfltubedepartures.feature.stopdetail;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.numan1617.tfltubedepartures.AppModule.tflService;

class StopDetailModule {
  static StopDetailPresenter stopDetailPresenter() {
    return new StopDetailPresenter(AndroidSchedulers.mainThread(), Schedulers.io(), tflService());
  }
}
