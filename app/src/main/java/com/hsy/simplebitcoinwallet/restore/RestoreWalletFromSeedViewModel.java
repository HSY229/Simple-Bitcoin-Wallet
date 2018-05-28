package com.hsy.simplebitcoinwallet.restore;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.hsy.simplebitcoinwallet.BR;
import com.hsy.simplebitcoinwallet.BaseViewModel;
import com.hsy.simplebitcoinwallet.core.BtcWalletManager;
import com.hsy.simplebitcoinwallet.R;
import com.hsy.simplebitcoinwallet.landing.LandingActivity;
import com.hsy.simplebitcoinwallet.main.MainActivity;
import io.reactivex.disposables.Disposable;

public class RestoreWalletFromSeedViewModel extends BaseViewModel {

  @NonNull
  private static final String TEST_MNEMONIC = "census flame uncle toe pony inject discover forget jazz giggle ugly escape";

  private static final long creationTimeSecond = 1526262375L;

  @NonNull
  private String mnemonic = "0.00";

  private boolean confirmBtnEnabled = true;

  private int progressVisibility = GONE;

  @Nullable
  private Disposable restoreDisposable;

  @NonNull
  private final BtcWalletManager btcWalletManager;

  public RestoreWalletFromSeedViewModel(@NonNull BtcWalletManager btcWalletManager) {
    this.btcWalletManager = btcWalletManager;
    setMnemonic(TEST_MNEMONIC);
  }

  void stop() {
    if (restoreDisposable != null && !restoreDisposable.isDisposed()) {
      restoreDisposable.dispose();
    }
  }

  public void restore(@NonNull LandingActivity landingActivity) {
    setConfirmBtnEnabled(false);
    setProgressVisibility(VISIBLE);
    restoreDisposable = btcWalletManager.create(mnemonic, creationTimeSecond)
        .subscribe(() -> {
          setProgressVisibility(GONE);
          MainActivity.startAndFinishCurrent(landingActivity);
        }, throwable -> {
          showSnackBarMessage(R.string.restore_error);
          setProgressVisibility(GONE);
          setConfirmBtnEnabled(true);
        });
  }

  @NonNull
  @Bindable
  public String getMnemonic() {
    return mnemonic;
  }

  private void setMnemonic(@NonNull String mnemonic) {
    this.mnemonic = mnemonic;
    notifyPropertyChanged(BR.mnemonic);
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
