package com.side.freedomdaybackend.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;

@Converter(autoApply = true)
public class YearMonthConverter implements AttributeConverter<YearMonth, LocalDate> {

    @Override
    public LocalDate convertToDatabaseColumn(YearMonth attribute) {
        return attribute == null ? null : LocalDate.of(attribute.getYear(), attribute.getMonth(), 1);
    }

    @Override
    public YearMonth convertToEntityAttribute(LocalDate dbData) {
        return dbData == null ? null : YearMonth.from(dbData);
    }
}