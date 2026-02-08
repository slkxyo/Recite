package org.slkxy.recite.fxcontroller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.slkxy.recite.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainController {
    @Autowired
    private WordService wordService;

    @FXML
    private Label welcomeText;


    @FXML
    protected void onHelloButtonClick() {
        wordService.importWords();
        welcomeText.setText("Hello World!");
    }
}