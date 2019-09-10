package com.whx.taotao.order.service;

import com.alibaba.fastjson.JSON;
import com.whx.taotao.pojo.TbOrder;
import com.whx.taotao.pojo.TbOrderItem;
import com.whx.taotao.pojo.TbOrderShipping;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import javax.annotation.Resource;
import java.util.List;
/*
   rocketmq的消费者，默认是一个并发的多线程消费场景

     默认的最少线程数量是20 最大XXX

     当我们的消息数量，超出线程数量的时候，每个线程处理一个，每次就只能取一部分消息回来处理了。

        //来模拟了一个场景，生产的快，soso的就生产了5个
        但是消费跟不上  同一时间只能处理1个



        <property name="pullBatchSize" value="1"></property> :每次只能取一个消息
        <property name="consumeThreadMin" value="1"></property>：同时最少有1个线程处理消息
        <property name="consumeThreadMax" value="1"></property>：同时最多有1个线程处理消息

 */

public class OrderLis implements MessageListenerConcurrently {
    @Resource
    private OrderServiceImpl orderService;

    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext Context) {


        for (MessageExt msg : list) {
            byte[] body = msg.getBody();
            String jsonString= new String(body);
            String tags = msg.getTags();
            TbOrder tbOrder =null;
            TbOrderItem tbOrderItem =null;
            TbOrderShipping tbOrderShipping =null;
            try {
            switch (tags){
                case "m1":
                    tbOrder=JSON.parseObject(jsonString, TbOrder.class);
                    orderService.insertOrder(tbOrder);
                    break;
                case "m2":
                    tbOrderItem = JSON.parseObject(jsonString, TbOrderItem.class);
                    orderService.insertOrderItem(tbOrderItem);
                    break;
                case "m3":
                    tbOrderShipping = JSON.parseObject(jsonString, TbOrderShipping.class);
                    orderService.insertOrderShip(tbOrderShipping);
                    break;
            }
//            orderService.insertOrder(tbOrder,tbOrderItem,tbOrderShipping);
        } catch (Exception e) {
            e.printStackTrace();
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
      }

//
        //设置当前次消息的处理结果
//                            return ConsumeConcurrentlyStatus.RECONSUME_LATER;

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}