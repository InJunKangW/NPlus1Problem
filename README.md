# N+1 문제 해결 실습 프로젝트

Spring JPA 사용 시 발생하는 **N+1 Select 문제**의 발생 및 해결 방식을 실험하고, 각 방식들을 비교 학습하기 위한 테스트용 프로젝트입니다.

---

## ❓ N+1 문제란?

연관된 엔티티를 조회할 때, **부모는 한 번만 조회되지만 자식은 N번의 추가 쿼리**가 실행되는 문제입니다.  
이는 예상치 못한 성능 저하를 초래할 수 있습니다.

```sql
-- 부모 조회 1회
select * from parent;

-- 자식 조회 N회
select * from child where parent_id = 1;
select * from child where parent_id = 2;
...

```

## ✅ N+1 문제 해결 방법

아래는 본 프로젝트에서 실험한 다양한 N+1 문제 해결 방식의 특징 및 주의사항입니다.

---

<details>
<summary><strong>Fetch Join</strong></summary>

  ## 특징
  
- 연관된 데이터를 한번에 로딩함 (제한 X)

## 주의사항

- 연관 데이터를 모두 가져오기 때문에 Page 처리 시 굉장히 비효율적으로 동작함


</details>

---

<details>
<summary><strong>Batch Size</strong></summary>
  
## 특징
  
- 정해진 사이즈 만큼 연관 데이터를 한번에 조회해 옴.
  

## 주의사항
- 다음과 같은 이유로 효율에 변동이 생길 수 있음
  - Batch Size는 동적으로 설정할 수 없고, 정적으로만 지정 가능함  
  - 이로 인해 적절한 Batch Size를 사전에 계산하기 어려움  
    → 예: Batch Size가 100으로 고정된 경우, 조회 대상이 101개일 때 마지막 1개를 위한 추가 쿼리가 발생하게 되어 비효율적일 수 있음
  - 특히 INSERT나 DELETE가 잦아 전체 데이터 수가 자주 변하는 환경에서는 비효율이 더욱 두드러짐
    

</details>

---

<details>
<summary><strong>Querydsl</strong></summary>

  ## 특징
  
- 동적 쿼리 작성이 용이해 복잡한 조건 처리에 적합
- SQL과 유사한 문법으로 직관적이고 편리한 쿼리 작성 가능
- Update, Delete 등 변경 쿼리도 지원 (Fetch Join, Batch Size는 Select 전용)
- Projection, @QueryProjection 등을 활용해 바로 DTO 객체 반환 가능 (불필요한 메모리 사용 감소)

## 주의사항

  - Querydsl로 Update시 JPA의 @Modifing 과 동일하게 JPQL update 이 일어나므로 영속성 컨텍스트에는 해당 수정 사항이 반영되지 않음  
    → 하나의 트랜잭션에서 수정 이후 SELECT할 시 영속성 관리에 유의해야 함!
  - @QueryProjection을 통해 DTO 반환 시, DTO 클래스가 Querydsl 라이브러리에 의존하게 됨
</details>



