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
class RawMaterialDeleteServiceTest {

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
    void testDelete_RawMaterialNotFound() {
        // Path 1: raw material tidak ditemukan
        Long rawMaterialId = 99L;

        when(rawMaterialRepository.findById(rawMaterialId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> rawMaterialService.delete(rawMaterialId)
        );

        assertEquals("Raw Material Not Found", exception.getMessage());

        verify(rawMaterialRepository, times(1)).findById(rawMaterialId);
        verify(rawMaterialRepository, never()).delete(any(RawMaterial.class));
    }

    @Test
    void testDelete_Success() {
        // Path 2: raw material ditemukan dan berhasil dihapus
        Long rawMaterialId = 1L;
        RawMaterial rawMaterial = sampleRawMaterial();

        when(rawMaterialRepository.findById(rawMaterialId))
                .thenReturn(Optional.of(rawMaterial));

        rawMaterialService.delete(rawMaterialId);

        verify(rawMaterialRepository, times(1)).findById(rawMaterialId);
        verify(rawMaterialRepository, times(1)).delete(rawMaterial);
    }
}