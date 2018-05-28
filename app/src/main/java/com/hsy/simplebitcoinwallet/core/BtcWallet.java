package com.hsy.simplebitcoinwallet.core;

import android.support.annotation.NonNull;
import org.bitcoinj.kits.WalletAppKit;

public class BtcWallet {

  @NonNull
  private final WalletAppKit walletAppKit;

  BtcWallet(@NonNull WalletAppKit walletAppKit) {
    this.walletAppKit = walletAppKit;
  }

  @NonNull
  public String getBalance() {
    return walletAppKit.wallet().getBalance().toFriendlyString();
  }

}
