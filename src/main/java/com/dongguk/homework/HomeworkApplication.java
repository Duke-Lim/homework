package com.dongguk.homework;

import java.io.File;

import com.dongguk.homework.common.PartitionChannel;
import com.dongguk.homework.common.Producer;
import com.dongguk.homework.manager.TextChannelManager;
import com.dongguk.homework.producer.TextProducer;

public class HomeworkApplication {

	public static void main(String[] args) {
		String fileName = args[0];
		String saveDir = args[1];
		int partitionSize = Integer.parseInt(args[2]);

		TextChannelManager manager = new TextChannelManager();
		manager.init();

		Producer<File> producer = new TextProducer(manager.getChannel(), partitionSize);
		producer.produceData(new File(fileName));
	}

}
