package org.slkxy.recite.util;


import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.awt.*;

public class ClipboardUtil {
    public static void copyToClipboard(String text) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }
}
