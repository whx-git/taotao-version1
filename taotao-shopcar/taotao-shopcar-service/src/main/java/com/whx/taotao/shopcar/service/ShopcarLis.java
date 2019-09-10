package com.whx.taotao.shopcar.service;

import com.alibaba.fastjson.JSON;
import com.whc.taotao.shopcar.ShopcarService;
import com.whx.taotao.pojo.TbShopcar;
import com.whx.taotao.shopcar.dao.ShopcarDao;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import javax.annotation.Resource;
import java.sql.SQLException;
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

public class ShopcarLis implements MessageListenerConcurrently {
    @Resource
    private ShopcarService shopcarService;

    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext Context) {


        for (MessageExt msg : list) {
            byte[] body = msg.getBody();
            String jsonString= new String(body);

            TbShopcar tbShopcar = JSON.parseObject(jsonString, TbShopcar.class);
            try {
                shopcarService.saveShopcarItem(tbShopcar);
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