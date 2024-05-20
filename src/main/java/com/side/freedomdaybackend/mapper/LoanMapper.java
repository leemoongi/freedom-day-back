package com.side.freedomdaybackend.mapper;


import com.side.freedomdaybackend.domain.loan.dto.LoanStatisticsDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LoanMapper {
    List<LoanStatisticsDto.RepaymentHistoryMonth> selectRepaymentHistoryList(long memberId);
//    List<LoanStatisticsDto.RepaymentHistoryMonthTmp> selectRepaymentHistoryList(long memberId);

}

