package com.side.freedomdaybackend.domain.member;

import com.side.freedomdaybackend.domain.member.dto.MemberDto;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;


@Mapper(componentModel = SPRING)
public interface MemberMapstruct {

    // entity -> dto
    MemberDto toMemberDto(Member entity);
    List<MemberDto> toMemberDtoList(List<Member> entity);


    // dto -> dto
//    List<MyLoanInfoDto.LoanSimpleDto> toLoanSimpleDto(List<LoanSimpleDto> dto);

}
