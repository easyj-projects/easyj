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
import org.springframework.core.Ordered;

/**
 * 其他的JAR所属组名加载器
 *
 * @author wangliang181230
 */
@LoadLevel(name = "other", order = Ordered.LOWEST_PRECEDENCE)
public class OtherJarGroupLoaderImpl implements IJarGroupLoader {

	@Override
	public String load(JarContext jarContext) {
		switch (jarContext.getName()) {
			case "apiguardian-api":
				return "org.apiguardian";
			case "bcprov-jdk15on":
			case "bcpkix-jdk15on":
				return "org.bouncycastle";
			case "hamcrest":
				return "org.hamcrest";
			case "json-path":
			case "json-path-assert":
				return "com.jayway.jsonpath";
			case "objenesis":
			case "objenesis-tck":
			case "objenesis-tck-android":
			case "objenesis-exotic":
			case "com.springsource.org.objenesis":
				return "org.objenesis";
			default:
				break;
		}

		if (jarContext.getName().startsWith("hamcrest-")) {
			return "org.hamcrest";
		}

		return null;
	}
}
