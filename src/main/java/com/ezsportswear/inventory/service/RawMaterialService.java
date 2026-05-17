package com.ezsportswear.inventory.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ezsportswear.inventory.dto.RawMaterialRequest;
import com.ezsportswear.inventory.entity.Category;
import com.ezsportswear.inventory.entity.RawMaterial;
import com.ezsportswear.inventory.entity.User;
import com.ezsportswear.inventory.exception.ResourceNotFoundException;
import com.ezsportswear.inventory.repository.CategoryRepository;
import com.ezsportswear.inventory.repository.RawMaterialRepository;
import com.ezsportswear.inventory.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RawMaterialService {
    private final RawMaterialRepository rawMaterialRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public Page<RawMaterial> getList(String search, Pageable pageable) {
        if (search != null && !search.isBlank()) {
            return rawMaterialRepository.findByNameContainingIgnoreCase(search, pageable);
        }

        return rawMaterialRepository.findAll(pageable);
    }

    // helper function
    // private User getAuthenticatedUser() {
    // Object principal = SecurityContextHolder.getContext()
    // .getAuthentication()
    // .getPrincipal();

    // if (principal instanceof User user) {
    // return user;
    // }

    // throw new IllegalArgumentException("User is not authenticated");
    // }

    private User getAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalArgumentException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User user) {
            return user;
        }

        throw new IllegalArgumentException("User is not authenticated");
    }

    public RawMaterial create(RawMaterialRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        User authenticatedUser = getAuthenticatedUser();

        RawMaterial rawMaterial = RawMaterial.builder()
                .code(request.getCode())
                .name(request.getName())
                .stock(request.getStock())
                .unit(request.getUnit())
                .description(request.getDescription())
                .category(category)
                .created_by(authenticatedUser)
                .build();

        return rawMaterialRepository.save(rawMaterial);
    }

    public RawMaterial update(Long id, RawMaterialRequest request) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material Not Found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));

        rawMaterial.setCode(request.getCode());
        rawMaterial.setName(request.getName());
        rawMaterial.setStock(request.getStock());
        rawMaterial.setUnit(request.getUnit());
        rawMaterial.setDescription(request.getDescription());
        rawMaterial.setCategory(category);

        return rawMaterialRepository.save(rawMaterial);
    }

    public void delete(Long id) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material Not Found"));

        rawMaterialRepository.delete(rawMaterial);
    }

    public RawMaterial stockIn(Long id, Integer quantity) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw material not found"));

        rawMaterial.setStock(rawMaterial.getStock() + quantity);

        return rawMaterialRepository.save(rawMaterial);
    }

    public RawMaterial stockOut(Long id, Integer quantity) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw material not found"));

        if (quantity > rawMaterial.getStock()) {
            throw new IllegalArgumentException("Stock is not enough");
        }

        rawMaterial.setStock(rawMaterial.getStock() - quantity);

        return rawMaterialRepository.save(rawMaterial);
    }

    public RawMaterial stockOpname(Long id, Integer actualStock) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw material not found"));

        rawMaterial.setStock(actualStock);

        return rawMaterialRepository.save(rawMaterial);
    }
}
