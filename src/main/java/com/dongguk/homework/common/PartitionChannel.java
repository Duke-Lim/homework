package com.dongguk.homework.common;

import java.util.ArrayList;
import java.util.List;

public class PartitionChannel<T> implements Channel<T> {

  private int partitionSize;

  private List<List<T>> partition = null;

  public PartitionChannel(Integer partitionSize) {
    if (partitionSize != null && partitionSize > 0) {
      this.partitionSize = partitionSize.intValue();
    }

    this.partition = new ArrayList<>(partitionSize);
  }

  @Override
  public void start() {
    for (int i = 0; i < this.partitionSize; i++) {
      this.partition.add(new ArrayList<T>());
    }
  }

  @Override
  public void destroy() {
    stop();
  }

  public synchronized List<T> get(int partitionIndex)
    throws InterruptedException {
    notifyAll();
    List<T> partitonData = partition.get(partitionIndex);
    System.out.println(partitonData);
    System.out.println(partitonData.isEmpty());

    while (partitonData.isEmpty()) {
      wait();
    }

    partition.set(partitionIndex, new ArrayList<>());

    return partitonData;
  }

  public synchronized void put(int partitionIndex, List<T> data)
    throws InterruptedException {
    List<T> partitonData = partition.get(partitionIndex);

    while (!partitonData.isEmpty()) {
      wait();
    }

    notifyAll();

    partition.set(partitionIndex, data);
    System.out.println(partition);
  }

  @Override
  public void stop() {
    // TODO Auto-generated method stub

  }
}
