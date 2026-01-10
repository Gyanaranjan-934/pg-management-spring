package com.gyan.pg_management.mapper;

import com.gyan.pg_management.dto.response.property.PropertyResponse;
import com.gyan.pg_management.entity.Property;

import java.util.Objects;

public final class PropertyMapper {
    private PropertyMapper(){}
    public static PropertyResponse toResponse(Property property){
        Objects.requireNonNull(property,"Property entity must not be not null for mapping");
        return PropertyResponse.builder()
                .propertyId(property.getId())
                .name(property.getName())
                .address(property.getAddress())
                .totalFloors(property.getTotalFloors())
                .isActive(property.getActive())
                .build();
    }
}
