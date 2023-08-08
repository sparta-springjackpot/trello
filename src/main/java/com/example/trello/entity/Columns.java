package com.example.trello.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "columns")
//클래스명을 Column으로 하면 어노테이션 예외로 에러 처리가 발생해서 Columns로 변경했습니다.
//혹시 몰라서 테이블명도 columns로 했습니다.
public class Columns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "column_id")
    private Long id;

    @Column(name = "column_name", nullable = false)
    private String columnName;

    @Column(name = "column_number", nullable = false)
    private Integer columnNumber;
}
