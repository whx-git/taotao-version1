package com.whx.taotao.inter;

import com.whx.taotao.easyUI.pojo.EasyUIPageDatasBean;
import com.whx.taotao.easyUI.pojo.EasyUITreeBean;
import com.whx.taotao.pojo.TbContent;

import java.util.List;

public interface ContentService {
    public List<EasyUITreeBean> getContentCats(String parentID);
    public List<EasyUITreeBean> getContentTopCats();
    public boolean contentSave(TbContent tbContent);

    public EasyUIPageDatasBean<TbContent> showQueryContent(String pageNow, String rows,String id);
    public boolean contentEdit(TbContent tbContent);
    public boolean contentDelete(long id);

    public List<TbContent> getBigADS();

    public List<TbContent> getLittleADS();
}
