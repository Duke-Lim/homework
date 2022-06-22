package com.dongguk.homework.processor;

import com.dongguk.homework.common.Processor;

public class TextConsumerProcessor implements Processor<String> {

    @Override
    public void process(String object) {
        System.out.println(object);
    }
    
}
