package com.ezsportswear.inventory.service;

import com.ezsportswear.inventory.entity.Category;
import com.ezsportswear.inventory.entity.RawMaterial;
import com.ezsportswear.inventory.entity.User;
import com.ezsportswear.inventory.repository.CategoryRepository;
import com.ezsportswear.inventory.repository.RawMaterialRepository;
import com.ezsportswear.inventory.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RawMaterialServiceTest {

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
                .password("password")
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
    void testGetList_WithSearch() {
        // Path 1: search berisi keyword
        String search = "Benang";
        Pageable pageable = PageRequest.of(0, 10);

        Page<RawMaterial> page = new PageImpl<>(List.of(sampleRawMaterial()));

        when(rawMaterialRepository.findByNameContainingIgnoreCase(search, pageable))
                .thenReturn(page);

        Page<RawMaterial> result = rawMaterialService.getList(search, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Benang", result.getContent().get(0).getName());

        verify(rawMaterialRepository, times(1))
                .findByNameContainingIgnoreCase(search, pageable);

        verify(rawMaterialRepository, never())
                .findAll(pageable);
    }

    @Test
    void testGetList_WithEmptySearch() {
        // Path 2: search kosong
        String search = "";
        Pageable pageable = PageRequest.of(0, 10);

        Page<RawMaterial> page = new PageImpl<>(List.of(sampleRawMaterial()));

        when(rawMaterialRepository.findAll(pageable))
                .thenReturn(page);

        Page<RawMaterial> result = rawMaterialService.getList(search, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Benang", result.getContent().get(0).getName());

        verify(rawMaterialRepository, times(1))
                .findAll(pageable);

        verify(rawMaterialRepository, never())
                .findByNameContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    void testGetList_WithNullSearch() {
        // Path 2: search bernilai null
        String search = null;
        Pageable pageable = PageRequest.of(0, 10);

        Page<RawMaterial> page = new PageImpl<>(List.of(sampleRawMaterial()));

        when(rawMaterialRepository.findAll(pageable))
                .thenReturn(page);

        Page<RawMaterial> result = rawMaterialService.getList(search, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Benang", result.getContent().get(0).getName());

        verify(rawMaterialRepository, times(1))
                .findAll(pageable);

        verify(rawMaterialRepository, never())
                .findByNameContainingIgnoreCase(anyString(), any(Pageable.class));
    }
}