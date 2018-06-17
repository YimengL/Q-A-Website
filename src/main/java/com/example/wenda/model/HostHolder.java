package com.example.wenda.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    // 如果不是ThreadLocal的话，两个User同时访问的话只能访问其中一个
    // 每条线程都有一个对象
    // Map<ThreadId, User>
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
