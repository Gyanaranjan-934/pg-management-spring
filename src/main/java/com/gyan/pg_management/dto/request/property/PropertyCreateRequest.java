package com.gyan.pg_management.dto.request.property;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PropertyCreateRequest {
    private String name;
    private String address;
    private Integer totalFloors;
    private Long ownerId;
}
