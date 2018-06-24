package com.example.wenda.controller;

import com.example.wenda.model.*;
import com.example.wenda.service.CommentService;
import com.example.wenda.service.QuestionService;
import com.example.wenda.service.UserService;
import com.example.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/question/add", method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content) {
        try {
            Question question = new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            question.setCommentCount(0);
            if (hostHolder.getUser() == null) {
                question.setUserId(WendaUtil.ANONYMOUS_USERID);
            }
            else {
                question.setUserId(hostHolder.getUser().getId());
            }
            if (questionService.addQuestion(question) > 0) {
                return WendaUtil.getJSONString(0);
            }
        }
        catch (Exception e) {
            logger.error("增加题目失败" + e.getMessage());
        }
        return WendaUtil.getJSONString(1, "失败");
    }

    @RequestMapping(value = "/question/{qid}")
    public String questionDetail(Model model, @PathVariable("qid") int qid) {
        Question question = questionService.selectById(qid);
        model.addAttribute("question", question);
        model.addAttribute("user", userService.getUser(question.getUserId()));

        List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments = new ArrayList<>();
        for (Comment comment : commentList) {
            // logger.info("haha");
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            vo.set("user", userService.getUser(comment.getUserId()));
            comments.add(vo);
        }

        model.addAttribute("comments", comments);
        return "detail";
    }
}
