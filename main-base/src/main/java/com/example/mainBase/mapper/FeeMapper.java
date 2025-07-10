package com.example.mainBase.mapper;

import com.example.mainBase.dto.FeeAddPayload;
import com.example.mainBase.dto.FeeResponsePayload;
import com.example.mainBase.entities.Fee;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FeeMapper {

  FeeMapper INSTANCE = Mappers.getMapper(FeeMapper.class);

  Fee toFee(FeeAddPayload payload);

  FeeAddPayload toPayload(Fee fee);

  List<FeeResponsePayload> toPayloadList(List<Fee> fees);
}
