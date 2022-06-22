package com.dongguk.homework.common;

public interface Channel<T> {
	public void start();
	public void stop();
	public void destroy();

}