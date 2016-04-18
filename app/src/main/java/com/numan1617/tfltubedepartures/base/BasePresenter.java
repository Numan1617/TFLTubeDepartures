package com.numan1617.tfltubedepartures.base;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BasePresenter<V extends PresenterView> {
  private CompositeSubscription subscriptions;
  private V v;

  @CallSuper public void onViewAttached(@NonNull final V view) {
    if (v != null) {
      throw new IllegalStateException("View " + v + " is already attached. Cannot attach " + view);
    }
    v = view;
  }

  @CallSuper public void onViewDetached() {
    v = null;

    if (subscriptions != null) {
      subscriptions.unsubscribe();
      subscriptions = null;
    }
  }

  @CallSuper protected void unsubscribeOnViewDetach(@NonNull final Subscription subscription) {
    if (subscriptions == null) {
      subscriptions = new CompositeSubscription();
    }
    subscriptions.add(subscription);
  }
}
