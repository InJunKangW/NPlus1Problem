package com.example.nplus1problem;

import com.example.nplus1problem.dto.ChildDto;
import com.example.nplus1problem.entity.Parent;
import com.example.nplus1problem.repository.FamilyQuerydsl;
import com.example.nplus1problem.repository.ParentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NPlus1Test {
    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private FamilyQuerydsl familyQuerydsl;

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        System.out.println("\n\n=== [START] " + testInfo.getDisplayName() + " ===\n");
    }

    @Test
    @Order(1)
    @DisplayName("N+1 문제 테스트. parent.getChildren()을 호출할 때마다 추가적인 Select 쿼리가 발생합니다.")
    void NPlus1Problem() {
        List<Parent> parentList = parentRepository.findAll();

        for (Parent parent : parentList) {
            List<ChildDto> childDtoList = parent.getChildren()
                .stream()
                .map(child -> new ChildDto(parent.getName(), child.getName()))
                .toList();

            for (ChildDto childDto : childDtoList) {
                System.out.println(childDto);
            }
        }
    }


    @Test
    @Order(2)
    @DisplayName("Fetch Join 을 통한 N+1 문제 해결 테스트. parentRepository.findAllFetch()를 호출할 때 children()을 같이 조회하기 때문에 추가 쿼리가 발생하지 않습니다.")
    void NPlus1ProblemSolvedWithFetchJoin() {
        List<Parent> parentList = parentRepository.findAllFetch();

        for (Parent parent : parentList) {
            List<ChildDto> childDtoList = parent.getChildren()
                .stream()
                .map(child -> new ChildDto(parent.getName(), child.getName()))
                .toList();

            for (ChildDto childDto : childDtoList) {
                System.out.println(childDto);
            }
        }
    }

    @Test
    @Order(3)
    @DisplayName("Querydsl 을 통한 N+1 문제 해결 테스트. 애초에 children 의 name 을 select 하는 쿼리를 발생시키기 때문에 추가 쿼리가 발생하지 않습니다.")
    void NPlus1ProblemSolvedWithQuerydsl() {
        System.out.println("\n\n\n NPlus1 Problem Solved With Querydsl Test Start! \n\n\n");

        List<ChildDto> childDtoList = familyQuerydsl.getChildDtoListWithQuerydsl();

        for (ChildDto childDto : childDtoList) {
            System.out.println(childDto);
        }
    }

    @Test
    @Order(4)
    @DisplayName("Querydsl 을 통한 N+1 문제 해결 테스트. @QueryProjection 사용을 통해서도 가능합니다.")
    void NPlus1ProblemSolvedWithQuerydslWithQueryProjection() {
        System.out.println("\n\n\n NPlus1 Problem Solved With Querydsl Test Start! \n\n\n");

        List<ChildDto> childDtoList = familyQuerydsl.getChildDtoListWithQuerydslWithQueryProjection();

        for (ChildDto childDto : childDtoList) {
            System.out.println(childDto);
        }
    }
}
