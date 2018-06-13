package com.hsy.simplebitcoinwallet.main.export;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import com.hsy.simplebitcoinwallet.R;
import com.hsy.simplebitcoinwallet.core.BtcWalletManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExportToMnemonicDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExportToMnemonicDialogFragment extends DialogFragment {

  private Activity activity;

  public ExportToMnemonicDialogFragment() {
    // Required empty public constructor
  }

  @NonNull
  public static ExportToMnemonicDialogFragment newInstance() {
    return new ExportToMnemonicDialogFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    return new AlertDialog.Builder(activity)
        .setTitle(R.string.export_to_mnemonic_title)
        .setMessage(BtcWalletManager.getInstance(activity).getCurrent().getMnemonic())
        .setNeutralButton(R.string.export_to_mnemonic_confirm, (dialogInterface, i) -> dismiss())
        .setCancelable(true)
        .create();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    activity = null;
  }
}
