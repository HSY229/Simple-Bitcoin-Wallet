package com.hsy.simplebitcoinwallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.res.Resources.NotFoundException;
import com.hsy.simplebitcoinwallet.core.BtcWallet;
import com.hsy.simplebitcoinwallet.core.BtcWalletManager;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BtcWalletManagerTest {

  @Test
  public void isValidMnemonic_shouldReturnFalseWhenEmptyMnemonic() {
    // arrange
    final String mnemonic = "";

    // act
    final boolean result = BtcWalletManager.isValidMnemonic(mnemonic);

    // assert
    assertThat(result).isFalse();
  }

  @Test
  public void isValidMnemonic_shouldReturnFalseWhenMnemonicHasDuplicatedWord() {
    // arrange
    final String mnemonic = "census flame uncle toe pony inject discover forget jazz giggle discover escape";

    // act
    final boolean result = BtcWalletManager.isValidMnemonic(mnemonic);

    // assert
    assertThat(result).isFalse();
  }

  @Test
  public void isValidMnemonic() {
    // arrange
    final String mnemonic = "afraid chat torch empty diagram course south cricket slight kitchen galaxy motor";

    // act
    final boolean result = BtcWalletManager.isValidMnemonic(mnemonic);

    // assert
    assertThat(result).isTrue();
  }

  @Test
  public void generateMnemonic() {
    // act
    final String result = BtcWalletManager.generateMnemonic();

    // assert
    assertThat(BtcWalletManager.isValidMnemonic(result)).isTrue();
  }

  @Test(expected = NotFoundException.class)
  public void getMnemonic_shouldThrowExceptionWhenNoMnemonic() {
    // arrange
    final DeterministicSeed seed = mock(DeterministicSeed.class);
    when(seed.getMnemonicCode()).thenReturn(null);

    final Wallet wallet = mock(Wallet.class);
    when(wallet.getKeyChainSeed()).thenReturn(seed);

    final WalletAppKit walletAppKit = mock(WalletAppKit.class);
    when(walletAppKit.wallet()).thenReturn(wallet);

    final BtcWallet btcWallet = new BtcWallet(walletAppKit);

    // act
    btcWallet.getMnemonic();
  }
}