package com.example.nplus1problem.dto;

import com.querydsl.core.annotations.QueryProjection;

public record ChildDto(
	String parentName,
	String childName
) {
	/**
	 * Querydsl의 DTO Projection 생성자입니다.
	 * 이 생성자에 @QueryProjection을 붙이면,
	 * Querydsl Q타입에서 타입 세이프하게 DTO를 직접 생성하는 코드를
	 * 컴파일 시점에 생성해 줍니다.
	 * 사용 시 QChildDto 클래스를 통해 DTO 생성이 가능합니다.
	 */
	@QueryProjection
	public ChildDto{}
}
