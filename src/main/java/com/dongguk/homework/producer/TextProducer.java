package com.dongguk.homework.producer;

import com.dongguk.homework.common.PartitionChannel;
import com.dongguk.homework.common.Producer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TextProducer extends Thread implements Producer<File> {

  private static final String FIRST_WORD_ALPHABET_OR_NUMBER = "^[a-zA-Z0-9]*$";

  private PartitionChannel<String> channel;
  private int partitionSize;
  private File file;

  public TextProducer(
    PartitionChannel<String> channel,
    int partitionSize,
    File file,
    String instanceName
  ) {
    super(instanceName);
    this.channel = channel;
    this.partitionSize = partitionSize;
    this.file = file;
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        this.produceData(file);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public boolean produceData(File object) {
    try {
      List<String> lines = Files.readAllLines(object.toPath());
      List<String> buffer = new ArrayList<>();

      for (String line : lines) {
        if (Pattern.matches(FIRST_WORD_ALPHABET_OR_NUMBER, line)) {
          buffer.add(line);
        }
      }

      int startNum = 0;
      int endNum = 0;
      double sliceNum = buffer.size() / partitionSize;

      for (int i = 0; i < partitionSize; i++) {
        endNum =
          (i == partitionSize - 1)
            ? buffer.size()
            : (int) (startNum + sliceNum);

        List<String> partitioning = new ArrayList<>(
          buffer.subList(startNum, endNum)
        );

        channel.put(i, partitioning);

        startNum += sliceNum;
      }

      return true;
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return false;
  }
}
