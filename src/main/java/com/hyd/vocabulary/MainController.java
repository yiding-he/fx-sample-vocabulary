package com.hyd.vocabulary;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MainController {

    public ListView<String> lstWords;

    public Label lblWord;

    public Label lblPronounce;

    public Label lblChinese;

    public VBox samplesPane;

    ////////////////////////////////////////////////////////////

    private List<String> words = new ArrayList<>();

    private Map<String, JSONObject> vocabulary = new HashMap<>();

    public void initialize() throws Exception {
        loadWords();
        loadWord(words.get(0));

        lstWords.getItems().setAll(this.words);
        lstWords.getSelectionModel().selectedItemProperty().addListener((_ob, _old, _new) -> {
            loadWord(_new);
        });
    }

    private void loadWord(String word) {
        lblWord.setText(word);

        JSONObject wordObj = vocabulary.get(word);
        if (wordObj == null) {
            return;
        }

        JSONObject wordContent = wordObj.getJSONObject("content").getJSONObject("word").getJSONObject("content");
        if (wordContent == null) {
            return;
        }

        String ukphone = wordContent.getString("ukphone");
        if (ukphone != null) {
            lblPronounce.setText(ukphone);
        }

        JSONArray transArr = wordContent.getJSONArray("trans");
        if (transArr != null && transArr.size() > 0) {
            String tranCn = transArr.getJSONObject(0).getString("tranCn");
            if (tranCn != null) {
                lblChinese.setText(tranCn);
            }
        }

        JSONObject sentenceObj = wordContent.getJSONObject("sentence");
        if (sentenceObj != null) {
            JSONArray sentencesArr = sentenceObj.getJSONArray("sentences");
            if (sentencesArr != null) {
                loadSamples(sentencesArr);
            }
        }
    }

    private void loadSamples(JSONArray sentencesArr) {
        samplesPane.getChildren().clear();

        sentencesArr.forEach(o -> {
            JSONObject sentenceObj = (JSONObject) o;
            String content = sentenceObj.getString("sContent");
            String cn = sentenceObj.getString("sCn");

            Label contentLabel = new Label(content);
            contentLabel.setStyle("-fx-font-size: 18");
            Label cnLabel = new Label(cn);
            cnLabel.setStyle("-fx-font-size: 14");

            VBox samplePane = new VBox(contentLabel, cnLabel);
            samplesPane.getChildren().add(samplePane);
        });
    }

    private void loadWords() throws IOException {
        Path jsonFile = Paths.get("./CET4.json");
        Files.lines(jsonFile)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(line -> line.length() > 0)
                .forEach(line -> {
                    JSONObject wordObj = JSON.parseObject(line);
                    String headWord = wordObj.getString("headWord");

                    words.add(headWord);
                    vocabulary.put(headWord, wordObj);
                });

        System.out.println("words.size() = " + words.size());
    }
}
