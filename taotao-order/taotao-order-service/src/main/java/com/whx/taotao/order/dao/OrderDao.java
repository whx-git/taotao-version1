package com.whx.taotao.order.dao;

import com.whx.taotao.pojo.TbOrder;
import com.whx.taotao.pojo.TbOrderItem;
import com.whx.taotao.pojo.TbOrderShipping;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDao {

    @Insert("insert into tb_order(order_id,payment,payment_type,status,shipping_name,user_id) values(#{orderId},#{payment},#{paymentType},#{status},#{shippingName},#{userId})")
    public void insertOrder(TbOrder order);

    @Insert("insert into tb_order_item values(null,#{itemId},#{orderId},#{num},#{title},#{price},#{totalFee},#{picPath})")
    public void insertOrdetItem(TbOrderItem tbOrderItem);

    @Insert("insert into tb_order_shipping(order_id,receiver_name,receiver_phone,receiver_mobile,receiver_state,receiver_city,receiver_address,created,updated)" +
            "values(#{orderId},#{receiverName},#{receiverPhone},#{receiverMobile},#{receiverState},#{receiverCity},#{receiverAddress},#{created},#{updated})")
    public void insertOrderShipping(TbOrderShipping tbOrderShipping);
}
