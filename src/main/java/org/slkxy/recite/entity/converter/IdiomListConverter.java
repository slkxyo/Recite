package org.slkxy.recite.entity.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slkxy.recite.entity.Idiom;

import java.util.List;

public class IdiomListConverter extends JsonConverter<List<Idiom>> {
    public IdiomListConverter() {
        super(new TypeReference<List<Idiom>>() {});
    }
}
