package com.hsy.simplebitcoinwallet.main;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * This is clone from [Google Sample](https://github.com/googlesamples/android-architecture/blob/todo-mvp-rxjava/todoapp/app/src/main/java/com/example/android/architecture/blueprints/todoapp/tasks/ScrollChildSwipeRefreshLayout.java)
 * Extends {@link SwipeRefreshLayout} to support non-direct descendant scrolling views.
 *
 * {@link SwipeRefreshLayout} works as expected when a scroll view is a direct child: it triggers the refresh only when the view is on top. This class
 * adds a way (@link #setScrollUpChild} to define which view controls this behavior.
 */
public class ScrollChildSwipeRefreshLayout extends SwipeRefreshLayout {

  private static final int DIRECTION_UP = -1;

  @Nullable
  private View scrollUpChild;

  public ScrollChildSwipeRefreshLayout(@NonNull Context context) {
    super(context);
  }

  public ScrollChildSwipeRefreshLayout(@NonNull Context context, @NonNull AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean canChildScrollUp() {
    return scrollUpChild != null ? scrollUpChild.canScrollVertically(DIRECTION_UP) : super.canChildScrollUp();
  }

  public void setScrollUpChild(@Nullable View scrollUpChild) {
    this.scrollUpChild = scrollUpChild;
  }

  /**
   * Reloads the data when the pull-to-refresh is triggered.
   * Creates the {@code android:onRefresh} for a {@link SwipeRefreshLayout}.
   */
  @BindingAdapter("android:onRefresh")
  public static void setSwipeRefreshLayoutOnRefreshListener(@NonNull SwipeRefreshLayout view, @NonNull MainViewModel viewModel) {
    view.setOnRefreshListener(viewModel::loadTxs);
  }
}