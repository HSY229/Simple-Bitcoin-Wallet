package com.hsy.simplebitcoinwallet.core;

import android.support.annotation.NonNull;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.kits.WalletAppKit;

public class BtcWallet {

  private static final String TAG = "BtcWallet";

  @NonNull
  private final WalletAppKit walletAppKit;

  BtcWallet(@NonNull WalletAppKit walletAppKit) {
    this.walletAppKit = walletAppKit;
  }

  @NonNull
  public String getBalance() {
    return walletAppKit.wallet().getBalance().toFriendlyString();
  }

  @NonNull
  public List<BtcTx> getTxs() {
    final List<Transaction> transitions = walletAppKit.wallet()
        .getTransactionsByTime();
    final List<BtcTx> result = new ArrayList<>(transitions.size());
    for (Transaction transaction : transitions) {
      result.add(new BtcTx(walletAppKit.wallet(), transaction));
    }
    return result;
  }

  public void addReceivedTxListener(@NonNull ReceivedTxListener listener) {
    walletAppKit.wallet()
        .addCoinsReceivedEventListener((wallet, tx, prevBalance, newBalance) -> {
          Log.d(TAG, "balance: " + getBalance());
          listener.onReceivedTx(new BtcTx(wallet, tx));
        });
  }

  public void addSentTxListener(@NonNull SentTxListener listener) {
    walletAppKit.wallet().addCoinsSentEventListener((wallet, tx, prevBalance, newBalance) -> {
      Log.d(TAG, "balance: " + getBalance());
      listener.onSentTx(new BtcTx(wallet, tx));
    });
  }

  public interface ReceivedTxListener {

    void onReceivedTx(@NonNull BtcTx tx);
  }

  public interface SentTxListener {

    void onSentTx(@NonNull BtcTx tx);
  }

}