package com.gyan.pg_management.dto.response.property;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PropertyResponse {
    Long propertyId;
    String name;
    String address;
    Integer totalFloors;
    Boolean isActive;
}
