package com.whx.taotao.detail.controller;

import com.whc.taotao.shopcar.ShopcarService;
import com.whx.taotao.inter.ItemService;
import com.whx.taotao.pojo.TbItem;
import com.whx.taotao.pojo.TbItemDesc;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class DeatilController {
    @Resource
    private ItemService itemService;
    @Resource
    private FTLCacheUtil ftlCacheUtil;
    @Resource
    private ShopcarService shopcarService;
    @RequestMapping("itemDetail/{itemid}")
    public void itemDetail(@PathVariable("itemid") String itemid, Model model, HttpServletResponse response){
        TbItem tbItem = itemService.getItemById(itemid);
        TbItemDesc itemdesc = itemService.getItemdescById(itemid);
        String[] images=tbItem.getImage().split(",");
        ftlCacheUtil.addAttribute("item",tbItem);
        ftlCacheUtil.addAttribute("itemdesc",itemdesc);
        ftlCacheUtil.addAttribute("images",images);
        try {
            ftlCacheUtil.processFtl(itemid+"","item.ftl" ,response );
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.getWriter().write("您的网络异常,请重试!!");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @RequestMapping("/cart/add/{itemid}/{num}")
    @ResponseBody
    public ShopcarResult addShopcar(@PathVariable("itemid") String itemid,@PathVariable("num") String num){
        boolean b = shopcarService.addShopcarItem(itemid, NumberUtils.createInteger(num));
        ShopcarResult shopcarResult=new ShopcarResult();
        shopcarResult.setSuccess(b);
        shopcarResult.setMessage(b?"添加购物车成功!":"网络异常请重试!");
        return shopcarResult;
    }
}
