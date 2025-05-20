package com.example.nplus1problem.repository;

import com.example.nplus1problem.dto.ChildDto;
import com.example.nplus1problem.dto.QChildDto;
import com.example.nplus1problem.entity.Parent;
import com.example.nplus1problem.entity.QChild;
import com.example.nplus1problem.entity.QParent;
import com.querydsl.core.types.Projections;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FamilyQuerydsl extends QuerydslRepositorySupport {
    private final QParent p = QParent.parent;
    private final QChild c = QChild.child;

    public FamilyQuerydsl() {
        super(Parent.class);
    }


    /**
     * Querydsl의 Projections.constructor를 사용하여 ChildDto 리스트를 조회합니다.
     * 이 방법은 DTO의 생성자를 리플렉션으로 호출하여 값을 매핑합니다.
     * 타입 안전성은 떨어지지만, 별도의 Q타입 생성 없이 바로 DTO를 조회할 수 있습니다.
     *
     * @return ChildDto 객체들의 리스트
     */
    public List<ChildDto> getChildDtoListWithQuerydsl() {
        return from(c)
            .select(Projections.constructor(ChildDto.class, p.name, c.name))
            .innerJoin(p)
            .on(c.parent.eq(p))
            .fetch();
    }

    /**
     * Querydsl의 @QueryProjection을 사용하여 생성된 QChildDto 클래스를 통해
     * 타입 세이프하게 ChildDto 리스트를 조회합니다.
     * 이 방법은 컴파일 시점에 DTO 생성자를 체크하므로 타입 안정성이 높고,
     * 리플렉션 사용 없이 빠르게 DTO를 생성합니다.
     *
     * @return ChildDto 객체들의 리스트
     */
    public List<ChildDto> getChildDtoListWithQuerydslWithQueryProjection() {
        return from(c)
            .select(new QChildDto(p.name, c.name))
            .innerJoin(p)
            .on(c.parent.eq(p))
            .fetch();
    }

    public void modifyChildName(Long id, String name) {
        update(c)
            .set(c.name, name)
            .where(c.id.eq(id))
            .execute();
    }
}
