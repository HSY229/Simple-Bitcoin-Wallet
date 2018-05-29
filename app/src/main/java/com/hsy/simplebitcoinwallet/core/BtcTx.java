package com.hsy.simplebitcoinwallet.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.hsy.simplebitcoinwallet.utils.WalletUtils;
import java.util.Date;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionConfidence.ConfidenceType;
import org.bitcoinj.wallet.Wallet;

public class BtcTx {

  @NonNull
  private final Wallet wallet;

  @NonNull
  private final Transaction transaction;

  BtcTx(@NonNull Wallet wallet, @NonNull Transaction transaction) {
    this.wallet = wallet;
    this.transaction = transaction;
  }

  @NonNull
  public Date getUpdateTime() {
    return transaction.getUpdateTime();
  }

  public boolean isSent() {
    return transaction.getValue(wallet)
        .signum() < 0;
  }

  @NonNull
  public String getHash() {
    return transaction.getHashAsString();
  }

  @NonNull
  public String getAddress() {
    return isSent() ? WalletUtils.getToAddressOfSent(transaction, wallet)
        : WalletUtils.getWalletAddressOfReceived(transaction, wallet);
  }

  @Nullable
  public String getFriendlyStringFee() {
    return transaction.getFee() != null ? transaction.getFee().toFriendlyString() : null;
  }

  @NonNull
  public String getFriendlyStringValue() {
    return transaction.getValue(wallet).toFriendlyString();
  }

  public int getConfirmation() {
    return transaction.getConfidence()
        .getDepthInBlocks();
  }

  @NonNull
  public String getStatus() {
    return convertType(transaction.getConfidence().getConfidenceType());
  }

  @NonNull
  private static String convertType(@NonNull ConfidenceType confidenceType) {
    switch (confidenceType) {
      case BUILDING: {
        return "BUILDING";
      }
      case PENDING: {
        return "PENDING";
      }
      case IN_CONFLICT: {
        return "IN CONFLICT";
      }
      case DEAD: {
        return "DEAD";
      }
      default: {
        return "UNKNOWN";
      }
    }
  }

}
