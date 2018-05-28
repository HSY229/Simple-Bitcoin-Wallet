package com.hsy.simplebitcoinwallet;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.hsy.simplebitcoinwallet.core.BtcWalletManager;
import com.hsy.simplebitcoinwallet.utils.FileUtils;
import com.hsy.simplebitcoinwallet.utils.FileWriter;
import io.reactivex.observers.TestObserver;
import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)

public class BtcWalletManagerTest {

  private Context context;

  private File directory;

  private BtcWalletManager manager;

  @Before
  public void setUp() {
    context = InstrumentationRegistry.getTargetContext();
    directory = context.getFilesDir();

    manager = BtcWalletManager.getInstance(context);
  }

  @After
  public void tearDown() {
    FileUtils.delete(directory);
  }

  @Test
  public void testPreConditions() {
    assertThat(context)
        .as("instance of Context")
        .isNotNull();

    assertThat(directory)
        .isDirectory()
        .exists();

    assertThat(manager)
        .as("instance of BtcWalletManager")
        .isNotNull();
  }

  @Test
  public void hasWallet_shouldReturnFalseWhenNoWallet() {
    // arrange
    assertThat(directory.listFiles()).isEmpty();

    // act
    final boolean result = manager.hasWallet();

    // assert
    assertThat(result)
        .describedAs("has least one wallet or not")
        .isFalse();
  }

  @Test
  public void hasWallet_shouldReturnTrueWhenHasOneWallet() {
    // arrange
    FileWriter.save(new File(directory, "test.wallet"), "");
    assertThat(directory.listFiles()).hasSize(1);

    // act
    final boolean result = manager.hasWallet();

    // assert
    assertThat(result)
        .describedAs("has least one wallet or not")
        .isTrue();
  }

  @Test
  public void create_shouldNotifyClientErrorWhenMnemonicIsNotValid() {
    // arrange
    final String mnemonic = "";
    final TestObserver tester = new TestObserver();

    // act
    manager.create(mnemonic)
        .subscribe(tester);

    // assert
    tester.assertSubscribed();
    tester.assertNotComplete();
    tester.assertError(IllegalArgumentException.class);
    tester.assertErrorMessage("invalid mnemonic: " + mnemonic);
  }

  @Test
  public void create_shouldNotifyClientWhenSuccessful() throws Exception {
    // arrange
    final String mnemonic = "census flame uncle toe pony inject discover forget jazz giggle ugly escape";
    final TestObserver tester = new TestObserver();

    // act
    manager.create(mnemonic)
        .subscribe(tester);

    // assert
    tester.await(10, SECONDS);
    tester.assertSubscribed()
        .assertNoErrors()
        .assertComplete();
  }
}