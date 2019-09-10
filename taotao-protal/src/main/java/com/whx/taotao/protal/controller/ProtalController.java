package com.whx.taotao.protal.controller;

import com.alibaba.dubbo.common.json.JSON;
import com.whx.taotao.commons.PicUrlUtil;
import com.whx.taotao.inter.ContentService;
import com.whx.taotao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProtalController {
    @Resource
    private ContentService contentService;
    @Value("${nginx_server}")
    private String nginxServer;
    @RequestMapping("index")
    public String index(Model model) throws IOException {
        List<TbContent> bigADS = contentService.getBigADS();
        List<BigADVO> bigADVOS=new ArrayList<>();
        for (TbContent bigAD : bigADS) {
            BigADVO bigADVO=new BigADVO();
            bigADVO.setAlt(bigAD.getTitle());
            bigADVO.setSrc(PicUrlUtil.genPullPaths(bigAD.getPic(),nginxServer));
            bigADVO.setSrcB(PicUrlUtil.genPullPaths(bigAD.getPic2(),nginxServer));
            bigADVO.setHref(bigAD.getUrl());
            bigADVOS.add(bigADVO);
        }
        model.addAttribute("ad1", JSON.json(bigADVOS));
        return "index";
    }


}
