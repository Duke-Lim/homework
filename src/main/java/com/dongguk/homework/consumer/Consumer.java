package com.dongguk.homework.consumer;

import com.dongguk.homework.common.PartitionChannel;
import com.dongguk.homework.common.Processor;
import com.dongguk.homework.processor.TextConsumerProcessor;
import java.util.List;

public class Consumer<T> implements Runnable {

  private PartitionChannel<T> partitionChannel;
  private int partitionIndex;
  private Processor<T> processor;

  public Consumer(
    PartitionChannel<T> partitionChannel,
    int partitionIndex,
    Processor<T> processor
  ) {
    this.partitionChannel = partitionChannel;
    this.partitionIndex = partitionIndex;
    this.processor = processor;
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        List<T> datas = partitionChannel.get(partitionIndex);

        for (T data : datas) {
          processor.process(data);
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
