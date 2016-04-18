package com.numan1617.tfltubedepartures.feature.stopdetail;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class StopDetailModule {
  static StopDetailPresenter stopDetailPresenter() {
    return new StopDetailPresenter(AndroidSchedulers.mainThread(), Schedulers.io());
  }
}
