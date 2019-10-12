package com.lty.s_b_consume.config;

import com.lty.common.bean.Order;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Map;

@Configuration
@ComponentScan({"com.lty.s_b_consume"})
public class RabbitRecive {

    @RabbitListener(
            bindings = @QueueBinding
                    (
            value = @Queue(value = "queue-01",durable = "true"),
            exchange = @Exchange(value = "exchange-01", durable = "true", type = "topic", ignoreDeclarationExceptions = "true"),
             key = "springboot.*"
                    )

    )
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception {
        System.err.println("--------消费端处理------");
        System.err.println("消费端得到的message.getPayload()："+message.getPayload());
       Long deliveryTag= (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
       //手工签收ack
        channel.basicAck(deliveryTag,false);
    }

    /**
     * 直接接收对象消费端
     * spring.rabbitmq.queue.name=queue-02
     * spring.rabbitmq.queue.durable=true
     * spring.rabbitmq.exchange.name=exchange-02
     * spring.rabbitmq.exchange.durable=true
     * spring.rabbitmq.exchange.type=topic
     * spring.rabbitmq.exchange.ignoreDeclarationExceptions=true
     * spring.rabbitmq.key.name=springboot.*
     */

    @RabbitListener(
            bindings = @QueueBinding
                    (
                            value = @Queue(value = "${spring.rabbitmq.queue.name}",durable = "${spring.rabbitmq.queue.durable}"),
                            exchange = @Exchange(value = "${spring.rabbitmq.exchange.name}", durable = "${spring.rabbitmq.exchange.durable}", type = "${spring.rabbitmq.exchange.type}", ignoreDeclarationExceptions = "${spring.rabbitmq.exchange.ignoreDeclarationExceptions}"),
                            key = "${spring.rabbitmq.key.name}"
                    )

    )
    @RabbitHandler
    public void onOrderMessage(@Payload Order order, @Headers Map<String,Object> headers, Channel channel) throws Exception {
        System.err.println("--------消费端处理------");
        System.err.println("消费端得到的Order对象："+order.toString());
        Long deliveryTag= (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        //手工签收ack
        channel.basicAck(deliveryTag,false);
    }


}
