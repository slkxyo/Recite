package org.slkxy.recite.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slkxy.recite.entity.converter.IdiomListConverter;
import org.slkxy.recite.entity.converter.MeanListConverter;

import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Word {
    @Id
    private String id;

    private String word;

    private int grade;

    private boolean lookedUp;

    @Column(columnDefinition = "BLOB")
    private byte[] audio;

    @Convert(converter = MeanListConverter.class)
    private List<Mean> means;

    @Convert(converter = IdiomListConverter.class)
    private List<Idiom> idioms;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}

