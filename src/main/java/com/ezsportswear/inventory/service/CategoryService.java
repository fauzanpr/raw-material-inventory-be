package com.ezsportswear.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ezsportswear.inventory.entity.Category;
import com.ezsportswear.inventory.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
}
