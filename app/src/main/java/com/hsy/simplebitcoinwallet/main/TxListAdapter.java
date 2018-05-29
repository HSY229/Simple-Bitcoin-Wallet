package com.hsy.simplebitcoinwallet.main;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.hsy.simplebitcoinwallet.BaseBindingAdapter;
import com.hsy.simplebitcoinwallet.R;
import com.hsy.simplebitcoinwallet.core.BtcTx;
import com.hsy.simplebitcoinwallet.databinding.TxItemBinding;
import com.hsy.simplebitcoinwallet.main.item.TxItemViewModel;
import java.util.List;

class TxListAdapter extends BaseBindingAdapter<BtcTx, TxItemBinding> {

  @NonNull
  private final Context context;

  TxListAdapter(@NonNull Context context, @NonNull List<BtcTx> transactions) {
    super(context, transactions);
    this.context = context;
  }

  @LayoutRes
  @Override
  protected int getLayoutResId(int viewType) {
    return R.layout.tx_item;
  }

  @Override
  protected void onBindItem(@Nullable TxItemBinding binding, @NonNull BtcTx transaction) {
    final TxItemViewModel viewModel = new TxItemViewModel();
    viewModel.setItem(context, transaction);
    if (binding != null) {
      binding.setViewModel(viewModel);
      binding.executePendingBindings();
    }
  }
}