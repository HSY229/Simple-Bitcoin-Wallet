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
  public Single<BtcTx> send(@NonNull String base58ToAddress, @NonNull String amountInSatoshis, @NonNull String feeInSatoshis) {
    return Single.create(emitter -> {

      if (isInvalidAddress(base58ToAddress)) {
        emitter.onError(new IllegalArgumentException("invalid address: " + base58ToAddress));
      } else if (isInvalidCoinValue(amountInSatoshis)) {
        emitter.onError(new IllegalArgumentException("invalid amount: " + amountInSatoshis));
      } else if (isInvalidCoinValue(feeInSatoshis)) {
        emitter.onError(new IllegalArgumentException("invalid fee: " + feeInSatoshis));
      } else if (isNotEnoughBalance(amountInSatoshis, feeInSatoshis)) {
        emitter.onError(new InsufficientMoneyException(
            Coin.parseCoin(amountInSatoshis)
                .add(Coin.parseCoin(feeInSatoshis))
                .minus(walletAppKit.wallet().getBalance())
        ));
      } else {
        try {
          final SendRequest request = SendRequest.to(
              Address.fromBase58(Constants.NETWORK_PARAMETERS, base58ToAddress),
              Coin.parseCoin(amountInSatoshis)
          );
          request.feePerKb = Coin.parseCoin(feeInSatoshis);
          walletAppKit.wallet().completeTx(request);
          walletAppKit.wallet().commitTx(request.tx);
          walletAppKit.peerGroup()
              .broadcastTransaction(request.tx)
              .broadcast();
          emitter.onSuccess(new BtcTx(walletAppKit.wallet(), request.tx));
        } catch (InsufficientMoneyException e) {
          emitter.onError(e);
        }
      }
    });
  }

  private boolean isInvalidAddress(@NonNull String base58ToAddress) {
    try {
      Address.fromBase58(Constants.NETWORK_PARAMETERS, base58ToAddress);
      return base58ToAddress.isEmpty();
    } catch (AddressFormatException e) {
      return true;
    }
  }

  private boolean isInvalidCoinValue(@NonNull String valueInSatoshis) {
    try {
      final Coin value = Coin.parseCoin(valueInSatoshis);
      return valueInSatoshis.isEmpty() || value.isZero() || value.isNegative();
    } catch (IllegalArgumentException e) {
      return true;
    }
  }

  private boolean isNotEnoughBalance(@NonNull String amountInSatoshis, @NonNull String feeInSatoshis) {
    final Coin value = Coin.parseCoin(amountInSatoshis);
    final Coin fee = Coin.parseCoin(feeInSatoshis);
    return walletAppKit.wallet()
        .getBalance()
        .isLessThan(value.add(fee));
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