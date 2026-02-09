package org.slkxy.recite.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

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

    public int grade;

    @Lob
    private byte[] audio;

    @Convert(converter = Jsr310JpaConverters.class)
    private List<Mean> means;

    private List<Idiom> idioms;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}

