package com.example.wenda;

import com.example.wenda.dao.QuestionDAO;
import com.example.wenda.dao.UserDAO;
import com.example.wenda.model.EntityType;
import com.example.wenda.model.Question;
import com.example.wenda.model.User;
import com.example.wenda.service.FollowService;
import com.example.wenda.service.SensitiveService;
import com.example.wenda.util.JedisAdapter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTests {

    @Autowired
    UserDAO userDAO;

    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    SensitiveService sensitiveUtil;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

	@Test
	public void initDatabase() {
		Random random = new Random();
		for (int i = 0; i < 11; ++i) {
			User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",
                    random.nextInt(1000)));
            user.setName(String.format("USER%d", i + 1));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);

            for (int j = 1; j < i; ++j) {
                followService.follow(j, EntityType.ENTITY_USER, i);
                System.out.println("User " + j + " follow User " + i + ": " + followService.getFolloweeCount(EntityType.ENTITY_USER, j));
            }

            user.setPassword("xx");
            userDAO.updatePassword(user);

            Question question = new Question();
            question.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * i);
            question.setCreatedDate(date);
            question.setUserId(i + 1);
            question.setTitle(String.format("TITLE{%d}", i));
            question.setContent(String.format("Blalalalalala Contet {%d}", i));

            questionDAO.addQuestion(question);
		}

        Assert.assertEquals("xx", userDAO.selectById(1).getPassword());
		// userDAO.deleteById(1);
		// Assert.assertNull(userDAO.selectById(1));

		// System.out.print(questionDAO.selectLatestQuestions(0, 0, 10));
	}

	public void testSensitive() {
	    String content = "question content <img src=\"https:\\/\\/baidu.com/ff.png\">色情赌博";
	    String result = sensitiveUtil.filter(content);
        System.out.println(result);
    }
}
