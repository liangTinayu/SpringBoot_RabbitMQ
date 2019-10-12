package com.lty.productor.config;

import com.lty.common.bean.Order;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 关于 msgSendConfirmCallBack 和 msgSendReturnCallback 的回调说明：
     * 1.如果消息没有到exchange,则confirm回调,ack=false
     * 2.如果消息到达exchange,则confirm回调,ack=true
     * 3.exchange到queue成功,则不回调return
     * 4.exchange到queue失败,则回调return(需设置mandatory=true,否则不回调,消息就丢了)
     */



    //回调 确认函数---  生产者发送至broker 是否成功
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.err.println("correlationData:"+correlationData);
            System.err.println("ack:"+ ack);
            if(!ack){
               System.err.println("ack:false,消息没有到exchange,confirm回调");
            }


        }
    };

    //返回函数 当消息从交换机到队列失败时，该方法被调用。（若成功，则不调用）
    //需要注意的是：该方法调用后，MsgSendConfirmCallBack中的confirm方法也会被调用，且ack = true

    final  RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
            System.err.println("return exchange: " + exchange + ", routingKey: "
                    + routingKey + ", replyCode: " + replyCode + ", replyText: " + replyText);
        }
    };


    public  void rabbitSender(byte[] messageContent , MessageProperties properties){
        MessageBuilder messageBuilder = MessageBuilder.withBody(messageContent).andProperties(properties);
        Message msg =messageBuilder.build();
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        //设置消息 CorrelationData correlationData
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
       rabbitTemplate.send("exchange-01","springboot.*",msg,correlationData);

    }

    public  void rabbitOrderSender(Order order){
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        //设置消息 CorrelationData correlationData
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("exchange-02","springboot.def",order,correlationData);
    }

}
