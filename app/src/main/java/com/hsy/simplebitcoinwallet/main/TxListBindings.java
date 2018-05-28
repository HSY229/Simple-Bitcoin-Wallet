package com.hsy.simplebitcoinwallet.main;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import com.hsy.simplebitcoinwallet.core.BtcTx;
import java.util.List;

/**
 * Contains {@link BindingAdapter}s for the {@link BtcTx} list.
 */
public class TxListBindings {

  /**
   * Handle to set all items info.
   *
   * @param view recyclerView to show all item info.
   * @param items the transaction detail list for providing data to view.
   */
  @BindingAdapter("items")
  public static void setItems(@NonNull RecyclerView view, @NonNull List<BtcTx> items) {
    TxListAdapter adapter = (TxListAdapter) view.getAdapter();
    if (adapter != null) {
      adapter.replaceData(items);
    }
  }
}