package com.ezsportswear.inventory.service;

import com.ezsportswear.inventory.dto.RawMaterialRequest;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RawMaterialUpdateServiceTest {

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

    private Category updatedCategory() {
        return Category.builder()
                .id(2L)
                .name("Accessories")
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
                .description("Data lama")
                .category(sampleCategory())
                .created_by(sampleUser())
                .build();
    }

    private RawMaterialRequest updateRequest() {
        RawMaterialRequest request = new RawMaterialRequest();
        request.setCode("RM002");
        request.setName("Benang Updated");
        request.setStock(20);
        request.setUnit("roll");
        request.setDescription("Data sudah diubah");
        request.setCategoryId(2L);
        return request;
    }

    @Test
    void testUpdate_RawMaterialNotFound() {
        // Path 1: raw material tidak ditemukan
        Long rawMaterialId = 99L;
        RawMaterialRequest request = updateRequest();

        when(rawMaterialRepository.findById(rawMaterialId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> rawMaterialService.update(rawMaterialId, request)
        );

        assertEquals("Raw Material Not Found", exception.getMessage());

        verify(rawMaterialRepository, times(1)).findById(rawMaterialId);
        verify(categoryRepository, never()).findById(anyLong());
        verify(rawMaterialRepository, never()).save(any(RawMaterial.class));
    }

    @Test
    void testUpdate_CategoryNotFound() {
        // Path 2: raw material ditemukan, tetapi category tidak ditemukan
        Long rawMaterialId = 1L;
        RawMaterialRequest request = updateRequest();

        when(rawMaterialRepository.findById(rawMaterialId))
                .thenReturn(Optional.of(sampleRawMaterial()));

        when(categoryRepository.findById(2L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> rawMaterialService.update(rawMaterialId, request)
        );

        assertEquals("Category Not Found", exception.getMessage());

        verify(rawMaterialRepository, times(1)).findById(rawMaterialId);
        verify(categoryRepository, times(1)).findById(2L);
        verify(rawMaterialRepository, never()).save(any(RawMaterial.class));
    }

    @Test
    void testUpdate_Success() {
        // Path 3: raw material dan category ditemukan, data berhasil diperbarui
        Long rawMaterialId = 1L;
        RawMaterialRequest request = updateRequest();

        RawMaterial existingRawMaterial = sampleRawMaterial();
        Category newCategory = updatedCategory();

        when(rawMaterialRepository.findById(rawMaterialId))
                .thenReturn(Optional.of(existingRawMaterial));

        when(categoryRepository.findById(2L))
                .thenReturn(Optional.of(newCategory));

        when(rawMaterialRepository.save(existingRawMaterial))
                .thenReturn(existingRawMaterial);

        RawMaterial result = rawMaterialService.update(rawMaterialId, request);

        assertNotNull(result);
        assertEquals("RM002", result.getCode());
        assertEquals("Benang Updated", result.getName());
        assertEquals(20, result.getStock());
        assertEquals("roll", result.getUnit());
        assertEquals("Data sudah diubah", result.getDescription());
        assertEquals(2L, result.getCategory().getId());
        assertEquals("Accessories", result.getCategory().getName());

        // memastikan createdBy tidak berubah saat update
        assertEquals(1L, result.getCreated_by().getId());
        assertEquals("Admin Gudang", result.getCreated_by().getName());

        verify(rawMaterialRepository, times(1)).findById(rawMaterialId);
        verify(categoryRepository, times(1)).findById(2L);
        verify(rawMaterialRepository, times(1)).save(existingRawMaterial);
    }
}