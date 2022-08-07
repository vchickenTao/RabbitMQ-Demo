package com.vchicken.rabbitmq.controller;

import com.vchicken.rabbitmq.RabbitContants;
import com.vchicken.rabbitmq.entity.OrderEntity;
import com.vchicken.rabbitmq.entity.RabbitMessBody;
import com.vchicken.rabbitmq.entity.RabbitMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
public class RabbitSendController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMq")
    public String sendMq(@RequestParam(value = "num", defaultValue = "10") Integer num) {
        for (int i = 0; i < num; i++) {
            if (i % 2 == 0) {
                RabbitMessage rabbitMessage = new RabbitMessage();
                rabbitMessage.setAge("18-" + i);
                rabbitMessage.setName("vchicken-" + i);
                rabbitTemplate.convertAndSend("hello-java-exchange",
                        "hello-java",
                        rabbitMessage,
                        new CorrelationData(UUID.randomUUID().toString())
                );
            } else {
                RabbitMessBody rabbitMessBody = new RabbitMessBody();
                rabbitMessBody.setBody("消息体-" + i);
                rabbitMessBody.setTitle("小菜鸡-" + i);
                rabbitTemplate.convertAndSend("hello-java-exchange",
                        "hello-java",
                        rabbitMessBody,
                        new CorrelationData(UUID.randomUUID().toString())
                );
            }
        }
        return "发送消息成功！";
    }


    @GetMapping("/test/createOrderTest")
    public String createOrderTest() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(UUID.randomUUID().toString());
        orderEntity.setModifyTime(new Date());
        //给MQ发送消息
        rabbitTemplate.convertAndSend(RabbitContants.ORDER_EVENT_EXCHANGE, RabbitContants.ORDER_CREATE_ROUTING_KEY, orderEntity);
        return "发送成功";
    }
}
