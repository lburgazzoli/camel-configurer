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

import com.github.lburgazzoli.camel.ComponentCustomizer;
import org.apache.camel.Component;
import org.apache.camel.Ordered;
import org.apache.camel.component.log.LogComponent;
import org.apache.camel.support.processor.DefaultExchangeFormatter;

public class MyCustomizer implements ComponentCustomizer {
    @Override
    public void configure(Component component) {
        LogComponent log = (LogComponent)component;

        DefaultExchangeFormatter formatter = (DefaultExchangeFormatter)log.getExchangeFormatter();
        formatter.setMultiline(false);
    }

    @Override
    public boolean isEnabled(Component component) {
        return component instanceof LogComponent;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST;
    }
}
