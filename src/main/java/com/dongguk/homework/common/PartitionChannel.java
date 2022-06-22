package com.dongguk.homework.common;

import java.util.ArrayList;
import java.util.List;

public class PartitionChannel<T> implements Channel<T> {

	private String instanceName;

	private int partitionSize;

	private List<List<T>> partition = null;

	private Processor<T> processor = null;

	private Thread[] workThreads = null;

	public PartitionChannel(String instanceName, Processor<T> processor, Integer partitionSize) {
		this.instanceName = instanceName;
		
		if(partitionSize != null && partitionSize > 0) {
			this.partitionSize = partitionSize.intValue();
		}

		this.partition = new ArrayList<>(partitionSize);
		this.processor = processor;
		this.workThreads = new Thread[this.partitionSize];
	}

	@Override
	public void start() {
		for(int i = 0; i < this.partitionSize; i++) {
			this.partition.add(new ArrayList<T>());

			try {
                Consumer consumeThread = new Consumer(this, i);
				workThreads[i] = new Thread(consumeThread, instanceName + "-" + i);
				
				workThreads[i].start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stop() {
		for(int i = 0; i < this.partitionSize; i++) {
			try {
				workThreads[i].interrupt();
				workThreads[i] = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void destroy() {
		stop();
	}

	class Consumer implements Runnable {

		private PartitionChannel<T> partitionChannel;
		private int partitionIndex;

		Consumer(PartitionChannel<T> partitionChannel, int partitionIndex) {
			this.partitionChannel = partitionChannel;
			this.partitionIndex = partitionIndex;
		}

		@Override
		public void run() {
			while(!Thread.currentThread().isInterrupted()) {
				try {
					List<T> datas = partitionChannel.get(partitionIndex);

					for (T data : datas) {
						processor.process(data);
					}
				} catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public synchronized List<T> get(int partitionIndex) throws InterruptedException {
		List<T> partitonData = partition.get(partitionIndex);

		while (partitonData.size() <= 0) {
            wait();
        }

		notifyAll();

		partition.set(partitionIndex, new ArrayList<>());

		return partitonData;
	}

	public synchronized void put(int partitionIndex, List<T> data) throws InterruptedException {
		List<T> partitonData = partition.get(partitionIndex);

		System.out.println(partitonData.size() <= 0);

		while (!(partitonData.size() <= 0)) {
            wait();
        }

		notifyAll();

		partition.set(partitionIndex, data);
	}
}