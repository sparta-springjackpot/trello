package com.example.trello.repository;

import com.example.trello.entity.Columns;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnsRepository extends JpaRepository<Columns, Long> {
}
