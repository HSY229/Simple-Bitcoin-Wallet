package com.hsy.simplebitcoinwallet.landing;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hsy.simplebitcoinwallet.BaseView;
import com.hsy.simplebitcoinwallet.R;
import com.hsy.simplebitcoinwallet.databinding.FragmentLandingBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LandingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandingFragment extends BaseView<LandingViewModel, FragmentLandingBinding> {

  public LandingFragment() {
    // Required empty public constructor
  }

  @NonNull
  public static LandingFragment newInstance() {
    return new LandingFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_landing, container, false);
    binding.setFragmentManager(getActivity().getSupportFragmentManager());
    binding.setViewModel(viewModel);
    return binding.getRoot();
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

}
