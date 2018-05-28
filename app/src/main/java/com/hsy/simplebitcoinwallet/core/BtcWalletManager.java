package com.hsy.simplebitcoinwallet.core;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.hsy.simplebitcoinwallet.Constants;
import com.hsy.simplebitcoinwallet.utils.FileUtils;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.listeners.DownloadProgressTracker;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.KeyChainGroup;
import org.bitcoinj.wallet.Wallet;

public class BtcWalletManager {

  private static final String TAG = "MY_BIT";

  @NonNull
  private static final String WALLET_FILE_EXTENSION = "wallet";

  @Nullable
  private static BtcWalletManager manager;

  @NonNull
  private final NetworkParameters networkParameters;

  @NonNull
  private final AssetManager assetManager;

  @NonNull
  private final File directory;

  @Nullable
  private WalletAppKit walletAppKit;

  @Nullable
  private ObservableEmitter<Integer> accountObservable;

  @Nullable
  private Observable<Integer> observable;

  private BtcWalletManager(@NonNull NetworkParameters networkParameters, @NonNull AssetManager assetManager, @NonNull File directory) {
    this.networkParameters = networkParameters;
    this.assetManager = assetManager;
    this.directory = directory;
  }

  @NonNull
  public static BtcWalletManager getInstance(@NonNull Context context) {
    if (manager == null) {
      manager = new BtcWalletManager(Constants.NETWORK_PARAMETERS, context.getAssets(), context.getFilesDir());
    }
    return manager;
  }

  public boolean hasWallet() {
    return FileUtils.isExist(directory, WALLET_FILE_EXTENSION);
  }

  public boolean isRunning() {
    return walletAppKit != null && walletAppKit.isRunning();
  }

  /**
   * Generate the random mnemonic.
   *
   * @return mnemonic text.
   */
  @NonNull
  public static String generateMnemonic() {
    final DeterministicSeed seed = new KeyChainGroup(Constants.NETWORK_PARAMETERS)
        .getActiveKeyChain()
        .getSeed();
    if (seed != null && seed.getMnemonicCode() != null) {
      return Joiner.on(" ").join(seed.getMnemonicCode());
    } else {
      throw new RuntimeException("generate mnemonic failed, please try again");
    }
  }

  @NonNull
  public Completable create(@NonNull String mnemonic) {
    return Completable.create(emitter -> {
      if (isValidMnemonic(mnemonic)) {
        create(new DeterministicSeed(mnemonic, null, "", System.currentTimeMillis()), emitter);
      } else {
        emitter.onError(new IllegalArgumentException("invalid mnemonic: " + mnemonic));
      }
    });
  }

  @NonNull
  public Completable create(@NonNull String mnemonic, long creationTimeSecond) {
    return Completable.create(emitter -> {
      if (isValidMnemonic(mnemonic)) {
        create(new DeterministicSeed(mnemonic, null, "", creationTimeSecond), emitter);
      } else {
        emitter.onError(new IllegalArgumentException("invalid mnemonic: " + mnemonic));
      }
    });
  }

  @VisibleForTesting
  public static boolean isValidMnemonic(@NonNull String mnemonic) {
    try {
      MnemonicCode.INSTANCE.check(Splitter.on(" ").splitToList(mnemonic));
      return true;
    } catch (MnemonicException e) {
      return false;
    }
  }

  private void create(@NonNull DeterministicSeed seed, @NonNull CompletableEmitter emitter) {
    walletAppKit = new WalletAppKit(networkParameters, directory, "users_wallet") {
      @Override
      protected void onSetupCompleted() {
        int keyAmount = wallet().getImportedKeys().size();
        Log.i(TAG, "ImportedKeys: " + keyAmount);
        if (keyAmount < 1) {
          wallet().importKey(new ECKey());
        }
        emitter.onComplete();
      }

      @NonNull
      @Override
      protected Wallet createWallet() {
        return Wallet.fromSeed(networkParameters, seed);
      }
    };

    walletAppKit.setDownloadListener(new DownloadProgressTracker() {
      @Override
      protected void progress(double percentage, int blocksSoFar, @NonNull Date date) {
        super.progress(percentage, blocksSoFar, date);
        if (accountObservable != null) {
          accountObservable.onNext((int) percentage);
        }
      }

      @Override
      protected void doneDownload() {
        super.doneDownload();
        if (accountObservable != null) {
          accountObservable.onComplete();
        }
      }
    });

    observable = initDownloadObservable();

    try {
      walletAppKit.setCheckpoints(assetManager.open("checkpoints-testnet.txt"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    walletAppKit.setBlockingStartup(false);
    walletAppKit.startAsync();
    Log.d(TAG, "call WalletAppKit#startAsync()");
  }

  @NonNull
  public Completable launch() {
    return Completable.create(emitter -> {
      if (!isRunning()) {
        walletAppKit = new WalletAppKit(networkParameters, directory, "users_wallet") {
          @Override
          protected void onSetupCompleted() {
            emitter.onComplete();
          }
        };

        walletAppKit.setDownloadListener(new DownloadProgressTracker() {
          @Override
          protected void progress(double percentage, int blocksSoFar, @NonNull Date date) {
            super.progress(percentage, blocksSoFar, date);
            if (accountObservable != null) {
              accountObservable.onNext((int) percentage);
            }
          }

          @Override
          protected void doneDownload() {
            super.doneDownload();
            if (accountObservable != null) {
              accountObservable.onComplete();
            }
          }
        });

        observable = initDownloadObservable();

        try {
          walletAppKit.setCheckpoints(assetManager.open("checkpoints-testnet.txt"));
          walletAppKit.setBlockingStartup(false);
        } catch (IOException e) {
          if (!emitter.isDisposed()) {
            emitter.onError(e);
          }
        }
        Log.d(TAG, "call WalletAppKit#startAsync()");
        walletAppKit.startAsync();
      } else if (!emitter.isDisposed()) {
        emitter.onError(new IllegalStateException("already launch this wallet"));
      }
    });
  }

  @NonNull
  private Observable<Integer> initDownloadObservable() {
    final ConnectableObservable<Integer> connectibleObservable = Observable
        .create((ObservableEmitter<Integer> emitter) -> accountObservable = emitter)
        .publish();
    connectibleObservable.connect();
    return connectibleObservable.share();
  }

  /**
   * Get download observable to know the sync progress.
   *
   * @throws IllegalStateException when wallet is not launched.
   */
  @NonNull
  public Observable<Integer> getDownloadObservable() {
    if (observable != null) {
      return observable;
    } else {
      throw new IllegalStateException("you have to invoke #launch() first");
    }
  }

  @NonNull
  public BtcWallet getCurrent() {
    if (walletAppKit != null) {
      return new BtcWallet(walletAppKit);
    } else {
      throw new IllegalStateException("you have to invoke #launch() first");
    }
  }

  public void shutdown() {
    if (walletAppKit != null) {
      walletAppKit.stopAsync()
          .awaitTerminated();
      walletAppKit = null;
    }
  }

}

