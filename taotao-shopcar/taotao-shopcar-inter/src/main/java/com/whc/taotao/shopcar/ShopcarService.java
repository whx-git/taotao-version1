package com.whc.taotao.shopcar;


import com.whx.taotao.pojo.TbShopcar;

import java.util.List;

public interface ShopcarService {
    public boolean addShopcarItem(String itemid, Integer num);

    public void saveShopcarItem(TbShopcar tbShopcar) throws Exception;

    public List<TbShopcar> getUserShopcar(Integer userid);

    public void deleteItem(Long id);

    public List<TbShopcar> getShopcarByIds(String[] shopcarids);
}