package com.gyan.pg_management.service.property;

import com.gyan.pg_management.dto.request.property.PropertyCreateRequest;
import com.gyan.pg_management.dto.response.property.PropertyResponse;
import com.gyan.pg_management.entity.Property;
import com.gyan.pg_management.entity.User;

public interface PropertyService {
    PropertyResponse createProperty(PropertyCreateRequest request);
    void deactivateProperty(Long propertyId);
}
