package com.whx.taotao.search.inter;

import com.whx.taotao.pojo.TbItem;

import java.util.List;

public interface SearchService {
    public List<TbItem> searchGoodsByKeyWord(String keyword);

    public  void addSearchItem(TbItem tbItem);
}
