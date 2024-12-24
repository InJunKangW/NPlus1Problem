package com.example.nplus1problem.repository;

import com.example.nplus1problem.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRepository extends JpaRepository<Child, Long> {
}
