package com.hsy.simplebitcoinwallet.utils;

import android.support.annotation.NonNull;
import java.io.File;

public class FileUtils {

  /**
   * {@code directoryOrFile} file will be deleted.
   */
  public static void delete(@NonNull File directoryOrFile) {
    final File[] contents = directoryOrFile.listFiles();
    if (contents != null) {
      for (File file : contents) {
        delete(file);
      }
    }
    directoryOrFile.delete();
  }

  /**
   * Check is the file with specific extension is exist or not.
   *
   * @param directory is the folder path of that file.
   * @param extension is the file extension of that file such as jpg and txt, etc.
   * @return {@code true} when file is exist, otherwise {@code false}.
   */
  public static boolean isExist(@NonNull File directory, @NonNull String extension) {
    if (!directory.exists()) {
      throw new RuntimeException(directory.getPath() + " is not exist");
    }

    if (!directory.isDirectory()) {
      throw new RuntimeException(directory.getPath() + " is not directory");
    }

    if (!extension.isEmpty()) {
      return directory.listFiles((dir, name) -> name.toLowerCase().endsWith(extension.toLowerCase()))
          .length > 0;
    } else {
      return directory.listFiles((dir, name) -> !name.contains("."))
          .length > 0;
    }
  }

}
