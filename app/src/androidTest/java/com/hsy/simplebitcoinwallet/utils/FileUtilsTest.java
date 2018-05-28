package com.hsy.simplebitcoinwallet.utils;

import static org.assertj.core.api.Assertions.assertThat;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FileUtilsTest {

  private Context context;

  private File directory;

  @Before
  public void setUp() {
    context = InstrumentationRegistry.getTargetContext();
    directory = context.getFilesDir();
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
  }

  @Test
  public void isExist_shouldThrowExceptionWhenInputIsNotDirectory() {
    // arrange
    final File file = new File(directory, "test.orange");
    FileWriter.save(file, "");

    // act
    try {
      FileUtils.isExist(file, "orange");
    } catch (Exception e) {
      assertThat(e).hasMessage(file.getPath() + " is not directory");
    }
  }

  @Test
  public void isExist_shouldThrowExceptionWhenDirectoryIsNotExist() {
    // arrange
    final File file = new File(directory, "test");
    assertThat(file)
        .doesNotExist();

    // act
    try {
      FileUtils.isExist(file, "orange");
    } catch (Exception e) {
      assertThat(e).hasMessage(file.getPath() + " is not exist");
    }
  }

  @Test
  public void isExist_shouldReturnFalseWhenNoFileWithSpecificExtension() {
    // arrange
    assertThat(directory.listFiles())
        .isEmpty();

    // act
    final boolean result = FileUtils.isExist(directory, "orange");

    // assert
    assertThat(result)
        .describedAs("is file exist")
        .isFalse();
  }

  @Test
  public void isExist_shouldReturnFalseWhenNoFileWithEmptyExtension() {
    // arrange
    FileWriter.save(new File(directory, "test.orange"), "");
    assertThat(directory.listFiles())
        .hasSize(1);

    // act
    final boolean result = FileUtils.isExist(directory, "");

    // assert
    assertThat(result)
        .describedAs("is file exist")
        .isFalse();
  }

  @Test
  public void isExist_shouldReturnTrueWhenHasOneFileWithSpecificExtension() {
    // arrange
    FileWriter.save(new File(directory, "test.orange"), "");
    assertThat(directory.listFiles())
        .hasSize(1);

    // act
    final boolean result = FileUtils.isExist(directory, "orange");

    // assert
    assertThat(result)
        .describedAs("is file exist")
        .isTrue();
  }

  @Test
  public void isExist_shouldReturnTrueWhenHasOneFileWithEmptyExtension() {
    // arrange
    FileWriter.save(new File(directory, "test2"), "");
    assertThat(directory.listFiles())
        .hasSize(1);

    // act
    final boolean result = FileUtils.isExist(directory, "");

    // assert
    assertThat(result)
        .describedAs("is file exist")
        .isTrue();
  }
}