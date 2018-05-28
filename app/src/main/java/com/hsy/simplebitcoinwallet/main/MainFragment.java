package com.hsy.simplebitcoinwallet.main;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hsy.simplebitcoinwallet.BaseView;
import com.hsy.simplebitcoinwallet.R;
import com.hsy.simplebitcoinwallet.core.BtcWalletManager;
import com.hsy.simplebitcoinwallet.databinding.FragmentMainBinding;
import com.hsy.simplebitcoinwallet.utils.ActivityUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends BaseView<MainViewModel, FragmentMainBinding> {

  public static void addToActivity(@NonNull FragmentManager fragmentManager, @NonNull BtcWalletManager btcWalletManager) {
    final MainFragment view = newInstance();
    view.setViewModel(new MainViewModel(btcWalletManager));
    ActivityUtils.addFragmentToActivity(fragmentManager, view, R.id.contentFrame);
  }

  public MainFragment() {
    // Required empty public constructor
  }

  @NonNull
  public static MainFragment newInstance() {
    return new MainFragment();
  }

  @NonNull
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_main, container, false);
    binding.setViewModel(viewModel);
    return binding.getRoot();
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if (viewModel != null) {
      viewModel.start();
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (viewModel != null) {
      viewModel.stop();
    }
  }
}
