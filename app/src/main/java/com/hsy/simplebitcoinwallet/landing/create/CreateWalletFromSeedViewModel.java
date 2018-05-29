package com.hsy.simplebitcoinwallet.landing.create;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.hsy.simplebitcoinwallet.BR;
import com.hsy.simplebitcoinwallet.BaseViewModel;
import com.hsy.simplebitcoinwallet.R;
import com.hsy.simplebitcoinwallet.core.BtcWalletManager;
import com.hsy.simplebitcoinwallet.landing.LandingActivity;
import com.hsy.simplebitcoinwallet.main.MainActivity;
import io.reactivex.disposables.Disposable;

public class CreateWalletFromSeedViewModel extends BaseViewModel {

  private boolean confirmBtnEnabled = true;

  private int progressVisibility = GONE;

  @Nullable
  private Disposable createDisposable;

  @NonNull
  private final BtcWalletManager btcWalletManager;

  @NonNull
  private final String mnemonic;

  public CreateWalletFromSeedViewModel(@NonNull BtcWalletManager btcWalletManager) {
    this.btcWalletManager = btcWalletManager;
    mnemonic = BtcWalletManager.generateMnemonic();
  }

  void stop() {
    // TODO: stop wallet creation.
    // TODO: stop sync chain data.
    if (createDisposable != null && !createDisposable.isDisposed()) {
      createDisposable.dispose();
    }
  }

  /**
   * Launch main page and finish current page when create wallet successful,
   * otherwise display the error message.
   */
  public void createWallet(@NonNull LandingActivity activity) {
    setConfirmBtnEnabled(false);
    setProgressVisibility(VISIBLE);
    createDisposable = btcWalletManager.create(mnemonic)
        .subscribe(() -> {
              setProgressVisibility(GONE);
              MainActivity.startAndFinishCurrent(activity);
            }, throwable -> {
              showSnackBarMessage(R.string.create_wallet_error);
              setProgressVisibility(GONE);
              setConfirmBtnEnabled(true);
            }
        );
  }

  @NonNull
  @Bindable
  public String getMnemonic() {
    return mnemonic;
  }

  @Bindable
  public boolean isConfirmBtnEnabled() {
    return confirmBtnEnabled;
  }

  private void setConfirmBtnEnabled(boolean confirmBtnEnabled) {
    this.confirmBtnEnabled = confirmBtnEnabled;
    notifyPropertyChanged(BR.confirmBtnEnabled);
  }

  @Bindable
  public int getProgressVisibility() {
    return progressVisibility;
  }

  private void setProgressVisibility(int progressVisibility) {
    this.progressVisibility = progressVisibility;
    notifyPropertyChanged(BR.progressVisibility);
  }
}
