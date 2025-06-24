package com.f1soft.sces.builder;

import com.f1soft.sces.dto.FeePayload;
import com.f1soft.sces.entities.Enrollment;
import com.f1soft.sces.entities.Fee;
import com.f1soft.sces.util.CommonUtility;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FeeBuilder {

  public Fee build(Enrollment enrollment, FeePayload payload) {
    return Fee.builder()
        .code(CommonUtility.generateCode("FEE-"))
        .enrollment(enrollment)
        .amount(payload.getAmount())
        .build();
  }
}
