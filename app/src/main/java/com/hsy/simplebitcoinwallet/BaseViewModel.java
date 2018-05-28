package com.hsy.simplebitcoinwallet;

import android.databinding.BaseObservable;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * This is the ViewModel of Model-View-ViewModel.
 */

public abstract class BaseViewModel extends BaseObservable {

  @NonNull
  private final ObservableField<Integer> snackBarMessageResId = new ObservableField<>(R.string.empty);

  public void addOnMessageChangedCallback(@NonNull Observable.OnPropertyChangedCallback callback) {
    snackBarMessageResId.addOnPropertyChangedCallback(callback);
  }

  public void removeOnMessageChangedCallback(@NonNull Observable.OnPropertyChangedCallback callback) {
    snackBarMessageResId.removeOnPropertyChangedCallback(callback);
  }

  @StringRes
  public int getSnackBarMessageResId() {
    return snackBarMessageResId.get();
  }

  protected void showSnackBarMessage(@StringRes int resId) {
    snackBarMessageResId.set(resId);
  }

}
