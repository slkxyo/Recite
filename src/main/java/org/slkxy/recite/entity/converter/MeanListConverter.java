package org.slkxy.recite.entity.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slkxy.recite.entity.Mean;

import java.util.List;

public class MeanListConverter extends JsonConverter<List<Mean>> {
    public MeanListConverter() {
        super(new TypeReference<List<Mean>>() {});
    }
}
