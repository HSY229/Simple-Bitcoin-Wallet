package com.hsy.simplebitcoinwallet.main;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;

@CoordinatorLayout.DefaultBehavior(PushUpBehavior.class)
public class PushUpConstraintLayout extends ConstraintLayout {

  public PushUpConstraintLayout(Context context) {
    super(context);
  }

  public PushUpConstraintLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public PushUpConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }
}