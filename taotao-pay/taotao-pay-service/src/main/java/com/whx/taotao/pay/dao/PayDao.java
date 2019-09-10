package com.whx.taotao.pay.dao;

import com.whx.taotao.pojo.TbOrderItem;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayDao {
    @Select("select * from tb_order_item where order_id=#{ordid}")
    public List<TbOrderItem> queryItemById(String ordid);
}
