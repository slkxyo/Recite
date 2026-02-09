package org.slkxy.recite.converter;

import jakarta.persistence.AttributeConverter;
import org.slkxy.recite.entity.Mean;
import org.slkxy.recite.entity.Word;

import java.util.List;

public class MeanListConverter implements AttributeConverter<List<Mean>,String> {

    @Override
    public String convertToDatabaseColumn(List<Mean> meanList) {
        return "";
    }

    @Override
    public List<Mean> convertToEntityAttribute(String s) {
        return null;
    }

}
