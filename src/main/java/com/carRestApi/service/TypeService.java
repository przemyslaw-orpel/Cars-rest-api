package com.carRestApi.service;

import com.carRestApi.model.Type;
import com.carRestApi.repository.TypeRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {
    private final TypeRepo typeRepo;

    public TypeService(TypeRepo typeRepo) {
        this.typeRepo = typeRepo;
    }

    public List<Type> findAll() {
        return typeRepo.findAll();
    }

    public Type findById(Long id) {
        return typeRepo.findById(id).orElse(null);
    }

    public Type save(Type type) {
        typeRepo.save(type);
        return type;
    }

    public void delete(Type type) {
        typeRepo.delete(type);
    }

    public Type findByName(String name) {
        return typeRepo.findByName(name).orElse(null);
    }
}
