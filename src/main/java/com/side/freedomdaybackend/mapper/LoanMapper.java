package com.side.freedomdaybackend.mapper;


import com.side.freedomdaybackend.domain.loan.dto.StatisticsDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LoanMapper {
    List<StatisticsDto.RepaymentHistoryMonthTmp> selectRepaymentHistoryList(long memberId);

}

