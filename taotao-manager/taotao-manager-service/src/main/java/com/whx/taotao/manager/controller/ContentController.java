package com.whx.taotao.manager.controller;

import com.whx.taotao.easyUI.pojo.EasyUIPageDatasBean;
import com.whx.taotao.easyUI.pojo.EasyUITreeBean;
import com.whx.taotao.inter.ContentService;
import com.whx.taotao.pojo.TbContent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Controller
public class ContentController {
    @Resource
    private ContentService contentService;
    @RequestMapping("content")
    public String content(){

        return "content";
    }
    @RequestMapping("content-add")
    public String contendAdd(){
        return "content-add";
    }

    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeBean> getContentCats(String id){
        if(StringUtils.isEmpty(id)){
            return contentService.getContentTopCats();
        }else {
            return contentService.getContentCats(id);
        }
    }
    @RequestMapping("/content/save")
    @ResponseBody
    public EasyUISaveItemBean contentSave(TbContent tbContent){
        boolean b = contentService.contentSave(tbContent);
        EasyUISaveItemBean easyUISaveItemBean=new EasyUISaveItemBean();
        if (b) {
            easyUISaveItemBean.setStatus(EasyUISaveItemBean.STATUS_OK);
            easyUISaveItemBean.setMessage("新增成功!");
        }else {
            easyUISaveItemBean.setStatus(EasyUISaveItemBean.STATUS_ERROR);
            easyUISaveItemBean.setMessage("新增失败,请检查网络设置!");
        }
        return easyUISaveItemBean;
    }
    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUIPageDatasBean contentQuery(@RequestParam(required = false,defaultValue = "1") String page,
                                            @RequestParam(required = false,defaultValue = "20") String rows,
                                            String categoryId){

     return    contentService.showQueryContent(page,rows,categoryId);
    }
    @RequestMapping("/content-edit")
    public String toContentEdit(){
        return "content-edit";
    }

    @RequestMapping("/rest/content/edit")
    @ResponseBody
    public EasyUISaveItemBean contentEdit(TbContent tbContent){
        boolean b = contentService.contentEdit(tbContent);
        EasyUISaveItemBean easyUISaveItemBean=new EasyUISaveItemBean();
        if (b) {
            easyUISaveItemBean.setStatus(EasyUISaveItemBean.STATUS_OK);
            easyUISaveItemBean.setMessage("编辑成功!");
        }else {
            easyUISaveItemBean.setStatus(EasyUISaveItemBean.STATUS_ERROR);
            easyUISaveItemBean.setMessage("编辑失败,请检查网络设置!");
        }
        return easyUISaveItemBean;
    }

    @RequestMapping("/content/delete")
    @ResponseBody
    public EasyUISaveItemBean deleteContent(long ids){
        boolean b = contentService.contentDelete(ids);
        EasyUISaveItemBean easyUISaveItemBean=new EasyUISaveItemBean();
        if (b) {

            easyUISaveItemBean.setStatus(EasyUISaveItemBean.STATUS_OK);
            easyUISaveItemBean.setMessage("删除成功!");
        }else {
            easyUISaveItemBean.setStatus(EasyUISaveItemBean.STATUS_ERROR);
            easyUISaveItemBean.setMessage("删除失败,请检查网络设置!");
        }
        return easyUISaveItemBean;
    }
}
