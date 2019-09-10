package com.whx.taotao.manager.dao;

import com.whx.taotao.pojo.TbItem;
import com.whx.taotao.pojo.TbItemCat;
import com.whx.taotao.pojo.TbItemDesc;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemDao {
    @Select("select * from tb_item")
    public List<TbItem> queryAllDatas();

    @Select("select * from tb_item_cat where parent_id=#{param1}")
    public List<TbItemCat> queryCatsById(String parentId);

    @Insert("insert into tb_item values(null,#{title},#{sellPoint},#{price},#{num},#{barcode},#{image}," +
            "#{cid},#{status},#{created},#{updated})")
    @SelectKey(before = false,statement = "select last_insert_id()",resultType = long.class,keyProperty = "id",keyColumn = "id")
    public void insertItem(TbItem tbItem);

    @Insert("insert into tb_item_desc values(#{itemId},#{itemDesc},#{created},#{updated})")
    public void insertItemDesc(TbItemDesc tbItemDesc);

    @Select("select * from tb_item where id=#{itemid}")
    public TbItem queryItemById(String itemid);

    @Select("select * from tb_item_desc where item_id=#{itemid}")
    public TbItemDesc queryItemDescById(String itemid);
}
