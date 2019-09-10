package com.whx.taotao.manager.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.whx.taotao.commons.ConstantsKey;
import com.whx.taotao.commons.PicUrlUtil;
import com.whx.taotao.easyUI.pojo.EasyUIPageDatasBean;
import com.whx.taotao.easyUI.pojo.EasyUITreeBean;
import com.whx.taotao.inter.ContentService;
import com.whx.taotao.manager.dao.ContentDao;
import com.whx.taotao.pojo.TbContent;
import com.whx.taotao.pojo.TbContentCategory;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
@EnableCaching
public class ContentServiceImpl implements ContentService {
    @Resource
    private ContentDao contentDao;
    @Value("${nginx_server}")
    private String nginxServer;
    @Override
    public List<EasyUITreeBean> getContentCats(String parentID) {
        List<TbContentCategory> tbContentCategories = contentDao.queryCatsByParentId(parentID);
        List<EasyUITreeBean> easyUITreeBeans=new ArrayList<>();
        for (TbContentCategory tbContentCategory : tbContentCategories) {
            EasyUITreeBean easyUITreeBean=new EasyUITreeBean();
            easyUITreeBean.setId(tbContentCategory.getId());
            easyUITreeBean.setText(tbContentCategory.getName());
            easyUITreeBean.setState(tbContentCategory.getIsParent()?EasyUITreeBean.STATE_CLOSED:EasyUITreeBean.STATE_OPEN);
            easyUITreeBeans.add(easyUITreeBean);
        }

        return easyUITreeBeans;
    }

    @Override
    public List<EasyUITreeBean> getContentTopCats() {

        return getContentCats("0");
    }

    @Override
    @CacheEvict(cacheNames = ConstantsKey.REDIS_CONTENT_MANAGER,key = ConstantsKey.REDIS_CONTENT_KEY+"+#tbContent.categoryId")
    public boolean contentSave(TbContent tbContent) {
        try {
            String pic = PicUrlUtil.genGroupPaths(tbContent.getPic(), nginxServer);
            String pic2 = PicUrlUtil.genGroupPaths(tbContent.getPic2(), nginxServer);
            tbContent.setPic(pic);
            tbContent.setPic2(pic2);
            tbContent.setCreated(new Date());
            tbContent.setUpdated(new Date());
            contentDao.insertContent(tbContent);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //自己添加的显示,编辑,删除
    @Override
    public EasyUIPageDatasBean<TbContent> showQueryContent(String pageNow, String rows,String id) {
        PageHelper.startPage(NumberUtils.createInteger(pageNow),NumberUtils.createInteger(rows));
        List<TbContent> tbContents = contentDao.queryContentById(id);
        PageInfo<TbContent> pageInfo=new PageInfo<>(tbContents);
        int pages = pageInfo.getPages();
        long total = pageInfo.getTotal();
        EasyUIPageDatasBean easyUIPageDatasBean=new EasyUIPageDatasBean();
        easyUIPageDatasBean.setTotal(total);
        easyUIPageDatasBean.setRows(tbContents);
        return easyUIPageDatasBean;
    }

    @Override
    @CacheEvict(cacheNames = ConstantsKey.REDIS_CONTENT_MANAGER,key = ConstantsKey.REDIS_CONTENT_KEY+"+#tbContent.categoryId")
    public boolean contentEdit(TbContent tbContent) {
        try {
            contentDao.deleteContent(tbContent.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        try {
            String pic = PicUrlUtil.genGroupPaths(tbContent.getPic(), nginxServer);
            String pic2 = PicUrlUtil.genGroupPaths(tbContent.getPic2(), nginxServer);
            tbContent.setUpdated(new Date());
            tbContent.setCreated(new Date());
            contentDao.insertContent(tbContent);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean contentDelete(long id) {
        try {
            contentDao.deleteContent(id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //获取大广告
    @Override
    @Cacheable(cacheNames = ConstantsKey.REDIS_CONTENT_MANAGER,key = ConstantsKey.REDIS_CONTENT_KEY+"+'89'")
    public List<TbContent> getBigADS() {

        List<TbContent> tbContents = contentDao.queryContentByCid("89");

        return tbContents;
    }

    //获取小广告
    @Override
    @Cacheable(cacheNames = ConstantsKey.REDIS_CONTENT_MANAGER,key = ConstantsKey.REDIS_CONTENT_KEY+"+'90'")
    public List<TbContent> getLittleADS() {
        List<TbContent> tbContents = contentDao.queryContentByCid("90");
        return tbContents;
    }
}
