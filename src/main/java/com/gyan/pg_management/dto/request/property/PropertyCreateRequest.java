package com.gyan.pg_management.dto.request.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyCreateRequest {
    private String name;
    private String address;
    private Integer totalFloors;
    private Long ownerId;
}
