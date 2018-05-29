package com.hsy.simplebitcoinwallet.restore;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hsy.simplebitcoinwallet.BaseView;
import com.hsy.simplebitcoinwallet.R;
import com.hsy.simplebitcoinwallet.databinding.FragmentRestoreWalletFromSeedBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestoreWalletFromSeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestoreWalletFromSeedFragment extends BaseView<RestoreWalletFromSeedViewModel, FragmentRestoreWalletFromSeedBinding> {

  /**
   * Default and empty constructor.
   */
  public RestoreWalletFromSeedFragment() {
    // Required empty public constructor
  }

  /**
   * Create and initialize a new instance of this class.
   */
  @NonNull
  public static RestoreWalletFromSeedFragment newInstance() {
    return new RestoreWalletFromSeedFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_restore_wallet_from_seed, container, false);
    if (getActivity() != null) {
      binding.setFragmentManager(getActivity().getSupportFragmentManager());
    }
    binding.setViewModel(viewModel);
    return binding.getRoot();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (viewModel != null) {
      viewModel.stop();
    }
  }


}
