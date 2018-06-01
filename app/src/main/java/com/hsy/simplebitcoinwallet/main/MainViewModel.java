package com.hsy.simplebitcoinwallet.main;

import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import com.hsy.simplebitcoinwallet.BR;
import com.hsy.simplebitcoinwallet.BaseViewModel;
import com.hsy.simplebitcoinwallet.Constants;
import com.hsy.simplebitcoinwallet.R;
import com.hsy.simplebitcoinwallet.core.BtcTx;
import com.hsy.simplebitcoinwallet.core.BtcWallet.ReceivedTxListener;
import com.hsy.simplebitcoinwallet.core.BtcWallet.SentTxListener;
import com.hsy.simplebitcoinwallet.core.BtcWalletManager;
import com.hsy.simplebitcoinwallet.main.send.SendCoinFragment;
import com.hsy.simplebitcoinwallet.main.send.SendCoinViewModel;
import com.hsy.simplebitcoinwallet.utils.ActivityUtils;
import com.hsy.simplebitcoinwallet.utils.WalletUtils;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends BaseViewModel implements CompletableObserver, ReceivedTxListener, SentTxListener, OnOffsetChangedListener {

  @NonNull
  private String balance = "0.00000000 BTC";

  @NonNull
  private String address = "";

  private int syncProgress = 0;

  @Nullable
  private Disposable launchWalletDisposable;

  @Nullable
  private Disposable syncDisposable;

  private boolean showProgressBar = false;

  private boolean shouldHideTitleWhenCollapsed = true;

  @Nullable
  private RecyclerView.Adapter adapter;

  @NonNull
  private final List<BtcTx> items = new ArrayList<>();

  @NonNull
  private final BtcWalletManager btcWalletManager;

  MainViewModel(@NonNull BtcWalletManager btcWalletManager) {
    this.btcWalletManager = btcWalletManager;
  }

  /**
   * Launch send coin page.
   */
  public void launchSendCoinPage(@NonNull FragmentManager fragmentManager) {
    final SendCoinFragment fragment = SendCoinFragment.newInstance();
    fragment.setViewModel(new SendCoinViewModel(btcWalletManager));
    ActivityUtils.replaceAndKeepOld(fragmentManager, fragment, R.id.contentFrame);
  }

  @NonNull
  @Bindable
  public List<BtcTx> getItems() {
    return items;
  }

  void start(@NonNull RecyclerView.Adapter adapter) {
    setShouldHideTitleWhenCollapsed(true);
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
  }

  void stop() {
    setShouldHideTitleWhenCollapsed(false);
    if (launchWalletDisposable != null && !launchWalletDisposable.isDisposed()) {
      launchWalletDisposable.dispose();
    }

    if (syncDisposable != null && !syncDisposable.isDisposed()) {
      syncDisposable.dispose();
    }
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
    btcWalletManager.getCurrent()
        .addReceivedTxListener(this);
    btcWalletManager.getCurrent()
        .addSentTxListener(this);
    setAddress(btcWalletManager.getCurrent().getAddress());
    setBalance(btcWalletManager.getCurrent().getBalance());
    loadTxs();
  }

  @Override
  public void onReceivedTx(@NonNull BtcTx tx) {
    showSnackBarMessage(R.string.main_tx_received);
    setAddress(btcWalletManager.getCurrent().getAddress());
    setBalance(btcWalletManager.getCurrent().getBalance());
  }

  @Override
  public void onSentTx(@NonNull BtcTx tx) {
    showSnackBarMessage(R.string.main_tx_sent);
    setBalance(btcWalletManager.getCurrent().getBalance());
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

  @NonNull
  @Bindable
  public String getAddress() {
    return address;
  }

  private void setAddress(@NonNull String address) {
    this.address = WalletUtils.formatHash(address, Constants.ADDRESS_FORMAT_GROUP_SIZE, Constants.ADDRESS_FORMAT_LINE_SIZE)
        .toString();
    notifyPropertyChanged(BR.address);
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

  @Bindable
  public boolean isShouldHideTitleWhenCollapsed() {
    return shouldHideTitleWhenCollapsed;
  }

  private void setShouldHideTitleWhenCollapsed(boolean shouldHideTitleWhenCollapsed) {
    this.shouldHideTitleWhenCollapsed = shouldHideTitleWhenCollapsed;
    notifyPropertyChanged(BR.shouldHideTitleWhenCollapsed);
  }

  /**
   * The databinding method for binding the listener of {@link AppBarLayout}.
   */
  @BindingAdapter(value = {"app:hideTitleWhenCollapsed", "app:offsetChangedListener"})
  public static void setHideTitleWhenCollapsed(@NonNull AppBarLayout appBarLayout, boolean isBinding, @NonNull OnOffsetChangedListener listener) {
    if (isBinding) {
      appBarLayout.addOnOffsetChangedListener(listener);
    } else {
      appBarLayout.removeOnOffsetChangedListener(listener);
    }
  }

  @Override
  public void onOffsetChanged(@NonNull AppBarLayout appBarLayout, int verticalOffset) {
    final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) appBarLayout.getChildAt(0);
    final Toolbar toolbar = (Toolbar) collapsingToolbarLayout.getChildAt(collapsingToolbarLayout.getChildCount() - 1);
    if (verticalOffset == 0) {
      toolbar.setTitle(R.string.empty);
    } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
      toolbar.setTitle(balance);
    }
  }
}
