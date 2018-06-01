package com.hsy.simplebitcoinwallet.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.TypefaceSpan;
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

  @NonNull
  public static Spanned formatHash(final String address, final int groupSize, final int lineSize) {
    return formatHash(address, groupSize, lineSize, Constants.CHAR_THIN_SPACE);
  }

  @NonNull
  private static Spanned formatHash(final String address, final int groupSize, final int lineSize, final char groupSeparator) {
    final SpannableStringBuilder builder = new SpannableStringBuilder();
    final int len = address.length();
    for (int i = 0; i < len; i += groupSize) {
      final int end = i + groupSize;
      final String part = address.substring(i, end < len ? end : len);

      builder.append(part);
      builder.setSpan(new MonospaceSpan(), builder.length() - part.length(), builder.length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      if (end < len) {
        final boolean endOfLine = lineSize > 0 && end % lineSize == 0;
        builder.append(endOfLine ? '\n' : groupSeparator);
      }
    }

    return SpannedString.valueOf(builder);
  }

  private static class MonospaceSpan extends TypefaceSpan {

    MonospaceSpan() {
      super("monospace");
    }

    // TypefaceSpan doesn't implement this, and we need it so that Spanned.equals() works.
    @Override
    public boolean equals(@Nullable final Object o) {
      return o == this || o != null && o.getClass() == getClass();
    }

    @Override
    public int hashCode() {
      return 0;
    }
  }

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