package com.hsy.simplebitcoinwallet;

import android.support.annotation.NonNull;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;

public final class Constants {

  @NonNull
  public static final NetworkParameters NETWORK_PARAMETERS = BuildConfig.DEBUG ? TestNet3Params.get() : MainNetParams.get();

  public static final char CHAR_THIN_SPACE = '\u2009';

  public static final int ADDRESS_FORMAT_GROUP_SIZE = 4;
  public static final int ADDRESS_FORMAT_LINE_SIZE = 12;

}
