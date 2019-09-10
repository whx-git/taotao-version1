package com.whx.taotao.inter;

import com.whx.taotao.easyUI.pojo.EasyUIPageDatasBean;
import com.whx.taotao.easyUI.pojo.EasyUITreeBean;
import com.whx.taotao.pojo.TbItem;
import com.whx.taotao.pojo.TbItemDesc;

import java.util.List;

public interface ItemService {
    public EasyUIPageDatasBean<TbItem> showPageItes(String pageNow, String rows);

    public List<EasyUITreeBean> itemCatTree(String parentId);

    public List<EasyUITreeBean> showTopItemCats();

    public boolean saveItem(TbItem tbItem, TbItemDesc tbItemDesc) throws Exception;

    public TbItem getItemById(String itemid);

    public TbItemDesc getItemdescById(String itemid);

}
