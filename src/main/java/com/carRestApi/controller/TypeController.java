package com.carRestApi.controller;

import com.carRestApi.model.Type;
import com.carRestApi.service.TypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/types")
public class TypeController {
    private final TypeService typeService;

    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Type> getType(@PathVariable Long id) {
        Type type = typeService.findById(id);
        if (type == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(type, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Type>> getTypes() {
        try {
            List<Type> types = typeService.findAll();
            if (types.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(types, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Type> createTypes(@RequestBody Type type) {
        try {
            Type _type = typeService.save(new Type(type.getName()));
            return new ResponseEntity<>(_type, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        Type type = typeService.findById(id);
        if (type == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        try {
            typeService.delete(type);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Type> editType(@PathVariable Long id, @RequestBody Type type) {
        if (!id.equals(type.getId()))
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        try {
            Type _type = typeService.findById(id);
            if (_type == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            _type.setName(type.getName());
            typeService.save(_type);
            return new ResponseEntity<>(_type, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
