package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.domain.loan.dto.LoanSimpleDto;
import com.side.freedomdaybackend.domain.loan.dto.MyLoanInfoDto;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;


@Mapper(componentModel = SPRING)
public interface LoanMapstruct {

    // entity -> dto
    LoanSimpleDto toLoanDto(Loan entity);
    List<LoanSimpleDto> toLoanDtoList(List<Loan> entity);


    // dto -> dto
    List<MyLoanInfoDto.LoanSimpleDto> toLoanSimpleDto(List<LoanSimpleDto> dto);

}
