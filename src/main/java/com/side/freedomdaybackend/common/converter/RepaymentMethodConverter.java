package com.side.freedomdaybackend.common.converter;


import com.side.freedomdaybackend.domain.loan.RepaymentMethod;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RepaymentMethodConverter implements AttributeConverter<RepaymentMethod, String> {

    @Override
    public String convertToDatabaseColumn(RepaymentMethod repaymentMethod) {
        if (repaymentMethod == null) {
            return null;
        }
        return repaymentMethod.getValue();
    }

    @Override
    public RepaymentMethod convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        for (RepaymentMethod method : RepaymentMethod.values()) {
            if (method.getValue().equals(dbData)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Unknown database value: " + dbData);
    }

}