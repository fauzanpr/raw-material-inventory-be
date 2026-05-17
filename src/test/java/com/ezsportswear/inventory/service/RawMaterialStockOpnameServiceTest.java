package com.ezsportswear.inventory.service;

import com.ezsportswear.inventory.entity.Category;
import com.ezsportswear.inventory.entity.RawMaterial;
import com.ezsportswear.inventory.entity.User;
import com.ezsportswear.inventory.exception.ResourceNotFoundException;
import com.ezsportswear.inventory.repository.CategoryRepository;
import com.ezsportswear.inventory.repository.RawMaterialRepository;
import com.ezsportswear.inventory.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RawMaterialStockOpnameServiceTest {

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RawMaterialService rawMaterialService;

    private Category sampleCategory() {
        return Category.builder()
                .id(1L)
                .name("Fabric")
                .build();
    }

    private User sampleUser() {
        return User.builder()
                .id(1L)
                .name("Admin Gudang")
                .email("admin@ezsportswear.com")
                .password("hashed-password")
                .build();
    }

    private RawMaterial sampleRawMaterial() {
        return RawMaterial.builder()
                .id(1L)
                .code("RM001")
                .name("Benang")
                .stock(10)
                .unit("kg")
                .description("")
                .category(sampleCategory())
                .created_by(sampleUser())
                .build();
    }

    @Test
    void testStockOpname_RawMaterialNotFound() {
        // Path 1: raw material tidak ditemukan
        Long rawMaterialId = 99L;
        Integer actualStock = 8;

        when(rawMaterialRepository.findById(rawMaterialId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> rawMaterialService.stockOpname(rawMaterialId, actualStock)
        );

        assertEquals("Raw material not found", exception.getMessage());

        verify(rawMaterialRepository, times(1)).findById(rawMaterialId);
        verify(rawMaterialRepository, never()).save(any(RawMaterial.class));
    }

    @Test
    void testStockOpname_Success() {
        // Path 2: raw material ditemukan dan stock berhasil disesuaikan
        Long rawMaterialId = 1L;
        Integer actualStock = 8;

        RawMaterial rawMaterial = sampleRawMaterial();

        when(rawMaterialRepository.findById(rawMaterialId))
                .thenReturn(Optional.of(rawMaterial));

        when(rawMaterialRepository.save(rawMaterial))
                .thenReturn(rawMaterial);

        RawMaterial result = rawMaterialService.stockOpname(rawMaterialId, actualStock);

        assertNotNull(result);
        assertEquals(8, result.getStock());
        assertEquals("Benang", result.getName());

        verify(rawMaterialRepository, times(1)).findById(rawMaterialId);
        verify(rawMaterialRepository, times(1)).save(rawMaterial);
    }
}