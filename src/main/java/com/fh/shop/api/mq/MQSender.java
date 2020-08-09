package com.fh.shop.api.mq;


import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.config.MQConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class MQSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

   /* public void  sendMail(String info){
        amqpTemplate.convertAndSend(MQConfig.MAILEXCHANGE,MQConfig.MAIL, info);
    }
*/
    public void  MailMessage(MailMessage mailMessage){

        String jsonMessage = JSONObject.toJSONString(mailMessage);

        amqpTemplate.convertAndSend(MQConfig.MAILEXCHANGE,MQConfig.MAIL,jsonMessage);
    }

    public void  sendMsg(String info){
        rabbitTemplate.convertAndSend(MQConfig.GOODS_FANOUT_EXCHANGE,info);
    }
}
