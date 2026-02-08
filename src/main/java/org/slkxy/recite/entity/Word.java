package org.slkxy.recite.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Word {
    @Id
    @Column(length = 36)
    private String id;

    private String word;

    public int grade;

    @PrePersist
    public void prePersist() {
    if (id == null) {
        id = UUID.randomUUID().toString();
    }
}
}
