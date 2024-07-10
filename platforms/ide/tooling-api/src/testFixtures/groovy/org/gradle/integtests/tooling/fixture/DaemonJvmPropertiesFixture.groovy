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

package org.gradle.integtests.tooling.fixture

import groovy.transform.SelfType
import org.gradle.internal.jvm.Jvm
import org.gradle.test.fixtures.file.TestFile

@SelfType(ToolingApiSpecification)
trait DaemonJvmPropertiesFixture {

    void withInstallations(File... jdks) {
        file("gradle.properties").writeProperties(
            // We hard-code these strings since this file needs to be loaded
            // in the target distributions' classloaders, and the classes that
            // contain these properties may not exist in older versions
            "org.gradle.java.installations.auto-detect": "false",
            "org.gradle.java.installations.paths": jdks.collect { it.canonicalPath }.join(",")
        )
    }

    void assertDaemonUsedJvm(Jvm jvm) {
        assertDaemonUsedJvm(jvm.javaHome)
    }

    /**
     * Prefer {@link #assertDaemonUsedJvm(Jvm)}. We should remove this method.
     */
    void assertDaemonUsedJvm(File expectedJavaHome) {
        assert file("javaHome.txt").text == expectedJavaHome.canonicalPath
    }

    void captureJavaHome() {
        buildFile << """
            def javaHome = org.gradle.internal.jvm.Jvm.current().javaHome.canonicalPath
            println javaHome
            file("javaHome.txt").text = javaHome
        """
    }

    void assertJvmCriteria(String version) {
        Map<String, String> properties = buildPropertiesFile.properties
        assert properties.get("toolchainVersion") == version
    }

    void writeJvmCriteria(Jvm jvm) {
        writeJvmCriteria(jvm.javaVersion.majorVersion)
    }

    void writeJvmCriteria(String version) {
        Properties properties = new Properties()
        properties.put("toolchainVersion", version)
        buildPropertiesFile.writeProperties(properties)
        assertJvmCriteria(version)
    }

    TestFile getBuildPropertiesFile() {
        return file("gradle/gradle-daemon-jvm.properties")
    }
}

