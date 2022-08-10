package shagiev.carwash.dto.entry.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import shagiev.carwash.dto.entry.EntryInfoDto;
import shagiev.carwash.model.entry.Entry;

@Mapper(componentModel = "spring")
public interface EntryInfoDtoMapper extends Converter<Entry, EntryInfoDto> {

    @Mapping(source = "entry.carBox.id", target = "carboxId")
    @Mapping(source = "entry.carwashService.id", target = "serviceId")
    @Mapping(source = "entry.user.id", target = "userId")
    EntryInfoDto convert(Entry entry);

}
