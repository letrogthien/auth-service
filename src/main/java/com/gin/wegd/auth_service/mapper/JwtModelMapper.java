package com.gin.wegd.auth_service.mapper;

import com.gin.wegd.auth_service.models.JwtModel;
import com.gin.wegd.auth_service.models.dtos.TokenDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface JwtModelMapper {
    JwtModelMapper INSTANCE = Mappers.getMapper(JwtModelMapper.class);

    JwtModel tokenDtoToJwtModel(TokenDto tokenDto);
    TokenDto jwtModelToTokenDto(JwtModel jwtModel);
}
