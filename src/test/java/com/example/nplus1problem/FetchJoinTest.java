package com.example.nplus1problem;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.example.nplus1problem.dto.ChildDto;
import com.example.nplus1problem.entity.Parent;
import com.example.nplus1problem.repository.ParentRepository;

@SpringBootTest
@Transactional
public class FetchJoinTest {
	@Autowired
	private ParentRepository parentRepository;

	@Test
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
	@DisplayName("Fetch Join + 페이징 시 N+1 문제는 해결되나 관련 데이터를 모두 조회해 메모리 부담이 커질 수 있습니다.")
	void fetchJoinPagingPerformanceTest() {
		Pageable pageable = PageRequest.of(0, 2);

		//1. JPA 내 기본 findAll : 페이징 쿼리(OFFSET, LIMIT) 적용
		Page<Parent> parentList = parentRepository.findAll(pageable);

		//2.fetch join 을 사용한 findAllFetch: 관련 엔티티 모두 즉시 로딩, 페이징 쿼리 미적용으로 메모리 부담 증가
		Page<Parent> parentListWithFetch = parentRepository.findAllFetch(pageable);
	}
}
