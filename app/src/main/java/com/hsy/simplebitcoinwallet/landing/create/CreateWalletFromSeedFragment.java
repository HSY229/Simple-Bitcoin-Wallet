package com.hsy.simplebitcoinwallet.landing.create;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hsy.simplebitcoinwallet.BaseView;
import com.hsy.simplebitcoinwallet.R;
import com.hsy.simplebitcoinwallet.databinding.FragmentCreateWalletFromSeedBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateWalletFromSeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateWalletFromSeedFragment extends BaseView<CreateWalletFromSeedViewModel, FragmentCreateWalletFromSeedBinding> {

  public CreateWalletFromSeedFragment() {
    // Required empty public constructor
  }

  @NonNull
  public static CreateWalletFromSeedFragment newInstance() {
    return new CreateWalletFromSeedFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_create_wallet_from_seed, container, false);
    binding.setViewModel(viewModel);
    return binding.getRoot();
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (viewModel != null) {
      viewModel.stop();
    }
  }
}
