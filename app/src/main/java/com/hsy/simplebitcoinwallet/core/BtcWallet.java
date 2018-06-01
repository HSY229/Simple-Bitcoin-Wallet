package com.hsy.simplebitcoinwallet.core;

import android.support.annotation.NonNull;
import android.util.Log;
import com.hsy.simplebitcoinwallet.Constants;
import io.reactivex.Single;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;

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

  /**
   * Gets the current address for receiving money.
   */
  @NonNull
  public String getAddress() {
    return walletAppKit.wallet()
        .currentReceiveAddress()
        .toBase58();
  }

  /**
   * Gets transactions of this wallet.
   */
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

  /**
   * Send coin.
   * Parses an amount expressed in the way humans are used to.
   *
   * @param amountInSatoshis takes string in a format understood by {@link BigDecimal#BigDecimal(String)},
   * for example "0", "1", "0.10", "1.23E3", "1234.5E-5".
   * @throws IllegalArgumentException if you try to specify fractional satoshis, or a value out of range.
   * Or {@code base58ToAddress} is invalid format.
   * @throws InsufficientMoneyException when balance is not enough.
   */
  @SuppressWarnings("JavaDoc")
  @NonNull
  public Single<BtcTx> send(@NonNull String base58ToAddress, @NonNull String amountInSatoshis) {
    return Single.create(emitter -> {
      if (base58ToAddress.isEmpty()) {
        emitter.onError(new IllegalArgumentException("invalid address: " + base58ToAddress));
        return;
      }

      final Address toAddress;
      try {
        toAddress = Address.fromBase58(Constants.NETWORK_PARAMETERS, base58ToAddress);
      } catch (AddressFormatException e) {
        emitter.onError(new IllegalArgumentException("invalid address: " + base58ToAddress, e));
        return;
      }

      if (amountInSatoshis.isEmpty()) {
        emitter.onError(new IllegalArgumentException("invalid amount: " + amountInSatoshis));
        return;
      }

      final Coin value;
      try {
        value = Coin.parseCoin(amountInSatoshis);
      } catch (IllegalArgumentException e) {
        emitter.onError(new IllegalArgumentException("invalid amount", e));
        return;
      }

      if (value.isZero() || value.isNegative()) {
        emitter.onError(new IllegalArgumentException("invalid amount: " + amountInSatoshis));
        return;
      }

      final Wallet wallet = walletAppKit.wallet();
      if (wallet.getBalance().isLessThan(value)) {
        emitter.onError(new InsufficientMoneyException(value));
        return;
      }

      final SendRequest request = SendRequest.to(toAddress, value);
      try {
        wallet.completeTx(request);
        wallet.commitTx(request.tx);
        walletAppKit.peerGroup()
            .broadcastTransaction(request.tx)
            .broadcast();
        emitter.onSuccess(new BtcTx(wallet, request.tx));
      } catch (InsufficientMoneyException e) {
        emitter.onError(e);
      }
    });
  }

  /**
   * Adds a listener called when transaction are received.
   */
  public void addReceivedTxListener(@NonNull ReceivedTxListener listener) {
    walletAppKit.wallet()
        .addCoinsReceivedEventListener((wallet, tx, prevBalance, newBalance) -> {
          Log.d(TAG, "balance: " + getBalance());
          listener.onReceivedTx(new BtcTx(wallet, tx));
        });
  }

  /**
   * Adds a listener called when transaction are send.
   */
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