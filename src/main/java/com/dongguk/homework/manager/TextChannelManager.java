package com.dongguk.homework.manager;

import com.dongguk.homework.common.Channel;
import com.dongguk.homework.common.ChannelManager;
import com.dongguk.homework.common.PartitionChannel;
import com.dongguk.homework.common.Processor;
import com.dongguk.homework.processor.TextConsumerProcessor;

public class TextChannelManager implements ChannelManager<String> {

	private Integer bulkProcessorCount = 10;

    private PartitionChannel<String> channel;

    @Override
    public void init() {
        Processor<String> processor = new TextConsumerProcessor();

        channel = new PartitionChannel<String>("MemoryQueueChannel", processor, bulkProcessorCount);
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
