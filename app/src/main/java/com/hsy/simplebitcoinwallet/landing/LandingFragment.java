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
 * Use the {@link #newInstance()} factory method to
 * create an instance of this fragment.
 */
public class LandingFragment extends BaseView<LandingViewModel, FragmentLandingBinding> {

  /**
   * Default and empty constructor.
   */
  public LandingFragment() {
    // Required empty public constructor
  }

  /**
   * Create and initialize a new instance of this class.
   */
  @NonNull
  public static LandingFragment newInstance() {
    return new LandingFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_landing, container, false);
    if (getActivity() != null) {
      binding.setFragmentManager(getActivity().getSupportFragmentManager());
    }
    binding.setViewModel(viewModel);
    return binding.getRoot();
  }

}
