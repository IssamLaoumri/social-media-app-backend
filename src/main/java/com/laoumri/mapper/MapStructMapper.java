package com.laoumri.mapper;

import com.laoumri.dto.UpdateUserInfoDto;
import com.laoumri.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MapStructMapper {
    User userUpdateDtoToUser(UpdateUserInfoDto updateUserInfoDto);
}
