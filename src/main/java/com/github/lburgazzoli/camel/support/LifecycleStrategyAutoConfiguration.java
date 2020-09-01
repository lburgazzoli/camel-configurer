/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.lburgazzoli.camel.support;

import java.util.Comparator;

import com.github.lburgazzoli.camel.ComponentCustomizer;
import org.apache.camel.Component;
import org.apache.camel.Ordered;
import org.apache.camel.support.LifecycleStrategySupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LifecycleStrategyAutoConfiguration {
    @Bean
    public LifecycleStrategySupport componentCustomizerStrategy(
            @Autowired ApplicationContext applicationContext) {

        return new LifecycleStrategySupport() {
            @Override
            public void onComponentAdd(String name, Component component) {
                applicationContext.getBeansOfType(ComponentCustomizer.class).values().stream()
                    .sorted(Comparator.comparingInt(Ordered::getOrder))
                    .filter(customizer -> customizer.isEnabled(component))
                    .forEach(customizer -> customizer.configure(component));
            }
        };
    }
}
