package com.whx.taotao.manager.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.whx.taotao.commons.ConstantsKey;
import com.whx.taotao.commons.PicUrlUtil;
import com.whx.taotao.commons.kafkaTopics.MessageItem;
import com.whx.taotao.easyUI.pojo.EasyUIPageDatasBean;
import com.whx.taotao.easyUI.pojo.EasyUITreeBean;
import com.whx.taotao.inter.ItemService;
import com.whx.taotao.manager.dao.ItemDao;
import com.whx.taotao.pojo.TbItem;
import com.whx.taotao.pojo.TbItemCat;
import com.whx.taotao.pojo.TbItemDesc;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Resource
    private ItemDao itemDao;
    @Value("${nginx_server}")
    private String nginxServer;
    @Resource
    private KafkaTemplate kafkaTemplate;
    @Override
    public EasyUIPageDatasBean<TbItem> showPageItes(String pageNow, String rows) {
        //分页起始设置
        PageHelper.startPage(NumberUtils.createInteger(pageNow),NumberUtils.createInteger(rows));

        List<TbItem> tbItems = itemDao.queryAllDatas();
        PageInfo<TbItem> pageInfo=new PageInfo<>(tbItems);
        int pages = pageInfo.getPages();
        long total = pageInfo.getTotal();

        EasyUIPageDatasBean easyUIPageDatasBean=new EasyUIPageDatasBean();
        easyUIPageDatasBean.setTotal(total);
        easyUIPageDatasBean.setRows(tbItems);
        return easyUIPageDatasBean;
    }

    @Override
    public List<EasyUITreeBean> itemCatTree(String parentId) {
        List<TbItemCat> tbItemCats = itemDao.queryCatsById(parentId);
        List<EasyUITreeBean> easyUITreeBeans=new ArrayList<>();
        for (TbItemCat tbItemCat : tbItemCats) {
            EasyUITreeBean easyUITreeBean=new EasyUITreeBean();
            easyUITreeBean.setId(tbItemCat.getId());
            easyUITreeBean.setText(tbItemCat.getName());
            easyUITreeBean.setState(tbItemCat.getIsParent()?EasyUITreeBean.STATE_CLOSED:EasyUITreeBean.STATE_OPEN);
            easyUITreeBeans.add(easyUITreeBean);
        }
        return easyUITreeBeans;
    }

    @Override
    public List<EasyUITreeBean> showTopItemCats() {
        return itemCatTree("0");
    }

    @Override
    @Transactional
    public boolean saveItem(TbItem tbItem, TbItemDesc tbItemDesc) throws Exception{
        String imageURLs = tbItem.getImage();
        tbItem.setImage(PicUrlUtil.genGroupPaths(imageURLs,nginxServer));
        tbItem.setStatus(1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        itemDao.insertItem(tbItem);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        tbItemDesc.setItemId(tbItem.getId());
        itemDao.insertItemDesc(tbItemDesc);

        MessageItem messageItem=new MessageItem();
        messageItem.setAction(MessageItem.ACTION_ADD);
        messageItem.setTbItem(tbItem);
        kafkaTemplate.send(ConstantsKey.KAFKA_TOPIC_ITEM, JSON.toJSONString(messageItem));
        return true;
    }

    @Override
    public TbItem getItemById(String itemid) {
        TbItem tbItem = itemDao.queryItemById(itemid);
        tbItem.setImage(PicUrlUtil.genPullPaths(tbItem.getImage(),nginxServer));
        return tbItem;
    }

    @Override
    public TbItemDesc getItemdescById(String itemid) {
        TbItemDesc tbItemDesc = itemDao.queryItemDescById(itemid);
        return tbItemDesc;
    }
}
