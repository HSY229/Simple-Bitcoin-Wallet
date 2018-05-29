package com.hsy.simplebitcoinwallet;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;
import static com.google.common.base.Preconditions.checkNotNull;

import android.databinding.Observable;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

/**
 * This is the view of Model-View-ViewModel.
 */

public abstract class BaseView<M extends BaseViewModel, T extends ViewDataBinding> extends Fragment {

  protected T binding;

  @NonNull
  private final Observable.OnPropertyChangedCallback snackBarCallback = new Observable.OnPropertyChangedCallback() {
    @Override
    public void onPropertyChanged(@NonNull Observable observable, int i) {
      final int messageRes = viewModel != null ? viewModel.getSnackBarMessageResId() : 0;
      if (messageRes != 0) {
        Snackbar.make(binding.getRoot(), messageRes, LENGTH_SHORT)
            .show();
      }
    }
  };

  @Nullable
  protected M viewModel;

  public void setViewModel(@NonNull M viewModel) {
    this.viewModel = checkNotNull(viewModel);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if (viewModel != null) {
      viewModel.addOnMessageChangedCallback(snackBarCallback);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (viewModel != null) {
      viewModel.removeOnMessageChangedCallback(snackBarCallback);
    }
  }
}
