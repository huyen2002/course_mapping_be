package com.example.course_mapping_be.services;


import com.example.course_mapping_be.dtos.AddressDto;
import com.example.course_mapping_be.models.Address;
import com.example.course_mapping_be.repositories.AddressRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    private final ModelMapper modelMapper;


    public AddressDto create(AddressDto addressDto) {
        Address address = modelMapper.map(addressDto, Address.class);
        address.setId(null);
        addressRepository.save(address);
        return modelMapper.map(address, AddressDto.class);
    }
}
