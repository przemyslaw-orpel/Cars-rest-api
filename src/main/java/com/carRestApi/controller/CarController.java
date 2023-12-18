package com.carRestApi.controller;

import com.carRestApi.dto.CarDTO;
import com.carRestApi.model.Car;
import com.carRestApi.model.Type;
import com.carRestApi.service.CarService;
import com.carRestApi.service.TypeService;
import com.carRestApi.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CarController {
    private final CarService carService;
    private final TypeService typeService;

    public CarController(CarService carService, TypeService typeService) {
        this.carService = carService;
        this.typeService = typeService;
    }
    @JsonView(Views.Web.class)
    @GetMapping("/v1/cars/{id}")
    public ResponseEntity<CarDTO> getCar(@PathVariable Long id) {
        CarDTO car = carService.convertToDto(carService.findById(id));
        if (car == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(car, HttpStatus.OK);
    }
    @JsonView(Views.Web.class)
    @GetMapping("/v1/cars")
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

    @PostMapping("/v1/cars")
    public ResponseEntity<CarDTO> createCar(@RequestBody CarDTO carDTO) {
        try {
            Type type = typeService.findByName(carDTO.getType());
            if (type == null)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            Car car = carService.save(new Car(carDTO.getBrand(), carDTO.getModel(), type, carDTO.getGen()));
            return new ResponseEntity<>(carService.convertToDto(car), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/v1/cars/{id}")
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

    @PutMapping("/v1/cars/{id}")
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
            car.setGen(carDTO.getGen());
            carService.save(car);
            return new ResponseEntity<>(carService.convertToDto(car), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/v1/cars/{id}")
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
            if (carDTO.getGen() != null)
                car.setGen(carDTO.getGen());
            carService.save(car);
            return new ResponseEntity<>(carService.convertToDto(car), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @JsonView(Views.Web.class)
    @GetMapping("/v1/cars/brand/{brand}")
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
    @JsonView(Views.Web.class)
    @GetMapping("/v1/cars/type/{type}")
    public ResponseEntity<List<CarDTO>> getCarsByType(@PathVariable String type) {
        try {
            List<CarDTO> cars = carService
                    .findAll()
                    .stream().filter(c -> type.equalsIgnoreCase(c.getType()))
                    .collect(Collectors.toList());
            if (cars.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(cars, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @JsonView(Views.Web.class)
    @GetMapping("/v1/cars/model/{model}")
    public ResponseEntity<List<CarDTO>> getCarsByModel(@PathVariable String model) {
        try {
            List<CarDTO> cars = carService
                    .findAll()
                    .stream().filter(c -> model.equalsIgnoreCase(c.getModel()))
                    .collect(Collectors.toList());
            if (cars.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(cars, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////
    //Endpoints for mobile devices
    @JsonView(Views.Mobile.class)
    @GetMapping("/v1/mobile/cars")
    public ResponseEntity<List<CarDTO>> getCarsMobile() {
        try {
            List<CarDTO> cars = carService.findAll();
            if (cars.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(cars, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(Views.Mobile.class)
    @GetMapping("/v1/mobile/cars/{id}")
    public ResponseEntity<CarDTO> getCarMobile(@PathVariable Long id) {
        CarDTO car = carService.convertToDto(carService.findById(id));
        if (car == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @JsonView(Views.Mobile.class)
    @GetMapping("/v1/mobile/cars/brand/{brand}")
    public ResponseEntity<List<CarDTO>> getCarsByBrandMobile(@PathVariable String brand) {
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
    @JsonView(Views.Mobile.class)
    @GetMapping("/v1/mobile/cars/type/{type}")
    public ResponseEntity<List<CarDTO>> getCarsByTypeMobile(@PathVariable String type) {
        try {
            List<CarDTO> cars = carService
                    .findAll()
                    .stream().filter(c -> type.equalsIgnoreCase(c.getType()))
                    .collect(Collectors.toList());
            if (cars.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(cars, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
