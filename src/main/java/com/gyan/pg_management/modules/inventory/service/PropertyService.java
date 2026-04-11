package com.gyan.pg_management.modules.inventory.service;

import com.gyan.pg_management.modules.inventory.dto.request.PropertyCreateRequest;
import com.gyan.pg_management.modules.inventory.dto.response.PropertyResponse;
import com.gyan.pg_management.modules.inventory.domain.Property;
import com.gyan.pg_management.modules.identity.domain.User;

public interface PropertyService {
    PropertyResponse createProperty(PropertyCreateRequest request);
    void deactivateProperty(Long propertyId);
    PropertyResponse[] getAllProperties(Long ownerId);
}
