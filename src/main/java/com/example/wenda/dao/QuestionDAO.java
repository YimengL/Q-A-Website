package com.example.wenda.dao;

import com.example.wenda.model.Question;
import com.example.wenda.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FEILDS = " id, " + INSERT_FIELDS;


    @Insert({"INSERT INTO", TABLE_NAME, " (", INSERT_FIELDS,
            ") VALUES (#{title}, #{content}, #{createdDate}, #{userId}, #{commentCount})"})
    int addQuestion(Question question);

    @Select({"SELECT ", SELECT_FEILDS, " FROM ", TABLE_NAME, " WHERE id=#{id}"})
    Question selectById(int id);

    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);
}
