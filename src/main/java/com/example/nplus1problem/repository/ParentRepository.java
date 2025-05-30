package com.example.nplus1problem.repository;

import com.example.nplus1problem.entity.Parent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParentRepository extends JpaRepository<Parent,Long> {
    @Query("SELECT p FROM Parent p JOIN FETCH p.children")
    List<Parent> findAllFetch();

    @Query("SELECT p FROM Parent p JOIN FETCH p.children")
    Page<Parent> findAllFetch(Pageable pageable);
}
