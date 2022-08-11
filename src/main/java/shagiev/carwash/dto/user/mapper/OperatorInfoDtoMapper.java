package shagiev.carwash.dto.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import shagiev.carwash.dto.user.operator.OperatorInfoDto;
import shagiev.carwash.model.user.OperatorInfo;

@Mapper(componentModel = "spring")
public interface OperatorInfoDtoMapper extends Converter<OperatorInfo, OperatorInfoDto> {

    @Mapping(source = "entity.carBox.id", target = "carboxId")
    @Mapping(source = "entity.user.id", target = "userId")
    OperatorInfoDto convert(OperatorInfo entity);

}
