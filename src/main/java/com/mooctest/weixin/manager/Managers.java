package com.mooctest.weixin.manager;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mooctest.weixin.config.Config;



@SuppressWarnings("resource")
public class Managers {

    public final static AccountManager accountManager;
    public final static QuizManager quizManager;
    public final static RollcallManager rollcallManager;
    public final static Config config;

    static {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        context.start();
        accountManager=context.getBean(AccountManager.class);
        quizManager=context.getBean(QuizManager.class);
        rollcallManager=context.getBean(RollcallManager.class);
        config = (Config) context.getBean("mooctestConfig");       
    }
}

