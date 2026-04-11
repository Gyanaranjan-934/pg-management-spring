package com.gyan.pg_management.modules.inventory.service;

import com.gyan.pg_management.modules.inventory.dto.request.PropertyCreateRequest;
import com.gyan.pg_management.modules.inventory.dto.response.PropertyResponse;
import com.gyan.pg_management.modules.inventory.domain.Property;
import com.gyan.pg_management.modules.identity.domain.User;
import com.gyan.pg_management.shared.exception.UserNotFoundException;
import com.gyan.pg_management.modules.inventory.mapper.PropertyMapper;
import com.gyan.pg_management.modules.inventory.repository.PropertyRepository;
import com.gyan.pg_management.modules.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
                .active(true)
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

    @Override
    public PropertyResponse[] getAllProperties(Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(()->new UserNotFoundException("User not found"));

        List<Property> properties = propertyRepository.findAllByOwner(owner);

        return  properties.stream().map(PropertyMapper::toResponse)
                .toArray(PropertyResponse[]::new);

    }


}
