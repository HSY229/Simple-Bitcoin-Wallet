package com.hsy.simplebitcoinwallet.main;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;

public class PushUpBehavior extends CoordinatorLayout.Behavior<View> {

  @Override
  public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
    return dependency instanceof Snackbar.SnackbarLayout;
  }

  @Override
  public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
    float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
    child.setTranslationY(translationY);
    return true;
  }
}