package com.f1soft.sces.mapper;

import com.f1soft.sces.dto.FeePayload;
import com.f1soft.sces.entities.Fee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FeeMapper {

  FeeMapper INSTANCE = Mappers.getMapper(FeeMapper.class);

  Fee toFee(FeePayload payload);

  FeePayload toPayload(Fee fee);
}
