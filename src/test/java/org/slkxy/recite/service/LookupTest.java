package org.slkxy.recite.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javazoom.jl.player.Player;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slkxy.recite.entity.Idiom;
import org.slkxy.recite.entity.Mean;
import org.slkxy.recite.entity.Word;
import org.slkxy.recite.repositories.WordsRepository;
import org.slkxy.recite.util.ClipboardUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.Example;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.runtime.ObjectMethods;
import java.net.URL;
import java.util.ArrayList;

@Slf4j
public class LookupTest {
    public static void main(String[] args) throws Exception {
        lookup("communication");
    }


    public static Word lookup(String word) throws Exception {
        Element body = Jsoup
                .connect(createLink(word))
                .get()
                .selectFirst("#entryContent")
                .selectFirst(".entry");
        Word.WordBuilder wb = Word.builder();
        assert body != null;
        Element audio = body.selectFirst(".top-container");
        setAudio(wb, audio);

        Element mean = body.selectFirst(".senses_multiple");
        setMeans(wb, mean);

        Element idioms = body.selectFirst(".idioms");
        setIdioms(wb, idioms);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(wb.build());
        Platform.startup(() -> {
            ClipboardUtil.copyToClipboard(json);
            Platform.exit();
        });
        log.warn("Looked up word: " + word);
        log.warn(json);

        return wb.build();
    }

    private static void setAudio(Word.WordBuilder builder, Element audio) throws Exception {
        if (audio == null) return;
        String link = audio.select("div.sound.audio_play_button.pron-us").attr("data-src-mp3");
        URL url = new URL(link);
        InputStream in = url.openStream();
        byte[] bytes = in.readAllBytes();
        builder.audio(bytes);
        in.close();
    }

    private static void playAudio(byte[] bytes) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Player player = new Player(bis);
        player.play();

    }

    private static void setMeans(Word.WordBuilder builder, Element mean) {
        if (mean == null) return;
        Elements eles = mean.select(".shcut-g");
        ArrayList<Mean> means = new ArrayList<>();
        eles.forEach(e -> means.add(createMean(e)) );
        builder.means(means);

    }

    private static Mean createMean(Element mean) {
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

    private static void setIdioms(Word.WordBuilder builder, Element idioms) {
        if (idioms == null) return;
        Elements idiomElements = idioms.select(".idm-g");
        ArrayList<Idiom> idiomList = new ArrayList<>();
        idiomElements.forEach(i -> idiomList.add(createIdiom(i)));
        builder.idioms(idiomList);
    }

    private static Idiom createIdiom(Element idiom) {
        Idiom i = new Idiom();
        i.setDef(idiom.select(".def").text());
        Elements examples = idiom.select(".examples li");
        ArrayList<String>  examplesStr = new ArrayList<>();
        examples.forEach(e -> examplesStr.add(e.text()));
        i.setExamples(String.join("@@", examplesStr));
        return i;
    }

    private static String createLink(String word) {
        return new StringBuilder()
                .append("https://www.oxfordlearnersdictionaries.com/definition/english/")
                .append(word)
//                .append("_1?q=")
//                .append(word)
                .toString();
    }
}
