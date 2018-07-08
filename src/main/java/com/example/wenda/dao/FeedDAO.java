package com.example.wenda.dao;

import com.example.wenda.model.Feed;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FeedDAO {
    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " user_id, data, created_date, type ";
    String SELECT_FEILDS = " id, " + INSERT_FIELDS;

    @Insert({"INSERT INTO", TABLE_NAME, " (", INSERT_FIELDS,
            ") VALUES (#{userId}, #{data}, #{createdDate}, #{type})"})
    int addFeed(Feed feed);

    @Select({"SELECT ", SELECT_FEILDS, " FROM ", TABLE_NAME, " WHERE id=#{id}"})
    Feed getFeedById(int id);

    // 未登录时要返回推荐或者最新的Feed
    List<Feed> selectUserFeeds(@Param("maxId") int maxId,
                               @Param("userIds") List<Integer> userIds,
                               @Param("count") int count);
}
