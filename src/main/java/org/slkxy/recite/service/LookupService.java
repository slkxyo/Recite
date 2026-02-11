package org.slkxy.recite.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import javazoom.jl.player.Player;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slkxy.recite.entity.Idiom;
import org.slkxy.recite.entity.LookupResult;
import org.slkxy.recite.entity.Mean;
import org.slkxy.recite.entity.Word;
import org.slkxy.recite.repositories.WordsRepository;
import org.slkxy.recite.util.ClipboardUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class LookupService {
    @Autowired
    private  WordsRepository wordsRepository;

    private AtomicInteger atomicInteger = new AtomicInteger(0);


    public LookupResult lookup(String word, boolean updateToDB) throws Exception{
        LookupResult result = lookup(word);
        if(updateToDB && result.isSuccess()){
            updateToDB(result);
            log.info("Saved LookupResult to DB: " + result.getWord() + "  Total: " + atomicInteger.incrementAndGet());
        }

        return result;
    }

    private void updateToDB(LookupResult result){
        Word word = wordsRepository.findByWord(result.getWord()).orElseThrow( () -> new RuntimeException("Word \"" + result.getWord() + "\" not found"));
        word.setAudio(result.getAudio());
        word.setMeans(result.getMeans());
        word.setIdioms(result.getIdioms());
        word.setLookedUp(true);

        wordsRepository.save(word);
    }


    private  LookupResult lookup(String word) throws Exception {
        LookupResult.LookupResultBuilder lb = LookupResult.builder();
        lb.word(word);
        Element body;
        try {
            body = Jsoup
                    .connect(createLink(word))
                    .get()
                    .selectFirst("#entryContent")
                    .selectFirst(".entry");

        }
        catch (Exception e) {
            return  lb.success(false).build();
        }
        Element audio = body.selectFirst(".top-container");
        setAudio(lb, audio);

        Element mean = body.selectFirst(".senses_multiple");
        if(mean == null) mean = body.selectFirst(".sense_single");
        setMeans(lb, mean);

        Element idioms = body.selectFirst(".idioms");
        setIdioms(lb, idioms);

        LookupResult result = lb.build();
        if(result.getAudio() != null && result.getMeans() != null )  result.setSuccess(true);
        else result.setSuccess(false);


        //JSON化后复制到剪切板（调试用）
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(result);
//        ClipboardUtil.copyToClipboard(json);
//        log.warn("Looked up word: " + word);
//        log.warn(json);

        return result;
    }

    private void setAudio(LookupResult.LookupResultBuilder builder, Element audio) throws Exception {
        if(audio == null) return;
//        String link = audio.select("div.sound.audio_play_button.pron-us").attr("data-src-mp3");
        String link = audio.select("pron-us").attr("data-src-mp3");
        if(link.isBlank()) link = audio.select("pron-us").attr("data-src-ogg");
        if(link.isBlank()) return;
        URL url = new URL(link);
        InputStream in = url.openStream();
        byte[] bytes = in.readAllBytes();
        builder.audio(bytes);
        //playAudio(bytes);
        in.close();
    }

    private void playAudio(byte[] bytes) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Player player = new Player(bis);
        player.play();

    }

    private void setMeans(LookupResult.LookupResultBuilder builder, Element mean) {
        if(mean == null) return;
        Elements eles = mean.select(".sense");
        ArrayList<Mean> means = new ArrayList<>();
        eles.forEach(e -> means.add(createMean(e)) );
        builder.means(means);

    }

    private  Mean createMean(Element mean) {
        Mean m = new Mean();
        Element grammer = mean.selectFirst("span.grammar");
        m.setGrammer(grammer != null ? grammer.text() : "");

        Element def = mean.selectFirst("span.def");
        m.setDef(def != null ? def.text() : "");

        Elements examplesElement = mean.select(".examples li");
        ArrayList<String>  examples = new ArrayList<>();
        examplesElement.forEach(e -> examples.add(e.text()));
        m.setExamples(String.join("@@", examples));
        return m;
    }

    private void setIdioms(LookupResult.LookupResultBuilder builder, Element idioms) {
        if(idioms == null) return;
        Elements idiomElements = idioms.select(".idm-g");
        ArrayList<Idiom> idiomList = new ArrayList<>();
        idiomElements.forEach(i -> idiomList.add(createIdiom(i)));
        builder.idioms(idiomList);
    }

    private Idiom createIdiom(Element idiom) {
        Idiom i = new Idiom();
        i.setDef(idiom.select(".def").text());
        Elements examples = idiom.select(".examples li");
        ArrayList<String>  examplesStr = new ArrayList<>();
        examples.forEach(e -> examplesStr.add(e.text()));
        i.setExamples(String.join("@@", examplesStr));
        return i;
    }

    private  String createLink(String word) {
        return new StringBuilder()
                .append("https://www.oxfordlearnersdictionaries.com/definition/english/")
                .append(word)
                .append("_1?q=")
                .append(word)
                .toString();
    }
}
