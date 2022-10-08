package com.example.testtaskoveronix.service.converter;

public interface Converter<Entity, Dto, Response> {
    Entity toEntity(Dto dto);

    Response toDto(Entity entity);
}
