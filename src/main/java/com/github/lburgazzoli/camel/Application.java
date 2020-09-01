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
package com.github.lburgazzoli.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.ExchangeFormatter;
import org.apache.camel.support.processor.DefaultExchangeFormatter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

@ImportResource("classpath:my-beans.xml")
@SpringBootApplication
public class Application {
    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ExchangeFormatter myFormatter() {
        DefaultExchangeFormatter formatter = new DefaultExchangeFormatter();
        formatter.setShowAll(true);
        formatter.setMultiline(true);

        return formatter;
    }

    @Bean
    public RouteBuilder myRoutes() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer:tick")
                    .to("log:info");
            }
        };
    }
}
