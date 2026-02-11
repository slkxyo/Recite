package org.slkxy.recite.service;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import lombok.extern.slf4j.Slf4j;
import org.slkxy.recite.entity.LookupResult;
import org.slkxy.recite.entity.Word;
import org.slkxy.recite.repositories.WordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class WordService {
    @Autowired
    private WordsRepository repository;

    @Autowired
    private LookupService lookupService;

    public void importWords() {
        Thread thread = new Thread(() ->{
            try (BufferedReader br = new BufferedReader(new FileReader("docs/words.txt"))) {
                String line;
                int count = 0;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        repository.save( Word.builder().word(line).grade(0).build() );
                        log.info("Imported: " + line + "  Total: " + count);
                        count++;
                    }
                }
                log.warn("Import Done! Total: " + count);
            }
            catch (Exception e) {
                log.error(e.getMessage());
            }
        });

        thread.start();
    }

    public void fillDB(){
        Thread thread = new Thread(() ->{
            List<Word> words = repository.findAll();
            words.forEach(word -> {
                if(!word.isLookedUp()){
                    try {
                        lookupService.lookup(word.getWord(), true);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        });

        thread.start();
    }

    public void fillDBMultiple(){
        Thread thread = new Thread(() ->{
            List<Word> words = repository.findAll();
            ExecutorService executor = Executors.newFixedThreadPool(10);
            List<Future<?>> futures = new ArrayList<>();
            words.forEach( word -> {
                if(!word.isLookedUp()){
                    log.warn("Got a Word from DB:" + word.getWord());
                    futures.add(executor.submit(() ->{
                        try {
                            lookupService.lookup(word.getWord(),true);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }));
                }
            });

            for(Future<?> future : futures){
                try {
                    future.get();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            executor.shutdown();
        });

        thread.start();
        log.warn("FillDB Done!");
    }
}
