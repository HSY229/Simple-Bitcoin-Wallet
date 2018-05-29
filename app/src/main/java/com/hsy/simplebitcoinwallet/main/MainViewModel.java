package com.hsy.simplebitcoinwallet.main;

import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.hsy.simplebitcoinwallet.BR;
import com.hsy.simplebitcoinwallet.BaseViewModel;
import com.hsy.simplebitcoinwallet.R;
import com.hsy.simplebitcoinwallet.core.BtcTx;
import com.hsy.simplebitcoinwallet.core.BtcWallet.ReceivedTxListener;
import com.hsy.simplebitcoinwallet.core.BtcWallet.SentTxListener;
import com.hsy.simplebitcoinwallet.core.BtcWalletManager;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends BaseViewModel implements CompletableObserver, ReceivedTxListener, SentTxListener {

  @NonNull
  private static final String TAG = "main";

  @NonNull
  private String balance = "0.00000000 BTC";

  private int syncProgress = 0;

  @Nullable
  private Disposable launchWalletDisposable;

  @Nullable
  private Disposable syncDisposable;

  private boolean showProgressBar = false;

  @Nullable
  private RecyclerView.Adapter adapter;

  @NonNull
  private final List<BtcTx> items = new ArrayList<>();

  @NonNull
  private final BtcWalletManager btcWalletManager;

  MainViewModel(@NonNull BtcWalletManager btcWalletManager) {
    this.btcWalletManager = btcWalletManager;
  }

  public void showMessage() {
    showSnackBarMessage(R.string.main_tx_received);
  }

  @NonNull
  @Bindable
  public List<BtcTx> getItems() {
    return items;
  }

  void start(@NonNull RecyclerView.Adapter adapter) {
    this.adapter = adapter;
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
          loadTxs();
        });
    showSnackBarMessage(R.string.main_tx_received);
  }

  void stop() {
    if (launchWalletDisposable != null && !launchWalletDisposable.isDisposed()) {
      launchWalletDisposable.dispose();
    }

    if (syncDisposable != null && !syncDisposable.isDisposed()) {
      syncDisposable.dispose();
    }

    btcWalletManager.shutdown();
    this.adapter = null;
    setShowProgressBar(false);
  }

  /**
   * Load the transactions and then update the list.
   */
  public void loadTxs() {
    setShowProgressBar(true);
    items.clear();
    items.addAll(btcWalletManager.getCurrent().getTxs());
    if (adapter != null) {
      adapter.notifyDataSetChanged();
    }
    setShowProgressBar(false);
  }

  @Override
  public void onSubscribe(@NonNull Disposable launchWalletDisposable) {
    this.launchWalletDisposable = launchWalletDisposable;
  }

  @Override
  public void onComplete() {
    Log.d(TAG, "launch wallet completed");
    btcWalletManager.getCurrent()
        .addReceivedTxListener(this);
    btcWalletManager.getCurrent()
        .addSentTxListener(this);
    loadTxs();
  }

  @Override
  public void onReceivedTx(@NonNull BtcTx tx) {
    showSnackBarMessage(R.string.main_tx_received);
  }

  @Override
  public void onSentTx(@NonNull BtcTx tx) {
    showSnackBarMessage(R.string.main_tx_sent);
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

  private void setBalance(String balance) {
    this.balance = balance;
    notifyPropertyChanged(BR.balance);
  }

  @Bindable
  public int getSyncProgress() {
    return syncProgress;
  }

  @SuppressWarnings("SameParameterValue")
  private void setSyncProgress(int syncProgress) {
    this.syncProgress = syncProgress;
    notifyPropertyChanged(BR.syncProgress);
  }

  @Bindable
  public boolean getShowProgressBar() {
    return showProgressBar;
  }

  private void setShowProgressBar(boolean showProgressBar) {
    this.showProgressBar = showProgressBar;
    notifyPropertyChanged(BR.showProgressBar);
  }
}
