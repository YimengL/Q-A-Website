package com.example.wenda.service;

import com.example.wenda.dao.LoginTicketDao;
import com.example.wenda.dao.UserDAO;
import com.example.wenda.model.LoginTicket;
import com.example.wenda.model.User;
import com.example.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDao loginTicketDao;

    public Map<String, String> register(String username, String password) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isEmpty(username)) {
            map.put("msg", "username can not be empty");
            return map;
        }

        if (StringUtils.isEmpty(password)) {
            map.put("msg", "password can not be empty");
            return map;
        }

        User user = userDAO.selectByName(username);
        if (user != null) {
            map.put("msg", "username had been registered before");
            return map;
        }

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",
                new Random().nextInt(1000)));
        user.setPassword(WendaUtil.MD5(password + user.getSalt()));
        userDAO.addUser(user);

        // Login
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket); // send to browser as cookie

        return map;
    }

    public Map<String, String> login(String username, String password) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isEmpty(username)) {
            map.put("msg", "username can not be empty");
            return map;
        }

        if (StringUtils.isEmpty(password)) {
            map.put("msg", "password can not be empty");
            return map;
        }

        User user = userDAO.selectByName(username);

        if (user == null) {
            map.put("msg", "username is not exist");
            return map;
        }

        if (!WendaUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "password is wrong");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket); // send to browser as cookie
        return map;
    }

    public String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        now.setTime(3600 * 24 * 100 + now.getTime());
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);       // 0 means VALID
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDao.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public void logout(String ticket) {
        loginTicketDao.updateStatus(ticket, 1); // 1 is invalid
    }

    public User getUser(int id) {
        return userDAO.selectById(id);
    }
}
