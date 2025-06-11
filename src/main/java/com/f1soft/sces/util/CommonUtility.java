package com.f1soft.sces.util;

import java.util.Random;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonUtility {

  private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final int code_length = 4;
  private static final Random RANDOM = new Random();

  public String generateUserCode(String prefix) {
    return prefix + System.currentTimeMillis();
  }

  public static String generateCourseCode(String prefix) {
    StringBuilder code = new StringBuilder(code_length);
    for (int i = 0; i < code_length; i++) {
      int index = RANDOM.nextInt(CHAR_POOL.length());
      code.append(CHAR_POOL.charAt(index));
    }
    return code.toString();
  }

}
