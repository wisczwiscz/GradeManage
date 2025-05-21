package com.wtu.backend.config;

import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.TestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllureTestListener implements TestLifecycleListener {
    private static final Logger logger = LoggerFactory.getLogger(AllureTestListener.class);

    @Override
    public void beforeTestStart(TestResult result) {
        logger.info("测试开始: {}", result.getName());
    }

    @Override
    public void afterTestStop(TestResult result) {
        logger.info("测试结束: {}, 状态: {}", result.getName(), result.getStatus());
    }
} 