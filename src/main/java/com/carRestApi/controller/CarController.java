package com.carRestApi.controller;

import com.carRestApi.dto.CarDTO;
import com.carRestApi.model.Car;
import com.carRestApi.model.Type;
import com.carRestApi.service.CarService;
import com.carRestApi.service.TypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarService carService;
    private final TypeService typeService;

    public CarController(CarService carService, TypeService typeService) {
        this.carService = carService;
        this.typeService = typeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getCar(@PathVariable Long id) {
        CarDTO car = carService.convertToDto(carService.findById(id));
        if (car == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<CarDTO>> getCars() {
        try {
            List<CarDTO> cars = carService.findAll();
            if (cars.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(cars, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<CarDTO> createCar(@RequestBody CarDTO carDTO) {
        try {
            Type type = typeService.findByName(carDTO.getType());
            if (type == null)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            Car car = carService.save(new Car(carDTO.getBrand(), carDTO.getModel(), type));
            return new ResponseEntity<>(carService.convertToDto(car), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCar(@PathVariable Long id) {
        try {
            Car car = carService.findById(id);
            if (car == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            carService.delete(car);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarDTO> editCar(@PathVariable Long id, @RequestBody CarDTO carDTO) {
        try {
            if (!id.equals(carDTO.getId()))
                return new ResponseEntity<>(HttpStatus.CONFLICT);

            Car car = carService.findById(id);
            Type type = typeService.findByName(carDTO.getType());
            if (car == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            if (type == null)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            car.setBrand(carDTO.getBrand());
            car.setModel(carDTO.getModel());
            car.setType(type);
            carService.save(car);
            return new ResponseEntity<>(carService.convertToDto(car), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CarDTO> editPartCar(@PathVariable Long id, @RequestBody CarDTO carDTO) {
        try {
            if (!id.equals(carDTO.getId()))
                return new ResponseEntity<>(HttpStatus.CONFLICT);

            Car car = carService.findById(id);
            if (car == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            if (carDTO.getBrand() != null)
                car.setBrand(carDTO.getBrand());
            if (carDTO.getModel() != null)
                car.setModel(carDTO.getModel());
            if (carDTO.getType() != null) {
                Type type = typeService.findByName(carDTO.getType());
                if (type != null) {
                    car.setType(type);
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
            }
            carService.save(car);
            return new ResponseEntity<>(carService.convertToDto(car), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<CarDTO>> getCarsByBrand(@PathVariable String brand) {
        try {
            List<CarDTO> cars = carService
                    .findAll()
                    .stream().filter(c -> brand.equalsIgnoreCase(c.getBrand()))
                    .collect(Collectors.toList());
            if (cars.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(cars, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
