package org.slkxy.recite.service;

import lombok.extern.slf4j.Slf4j;
import org.slkxy.recite.entity.Word;
import org.slkxy.recite.repositories.WordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;

@Service
@Slf4j
public class WordService {
    @Autowired
    private WordsRepository repository;

    public void importWords() {
        try (BufferedReader br = new BufferedReader(new FileReader("docs/words.txt"))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    repository.save( Word.builder().word(line).grade(0).build() );
                    count++;
                }
            }
            System.out.println("成功导入 " + count + " 个单词！");
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
