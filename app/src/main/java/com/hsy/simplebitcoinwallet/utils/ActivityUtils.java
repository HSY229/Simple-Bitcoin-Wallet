package com.hsy.simplebitcoinwallet.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * This provides methods to help Activities load their UI.
 */
public final class ActivityUtils {

  /**
   * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
   * performed by the {@code fragmentManager}.
   */
  public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId) {
    checkNotNull(fragmentManager);
    checkNotNull(fragment);
    final FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.add(frameId, fragment);
    transaction.commitAllowingStateLoss();
  }

  /**
   * The {@code fragment} is replaced to the container view with id {@code frameId}. The operation
   * is performed by the {@code fragmentManager}.
   */
  public static void replaceAndKeepOld(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId) {
    checkNotNull(fragmentManager);
    checkNotNull(fragment);
    final FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(frameId, fragment);
    transaction.addToBackStack(null);
    transaction.commitAllowingStateLoss();
  }

  /**
   * The {@code fragment} is replaced to the container view with id {@code frameId}. The operation
   * is performed by the {@code fragmentManager}.
   */
  public static void replace(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId) {
    checkNotNull(fragmentManager);
    checkNotNull(fragment);
    final FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(frameId, fragment);
    transaction.commitAllowingStateLoss();
  }
}
