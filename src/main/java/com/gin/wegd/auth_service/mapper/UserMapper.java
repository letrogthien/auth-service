package com.gin.wegd.auth_service.mapper;


import com.gin.wegd.auth_service.models.User;
import com.gin.wegd.auth_service.models.requests.RegisterRq;
import com.gin.wegd.auth_service.models.requests.UpdateUserRq;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User userDtoToUser(RegisterRq registerRq);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User updateUserRqToUser(UpdateUserRq updateUserRq,@MappingTarget User user);
}
