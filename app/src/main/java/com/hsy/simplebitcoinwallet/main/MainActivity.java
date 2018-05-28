package com.hsy.simplebitcoinwallet.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.hsy.simplebitcoinwallet.R;
import com.hsy.simplebitcoinwallet.core.BtcWalletManager;

public class MainActivity extends AppCompatActivity {

  public static void startAndFinishCurrent(@NonNull Activity current) {
    current.startActivity(new Intent(current, MainActivity.class));
    current.finish();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    MainFragment.addToActivity(getSupportFragmentManager(), BtcWalletManager.getInstance(this));
  }
}
