package com.hsy.simplebitcoinwallet.landing;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import com.hsy.simplebitcoinwallet.BaseViewModel;
import com.hsy.simplebitcoinwallet.R;
import com.hsy.simplebitcoinwallet.core.BtcWalletManager;
import com.hsy.simplebitcoinwallet.landing.create.CreateWalletFromSeedFragment;
import com.hsy.simplebitcoinwallet.landing.create.CreateWalletFromSeedViewModel;
import com.hsy.simplebitcoinwallet.utils.ActivityUtils;

public class LandingViewModel extends BaseViewModel {

  @NonNull
  private final BtcWalletManager btcWalletManager;

  LandingViewModel(@NonNull BtcWalletManager btcWalletManager) {
    this.btcWalletManager = btcWalletManager;
  }

  public void goToCreateWalletPage(@NonNull FragmentManager fragmentManager) {
    final CreateWalletFromSeedFragment fragment = CreateWalletFromSeedFragment.newInstance();
    fragment.setViewModel(new CreateWalletFromSeedViewModel(btcWalletManager));
    ActivityUtils.replaceAndKeepOld(fragmentManager, fragment, R.id.contentFrame);
  }

}
