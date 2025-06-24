package com.f1soft.sces;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BCryptHashGenerator {

  private static final BigDecimal THRESHOLD = BigDecimal.valueOf(50);

  public static void main(String[] args) {
    System.out.println(checkThreshold(BigDecimal.valueOf(72000), BigDecimal.valueOf(1)));
  }

  public static Boolean checkThreshold(BigDecimal totalAmount, BigDecimal outstandingAmount) {
    if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) == 0) {
      return false;
    }

    BigDecimal thresholdAmount = totalAmount.multiply(
        THRESHOLD.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
    System.out.println(thresholdAmount);
    return outstandingAmount.compareTo(thresholdAmount) > 0;
  }

}
