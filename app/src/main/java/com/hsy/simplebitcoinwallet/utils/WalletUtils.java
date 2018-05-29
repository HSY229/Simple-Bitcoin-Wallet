package com.hsy.simplebitcoinwallet.utils;

import android.support.annotation.NonNull;
import com.hsy.simplebitcoinwallet.Constants;
import org.bitcoinj.core.ScriptException;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.Wallet;

/**
 * https://github.com/bitcoin-wallet/bitcoin-wallet/blob/master/wallet/src/de/schildbach/wallet/util/WalletUtils.java
 *
 * @author Andreas Schildbach
 */
public class WalletUtils {

  /**
   * Parse transaction to get its to address.
   *
   * @param wallet should be a party of this transaction.
   * @return to address of transaction when this transaction is send from the specific wallet, otherwise return empty {@link String}.
   */
  @NonNull
  public static String getToAddressOfSent(final Transaction tx, final Wallet wallet) {
    for (final TransactionOutput output : tx.getOutputs()) {
      try {
        if (!output.isMine(wallet)) {
          final Script script = output.getScriptPubKey();
          return script.getToAddress(Constants.NETWORK_PARAMETERS, true)
              .toString();
        }
      } catch (final ScriptException x) {
        // swallow
      }
    }

    return "";
  }

  /**
   * Parse transaction to get its from address.
   *
   * @param wallet should be a party of this transaction.
   * @return from address of transaction when this transaction is received from the specific wallet, otherwise return empty {@link String}.
   */
  @NonNull
  public static String getWalletAddressOfReceived(final Transaction tx, final Wallet wallet) {
    for (final TransactionOutput output : tx.getOutputs()) {
      try {
        if (output.isMine(wallet)) {
          final Script script = output.getScriptPubKey();
          return script.getToAddress(Constants.NETWORK_PARAMETERS, true)
              .toString();
        }
      } catch (final ScriptException x) {
        // swallow
      }
    }

    return "";
  }
}