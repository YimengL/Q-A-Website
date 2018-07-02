package com.example.wenda.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;


@Component
@PropertySource("classpath:application.properties")
public class MailSender implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;

    @Autowired
    private Configuration freemarkerConfig;

    @Autowired
    private ConfigurableEnvironment env;

    public boolean sendWithHTMLTemplate(String to, String subject, String template,
                                        Map<String, Object> model) {
        try {
            String nick = MimeUtility.encodeText("确认信件");
            InternetAddress from = new InternetAddress(nick + "<admin@wenda.com>");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            Template t = freemarkerConfig.getTemplate(template);
            String result = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setText(result, true);
            mailSender.send(mimeMessage);
            return true;
        }
        catch (Exception e) {
            logger.error("邮件发送失败" + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("lym1108csu@gmail.com");
        logger.error(env.getProperty("spring.mail.password"));
        // logger.error(environment.getProperty("$gmail_password"));
        mailSender.setPassword(env.getProperty("spring.mail.password"));

        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        Properties javaMailProperties = mailSender.getJavaMailProperties();
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.debug", "true");
        javaMailProperties.put("mail.default-encoding", "UTF-8");
        mailSender.setJavaMailProperties(javaMailProperties);
    }

}
