package com.example.hotel.controller.listener;

import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletContextEvent;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.DAYS;

public class ServletContextListener implements jakarta.servlet.ServletContextListener {
    public static final Logger log = Logger.getLogger(ServletContextListener.class);
    public static final long PERIOD = 1L;
    public static final long INITIAL_DELAY = 0L;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();

    private final Collection<Runnable> commandsToExecute = new HashSet<>() {{
        add(applicationService::removeOutdatedTemporaryApplications);
        add(applicationService::cancelFinishedApprovedApplications);
    }};

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        try {
            commandsToExecute
                    .forEach(service -> executor.scheduleAtFixedRate(service, INITIAL_DELAY, PERIOD, DAYS));

            jakarta.servlet.ServletContextListener.super.contextInitialized(sce);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        executor.shutdown();
        jakarta.servlet.ServletContextListener.super.contextDestroyed(sce);
    }
}
