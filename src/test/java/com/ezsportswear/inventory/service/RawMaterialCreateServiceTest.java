package com.ezsportswear.inventory.service;

import com.ezsportswear.inventory.dto.RawMaterialRequest;
import com.ezsportswear.inventory.entity.Category;
import com.ezsportswear.inventory.entity.RawMaterial;
import com.ezsportswear.inventory.entity.User;
import com.ezsportswear.inventory.exception.ResourceNotFoundException;
import com.ezsportswear.inventory.repository.CategoryRepository;
import com.ezsportswear.inventory.repository.RawMaterialRepository;
import com.ezsportswear.inventory.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RawMaterialCreateServiceTest {
    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RawMaterialService rawMaterialService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private RawMaterialRequest sampleRequest() {
        RawMaterialRequest request = new RawMaterialRequest();
        request.setCode("RM001");
        request.setName("Benang");
        request.setStock(10);
        request.setUnit("kg");
        request.setDescription("");
        request.setCategoryId(1L);
        return request;
    }

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

    @Test
    void testCreate_CategoryNotFound() {
        // Path 1: category tidak ditemukan
        RawMaterialRequest request = sampleRequest();
        request.setCategoryId(99L);

        when(categoryRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> rawMaterialService.create(request)
        );

        assertEquals("Category not found", exception.getMessage());

        verify(categoryRepository, times(1)).findById(99L);
        verify(rawMaterialRepository, never()).save(any(RawMaterial.class));
    }

    @Test
    void testCreate_UserNotAuthenticated() {
        // Path 2: category ditemukan, tetapi user tidak terautentikasi
        RawMaterialRequest request = sampleRequest();

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(sampleCategory()));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> rawMaterialService.create(request)
        );

        assertEquals("User is not authenticated", exception.getMessage());

        verify(categoryRepository, times(1)).findById(1L);
        verify(rawMaterialRepository, never()).save(any(RawMaterial.class));
    }

    @Test
    void testCreate_Success() {
        // Path 3: category ditemukan dan user terautentikasi
        RawMaterialRequest request = sampleRequest();
        Category category = sampleCategory();
        User user = sampleUser();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, List.of())
        );

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(rawMaterialRepository.save(any(RawMaterial.class)))
                .thenAnswer(invocation -> {
                    RawMaterial rawMaterial = invocation.getArgument(0);
                    rawMaterial.setId(1L);
                    return rawMaterial;
                });

        RawMaterial result = rawMaterialService.create(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("RM001", result.getCode());
        assertEquals("Benang", result.getName());
        assertEquals(10, result.getStock());
        assertEquals("kg", result.getUnit());
        assertEquals(category.getId(), result.getCategory().getId());
        assertEquals(user.getId(), result.getCreated_by().getId());

        verify(categoryRepository, times(1)).findById(1L);
        verify(rawMaterialRepository, times(1)).save(any(RawMaterial.class));
    }
}