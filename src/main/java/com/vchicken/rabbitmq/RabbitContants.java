package com.vchicken.rabbitmq;

/**
 * @Description: 常量
 * @author vchicken
 * @Date 2022/8/7 19:52
 * @Param
 * @return
 * @since version-1.0
 */
public class RabbitContants {

    /**
     * 延迟队列交换机
     */
    public final static String ORDER_EVENT_EXCHANGE = "order-event-exchange";

    /**
     * 延迟队列
     */
    public final static String ORDER_DELAY_QUEUE = "order.delay.queue";

    /**
     * release队列
     */
    public final static String ORDER_RELEASE_QUEUE = "order.release.order.queue";

    /**
     * order.release消息的路由键
     */
    public final static String ORDER_RELEASE_ROUTING_KEY = "order.release.order";

    /**
     * order.create消息的路由键
     */
    public final static String ORDER_CREATE_ROUTING_KEY = "order.create.order";


}
