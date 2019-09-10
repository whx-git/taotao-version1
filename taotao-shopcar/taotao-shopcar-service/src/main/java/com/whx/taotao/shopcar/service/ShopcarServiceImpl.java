package com.whx.taotao.shopcar.service;

import com.alibaba.fastjson.JSON;
import com.whc.taotao.shopcar.ShopcarService;
import com.whx.taotao.inter.ItemService;
import com.whx.taotao.pojo.TbItem;
import com.whx.taotao.pojo.TbShopcar;
import com.whx.taotao.shopcar.dao.ShopcarDao;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ShopcarServiceImpl implements ShopcarService {
    @Resource
    private ShopcarDao shopcarDao;
    @Resource
    private ItemService itemService;
    @Resource
    private DefaultMQProducer defaultMQProducer;
    @Override
    public boolean addShopcarItem(String itemid, Integer num) {

        try {
            TbItem tbItem = itemService.getItemById(itemid);
            TbShopcar tbShopcar=new TbShopcar();
            tbShopcar.setAddDate(new Date());
            tbShopcar.setItemId(NumberUtils.createLong(itemid));
            tbShopcar.setItemImg(tbItem.getImage().split(",")[0]);
            tbShopcar.setItemTitle(tbItem.getTitle());
            tbShopcar.setPrice((int)(long)tbItem.getPrice());
            tbShopcar.setTotalPrice((int)(long)tbItem.getPrice()*num);
            tbShopcar.setTag(System.currentTimeMillis());
            tbShopcar.setNum(num);
            tbShopcar.setUserId(123456);

            String s = JSON.toJSONString(tbShopcar);
            Message msg=new Message("taotaoshopcar","","",s.getBytes());
            //生产者发送消息到消息队列
            SendResult result = defaultMQProducer.send(msg);
            if (result.getSendStatus()== SendStatus.SEND_OK) {
                return true;
            }else {
                return false;
            }


        }catch(Exception e){
            e.printStackTrace();
        }


        return false;
    }

    @Override
    public void saveShopcarItem(TbShopcar tbShopcar) throws Exception {
        TbShopcar tbShopcarDB= shopcarDao.queryDataByItemAndUserId(tbShopcar.getItemId(), tbShopcar.getUserId());
        if (tbShopcarDB != null) {
            shopcarDao.updateTbShopcar(tbShopcar);
        }else {
            shopcarDao.insertShopcarItem(tbShopcar);
        }

    }

    @Override
    public List<TbShopcar> getUserShopcar(Integer userid) {
        List<TbShopcar> tbShopcars = shopcarDao.queryDataByUserId(userid);
        return tbShopcars;
    }

    @Override
    public void deleteItem(Long id) {
        shopcarDao.deleteItem(id);
    }

    @Override
    public List<TbShopcar> getShopcarByIds(String[] shopcarids) {

        return shopcarDao.queryById(shopcarids);
    }
}
