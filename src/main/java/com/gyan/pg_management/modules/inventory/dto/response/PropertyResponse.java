package com.gyan.pg_management.modules.inventory.dto.response;

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
