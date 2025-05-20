# N+1 문제 테스트 프로젝트

이 프로젝트는 JPA 사용 시 자주 발생하는 **N+1 Select 문제**를 테스트하고 해결 방안을 학습하기 위해 만들어졌습니다.

## 🔍 N+1 문제란?

JPA로 연관 관계가 설정된 엔티티를 조회할 때, **부모(Parent)를 한 번 조회한 후 자식(Child)을 N번 추가로 조회**하는 문제가 발생할 수 있습니다. 이러한 예상치 못한 쿼리 남발로 인해 발생하는 과도한 데이터베이스 IO는 곧 성능 저하로 이어질 수 있습니다.

예시:
- `select * from parent` → 1번 쿼리
- 각 부모마다 자식 조회 → `select * from child where parent_id = ?` N번 실행

## 📁 프로젝트 구조
``` bash
src/
├── dto/
│ ├── ChildDto.java
├── entity/
│ ├── Parent.java
│ └── Child.java
├── initData/
│ ├── Datainitializer.java
└── repository/
  ├── ParentRepository.java
  ├── ChildRepository.java
  └── FamilyQuerydsl.java
...
```

## ⚙️ 주요 테스트 설명

## ✅ 테스트 시나리오 요약

본 프로젝트는 다양한 방식으로 **JPA N+1 문제를 유발하거나 해결**하는 테스트를 포함하고 있습니다.

| 순번 | 테스트 메서드 이름 | 설명 | 사용 기술 |
|------|----------------------|------|------------|
| 1 | `NPlus1Problem` | `parent.getChildren()` 호출 시 자식 엔티티를 지연 로딩하면서 N+1 문제 발생 | JPA 기본 (지연 로딩) |
| 2 | `NPlus1ProblemSolvedWithFetchJoin` | `findAllFetch()` 메서드로 Fetch Join 사용, 추가 쿼리 없이 자식 엔티티 함께 로딩 | Fetch Join |
| 3 | `NPlus1ProblemSolvedWithQuerydsl` | Querydsl을 사용해 필요한 필드만 직접 조회하여 N+1 문제 회피 | Querydsl + DTO 프로젝션 |
| 4 | `NPlus1ProblemSolvedWithQuerydslWithQueryProjection` | `@QueryProjection`을 활용한 타입 안정성 있는 Querydsl DTO 조회 | Querydsl + @QueryProjection |
| 5 | `querydslUpdate_skipsPersistenceContext` | Querydsl을 사용한 UPDATE시 JPA의 영속성 컨텍스트를 거치지 않는 현상 | Querydsl|

1 ~ 4 테스트는 `NPlus1Test`, 5 테스트는 `QuerydslTest` 클래스에 위치하며, 실행 시 콘솔 로그를 통해 쿼리 발생 여부 및 최종 DTO 구조를 확인할 수 있습니다.

