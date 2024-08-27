/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.internal.execution.schema;

import com.google.common.collect.ImmutableList;
import org.gradle.internal.properties.schema.InstanceSchema;
import org.gradle.internal.properties.schema.NestedPropertySchema;
import org.gradle.internal.reflect.validation.ReplayingTypeValidationContext;

import java.util.Collection;

public interface WorkInstanceSchema extends InstanceSchema {

    Collection<ScalarInputPropertySchema> getScalarInputs();

    Collection<FileInputPropertySchema> getFileInputs();

    abstract class Builder<S extends WorkInstanceSchema> extends InstanceSchema.Builder<S> {
        private final ImmutableList.Builder<ScalarInputPropertySchema> scalarInputs = ImmutableList.builder();
        private final ImmutableList.Builder<FileInputPropertySchema> fileInputs = ImmutableList.builder();

        public void add(ScalarInputPropertySchema property) {
            scalarInputs.add(property);
        }

        public void add(FileInputPropertySchema property) {
            fileInputs.add(property);
        }

        @Override
        protected S build(ReplayingTypeValidationContext validationProblems, ImmutableList<NestedPropertySchema> nestedPropertySchemas) {
            return build(
                validationProblems,
                nestedPropertySchemas,
                toSortedList(scalarInputs),
                toSortedList(fileInputs)
            );
        }

        protected abstract S build(
            ReplayingTypeValidationContext validationProblems,
            ImmutableList<NestedPropertySchema> nestedPropertySchemas,
            ImmutableList<ScalarInputPropertySchema> scalarInputs,
            ImmutableList<FileInputPropertySchema> fileInputs
        );
    }
}
