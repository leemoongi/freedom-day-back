package com.side.freedomdaybackend.domain.loan;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RepaymentMethod {
    BULLET_REPAYMENT("BR"),              // 만기일시
    EQUAL_PRINCIPAL_AND_INTEREST("EPI"), // 월리금균등
    EQUAL_PRINCIPAL("EP");                // 원금균등

    private final String value;

    private RepaymentMethod(String value) {
        this.value = value;
    }

//    @JsonCreator
//    public static RepaymentMethod fromValue(String value) {
//        for (RepaymentMethod method : RepaymentMethod.values()) {
//            if (method.getValue().equals(value)) {
//                return method;
//            }
//        }
//        throw new IllegalArgumentException("Unknown value: " + value);
//    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
