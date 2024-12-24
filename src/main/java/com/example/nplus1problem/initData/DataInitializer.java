package com.example.nplus1problem.initData;

import com.example.nplus1problem.entity.Child;
import com.example.nplus1problem.entity.Parent;
import com.example.nplus1problem.repository.ChildRepository;
import com.example.nplus1problem.repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0; i < 10; i++) {
            Parent parent = Parent.builder()
                    .name("parent" + i)
                    .build();
            parentRepository.save(parent);

            for (int j = 0; j < 10; j++) {
                childRepository.save(Child.builder()
                        .parent(parent)
                        .name("child" + j + "parent : " + i)
                        .build());
            }
        }
    }
}
