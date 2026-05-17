package com.ezsportswear.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezsportswear.inventory.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
