package com.hsy.simplebitcoinwallet;

import android.support.annotation.NonNull;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;

public final class Constants {

  @NonNull
  public static final NetworkParameters NETWORK_PARAMETERS = BuildConfig.DEBUG ? TestNet3Params.get() : MainNetParams.get();

  /**
   * Number of confirmations until a transaction is fully confirmed.
   */
  public static final int MAX_NUM_CONFIRMATIONS = 6;
}
