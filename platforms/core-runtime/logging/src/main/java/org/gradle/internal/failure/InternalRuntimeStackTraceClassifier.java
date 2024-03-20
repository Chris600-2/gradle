/*
 * Copyright 2024 the original author or authors.
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

package org.gradle.internal.failure;

import org.gradle.internal.problems.failure.StackTraceRelevance;

import javax.annotation.Nullable;

public class InternalRuntimeStackTraceClassifier implements StackTraceClassifier {

    @Nullable
    @Override
    public StackTraceRelevance classify(StackTraceElement frame) {
        return isSystemStackFrame(frame.getClassName()) ? StackTraceRelevance.RUNTIME : null;
    }

    private static boolean isSystemStackFrame(String className) {
        // JDK calls
        return className.startsWith("java.") ||
            className.startsWith("jdk.") ||
            className.startsWith("sun.") ||
            className.startsWith("com.sun.") ||
            // Groovy calls
            className.startsWith("groovy.lang") ||
            className.startsWith("org.codehaus.groovy.") ||
            // Gradle calls
            className.startsWith("org.gradle.");
    }
}
