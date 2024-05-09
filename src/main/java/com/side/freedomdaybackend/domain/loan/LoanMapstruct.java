package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.domain.loan.dto.LoanCreateDto;
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

    // dto -> entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "loanRepaymentMonthHistory", ignore = true)
    @Mapping(target = "status", ignore = true)
    Loan toLoan(LoanCreateDto dto);


    // dto -> dto
    List<MyLoanInfoDto.LoanSimpleDto> toLoanSimpleDto(List<LoanSimpleDto> dto);

}
