package com.example.demo.hello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.hello.entity.Ingredients;

public interface IngredientsRepository extends JpaRepository<Ingredients, Long> {
}
