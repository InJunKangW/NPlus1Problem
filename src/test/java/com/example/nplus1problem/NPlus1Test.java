package com.example.nplus1problem;

import com.example.nplus1problem.entity.Child;
import com.example.nplus1problem.entity.Parent;
import com.example.nplus1problem.repository.ParentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class NPlus1Test {
    @Autowired
    private ParentRepository parentRepository;

    @Test
    @Transactional
    void NPlus1Problem() {
        List<Parent> parents = parentRepository.findAll();
        for (Parent parent : parents) {
            System.out.println(parent);
            List<Child> children = parent.getChildren();
            for (Child child : children) {
                System.out.println(child);
            }
        }
    }
}
