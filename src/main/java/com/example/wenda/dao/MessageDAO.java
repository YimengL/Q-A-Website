package com.example.wenda.dao;

import com.example.wenda.model.Comment;
import com.example.wenda.model.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, has_read, conversation_id, created_date ";
    String SELECT_FEILDS = " id, " + INSERT_FIELDS;


    @Insert({"INSERT INTO", TABLE_NAME, " (", INSERT_FIELDS,
            ") VALUES (#{fromId}, #{toId}, #{content}, #{hasRead}, #{conversationId}, #{createdDate})"})
    int addMessage(Message message);

    @Select({"SELECT ", SELECT_FEILDS, " FROM ", TABLE_NAME,
            " WHERE conversation_id=#{conversationId} ORDER BY created_date DESC LIMIT #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    @Select({"SELECT ", INSERT_FIELDS, " , COUNT(id) AS id FROM ( SELECT * FROM ", TABLE_NAME,
            " WHERE from_id=#{userId} OR to_id=#{userId} ORDER BY created_date DESC) tt ",
            " GROUP BY conversation_id ORDER BY created_date DESC LIMIT #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int user,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    @Select({"SELECT COUNT(id) FROM ", TABLE_NAME, " WHERE has_read=0 AND to_id=#{userId} AND conversation_id=#{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);
}