package com.side.freedomdaybackend.mapper;


import com.side.freedomdaybackend.domain.loan.dto.LoanStatisticsDto;
import com.side.freedomdaybackend.mapper.vo.ExistsHistoryDateVo;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface LoanMapper {
    List<LoanStatisticsDto.RepaymentHistoryMonth> selectRepaymentHistoryList(long memberId);

    boolean existsHistoryDate(ExistsHistoryDateVo vo);
//    List<LoanStatisticsDto.RepaymentHistoryMonthTmp> selectRepaymentHistoryList(long memberId);

}

