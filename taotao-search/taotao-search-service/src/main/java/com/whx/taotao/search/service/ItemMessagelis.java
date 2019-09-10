package com.whx.taotao.search.service;

import com.alibaba.fastjson.JSON;
import com.whx.taotao.commons.ConstantsKey;
import com.whx.taotao.commons.kafkaTopics.MessageItem;
import com.whx.taotao.pojo.TbItem;
import com.whx.taotao.search.inter.SearchService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

import javax.annotation.Resource;

public class ItemMessagelis implements MessageListener<String,String> {
    @Resource
    private SearchService searchService;
    public void onMessage(ConsumerRecord<String, String> data) {
            //根据不同主题，消费
            System.out.println("========");
            if(ConstantsKey.KAFKA_TOPIC_ITEM.equals(data.topic())){
                String value = data.value();

                    MessageItem messageItem = JSON.parseObject(value, MessageItem.class);

                    int action = messageItem.getAction();
                    TbItem tbItem = messageItem.getTbItem();
                    switch (action) {
                        case MessageItem.ACTION_ADD:
                            searchService.addSearchItem(tbItem);
                            break;
                    }

            }
        }
}