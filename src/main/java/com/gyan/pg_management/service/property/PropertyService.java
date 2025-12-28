package com.gyan.pg_management.service.property;

import com.gyan.pg_management.entity.Property;
import com.gyan.pg_management.entity.User;

public interface PropertyService {
    Property createProperty(
            String name,
            String address,
            Integer totalFloors,
            User owner
    );
    void deactivateProperty(Long propertyId);
}
