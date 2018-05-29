package com.hsy.simplebitcoinwallet.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import com.hsy.simplebitcoinwallet.R;
import com.hsy.simplebitcoinwallet.core.BtcWalletManager;

public class MainActivity extends AppCompatActivity {

  /**
   * Launch a new {@link MainActivity} activity. Then finish the passing activity of parameter.
   *
   * @param current activity you want to close.
   */
  public static void startAndFinishCurrent(@NonNull Activity current) {
    current.startActivity(new Intent(current, MainActivity.class));
    current.finish();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    MainFragment.addToActivity(getSupportFragmentManager(), BtcWalletManager.getInstance(this));
  }
}
