package com.example.mainBase.builder;

import com.example.mainBase.dto.FeeAddPayload;
import com.example.mainBase.entities.Enrollment;
import com.example.mainBase.entities.Fee;
import com.example.mainBase.util.CommonUtility;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FeeBuilder {

  public Fee build(Enrollment enrollment, FeeAddPayload payload) {
    return Fee.builder()
        .code(CommonUtility.generateCode("FEE-"))
        .enrollment(enrollment)
        .amount(payload.getAmount())
        .build();
  }
}
