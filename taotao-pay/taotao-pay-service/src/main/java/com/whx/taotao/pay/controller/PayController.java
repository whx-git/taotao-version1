package com.whx.taotao.pay.controller;

import com.whx.taotao.pay.inter.PayService;
import com.whx.taotao.pojo.TbOrderItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class PayController {

    @Resource
    private PayService payService;
    @RequestMapping("pay")
    public void pay(String ordid, HttpServletResponse response){
        response.setContentType("text/html;charset=utf-8");
        List<TbOrderItem> tbOrderItem = payService.getTbOrderItem(ordid);
        payService.payOrder(tbOrderItem,response);

    }
}
