package com.dongguk.homework.processor;

import com.dongguk.homework.common.Processor;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

public class TextConsumerProcessor implements Processor<String> {

  private static final String FIRST_WORD_ALPHABET = "^[a-zA-Z]*$";

  private String saveDir;

  public TextConsumerProcessor(String saveDir) {
    this.saveDir = saveDir;
  }

  @Override
  public void process(String object) {
    try {
      processWarpper(object);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void processWarpper(String text) throws IOException {
    String saveFile;

    if (Pattern.matches(FIRST_WORD_ALPHABET, text)) {
      saveFile = text.substring(0, 1).toLowerCase() + ".txt";
    } else {
      saveFile = "number.txt";
    }

    File file = new File(saveDir + "/" + saveFile);
    FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
    fw.write(text + "\n");
    fw.close();
  }
}
