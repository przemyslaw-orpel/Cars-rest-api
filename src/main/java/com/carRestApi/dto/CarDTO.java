package com.carRestApi.dto;

import com.carRestApi.view.Views;
import com.fasterxml.jackson.annotation.JsonView;

public class CarDTO {
    @JsonView(Views.Mobile.class)
    private Long id;
    @JsonView(Views.Mobile.class)

    private String brand;
    @JsonView(Views.Mobile.class)

    private String model;
    @JsonView(Views.Web.class)

    private String type;
    @JsonView(Views.Web.class)

    private String gen;

    public CarDTO() {
    }

    public CarDTO(Long id, String brand, String model, String type,String gen) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.gen = gen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }
}
