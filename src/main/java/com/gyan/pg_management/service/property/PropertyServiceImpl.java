package com.gyan.pg_management.service.property;

import com.gyan.pg_management.dto.request.property.PropertyCreateRequest;
import com.gyan.pg_management.dto.response.property.PropertyResponse;
import com.gyan.pg_management.entity.Property;
import com.gyan.pg_management.entity.User;
import com.gyan.pg_management.exceptions.user.UserNotFoundException;
import com.gyan.pg_management.mapper.PropertyMapper;
import com.gyan.pg_management.repository.PropertyRepository;
import com.gyan.pg_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService{

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Override
    public PropertyResponse createProperty(PropertyCreateRequest request) {
        User owner = userRepository.findById(request.getOwnerId())
                        .orElseThrow(()->new UserNotFoundException("User not found"));

        Property property = Property.builder()
                .name(request.getName())
                .address(request.getAddress())
                .totalFloors(request.getTotalFloors())
                .owner(owner)
                .build();

        property = propertyRepository.save(property);
        return PropertyMapper.toResponse(property);
    }

    @Override
    public void deactivateProperty(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found"));

        property.setActive(false);
        propertyRepository.save(property);
    }
}
