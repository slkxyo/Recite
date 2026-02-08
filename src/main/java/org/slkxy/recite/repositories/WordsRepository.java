package org.slkxy.recite.repositories;

import org.slkxy.recite.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WordsRepository  extends JpaRepository<Word, UUID> {}
