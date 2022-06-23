package com.dongguk.homework;

import com.dongguk.homework.common.PartitionChannel;
import com.dongguk.homework.common.Producer;
import com.dongguk.homework.consumer.Consumer;
import com.dongguk.homework.manager.TextChannelManager;
import com.dongguk.homework.processor.TextConsumerProcessor;
import com.dongguk.homework.producer.TextProducer;
import java.io.File;

public class HomeworkApplication {

  public static void main(String[] args) {
    String fileName = args[0];
    String saveDir = args[1];
    int partitionSize = Integer.parseInt(args[2]);

    TextChannelManager manager = new TextChannelManager(saveDir, partitionSize);
    manager.init();
    TextConsumerProcessor processor = new TextConsumerProcessor(saveDir);
    new TextProducer(
      manager.getChannel(),
      partitionSize,
      new File(fileName),
      "PRODUCER"
    )
      .start();

    for (int i = 0; i < partitionSize; i++) {
      try {
        Consumer<String> consumeThread = new Consumer<>(
          manager.getChannel(),
          i,
          processor
        );
        Thread thread = new Thread(consumeThread, "CONSUMER-" + i);
        thread.start();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
