package com.side.freedomdaybackend.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // COMMON : 공통
    INTERNAL_SERVER_ERROR("COMMON-001",500,"서버에 문제가 발생했습니다."),
    BAD_REQUEST("COMMON-002",400,"데이터 형식이 잘못 되었습니다."),

    // JWT-ACCESS : 엑세스 토큰
    JWT_ERROR("JWT-001",401,"JWT 문제가 발생했습니다."),
    JWT_ACCESS_EXPIRE("JWT-ACCESS-001", 401, "accessToken 만료시간이 지났습니다."),
    JWT_ACCESS_INVALID_SIGNATURE("JWT-ACCESS-002",401,"accessToken 올바르지 않은 서명입니다."),

    // JWT-REFRESH : 리프레쉬 토큰
    JWT_REFRESH_EXPIRE("JWT-REFRESH-001",401,"refreshToken 만료시간이 지났습니다."),
    JWT_REFRESH_INVALID_SIGNATURE("JWT-REFRESH-002",401,"refreshToken 올바르지 않은 서명입니다."),
    JWT_REFRESH_NOT_FOUND("JWT-REFRESH-003",404,"저장되어 있지 않은 refreshToken 입니다"),
    JWT_REFRESH_LOGOUT("JWT-REFRESH-004",401,"로그아웃 처리된 refreshToken 입니다."),

    // ACCOUNT : 계정
    ACCOUNT_NOT_FOUND("ACCOUNT-001",404, "계정이 존재하지 않습니다."),
    ACCOUNT_PASSWORD_NOT_MATCH("ACCOUNT-002", 401,"비밀번호가 일치하지 않습니다."),
    ACCOUNT_NOT_ACTIVE("ACCOUNT-003", 401,"비활성화된 계정입니다."),
    ACCOUNT_LOCK("ACCOUNT-004", 401,"비밀번호가 5번 틀렸습니다. 계정이 잠금됩니다."),
    ACCOUNT_EXIST_EMAIL("ACCOUNT-005", 400, "이미 존재하는 이메일입니다."),
    ACCOUNT_EMAIL_ERROR("ACCOUNT-006", 400, "이메일 인증이 필요합니다."),

    // LOAN : 대출
    LOAN_NOT_FOUND("LOAN-001",404, "대출이 존재하지 않습니다."),
    LOAN_REPAYMENT_HISTORY_EXIST("LOAN-002",404, "해당 월에 상환 내역이 존재합니다."),
    LOAN_UNAUTHORIZED("LOAN-003",401, "해당 대출에 권한이 없습니다."),

    ;

    public String code;
    public int httpStatus;
    public String message;

    ErrorCode(String code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
