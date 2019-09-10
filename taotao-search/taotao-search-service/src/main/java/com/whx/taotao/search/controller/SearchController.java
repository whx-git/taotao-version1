package com.whx.taotao.search.controller;

import com.whx.taotao.pojo.TbItem;
import com.whx.taotao.search.inter.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class SearchController {
    @Resource
    private SearchService searchService;
    @RequestMapping("search")
    public String search(String q, Model model){
        System.out.println("q = " + q);
        List<TbItem> tbItems=null;
        if (StringUtils.isEmpty(q)) {
            tbItems= searchService.searchGoodsByKeyWord("all");
        }else{
            tbItems = searchService.searchGoodsByKeyWord(q);
        }
        model.addAttribute("query",q);
        model.addAttribute("tbItems",tbItems);


        return "search";
    }
}
