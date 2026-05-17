package com.ezsportswear.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezsportswear.inventory.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}