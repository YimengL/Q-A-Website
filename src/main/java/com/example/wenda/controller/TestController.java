package com.example.wenda.controller;

import com.example.wenda.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TestController {
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/yimeng"}, method = RequestMethod.GET)
    public String yimeng() {
        return "haha";
    }
}
