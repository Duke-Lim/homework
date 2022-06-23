package com.dongguk.homework.manager;

import com.dongguk.homework.common.ChannelManager;
import com.dongguk.homework.common.PartitionChannel;
import com.dongguk.homework.common.Processor;
import com.dongguk.homework.processor.TextConsumerProcessor;

public class TextChannelManager implements ChannelManager<String> {

  private PartitionChannel<String> channel;

  private String saveDir;
  private int partitionSize;

  public TextChannelManager(String saveDir, int partitionSize) {
    this.saveDir = saveDir;
    this.partitionSize = partitionSize;
  }

  @Override
  public void init() {
    channel = new PartitionChannel<>(partitionSize);
    channel.start();
  }

  @Override
  public void destroy() {
    channel.destroy();
  }

  public PartitionChannel<String> getChannel() {
    return this.channel;
  }
}
