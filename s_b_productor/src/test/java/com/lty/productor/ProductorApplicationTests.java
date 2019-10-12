package com.lty.productor;

import com.lty.common.bean.Order;
import com.lty.productor.config.RabbitSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductorApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
     private RabbitSender rabbitSender;

    @Test
    public void rabbitSenderTest(){
        MessageProperties mp = new MessageProperties();
        mp.setContentType("text/plain");
        rabbitSender.rabbitSender("this is a message".getBytes(),mp);

    }

    @Test
    public void rabbitOrderSenderTest(){
        Order order = new Order();
        order.setId(1001);
        order.setName("iphone");
         rabbitSender.rabbitOrderSender(order);

    }

}
