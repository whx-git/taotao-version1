package com.whx.taotao.manager.dao;

import com.whx.taotao.pojo.TbContent;
import com.whx.taotao.pojo.TbContentCategory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public interface ContentDao {

    @Select("select * from tb_content_category where parent_id=#{parentID}")
    public List<TbContentCategory> queryCatsByParentId(String parentID);

    @Insert("insert into tb_content values(NULL,#{categoryId},#{title},#{subTitle},#{titleDesc},#{url}," +
            "#{pic},#{pic2},#{content},#{created},#{updated})")
    public void insertContent(TbContent tbContent) throws SQLException;

    @Select("select * from tb_content where category_id=#{id} order by created desc")
    public List<TbContent> queryContentById(String id);

    @Delete("delete from tb_content where id=#{id}")
    public void deleteContent(long id) throws SQLException;

    @Insert("insert into tb_content values(#{id},#{categoryId},#{title},#{subTitle},#{titleDesc},#{url}," +
            "#{pic},#{pic2},#{content},#{created},#{updated})")
    public void editContent(TbContent tbContent) throws SQLException;

    @Select("select * from tb_content where category_id=#{cid}")
    public List<TbContent> queryContentByCid(String cid);

}
