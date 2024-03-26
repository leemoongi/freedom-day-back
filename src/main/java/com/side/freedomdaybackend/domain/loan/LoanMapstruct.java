package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.domain.loan.dto.LoanListDto;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;


@Mapper(componentModel = SPRING)
public interface LoanMapstruct {

    // entity -> dto
    LoanListDto toLoanDto(Loan entity);
    List<LoanListDto> toLoanDtoList(List<Loan> entity);

}
