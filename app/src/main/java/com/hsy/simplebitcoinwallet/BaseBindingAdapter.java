package com.hsy.simplebitcoinwallet;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;

public abstract class BaseBindingAdapter<M, B extends ViewDataBinding> extends RecyclerView.Adapter {

  @NonNull
  private final Context context;

  protected List<M> items;

  public BaseBindingAdapter(@NonNull Context context, @NonNull List<M> items) {
    this.context = context;
    this.items = items;
  }

  public void replaceData(@NonNull List<M> items) {
    this.items = items;
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return items != null ? items.size() : 0;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    B binding = DataBindingUtil
        .inflate(LayoutInflater.from(context), getLayoutResId(viewType), parent, false);
    return new BaseBindingViewHolder(binding.getRoot());
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    holder.itemView.setBackgroundColor(0xFFFFFFFF);
    B binding = DataBindingUtil.getBinding(holder.itemView);
    onBindItem(binding, items.get(position));
  }

  @SuppressWarnings("SameReturnValue")
  @LayoutRes
  protected abstract int getLayoutResId(int viewType);

  protected abstract void onBindItem(@NonNull B binding, @NonNull M item);
}