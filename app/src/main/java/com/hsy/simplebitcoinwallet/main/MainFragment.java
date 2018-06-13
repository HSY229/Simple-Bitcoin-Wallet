package com.hsy.simplebitcoinwallet.main;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.hsy.simplebitcoinwallet.BaseView;
import com.hsy.simplebitcoinwallet.R;
import com.hsy.simplebitcoinwallet.core.BtcWalletManager;
import com.hsy.simplebitcoinwallet.databinding.FragmentMainBinding;
import com.hsy.simplebitcoinwallet.main.export.ExportToMnemonicDialogFragment;
import com.hsy.simplebitcoinwallet.utils.ActivityUtils;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link #newInstance()} factory method to create an instance of this fragment.
 */
public class MainFragment extends BaseView<MainViewModel, FragmentMainBinding> {

  @NonNull
  private static final String EXPORT_DIALOG_TAG = "EXPORT_TO_MNEMONIC";

  private TxListAdapter txListAdapter;

  /**
   * Call {@link #newInstance()} to create this fragment, and then give it {@link MainViewModel}.
   * Finally, add it to activity.
   */
  public static void addToActivity(@NonNull FragmentManager fragmentManager, @NonNull BtcWalletManager btcWalletManager) {
    final MainFragment view = newInstance();
    view.setViewModel(new MainViewModel(btcWalletManager));
    ActivityUtils.addFragmentToActivity(fragmentManager, view, R.id.contentFrame);
  }

  /**
   * Default and empty constructor.
   */
  public MainFragment() {
    // Required empty public constructor
  }

  /**
   * Create and initialize a new instance of this class.
   */
  @NonNull
  public static MainFragment newInstance() {
    return new MainFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @NonNull
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
    if (getActivity() != null) {
      binding.setFragmentManager(getActivity().getSupportFragmentManager());
    }
    binding.setViewModel(viewModel);
    return binding.getRoot();
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    setUpActionBar();
    setupListAdapter();
    setupRefreshLayout();
    txListAdapter.notifyDataSetChanged();
    final Adapter adapter = binding.txsContainer.getAdapter();
    if (viewModel != null && adapter != null) {
      viewModel.start(adapter);
    }
  }

  private void setUpActionBar() {
    final AppCompatActivity appCompatActivity = ((AppCompatActivity) getActivity());
    if (appCompatActivity != null) {
      appCompatActivity.setSupportActionBar(binding.toolbar);
    }
    binding.toolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
    binding.toolbarLayout.setExpandedTitleColor(Color.YELLOW);
  }

  private void setupListAdapter() {
    final Context context = getContext();
    if (context != null) {
      binding.txsContainer.setLayoutManager(new LinearLayoutManager(context));
      txListAdapter = new TxListAdapter(context, new ArrayList<>(0));
      binding.txsContainer.setAdapter(txListAdapter);
    }

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
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    inflater.inflate(R.menu.main, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.actionExport:
        showExportDialog();
        break;
      default:
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * Launch the create wallet page.
   */
  private void showExportDialog() {
    final FragmentActivity activity = getActivity();
    if (activity != null) {
      ExportToMnemonicDialogFragment.newInstance()
          .show(activity.getSupportFragmentManager(), EXPORT_DIALOG_TAG);
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
