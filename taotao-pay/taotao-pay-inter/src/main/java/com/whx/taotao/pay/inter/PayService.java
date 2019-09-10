package com.whx.taotao.pay.inter;

import com.whx.taotao.order.OrderBean;
import com.whx.taotao.order.OrderItem;
import com.whx.taotao.pojo.TbOrderItem;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface PayService {
    public boolean payOrder(List<TbOrderItem> tbOrderItems, HttpServletResponse response);
    public List<TbOrderItem> getTbOrderItem(String ordid);
}
