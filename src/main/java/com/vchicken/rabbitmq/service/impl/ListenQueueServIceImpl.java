package com.vchicken.rabbitmq.service.impl;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;

@Service
public class ListenQueueServIceImpl implements Serializable {


    /**
     * 监听队列消息
     * @param message 原生消息类型
     * @param content 发送的消息类型
     * @param channel 当前传输数据的通道
     */
//    @RabbitListener(queues = "hello-java-queue")
    void receiveMessage(Message message, Map<String, Object> content, Channel channel) {
        System.out.println("收到消息了！" + message);
        System.out.println("消息体是" + content);
        System.out.println("数据通道是" + channel);
    }
}
