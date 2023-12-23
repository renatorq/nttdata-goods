package com.nttdata.goods.kafka;

public interface KafkaProducer<T> {
    void sendMessage(T data);
}
