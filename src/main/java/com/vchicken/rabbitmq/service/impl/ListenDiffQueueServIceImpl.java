package com.vchicken.rabbitmq.service.impl;

import com.rabbitmq.client.Channel;
import com.vchicken.rabbitmq.entity.RabbitMessBody;
import com.vchicken.rabbitmq.entity.RabbitMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RabbitListener(queues = "hello-java-queue")
public class ListenDiffQueueServIceImpl {


    /**
     * 监听队列消息1
     *
     * @param message 原生消息类型
     * @param content 发送的消息类型
     * @param channel 当前传输数据的通道
     */
    @RabbitHandler
    void receiveMessage(Message message, RabbitMessage content, Channel channel) {
//        System.out.println("收到消息了！" + message);
        System.out.println("消息体是=======>1" + content);
        //channel内顺序自增的
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("deliveryTag=" + deliveryTag);

        //签收货物
        try {
            if (deliveryTag % 2 == 0) {
                channel.basicAck(deliveryTag, false);
                System.out.println("货物签收了" + deliveryTag);
            } else {
                //requeue=false 丢弃 true 发回服务器，服务器重新入队
                channel.basicNack(deliveryTag,false,true);
                System.out.println("货物没被签收" + deliveryTag);
            }
        } catch (IOException e) {
            System.out.println("签收异常" + e);
        }
    }

    /**
     * 监听队列消息2
     *
     * @param content 发送的消息类型
     */
    @RabbitHandler
    void receiveMessage(RabbitMessBody content) {
        System.out.println("消息体是======>2" + content);
    }

}
