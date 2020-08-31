package com.github.lburgazzoli.camel;

import org.apache.camel.Component;

public interface ComponentCustomizer {
    void configure(Component component);

    default boolean isEnabled(Component component) {
        return true;
    }
}
