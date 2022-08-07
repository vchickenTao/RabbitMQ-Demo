package com.vchicken.rabbitmq.config;

import com.rabbitmq.client.Channel;
import com.vchicken.rabbitmq.RabbitContants;
import com.vchicken.rabbitmq.entity.OrderEntity;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MyMQConfig {

    @RabbitListener(queues = "order.release.order.queue")
    void listener(OrderEntity orderEntity, Channel channel, Message message) {
        System.out.println("订单过期，准备关闭订单,订单号=" + orderEntity.getOrderSn());
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 容器中的Binding,Queue,Exchange 都会自动创建（RabbitMQ没有这些的情况下创建）
     * RabbitMQ只要有，@Bean声明的属性发送变化也不会覆盖
     *
     * @return
     */
    @Bean
    public Queue orderDelayQueue() {
        Map<String, Object> arguments = new HashMap<>(5);
        arguments.put("x-dead-letter-exchange", RabbitContants.ORDER_EVENT_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", RabbitContants.ORDER_RELEASE_ROUTING_KEY);
        arguments.put("x-message-ttl", 60000);
        return new Queue(RabbitContants.ORDER_DELAY_QUEUE, true, false, false, arguments);
    }

    @Bean
    public Queue orderReleaseOrderQueue() {
        return new Queue(RabbitContants.ORDER_RELEASE_QUEUE, true, false, false);
    }

    @Bean
    public Exchange orderEventExchange() {
        return new TopicExchange(RabbitContants.ORDER_EVENT_EXCHANGE, true, false);
    }

    @Bean
    public Binding orderCreateOrder() {
        return new Binding(RabbitContants.ORDER_DELAY_QUEUE,
                Binding.DestinationType.QUEUE,
                RabbitContants.ORDER_EVENT_EXCHANGE,
                RabbitContants.ORDER_CREATE_ROUTING_KEY,
                null);
    }

    @Bean
    public Binding orderReleaseOrder() {
        return new Binding(RabbitContants.ORDER_RELEASE_QUEUE,
                Binding.DestinationType.QUEUE,
                RabbitContants.ORDER_EVENT_EXCHANGE,
                RabbitContants.ORDER_RELEASE_ROUTING_KEY,
                null);
    }
}
