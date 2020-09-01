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

import java.util.function.Predicate;

import org.apache.camel.Component;
import org.apache.camel.Ordered;
import org.apache.camel.util.function.ThrowingConsumer;

public interface ComponentCustomizer extends Ordered {
    void configure(Component component);

    default boolean isEnabled(Component component) {
        return true;
    }

    @Override
    default int getOrder() {
        return 0;
    }

    // ***************************************
    //
    // Helpers
    //
    // ***************************************

    default Builder builder() {
        return new Builder();
    }

    default <T extends Component> TypedBuilder<T> builder(Class<T> type) {
        return new TypedBuilder<>(type);
    }

    // ***************************************
    //
    // Builders
    //
    // ***************************************

    class Builder {
        private Predicate<Component> condition;
        private int order = 0;

        Builder withCondition(Predicate<Component> condition) {
            this.condition = condition;

            return this;
        }

        Builder withOrder(int order) {
            this.order = order;

            return this;
        }

        ComponentCustomizer build(ThrowingConsumer<Component, Exception> consumer) {
            final int order = this.order;
            final Predicate<Component> condition = this.condition != null ? this.condition : component -> true;

            return new ComponentCustomizer() {
                @Override
                public void configure(Component component) {
                    try {
                        consumer.accept(component);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public boolean isEnabled(Component component) {
                    return condition.test(component);
                }

                @Override
                public int getOrder() {
                    return order;
                }
            };
        }
    }

    class TypedBuilder<T extends Component> {
        private final Class<T> type;
        private int order = 0;

        public TypedBuilder(Class<T> type) {
            this.type = type;
        }

        TypedBuilder<T> withOrder(int order) {
            this.order = order;

            return this;
        }

        ComponentCustomizer build(ThrowingConsumer<T, Exception> consumer) {
            final int order = this.order;

            return new ComponentCustomizer() {
                @Override
                public void configure(Component component) {
                    try {
                        consumer.accept((T)component);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public boolean isEnabled(Component component) {
                    return type.isAssignableFrom(component.getClass());
                }

                @Override
                public int getOrder() {
                    return order;
                }
            };
        }
    }
}
