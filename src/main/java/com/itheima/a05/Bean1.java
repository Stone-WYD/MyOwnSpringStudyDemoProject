package com.itheima.a05;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bean1 {

    private static final Logger log = LoggerFactory.getLogger(Bean1.class);

    private String name;

    public Bean1() {
        log.debug("我被 Spring 管理啦");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
