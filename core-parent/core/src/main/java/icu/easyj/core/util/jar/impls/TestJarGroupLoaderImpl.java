/*
 * Copyright 2021-2024 the original author or authors.
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

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.util.jar.IJarGroupLoader;
import icu.easyj.core.util.jar.JarContext;

/**
 * test相关的JAR所属组名加载器
 *
 * @author wangliang181230
 */
@LoadLevel(name = "test")
public class TestJarGroupLoaderImpl implements IJarGroupLoader {

	@Override
	public String load(JarContext jarContext) {
		String name = jarContext.getName();

		// junit5
		if (name.startsWith("junit-jupiter")) {
			return "org.junit.jupiter";
		} else if (name.startsWith("junit-platform")) {
			return "org.junit.platform";
		} else if (name.startsWith("junit-vintage")) {
			return "org.junit.vintage";
		}

		switch (name) {
			// junit4
			case "junit":
				return "junit";
			// junit5
			case "junit-theories":
				return "org.junit.contrib";
			case "org.junit.source":
				return "org.junit.source";
			// mockito
			case "mockito-core":
			case "mockito-junit-jupiter":
				return "org.mockito";
			// opentest4j
			case "opentest4j":
				return "org.opentest4j";
			// android-json
			case "android-json":
				return "com.vaadin.external.google";
			// asm
			case "asm":
				if (jarContext.getVersionInfo().compareTo("4.0-SNAPSHOT") >= 0) {
					return "org.ow2.asm";
				} else {
					return "asm";
				}
				// aspectjweaver
			case "aspectjweaver":
				if (jarContext.getVersionInfo().compareTo("1.6.0-SNAPSHOT") >= 0) {
					return "org.aspectj";
				} else {
					return "aspectj";
				}
			default:
				break;
		}

		return null;
	}
}
