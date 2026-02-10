package org.slkxy.recite.entity;

import jakarta.persistence.Convert;
import lombok.Builder;
import lombok.Data;
import org.slkxy.recite.entity.converter.IdiomListConverter;
import org.slkxy.recite.entity.converter.MeanListConverter;

import java.util.List;

@Data
@Builder
public class LookupResult {
    private String word;

    private byte[] audio;

    private List<Mean> means;

    private List<Idiom> idioms;
}
