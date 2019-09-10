package com.whx.taotao.order.controller;

import com.whc.taotao.shopcar.ShopcarService;
import com.whx.taotao.order.OrderBean;
import com.whx.taotao.order.inter.OrderService;
import com.whx.taotao.pojo.TbShopcar;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {
    @Resource
    private ShopcarService shopcarService;

    @Resource
    private OrderService orderService;
    @RequestMapping("/order/order-cart")
    public String genOrder(String[] checkItem, String[] num, Model model){
        System.out.println("checkItem = [" + Arrays.toString(checkItem) + "], num = [" + Arrays.toString(num) + "]");
        List<TbShopcar> shopcars = shopcarService.getShopcarByIds(checkItem);
        List<String> checklist = Arrays.asList(checkItem);
        for(int i=0;i<shopcars.size();i++){
            Long id = shopcars.get(i).getId();
            int index = checklist.indexOf(id+"");
            shopcars.get(i).setNum(NumberUtils.createInteger(num[index]));
        }
        model.addAttribute("carts",shopcars);
        return "order-cart";
    }

    @RequestMapping("/create")
    public void createOrder(OrderBean orderBean, HttpSession session,HttpServletResponse response)throws Exception{
        boolean b = orderService.addOrderItem(orderBean,session);

       String ordid = (String) session.getAttribute("ordid");


        response.sendRedirect("http://localhost:8087/pay.html?ordid="+ordid);
    }

    @RequestMapping("orderSuccess")
    public String orderSuccess(HttpSession session, Model model){
        Integer payment = (Integer) session.getAttribute("payment");
        String ordid = (String) session.getAttribute("ordid");
        Date date = (Date) session.getAttribute("date");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        String date1 = simpleDateFormat.format(date);
        model.addAttribute("orderId",ordid);
        model.addAttribute("payment",payment);
        model.addAttribute("date",date1);
        return "success";
    }
}
