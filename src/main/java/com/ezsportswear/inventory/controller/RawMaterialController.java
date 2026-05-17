package com.ezsportswear.inventory.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezsportswear.inventory.dto.RawMaterialRequest;
import com.ezsportswear.inventory.dto.StockOpnameRequest;
import com.ezsportswear.inventory.dto.StockRequest;
import com.ezsportswear.inventory.entity.RawMaterial;
import com.ezsportswear.inventory.service.RawMaterialService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/raw-materials")
@RequiredArgsConstructor
@CrossOrigin(origins = {
    "http://localhost:3012",
    "http://127.0.0.1:3012"
})
public class RawMaterialController {
    private final RawMaterialService rawMaterialService;

    @GetMapping
    public Page<RawMaterial> getList(
        @RequestParam(defaultValue = "") String search,
        @RequestParam(defaultValue = "0") int  page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return rawMaterialService.getList(search, PageRequest.of(page, size));
    }

    @PostMapping
    public RawMaterial create(@Valid @RequestBody RawMaterialRequest request) {
        return rawMaterialService.create(request);
    }

    @PutMapping("/{id}")
    public RawMaterial update(
        @PathVariable Long id, 
        @Valid @RequestBody RawMaterialRequest request
    ) {
        return rawMaterialService.update(id, request);
    }
    
    @DeleteMapping("/{id}")
    public void delete(
        @PathVariable Long id
    ) {
        rawMaterialService.delete(id);
    }

    @PatchMapping("/{id}/stock-in")
    public RawMaterial stockIn(
        @PathVariable Long id,
        @Valid @RequestBody StockRequest request
    ) {
        return rawMaterialService.stockIn(id, request.getQuantity());
    }

    @PatchMapping("/{id}/stock-out")
    public RawMaterial stockOut(
        @PathVariable Long id,
        @Valid @RequestBody StockRequest request
    ) {
        return rawMaterialService.stockOut(id, request.getQuantity());
    }

    @PatchMapping("/{id}/stock-opname")
    public RawMaterial stockOpname(
        @PathVariable Long id,
        @Valid @RequestBody StockOpnameRequest request
    ) {
        return rawMaterialService.stockOpname(id, request.getActualStock());
    }
}
