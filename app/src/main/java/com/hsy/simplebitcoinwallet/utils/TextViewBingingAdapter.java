package com.hsy.simplebitcoinwallet.utils;

import android.databinding.BindingAdapter;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

/**
 * This provides methods to help to set image to {@link TextView}.
 */

public class TextViewBingingAdapter {

  /**
   * Set the text color with color resource. This uses data binding,
   * you can use {@code app:textColorRes} attribute for {@link TextView} in your XML layout.
   *
   * @param colorRes is the resource id of color.
   */
  @BindingAdapter("app:textColorRes")
  public static void setTextColorResource(@NonNull TextView view, @ColorRes int colorRes) {
    view.setTextColor(ContextCompat.getColor(view.getContext(), colorRes));
  }

}
