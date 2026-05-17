package com.ezsportswear.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezsportswear.inventory.entity.RawMaterial;

public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {
    Page<RawMaterial> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
