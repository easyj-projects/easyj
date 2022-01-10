/*
 * Copyright 2021-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package icu.easyj.core.util.jar.impls;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link MavenJarGroupLoaderImpl} 测试类
 *
 * @author wangliang181230
 */
public class MavenJarGroupLoaderImplTest {

	@Test
	public void testParseGroup() {
		MavenJarGroupLoaderImpl mavenJarGroupLoader = new MavenJarGroupLoaderImpl();

		Assertions.assertEquals("org.apache",
				mavenJarGroupLoader.parseGroup("D://apache-aaa.jar!/META-INF/maven/org.apache/apache-aaa/pom.xml"));

		Assertions.assertEquals("org.apache.bbb",
				mavenJarGroupLoader.parseGroup("D://apache-bbb.jar!/META-INF/maven/org.apache.bbb/apache-bbb/pom.xml"));
	}
}
