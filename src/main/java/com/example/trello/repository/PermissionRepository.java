package com.example.trello.repository;

import com.example.trello.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
//    boolean existsByBoardIdAndUserId(Long boardId, Long userId);
//
//    List<Permission> findAllByBoardId(Long boardId);

    List<Permission> findAllByUserId(Long userId);
}
