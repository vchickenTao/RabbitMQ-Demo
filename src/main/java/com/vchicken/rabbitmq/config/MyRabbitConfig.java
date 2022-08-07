package com.vchicken.rabbitmq.config;

import lombok.Builder;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MyRabbitConfig {

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 使用JSON序列化机制，进行消息转换
     *
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 定制RabbitTemplate
     *  1.服务收到消息就回调
     *    1.spring.rabbitmq.publisher-confirm-type=correlated
     *    2.设置确定回调ConfirmRollback
     *  2.消息正确抵达队列进行回调
     *    1.spring.rabbitmq.publisher-returns=true
     *    spring.rabbitmq.template.mandatory=true
     *    2.设置确认回调ReturnCallback
     *  3.消费端确认（保证每个消息被正确消费，此时broker才可以删除这个消息）
     *    1.默认是自动确认的，只要消息接收到，客户端就会自动确认，服务端就会移除这个消息
     *      问题：
     *          我们收到很多消息，自动回复给服务器ack，只有一个消息处理成功，宕机了，然后消息丢失了；
     *          手动确认，只要我们没有明确告诉MQ,货物被接收，没有ack，消息就会一直是unacked状态。即使consumer
     *          宕机，消息也不会丢失，会重新变为Ready状态，下一次有新的consumer连接进来就发送给它
     *    2.如何签收
     */
    @Bean
    //MyRabbitConfig对象创建完成以后，执行该方法
    @PostConstruct
    public void initRabbitTemplate() {
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {

            /**
             * @param correlationData 当前消息的唯一关联数据（消息的唯一id）
             * @param b 消息是否成功收到
             * @param s 失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                System.out.println(correlationData + "|" + b + "|" + s);
            }
        });

        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {

            /**
             * 只要消息没有投递给指定的队列，就触发这个失败回调
             * @param returnedMessage
             *  private final Message message;   投递失败的消息详细信息
             *  private final int replyCode;     回复的状态码
             *  private final String replyText;  回复的文本内容
             *  private final String exchange;   当时这个消息是发给哪个交换机
             *  private final String routingKey; 当时这个消息用哪个路由键
             */
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                System.out.println(returnedMessage);
            }
        });
    }


}
