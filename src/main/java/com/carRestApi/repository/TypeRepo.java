package com.carRestApi.repository;

import com.carRestApi.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeRepo extends JpaRepository<Type, Long> {
    Optional<Type> findByName(String name);
}
