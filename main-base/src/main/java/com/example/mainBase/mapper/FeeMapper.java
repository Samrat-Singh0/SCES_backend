package com.example.mainBase.mapper;

import com.example.mainBase.dto.FeePayload;
import com.example.mainBase.entities.Fee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FeeMapper {

  FeeMapper INSTANCE = Mappers.getMapper(FeeMapper.class);

  Fee toFee(FeePayload payload);

  FeePayload toPayload(Fee fee);
}
