package com.whx.taotao.manager.controller;

import com.whx.taotao.easyUI.pojo.EasyUIPageDatasBean;
import com.whx.taotao.easyUI.pojo.EasyUITreeBean;
import com.whx.taotao.inter.ItemService;
import com.whx.taotao.manager.util.FastDFSUtil;
import com.whx.taotao.pojo.TbItem;
import com.whx.taotao.pojo.TbItemDesc;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class ItemController {
    @Resource
    private ItemService itemService;
    @Resource
    private FastDFSUtil fastDFSUtil;
    @Value("${nginx_server}")
    private String nginxServer;

    @RequestMapping("item-list")
    public String itemList(){
        return "item-list";
    }

    @RequestMapping("/item/list.json")
    @ResponseBody
    public EasyUIPageDatasBean getItemPageDatas(@RequestParam(required = false,defaultValue = "1") String page,
                                                @RequestParam(required = false,defaultValue = "20") String rows){
        return itemService.showPageItes(page,rows);
    }

    @RequestMapping("item-add")
    public String itemAdd(){
        return "item-add";
    }


    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUITreeBean> itemCatTree(String id){
        if(StringUtils.isEmpty(id)){
            //一级类目
            return itemService.showTopItemCats();
        }else{
            return itemService.itemCatTree(id);
        }
    }

    @RequestMapping("/pic/upload")
    @ResponseBody
    public EasyUIPicUploadBean picUpload(MultipartFile uploadFile){
        EasyUIPicUploadBean easyUIPicUploadBean=new EasyUIPicUploadBean();
        try {
            String url = fastDFSUtil.uploadFile(uploadFile.getBytes(), uploadFile.getOriginalFilename(), null);
            easyUIPicUploadBean.setError(0);
            easyUIPicUploadBean.setMessage("上传成功!");
            easyUIPicUploadBean.setUrl(nginxServer+url);
        } catch (Exception e) {
            e.printStackTrace();
            easyUIPicUploadBean.setError(1);
            easyUIPicUploadBean.setMessage("上传失败!");
        }

        return easyUIPicUploadBean;
    }
    @RequestMapping("/item/save")
    @ResponseBody
    public EasyUISaveItemBean iteaSave(TbItem tbItem,String desc){
        TbItemDesc tbItemDesc=new TbItemDesc();
        tbItemDesc.setItemDesc(desc);
        boolean b = false;
        try {
            b = itemService.saveItem(tbItem, tbItemDesc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        EasyUISaveItemBean easyUISaveItemBean=new EasyUISaveItemBean();
        if (b) {
            easyUISaveItemBean.setStatus(EasyUISaveItemBean.STATUS_OK);
            easyUISaveItemBean.setMessage(tbItem.getTitle()+"商品添加成功!");
        }else {
            easyUISaveItemBean.setStatus(EasyUISaveItemBean.STATUS_ERROR);
            easyUISaveItemBean.setMessage(tbItem.getTitle()+"商品添加失败,请检查网络设置!!");
        }
        return easyUISaveItemBean;
    }
}
