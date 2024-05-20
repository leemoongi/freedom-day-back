package com.side.freedomdaybackend.mapper.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExistsHistoryDateVo {
    private long loanId;
    private String historyDate;
}
