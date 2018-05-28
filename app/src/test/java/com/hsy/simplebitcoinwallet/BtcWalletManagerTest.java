package com.hsy.simplebitcoinwallet;

import static org.assertj.core.api.Assertions.assertThat;

import com.hsy.simplebitcoinwallet.core.BtcWalletManager;
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
}