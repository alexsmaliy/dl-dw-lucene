package com.alexsmaliy.dl4s.healthcheck;

import com.codahale.metrics.health.HealthCheck;

public class DummyHealthcheck extends HealthCheck {
    private final String template;

    public DummyHealthcheck(String template) {
        this.template = template;
    }

    @Override
    protected Result check() throws Exception {
        if (!String.format(template, "TEST").contains("TEST")) {
            return Result.unhealthy("No template placeholder found!");
        }
        return Result.healthy();
    }
}
