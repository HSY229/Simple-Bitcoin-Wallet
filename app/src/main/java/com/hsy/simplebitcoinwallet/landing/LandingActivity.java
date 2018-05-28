package com.hsy.simplebitcoinwallet.landing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.hsy.simplebitcoinwallet.core.BtcWalletManager;
import com.hsy.simplebitcoinwallet.R;
import com.hsy.simplebitcoinwallet.main.MainActivity;
import com.hsy.simplebitcoinwallet.utils.ActivityUtils;

public class LandingActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_landing);

    if (BtcWalletManager.getInstance(this).hasWallet()) {
      MainActivity.startAndFinishCurrent(this);
    } else {
      showLandingPage();
    }
  }

  private void showLandingPage() {
    final LandingFragment fragment = LandingFragment.newInstance();
    fragment.setViewModel(new LandingViewModel(BtcWalletManager.getInstance(this)));
    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
  }
}
