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

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.util.jar.IJarGroupLoader;
import icu.easyj.core.util.jar.JarContext;
import org.springframework.core.Ordered;

/**
 * JetBrains的IDEA开发工具中，会加载一些JAR进来，但是从JAR中很难读取
 *
 * @author wangliang181230
 */
@LoadLevel(name = "idea", order = Ordered.LOWEST_PRECEDENCE - 100)
public class JetBrainsIdeaJarGroupLoaderImpl implements IJarGroupLoader {

	@Override
	public String load(JarContext jarContext) {
		switch (jarContext.getName()) {
			case "debugger-agent":
			case "idea_rt":
			case "intellij-coverage-agent":
				return "org.jetbrains.intellij";
			default:
				break;
		}
		return null;
	}
}
