package com.vchicken.rabbitmq;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 1.引入amqp场景；RabbitAutoConfiguration就会自动生效
 * 2.给容器中自动配置了
 *      RabbitTemplate,AmqpAdmin,CachingConnectionFactory,RabbitMassagingTemplate;
 *      所有属性都是spring.rabbitmq
 *
 * 3.给配置文件中配置spring.rabbitmq 信息
 * 4.@EnableRabbit：开启功能
 * 5.监听消息：使用@RabbitListener，必须有@EnableRabbit
 *   @RabbitListener: 类+方法上(监听哪些队列即可)
 *   @RabbitHandle: 方法上(承载区分不同的消息)
 */
@EnableRabbit
@SpringBootApplication
public class RabbitmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqApplication.class, args);
    }

}
