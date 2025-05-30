package com.example.nplus1problem;

import com.example.nplus1problem.dto.ChildDto;
import com.example.nplus1problem.entity.Parent;
import com.example.nplus1problem.repository.ParentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class NPlus1Test {
    @Autowired
    private ParentRepository parentRepository;

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        System.out.println("\n\n=== [START] " + testInfo.getDisplayName() + " ===\n");
    }

    @Test
    // @DisplayName("BatchSize 를 통한 N+1 문제 해결 테스트. Parent 클래스 내 설정된 Batch Size ( = 10) 단위로 끊어져서 추가 쿼리가 실행됩니다.")
    // void NPlus1ProblemSolvedWithBatchSize() { // 활성화 시, Parent 클래스 내 주석된 부분을 해제한 이후 실행해야 합니다.
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
}
