package com.example.nplus1problem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "parent")
    @ToString.Exclude
    @Builder.Default
//    @BatchSize(size = 10)
    private List<Child> children = new ArrayList<>();
}
