package com.hooks;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Cucumber Hooks to capture and embed Log4j 2 logs into Extent Reports
 * NO CHANGES TO STEP DEFINITIONS REQUIRED
 */
public class Hooks {

    private static final ThreadLocal<List<String>> stepLogs = ThreadLocal.withInitial(ArrayList::new);
    private static CucumberLogAppender customAppender;

    @Before
    public void beforeScenario(Scenario scenario) {
        stepLogs.get().clear();

        if (customAppender == null) {
            setupLogAppender();
        }
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        List<String> logs = new ArrayList<>(stepLogs.get());

        if (!logs.isEmpty()) {
            StringBuilder logOutput = new StringBuilder();
            for (String log : logs) {
                // ✅ Skip internal hook method logs
                if (log.contains("com.hooks.Hooks.afterStep")) {
                    continue;
                }
                logOutput.append(log);
            }

            // ✅ Only log if something meaningful remains
            if (logOutput.length() > 0) {
                scenario.log(logOutput.toString());
            }
        }

        stepLogs.get().clear();
    }


    @After
    public void afterScenario(Scenario scenario) {
        stepLogs.get().clear();
    }

    private void setupLogAppender() {
        customAppender = CucumberLogAppender.createAppender(
                "CucumberAppender",
                null,
                PatternLayout.newBuilder()
                        .withPattern("%d{yyyy-MM-dd HH:mm:ss,SSS} [%t] %-5p - %m%n")
                        .build(),
                null
        );
        customAppender.start();

        org.apache.logging.log4j.core.Logger rootLogger =
                (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
        rootLogger.addAppender(customAppender);
    }

    @Plugin(name = "CucumberLogAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE)
    public static class CucumberLogAppender extends AbstractAppender {

        protected CucumberLogAppender(String name, Filter filter,
                                      org.apache.logging.log4j.core.Layout<? extends Serializable> layout,
                                      boolean ignoreExceptions) {
            super(name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
        }

        @PluginFactory
        public static CucumberLogAppender createAppender(
                @PluginAttribute("name") String name,
                @PluginElement("Filter") Filter filter,
                @PluginElement("Layout") org.apache.logging.log4j.core.Layout<? extends Serializable> layout,
                @PluginAttribute("ignoreExceptions") Boolean ignoreExceptions) {

            if (name == null) {
                LOGGER.error("No name provided for CucumberLogAppender");
                return null;
            }
            if (layout == null) {
                layout = PatternLayout.createDefaultLayout();
            }
            return new CucumberLogAppender(name, filter, layout,
                    ignoreExceptions != null && ignoreExceptions);
        }

        @Override
        public void append(LogEvent event) {
            String formattedMessage = new String(getLayout().toByteArray(event));
            stepLogs.get().add(formattedMessage);
        }
    }
}