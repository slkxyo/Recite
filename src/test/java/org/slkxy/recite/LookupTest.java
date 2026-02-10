package org.slkxy.recite;

import javazoom.jl.player.Player;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slkxy.recite.entity.Mean;
import org.slkxy.recite.entity.Word;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

@Slf4j
public class LookupTest {
    public static void main(String[] args) throws Exception {
        lookup("room");
    }

    public static void lookup(String word) throws Exception {
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
        setMean(wb, mean);

        Element idioms = body.selectFirst(".idioms");
        setIdioms(wb, idioms);

        System.out.println(wb.build());
    }

    private static void setAudio(Word.WordBuilder builder, Element audio) throws Exception {
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

    private static void setMean(Word.WordBuilder builder, Element mean) {
        Elements eles = mean.select(".shcut-g");
        ArrayList<Mean> means = new ArrayList<>();
        eles.forEach(e -> means.add(createMean(e)) );
        builder.means();

    }

    private static Mean createMean(Element mean) {
        Mean m = new Mean();
        Element grammer = mean.selectFirst("span.grammar");
        m.setGrammer(grammer != null ? grammer.text() : "");
        return m;
    }

    private static void setIdioms(Word.WordBuilder builder, Element idioms) {

    }

    private static String createLink(String word) {
        return new StringBuilder()
                .append("https://www.oxfordlearnersdictionaries.com/definition/english/")
                .append(word)
                .append("_1?q=")
                .append(word)
                .toString();
    }
}
