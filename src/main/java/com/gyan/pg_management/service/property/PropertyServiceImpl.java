package com.gyan.pg_management.service.property;

import com.gyan.pg_management.entity.Property;
import com.gyan.pg_management.entity.User;
import com.gyan.pg_management.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService{

    private final PropertyRepository propertyRepository;

    @Override
    public Property createProperty(
            String name,
            String address,
            Integer totalFloors,
            User owner
    ) {
        Property property = Property.builder()
                .name(name)
                .address(address)
                .totalFloors(totalFloors)
                .owner(owner)
                .active(true)
                .build();

        return propertyRepository.save(property);
    }

    @Override
    public void deactivateProperty(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found"));

        property.setActive(false);
        propertyRepository.save(property);
    }
}
