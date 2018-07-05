package com.example.wenda.dao;

import com.example.wenda.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status ";
    String SELECT_FEILDS = " id, " + INSERT_FIELDS;


    @Insert({"INSERT INTO", TABLE_NAME, " (", INSERT_FIELDS,
            ") VALUES (#{userId}, #{content}, #{createdDate}, #{entityId}, #{entityType}, #{status})"})
    int addComment(Comment comment);

    @Select({"SELECT ", SELECT_FEILDS, " FROM ", TABLE_NAME, " WHERE id=#{id}"})
    Comment getCommentById(int id);

    @Select({"SELECT ", SELECT_FEILDS, " FROM ", TABLE_NAME,
            " WHERE entity_id=#{entityId} AND entity_type=#{entityType} ORDER BY created_date DESC"})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({"SELECT COUNT(id) FROM ", TABLE_NAME, " WHERE entity_id=#{entityId} AND entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update({"UPDATE ", TABLE_NAME, " SET status=#{status} WHERE id=#{id}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);

    @Select({"SELECT COUNT(id) FROM ", TABLE_NAME, " WHERE user_id=#{userId}"})
    int getUserCommentCount(int userId);
}


