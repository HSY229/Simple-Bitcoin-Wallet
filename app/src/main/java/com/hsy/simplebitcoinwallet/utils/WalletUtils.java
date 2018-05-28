package com.hsy.simplebitcoinwallet.utils;

import com.hsy.simplebitcoinwallet.Constants;
import javax.annotation.Nullable;
import org.bitcoinj.core.Address;
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

  @Nullable
  public static Address getToAddressOfSent(final Transaction tx, final Wallet wallet) {
    for (final TransactionOutput output : tx.getOutputs()) {
      try {
        if (!output.isMine(wallet)) {
          final Script script = output.getScriptPubKey();
          return script.getToAddress(Constants.NETWORK_PARAMETERS, true);
        }
      } catch (final ScriptException x) {
        // swallow
      }
    }

    return null;
  }

  @Nullable
  public static Address getWalletAddressOfReceived(final Transaction tx, final Wallet wallet) {
    for (final TransactionOutput output : tx.getOutputs()) {
      try {
        if (output.isMine(wallet)) {
          final Script script = output.getScriptPubKey();
          return script.getToAddress(Constants.NETWORK_PARAMETERS, true);
        }
      } catch (final ScriptException x) {
        // swallow
      }
    }

    return null;
  }
}