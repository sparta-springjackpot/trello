package com.example.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.security.Permission;
import java.util.List;


public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByBoardIdAndUserId(Long boardId, Long userId);

    List<Permission> findAllByBoardId(Long boardId);

    List<Permission> findAllByUserId(Long userId);
}
