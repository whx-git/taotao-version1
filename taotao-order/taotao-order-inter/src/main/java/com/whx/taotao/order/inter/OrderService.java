package com.whx.taotao.order.inter;

import com.whx.taotao.order.OrderBean;
import com.whx.taotao.pojo.TbOrder;
import com.whx.taotao.pojo.TbOrderItem;
import com.whx.taotao.pojo.TbOrderShipping;

import javax.servlet.http.HttpSession;

public interface OrderService {
    public boolean addOrderItem(OrderBean orderBean, HttpSession session);
    public void insertOrder(TbOrder tbOrder, TbOrderItem tbOrderItem, TbOrderShipping tbOrderShipping);
    public void insertOrder(TbOrder tbOrder);
    public void insertOrderItem(TbOrderItem tbOrderItem);
    public void insertOrderShip(TbOrderShipping tbOrderShipping);
}
