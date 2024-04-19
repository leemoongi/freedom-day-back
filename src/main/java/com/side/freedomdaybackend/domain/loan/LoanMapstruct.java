package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.domain.loan.dto.LoanSimpleDto;
import com.side.freedomdaybackend.domain.loan.dto.MyLoanInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;


@Mapper(componentModel = SPRING)
public interface LoanMapstruct {

    // entity -> dto
    @Mapping(target = "paymentDDay", ignore = true)
    @Mapping(target = "outstandingPrincipal", ignore = true)
    @Mapping(target = "paymentPercentage", ignore = true)
    LoanSimpleDto toLoanDto(Loan entity);
    List<LoanSimpleDto> toLoanDtoList(List<Loan> entity);


    // dto -> dto
    List<MyLoanInfoDto.LoanSimpleDto> toLoanSimpleDto(List<LoanSimpleDto> dto);

}
