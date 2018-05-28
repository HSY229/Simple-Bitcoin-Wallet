package com.hsy.simplebitcoinwallet.main;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hsy.simplebitcoinwallet.BaseView;
import com.hsy.simplebitcoinwallet.R;
import com.hsy.simplebitcoinwallet.core.BtcWalletManager;
import com.hsy.simplebitcoinwallet.databinding.FragmentMainBinding;
import com.hsy.simplebitcoinwallet.utils.ActivityUtils;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends BaseView<MainViewModel, FragmentMainBinding> {

  private TxListAdapter txListAdapter;

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
    setupListAdapter();
    setupRefreshLayout();
    ViewCompat.setNestedScrollingEnabled(binding.txsContainer, false);
    txListAdapter.notifyDataSetChanged();
    if (viewModel != null) {
      viewModel.start(binding.txsContainer.getAdapter());
    }
  }

  private void setupListAdapter() {
    binding.txsContainer.setLayoutManager(new LinearLayoutManager(getContext()));
    txListAdapter = new TxListAdapter(
        getContext(),
        new ArrayList<>(0));
    binding.txsContainer.setAdapter(txListAdapter);

    ViewCompat.setNestedScrollingEnabled(binding.txsContainer, false);
  }

  private void setupRefreshLayout() {
    final SwipeRefreshLayout swipeRefreshLayout = binding.refreshLayout;
    final Activity activity = getActivity();
    if (activity != null) {
      swipeRefreshLayout.setColorSchemeColors(
          ContextCompat.getColor(activity, R.color.colorPrimary),
          ContextCompat.getColor(activity, R.color.colorAccent),
          ContextCompat.getColor(activity, R.color.colorPrimaryDark)
      );
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
