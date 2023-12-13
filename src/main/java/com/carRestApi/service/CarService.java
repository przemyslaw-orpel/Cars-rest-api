package com.carRestApi.service;


import com.carRestApi.dto.CarDTO;
import com.carRestApi.model.Car;
import com.carRestApi.model.Type;
import com.carRestApi.repository.CarRepo;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {
    private final CarRepo carRepo;
    private final TypeService typeService;
    private final ModelMapper modelMapper;

    public CarService(CarRepo carRepo, TypeService typeService, ModelMapper modelMapper) {
        this.carRepo = carRepo;
        this.typeService = typeService;
        this.modelMapper = modelMapper;
        configureModelMapper();
    }

    public List<CarDTO> findAll() {
        return carRepo.findAll()
                .stream().
                map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Car findById(Long id) {
        return carRepo.findById(id).orElse(null);
    }

    public void delete(Car car) {
        carRepo.delete(car);
    }


    public Car save(Car car) {
        carRepo.save(car);
        return car;
    }

    public CarDTO convertToDto(Car car) {
        return modelMapper.map(car, CarDTO.class);
    }

    private Car convertToEntity(CarDTO carDTO) {
        return modelMapper.map(carDTO, Car.class);
    }

    private void configureModelMapper() {
        //Entity -> DTO
        modelMapper.addMappings(new PropertyMap<Car, CarDTO>() {
            @Override
            protected void configure() {
                map().setType(source.getType().getName());
            }
        });

        //DTO -> Entity
        Converter<String, Type> typeNameToTypeObj = ctx -> {
            if (ctx.getSource() == null) {
                return null;
            }
            return typeService.findByName(ctx.getSource());
        };
        modelMapper.addConverter(typeNameToTypeObj);
        modelMapper.addMappings(new PropertyMap<CarDTO, Car>() {
            @Override
            protected void configure() {
                using(typeNameToTypeObj).map(source.getType()).setType(null);
            }
        });
    }

}
