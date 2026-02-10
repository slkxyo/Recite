package org.slkxy.recite.fxcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.slkxy.recite.entity.LookupResult;
import org.slkxy.recite.entity.Word;
import org.slkxy.recite.service.LookupService;
import org.slkxy.recite.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
@Slf4j
public class MainController implements Initializable {
    @Autowired
    private WordService wordService;

    @Autowired
    private LookupService lookupService;

    @FXML
    private Button bt_search;

    @FXML
    private Button bt_filldb;

    @FXML
    private TextField tf;

    @FXML
    private TextArea ta;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
       bt_search.setOnAction(event -> {
           log.warn("looked up word");
           String word = tf.getText().trim();
           try {
               LookupResult res = lookupService.lookup(word,false);
               ObjectMapper mapper = new ObjectMapper();
               ta.setText(mapper.writeValueAsString(res));
           } catch (Exception e) {
               throw new RuntimeException(e);
           }
       });

       bt_filldb.setOnAction(event -> {
           List<Word> words 
       })
    }
}