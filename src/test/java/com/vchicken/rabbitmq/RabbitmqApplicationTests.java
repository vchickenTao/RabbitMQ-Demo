package com.vchicken.rabbitmq;

import com.vchicken.rabbitmq.entity.RabbitMessBody;
import com.vchicken.rabbitmq.entity.RabbitMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

@Slf4j
@SpringBootTest
class RabbitmqApplicationTests {

    @Resource
    AmqpAdmin amqpAdmin;

    @Resource
    RabbitTemplate rabbitTemplate;

    @Test
    void createExchange() {
        // 创建交换机
        // String name, boolean durable, boolean autoDelete
        DirectExchange exchange = new DirectExchange("hello-java-exchange", true, false);
        amqpAdmin.declareExchange(exchange);
        log.info("Exchange创建[{}]成功", "hello-java-exchange");
    }

    @Test
    void createQueue() {
        amqpAdmin.declareQueue(new Queue("hello-java-queue", true, false, false));
        log.info("Queue创建[{}]成功", "hello-java-queue");
    }

    @Test
    void createBingding() {
        amqpAdmin.declareBinding(new Binding("hello-java-queue",
                Binding.DestinationType.QUEUE,
                "hello-java-exchange",
                "hello-java",
                null
        ));
        log.info("Binding创建[{}]成功", "hello-java-binding");
    }

    @Test
    void sendMessageTest() {
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                RabbitMessage rabbitMessage = new RabbitMessage();
                rabbitMessage.setAge("18-" + i);
                rabbitMessage.setName("vchicken-" + i);
                rabbitTemplate.convertAndSend("hello-java-exchange",
                        "hello-java",
                        rabbitMessage
                );
            } else {
                RabbitMessBody rabbitMessBody = new RabbitMessBody();
                rabbitMessBody.setBody("消息体-" + i);
                rabbitMessBody.setTitle("小菜鸡-" + i);
                rabbitTemplate.convertAndSend("hello-java-exchange",
                        "hello-java",
                        rabbitMessBody
                );
            }
        }
    }

    @Test
    void deCode() throws UnsupportedEncodingException {
        String decode = URLDecoder.decode("rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAACdAAEbmFtZXQA\n" +
                "CHZjaGlja2VudAADYWdldAADMTAweA==", "UTF-8");
        log.info("encode{}", decode);
    }
}
