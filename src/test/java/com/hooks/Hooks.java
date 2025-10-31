package com.hooks;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

import java.util.ArrayList;
import java.util.List;

public class Hooks {

    private static final ThreadLocal<List<String>> currentLogs = ThreadLocal.withInitial(ArrayList::new);
    private static MemoryAppender memoryAppender;

    @Before
    public void beforeScenario(Scenario scenario) {
        currentLogs.get().clear();
        setupMemoryAppender();
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        List<String> logs = new ArrayList<>(currentLogs.get());

        if (!logs.isEmpty()) {
            StringBuilder logOutput = new StringBuilder();
            logOutput.append("<div style='background:#f5f5f5; padding:10px; margin:5px 0;'>");
            logOutput.append("<b>Step Logs:</b><br/>");

            for (String log : logs) {
                logOutput.append(log).append("<br/>");
            }
            logOutput.append("</div>");

            scenario.log(logOutput.toString());
        }

        currentLogs.get().clear();
    }

    @After
    public void afterScenario(Scenario scenario) {
        currentLogs.get().clear();
    }

    private void setupMemoryAppender() {
        if (memoryAppender == null) {
            memoryAppender = new MemoryAppender("CucumberMemoryAppender");
            memoryAppender.start();

            // Add to root logger
            org.apache.logging.log4j.core.Logger rootLogger =
                    (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
            rootLogger.addAppender(memoryAppender);
        }
    }

    // Simple Memory Appender
    private static class MemoryAppender extends AbstractAppender {

        protected MemoryAppender(String name) {
            super(name, null, null, true, null);
        }

        @Override
        public void append(LogEvent event) {
            String logMessage = event.getMessage().getFormattedMessage();
            // Skip hooks class logs to avoid recursion
            if (!event.getLoggerName().contains("com.hooks.Hooks")) {
                currentLogs.get().add(logMessage);
            }
        }
    }
}