package com.example.nplus1problem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.nplus1problem.entity.Child;
import com.example.nplus1problem.repository.ChildRepository;
import com.example.nplus1problem.repository.FamilyQuerydsl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest
@Transactional
public class QuerydslTest {
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private FamilyQuerydsl familyQuerydsl;

	@Autowired
	private ChildRepository childRepository;

	@BeforeEach
	void beforeEach(TestInfo testInfo) {
		System.out.println("\n\n=== [START] " + testInfo.getDisplayName() + " ===\n");
	}

	@Test
	@DisplayName("Querydsl을 통한 엔티티 필드 업데이트 확인 및 1차 캐시/DB 반영 여부 확인")
	void test() {
		// 1. ID가 1인 자식 엔티티를 조회하여 이름 출력 (영속성 컨텍스트에 해당 조회 내역 저장)
		Child child = childRepository.findById(1L).orElseThrow();
		System.out.println("원래 이름: " + child.getName());

		// 2. Querydsl을 이용하여 자식 이름을 변경 (JPQL update - 영속성 컨텍스트를 거치지 않고 DB에 바로 UPDATE 쿼리가 실행)
		familyQuerydsl.modifyChildName(1L, "child name changed!");

		// 3. 같은 ID로 다시 조회 - 같은 트랜잭션, 영속성 컨텍스트 내의 내역을 바로 조회하므로 DB 변경 사항이 반영되지 않음
		Child modifiedChild = childRepository.findById(1L).orElseThrow();
		System.out.println("변경 시도 후(영속성 컨택스트 내): " + modifiedChild.getName());

		// 4. 영속성 컨텍스트 초기화 (영속성 컨텍스트 초기화)
		em.clear();

		// 5. 다시 조회하면 DB에서 조회하므로 변경된 이름 확인 가능
		Child modifiedChild2 = childRepository.findById(1L).orElseThrow();
		System.out.println("변경 시도 후(DB에서 다시 조회): " + modifiedChild2.getName());
	}
}
