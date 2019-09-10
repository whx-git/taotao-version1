package com.whx.taotao.shopcar.dao;

import com.whx.taotao.pojo.TbShopcar;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;
import java.util.List;

@Repository
public interface ShopcarDao {

    @Insert("insert into tb_shopcar values(null,#{userId},#{itemId},#{itemImg},#{itemTitle},#{price},#{totalPrice},#{num}," +
            "#{addDate},#{tag})")
    public void insertShopcarItem(TbShopcar tbShopcar) throws SQLException;

    @Select("select * from tb_shopcar where itemId=#{itemid} and userId=#{userid}")
    public TbShopcar queryDataByItemAndUserId(@Param("itemid") Long itemid,@Param("userid")Integer userid);


    @Update("update tb_shopcar set tag=#{tag},totalPrice=totalPrice+#{totalPrice},num=num+#{num} where itemId=#{itemId} and userId=#{userId}")
    public void updateTbShopcar(TbShopcar tbShopcar);

    @Select("select * from tb_shopcar where userId=#{userid} order by tag desc")
    public List<TbShopcar> queryDataByUserId(Integer userid);

    @Delete("delete from tb_shopcar where id=#{id}")
    public void deleteItem(Long id);

    public List<TbShopcar> queryById(String[] ids);

}
