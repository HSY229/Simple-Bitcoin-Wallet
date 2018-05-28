package com.hsy.simplebitcoinwallet.main;

import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.hsy.simplebitcoinwallet.BR;
import com.hsy.simplebitcoinwallet.BaseViewModel;
import com.hsy.simplebitcoinwallet.core.BtcWalletManager;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainViewModel extends BaseViewModel implements CompletableObserver {

  @NonNull
  private static final String TAG = "main";

  @NonNull
  private String balance = "0.00000000 BTC";

  private int syncProgress = 0;

  @Nullable
  private Disposable launchWalletDisposable;

  @Nullable
  private Disposable syncDisposable;

  @NonNull
  private final BtcWalletManager btcWalletManager;

  MainViewModel(@NonNull BtcWalletManager btcWalletManager) {
    this.btcWalletManager = btcWalletManager;
  }

  void start() {
    if (!btcWalletManager.isRunning()) {
      btcWalletManager.launch()
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this);
    }
    syncDisposable = btcWalletManager.getDownloadObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::setSyncProgress, Throwable::printStackTrace, () -> {
          setSyncProgress(100);
          setBalance(btcWalletManager.getCurrent().getBalance());
        });
  }

  void stop() {
    if (launchWalletDisposable != null && !launchWalletDisposable.isDisposed()) {
      launchWalletDisposable.dispose();
    }

    if (syncDisposable != null && !syncDisposable.isDisposed()) {
      syncDisposable.dispose();
    }

    btcWalletManager.shutdown();
  }

  @Override
  public void onSubscribe(@NonNull Disposable launchWalletDisposable) {
    this.launchWalletDisposable = launchWalletDisposable;
  }

  @Override
  public void onComplete() {
    Log.d(TAG, "launch wallet completed");
  }

  @Override
  public void onError(@NonNull Throwable e) {
    e.printStackTrace();
  }

  @NonNull
  @Bindable
  public String getBalance() {
    return balance;
  }

  public void setBalance(String balance) {
    this.balance = balance;
    notifyPropertyChanged(BR.balance);
  }

  @Bindable
  public int getSyncProgress() {
    return syncProgress;
  }

  private void setSyncProgress(int syncProgress) {
    this.syncProgress = syncProgress;
    notifyPropertyChanged(BR.syncProgress);
  }
}
