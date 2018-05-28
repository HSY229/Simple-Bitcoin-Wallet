package com.hsy.simplebitcoinwallet.utils;

import android.support.annotation.NonNull;
import java.io.File;
import java.io.FileOutputStream;

/**
 * This provides methods to help write text to file.
 */

public final class FileWriter {

  /**
   * Write content into file.
   */
  public static void save(@NonNull File file, @NonNull String content) {
    final FileOutputStream outputStream;
    try {
      outputStream = new FileOutputStream(file);
      outputStream.write(content.getBytes("UTF-8"));
      outputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}