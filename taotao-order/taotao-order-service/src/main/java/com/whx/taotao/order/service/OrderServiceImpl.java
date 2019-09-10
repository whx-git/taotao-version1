package com.whx.taotao.order.service;

import com.alibaba.fastjson.JSON;
import com.whx.taotao.order.dao.OrderDao;
import com.whx.taotao.order.OrderBean;
import com.whx.taotao.order.OrderItem;
import com.whx.taotao.order.OrderShipping;
import com.whx.taotao.order.domain.OrderStaus;
import com.whx.taotao.order.inter.OrderService;
import com.whx.taotao.pojo.TbOrder;
import com.whx.taotao.pojo.TbOrderItem;
import com.whx.taotao.pojo.TbOrderShipping;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private DefaultMQProducer defaultMQProducer;
    @Resource
    private OrderDao orderDao;

    public boolean addOrderItem(OrderBean orderBean, HttpSession session){
        try{
            List<OrderItem> orderItems = orderBean.getOrderItems();
            OrderShipping orderShipping = orderBean.getOrderShipping();
            Integer payment = orderBean.getPayment();

            TbOrder tbOrder=new TbOrder();
            TbOrderItem tbOrderItem=new TbOrderItem();
            TbOrderShipping tbOrderShipping=new TbOrderShipping();

            String orderid = UtilGenOrderID.genOrderID("1", "2", "3", "123456");

            tbOrder.setOrderId(orderid);
            tbOrder.setPayment(payment+"");
            tbOrder.setPaymentType(2);
            tbOrder.setStatus(OrderStaus.getWZF());
//            tbOrder.setCreateTime(new Date());
//            tbOrder.setUpdateTime(new Date());
//            tbOrder.setPaymentTime(new Date());
//            tbOrder.setConsignTime(new Date());
//            tbOrder.setCloseTime(new Date());
            tbOrder.setShippingName(orderShipping.getReceiverName());
            tbOrder.setUserId((long) 123456);

            for (int i = 0; i < orderItems.size(); i++) {
                Long itemId = orderItems.get(i).getItemId();
                Integer num = orderItems.get(i).getNum();
                String title = orderItems.get(i).getTitle();
                Integer price = orderItems.get(i).getPrice();
                Integer totalFee = orderItems.get(i).getTotalFee();
                String picPath = orderItems.get(i).getPicPath();
                tbOrderItem.setItemId(itemId+"");
                tbOrderItem.setOrderId(orderid);
                tbOrderItem.setNum(num);
                tbOrderItem.setTitle(title);
                tbOrderItem.setPrice(NumberUtils.createLong(price+""));
                tbOrderItem.setTotalFee(NumberUtils.createLong(totalFee+""));
                tbOrderItem.setPicPath(picPath);
            }

            tbOrderShipping.setOrderId(orderid);
            tbOrderShipping.setReceiverName(orderShipping.getReceiverName());
            tbOrderShipping.setReceiverPhone(orderShipping.getReceiverMobile());
            tbOrderShipping.setReceiverMobile("13245688");
            tbOrderShipping.setReceiverState(orderShipping.getReceiverState());
            tbOrderShipping.setReceiverCity(orderShipping.getReceiverCity());
            tbOrderShipping.setReceiverAddress(orderShipping.getReceiverAddress());
            tbOrderShipping.setCreated(new Date());
            tbOrderShipping.setUpdated(new Date());

            session.setAttribute("ordid",orderid);
            session.setAttribute("payment",payment);
            session.setAttribute("date",new Date());

            String order = JSON.toJSONString(tbOrder);
            String orderItem = JSON.toJSONString(tbOrderItem);
            String orderShip = JSON.toJSONString(tbOrderShipping);

//            String s=order+"-"+orderItem+"-"+orderShip;
            Message msg1=new Message("taotaoOrder","m1","",order.getBytes());
            Message msg2=new Message("taotaoOrder","m2","",orderItem.getBytes());
            Message msg3=new Message("taotaoOrder","m3","",orderShip.getBytes());
            SendResult result1 = defaultMQProducer.send(msg1);
            SendResult result2 = defaultMQProducer.send(msg2);
            SendResult result3 = defaultMQProducer.send(msg3);
            if (result1.getSendStatus()== SendStatus.SEND_OK && result2.getSendStatus()== SendStatus.SEND_OK &&result3.getSendStatus()== SendStatus.SEND_OK) {
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void insertOrder(TbOrder tbOrder,TbOrderItem tbOrderItem,TbOrderShipping tbOrderShipping){
        orderDao.insertOrder(tbOrder);
        orderDao.insertOrdetItem(tbOrderItem);
        orderDao.insertOrderShipping(tbOrderShipping);
    }

    @Override
    public void insertOrder(TbOrder tbOrder) {
        orderDao.insertOrder(tbOrder);
    }

    @Override
    public void insertOrderItem(TbOrderItem tbOrderItem) {
        orderDao.insertOrdetItem(tbOrderItem);
    }

    @Override
    public void insertOrderShip(TbOrderShipping tbOrderShipping) {
        orderDao.insertOrderShipping(tbOrderShipping);
    }

}
