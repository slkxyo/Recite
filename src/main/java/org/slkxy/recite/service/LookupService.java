package org.slkxy.recite.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class LookupService {
    public static void lookup(String word) throws IOException {
        Document doc = Jsoup.connect(createLink(word)).get();
        System.out.println(doc);
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
