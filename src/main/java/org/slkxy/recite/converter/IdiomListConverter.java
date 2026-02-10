package org.slkxy.recite.converter;

import jakarta.persistence.AttributeConverter;
import org.slkxy.recite.entity.Idiom;

import java.util.List;

public class IdiomListConverter implements AttributeConverter<List<Idiom>, String> {
    @Override
    public String convertToDatabaseColumn(List<Idiom> idiomList) {
        return "";
    }

    @Override
    public List<Idiom> convertToEntityAttribute(String s) {
        return null;
    }
}
