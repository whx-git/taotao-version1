package com.whx.taotao.shopcar.controller;

import com.whc.taotao.shopcar.ShopcarService;
import com.whx.taotao.pojo.TbShopcar;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class ShopcatController {
    @Resource
    private ShopcarService shopcarService;
    @RequestMapping("cartSuccess")
    public String cartSuccess(){
        return "cartSuccess";
    }

    @RequestMapping("cart")
    public String goToCart(Model model){
        List<TbShopcar> userShopcar = shopcarService.getUserShopcar(123456);
        model.addAttribute("cartList",userShopcar);
        return "cart";
    }

    @RequestMapping("/cart/delete/{id}")
    public String deleteItem(@PathVariable("id")Long id){
        shopcarService.deleteItem(id);
        return "forward:/cart.html";
    }
}

