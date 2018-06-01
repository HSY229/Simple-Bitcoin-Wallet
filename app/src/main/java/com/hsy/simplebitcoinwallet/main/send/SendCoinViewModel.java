package com.hsy.simplebitcoinwallet.main.send;

import static java.util.concurrent.TimeUnit.SECONDS;

import android.app.Activity;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import com.hsy.simplebitcoinwallet.BR;
import com.hsy.simplebitcoinwallet.BaseViewModel;
import com.hsy.simplebitcoinwallet.R;
import com.hsy.simplebitcoinwallet.core.BtcWalletManager;
import com.hsy.simplebitcoinwallet.utils.Keyboard;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.bitcoinj.core.InsufficientMoneyException;

public class SendCoinViewModel extends BaseViewModel {

  @NonNull
  private String toAddress = "";

  @NonNull
  private String amount = "";

  private boolean confirmBtnEnabled = true;

  @Nullable
  private Disposable sendCoinDisposable;

  @NonNull
  private final BtcWalletManager btcWalletManager;

  public SendCoinViewModel(@NonNull BtcWalletManager btcWalletManager) {
    this.btcWalletManager = btcWalletManager;
  }

  void stop() {
    if (sendCoinDisposable != null && !sendCoinDisposable.isDisposed()) {
      sendCoinDisposable.dispose();
    }
  }

  public void back(@NonNull FragmentManager fragmentManager) {
    fragmentManager.popBackStack();
  }

  /**
   * Send coin then close current page.
   */
  public void send(@NonNull FragmentManager fragmentManager, @NonNull Activity activity) {
    setConfirmBtnEnabled(false);
    Keyboard.hideSoftKeyboard(activity);
    sendCoinDisposable = btcWalletManager.getCurrent()
        .send(toAddress, amount)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess(btcTx -> showSnackBarMessage(R.string.send_coin_send_done))
        .observeOn(Schedulers.io())
        .doOnSuccess(btcTx -> SECONDS.sleep(1))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(btcTx -> {
              back(fragmentManager);
            }, throwable -> {
              if (isInValidAddressException(throwable)) {
                showSnackBarMessage(R.string.send_coin_invalid_to_address);
              } else if (isInValidAmountException(throwable)) {
                showSnackBarMessage(R.string.send_coin_invalid_amount);
              } else if (isInsufficientMoneyException(throwable)) {
                showSnackBarMessage(R.string.send_coin_not_enough_balance);
              } else {
                showSnackBarMessage(R.string.send_coin_failed);
              }
              setConfirmBtnEnabled(true);
            }
        );
  }

  private boolean isInValidAddressException(@NonNull Throwable throwable) {
    return throwable instanceof IllegalArgumentException && throwable.getMessage().contains("invalid address");
  }

  private boolean isInValidAmountException(@NonNull Throwable throwable) {
    return throwable instanceof IllegalArgumentException && throwable.getMessage().contains("invalid amount");
  }

  private boolean isInsufficientMoneyException(@NonNull Throwable throwable) {
    return throwable instanceof InsufficientMoneyException;
  }

  @Bindable
  @NonNull
  public String getToAddress() {
    return toAddress;
  }

  public void setToAddress(@NonNull String toAddress) {
    this.toAddress = toAddress;
    notifyPropertyChanged(BR.toAddress);
  }

  @Bindable
  @NonNull
  public String getAmount() {
    return amount;
  }

  public void setAmount(@NonNull String amount) {
    this.amount = amount;
    notifyPropertyChanged(BR.amount);
  }

  @Bindable
  public boolean isConfirmBtnEnabled() {
    return confirmBtnEnabled;
  }

  private void setConfirmBtnEnabled(boolean confirmBtnEnabled) {
    this.confirmBtnEnabled = confirmBtnEnabled;
    notifyPropertyChanged(BR.confirmBtnEnabled);
  }
}
