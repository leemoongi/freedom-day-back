package com.side.freedomdaybackend.domain.loan;

import com.side.freedomdaybackend.domain.loan.dto.LoanDto;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;


@Mapper(componentModel = SPRING)
public interface LoanMapstruct {

    // entity -> dto
    LoanDto toLoanDto(Loan entity);
    List<LoanDto> toLoanDtoList(List<Loan> entity);

}
