package com.ezsportswear.inventory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "raw_materials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable =  false)
    private String code;

    @Column(nullable =  false)
    private String name;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private String unit;

    private String description;

    @ManyToOne
    @JoinColumn(name =  "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User created_by;
}
