package com.itheima.a05.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Bean2 {

    private static final Logger log = LoggerFactory.getLogger(Bean2.class);

    public Bean2() {
        log.debug("我被 Spring 管理啦");
    }

/*
    这个注解只能在 @Configuration 标注的类上用
    @Bean
    public Bean3 getBean3(){
        log.debug("我把 bean3 交给 Spring 管理啦");
        return new Bean3();
    }*/
}
