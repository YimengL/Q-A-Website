package com.example.wenda.dao;

import com.example.wenda.model.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface LoginTicketDAO {
    String TABLE_NAME = " login_ticket ";
    String INSERT_FIELDS = " user_id, expired, status, ticket ";
    String SELECT_FEILDS = " id, " + INSERT_FIELDS;

    @Insert({"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS,
            ") VALUES (#{userId}, #{expired}, #{status}, #{ticket})"})
    int addTicket(LoginTicket ticket);

    @Select({"SELECT ", SELECT_FEILDS, " FROM ", TABLE_NAME, " WHERE ticket = #{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"UPDATE ", TABLE_NAME, " SET status=#{status} WHERE ticket=#{ticket}"})
    boolean updateStatus(@Param("ticket") String ticket,  @Param("status") int status);
}
